package com.jonghyun.koneps.api.data.database;

import com.jonghyun.koneps.api.data.ConnectionPool;
import com.jonghyun.koneps.api.data.VisualConnectionPool;
import com.jonghyun.koneps.api.system.seq.SeqService;
import com.jonghyun.koneps.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {
    private final DatabaseRepository databaseRepository;

    private final Util util;
    private final SeqService seqService;

    @Override
    public boolean testConnection(String type, String username, String password, String database){

        boolean isValid = false;
        StringBuilder url = new StringBuilder();
        url.append("jdbc:");
        url.append(type);
        url.append("://");
        url.append(database);
        // sample url : jdbc:mariadb://localhost:3317/framework

        try {
            ConnectionPool connectionPool = VisualConnectionPool
                    .create(url.toString(), username, password);
            Connection conn = connectionPool.getConnection();
            isValid = conn.isValid(1);

            //connectionPool.shutdown();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables.getMessage());
        }

        return isValid;
    }

    @Override
    public List<Map<String, Object>> searchDatabaseList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();

        databaseRepository.findByDatabaseNameContainsOrDatabaseTypeContainsOrCreatedByContains(keyword, keyword, keyword)
                .stream()
                .forEach(database -> {
                    String modifiedDate = "";
                    if(database.getModifiedDate() != null) {
                        modifiedDate = database.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("databaseId", database.getDatabaseId());
                    map.put("databaseName", database.getDatabaseName());
                    map.put("databaseType", database.getDatabaseType());
                    map.put("datasourceUrl", database.getDatasourceUrl());
                    map.put("createdBy", database.getCreatedBy());
                    map.put("modifiedDate", modifiedDate);
                    map.put("action", "");

                    result.add(map);
                });

        return result;
    }

    @Override
    public boolean addConnection(DatabaseDto databaseDto) {
        String type = databaseDto.type;
        String username = databaseDto.username;
        String password = databaseDto.password;
        String database = databaseDto.database;

        String databaseId = seqService.getSequenceBySeqPrefix("database");

        StringBuilder datasource = new StringBuilder();
        datasource.append(type);
        datasource.append("://");
        datasource.append(username);
        datasource.append(":XXXXXXXXXX@");
        datasource.append(database);

        String loginId = util.getLoginId();
        Database newDatabase = new Database();
        newDatabase.newDatabase(
                databaseId,
                databaseDto.getDatabaseName(),
                type,
                username,
                password,
                database,
                datasource.toString(),
                loginId,
                LocalDateTime.now());

        databaseRepository.save(newDatabase);

        return true;
    }

    @Override
    public boolean updateConnection(DatabaseDto databaseDto) {
        String databaseId = databaseDto.getDatabaseId();
        String type = databaseDto.type;
        String username = databaseDto.username;
        String password = databaseDto.password;
        String database = databaseDto.database;

        StringBuilder datasource = new StringBuilder();
        datasource.append(type);
        datasource.append("://");
        datasource.append(username);
        datasource.append(":XXXXXXXXXX@");
        datasource.append(database);

        String loginId = util.getLoginId();
        databaseRepository.findById(databaseId).ifPresent(c -> {
            c.editDatabase(
                    databaseDto.getDatabaseName(),
                    type,
                    username,
                    password,
                    database,
                    datasource.toString(),
                    loginId,
                    LocalDateTime.now());

            databaseRepository.save(c);
        });

        return true;
    }

    @Override
    public boolean deleteConnection(String databaseId) {
        try {
            databaseRepository.deleteById(databaseId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
