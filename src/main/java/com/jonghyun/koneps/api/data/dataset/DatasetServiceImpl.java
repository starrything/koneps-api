package com.jonghyun.koneps.api.data.dataset;

import com.jonghyun.koneps.api.data.ConnectionPool;
import com.jonghyun.koneps.api.data.VisualConnectionPool;
import com.jonghyun.koneps.api.data.database.Database;
import com.jonghyun.koneps.api.data.database.DatabaseRepository;
import com.jonghyun.koneps.api.system.seq.SeqService;
import com.jonghyun.koneps.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {
    private final DatasetRepository datasetRepository;
    private final DatabaseRepository databaseRepository;

    private final Util util;
    private final SeqService seqService;

    @Override
    public List<Map<String, Object>> getDatasourceList() {
        List<Map<String, Object>> result = new ArrayList<>();

        databaseRepository.findAll()
                .stream().forEach(database -> {
            Map<String, Object> map = new HashMap<>();
            map.put("databaseId", database.getDatabaseId());
            map.put("type", database.getDatabaseType());
            map.put("datasourceName", database.getDatabaseName());

            result.add(map);
        });

        return result;
    }

    @Override
    public List<String> getCatalogSchemaByDatabase(String databaseId) {
        List<String> result = new ArrayList<>();

        databaseRepository.findById(databaseId)
                .stream()
                .forEach(database -> {
                    String username = database.getUsername();
                    String password = database.getPassword();
                    String type = database.getDatabaseType();
                    String uri = database.getDatabaseUri();

                    StringBuilder jdbcUrl = new StringBuilder();
                    jdbcUrl.append("jdbc:");
                    jdbcUrl.append(type);
                    jdbcUrl.append("://");
                    jdbcUrl.append(uri);

                    try {
                        this.catalogOrSchema(type, jdbcUrl.toString(), username, password)
                                .stream()
                                .forEach(el -> {
                                    result.add(el);
                                });
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    }
                });

        return result;
    }

    private List<String> catalogOrSchema(String type, String jdbcUrl, String username, String password) throws SQLException {
        List<String> result = new ArrayList<>();

        ConnectionPool connectionPool = VisualConnectionPool.create(jdbcUrl, username, password);

        Connection conn = connectionPool.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = null;
        if ("mariadb".equals(type)) {
            rs = meta.getCatalogs();
            while (rs.next()) {
                String cat = rs.getString("table_cat");
                result.add(cat);
            }
        } else if ("postgresql".equals(type)) {
            rs = meta.getSchemas();
            while (rs.next()) {
                String schema = rs.getString("TABLE_SCHEM");
                if (!schema.contains("_pkey")) {
                    result.add(schema);
                }
            }
        }

        connectionPool.releaseConnection(conn);
        connectionPool.shutdown();

        return result;
    }

    @Override
    public List<String> getTablesByDatabase(String databaseId, String catalogSchema) {
        List<String> result = new ArrayList<>();

        databaseRepository.findById(databaseId)
                .stream()
                .forEach(database -> {
                    String username = database.getUsername();
                    String password = database.getPassword();
                    String type = database.getDatabaseType();
                    String uri = database.getDatabaseUri();

                    StringBuilder jdbcUrl = new StringBuilder();
                    jdbcUrl.append("jdbc:");
                    jdbcUrl.append(type);
                    jdbcUrl.append("://");
                    jdbcUrl.append(uri);

                    try {
                        ConnectionPool connectionPool = null;
                        connectionPool = VisualConnectionPool
                                .create(jdbcUrl.toString(), username, password);

                        Connection conn = connectionPool.getConnection();
                        DatabaseMetaData meta = conn.getMetaData();
                        ResultSet tables = null;
                        if ("mariadb".equals(type)) {
                            tables = meta.getTables(catalogSchema, null, "%", null);
                        } else if ("postgresql".equals(type)) {
                            tables = meta.getTables(null, catalogSchema, "%", null);
                        }
                        while (tables.next()) {
                            //System.out.println(tables.getString(3));
                            result.add(tables.getString(3));
                        }

                        connectionPool.releaseConnection(conn);
                        connectionPool.shutdown();
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    }
                });

        return result;
    }

    @Override
    public List<Map<String, Object>> searchDatasetList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();

        datasetRepository.findByDatasetNameContainsOrDatasetTypeContainsOrSourceContainsOrSchemaContains(keyword, keyword, keyword, keyword)
                .stream()
                .forEach(dataset -> {
                    String modifiedDate = "";
                    if (dataset.getModifiedDate() != null) {
                        modifiedDate = dataset.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("datasetId", dataset.getDatasetId());
                    map.put("datasetName", dataset.getDatasetName());
                    map.put("datasetType", dataset.getDatasetType());
                    map.put("databaseId", dataset.getDatabaseId());
                    map.put("source", dataset.getSource());
                    map.put("schema", dataset.getSchema());
                    map.put("modifiedBy", dataset.getModifiedBy());
                    map.put("modifiedDate", modifiedDate);
                    map.put("action", "");

                    result.add(map);
                });

        return result;
    }

    @Override
    public boolean addDataset(DatasetDto datasetDto) {
        String datasetName = datasetDto.getDatasetName();
        String datasetType = datasetDto.getDatasetType();
        String databaseId = datasetDto.getSource();
        String source = "";
        String schema = datasetDto.getSchema();

        Optional<Database> database = databaseRepository.findById(databaseId);

        if (database.isPresent()) {

            String databaseUri = database.get().getDatabaseUri();
            source = databaseUri.substring(databaseUri.indexOf("/") + 1);
        }

        /* Table PK */
        String datasetId = seqService.getSequenceBySeqPrefix("dataset");

        String loginId = util.getLoginId();
        Dataset newDataset = new Dataset();
        newDataset.newDataset(
                datasetId,
                datasetName,
                datasetType,
                databaseId,
                source,
                schema,
                loginId,
                LocalDateTime.now());

        datasetRepository.save(newDataset);

        return true;
    }

    @Override
    public boolean deleteDataset(String datasetId) {
        try {
            datasetRepository.deleteById(datasetId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updateDataset(DatasetDto datasetDto) {
        String datasetId = datasetDto.getDatasetId();
        String datasetName = datasetDto.getDatasetName();
        String schema = datasetDto.getSchema();

        String loginId = util.getLoginId();
        datasetRepository.findById(datasetId).ifPresent(c -> {
            c.editDataset(
                    datasetName,
                    c.datasetType,
                    schema,
                    loginId,
                    LocalDateTime.now());

            datasetRepository.save(c);
        });

        return true;
    }

    @Override
    public Dataset getDatasetByDatasetId(String datasetId) {

        Optional<Dataset> dataset = datasetRepository.findById(datasetId);

        if (dataset.isPresent()) {
            return dataset.get();
        } else {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getDatasetSpecByDatasetId(String datasetId) {
        List<Map<String, Object>> result = new ArrayList<>();

        datasetRepository.findById(datasetId).ifPresent(c -> {
            String catalog = c.getSource();
            String schema = c.getSchema();
            String datasetName = c.getDatasetName();

            databaseRepository.findById(c.databaseId).ifPresent(database -> {
                String username = database.getUsername();
                String password = database.getPassword();
                String type = database.getDatabaseType();
                String uri = database.getDatabaseUri();

                StringBuilder jdbcUrl = new StringBuilder();
                jdbcUrl.append("jdbc:");
                jdbcUrl.append(type);
                jdbcUrl.append("://");
                jdbcUrl.append(uri);

                try {
                    ConnectionPool connectionPool = VisualConnectionPool
                            .create(jdbcUrl.toString(), username, password);

                    Connection conn = connectionPool.getConnection();
                    DatabaseMetaData meta = conn.getMetaData();
                    ResultSet columns = null;
                    if ("mariadb".equals(type)) {
                        columns = meta.getColumns(catalog, null, datasetName, "%");
                    } else if ("postgresql".equals(type)) {
                        columns = meta.getColumns(null, schema, datasetName, "%");
                    }

                    while (columns.next()) {
                        String colName = columns.getString("COLUMN_NAME");
                        int colPosition = columns.getInt("ORDINAL_POSITION");
                        String colType = columns.getString("TYPE_NAME");
                        int colLength = columns.getInt("CHAR_OCTET_LENGTH");

                        Map<String, Object> map = new HashMap<>();
                        map.put("columnName", colName);
                        map.put("columnPosition", colPosition);
                        map.put("columnType", colType);
                        map.put("columnLength", colLength);
                        result.add(map);
                    }

                    connectionPool.releaseConnection(conn);
                    connectionPool.shutdown();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                    throw new RuntimeException(throwable.getMessage());
                }
            });
        });

        return result;
    }
}
