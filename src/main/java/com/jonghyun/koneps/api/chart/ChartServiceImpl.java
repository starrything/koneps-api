package com.jonghyun.koneps.api.chart;

import com.jonghyun.koneps.api.chart.query.*;
import com.jonghyun.koneps.api.data.ConnectionPool;
import com.jonghyun.koneps.api.data.VisualConnectionPool;
import com.jonghyun.koneps.api.data.database.DatabaseRepository;
import com.jonghyun.koneps.api.data.dataset.Dataset;
import com.jonghyun.koneps.api.data.dataset.DatasetRepository;
import com.jonghyun.koneps.api.system.seq.SeqService;
import com.jonghyun.koneps.core.util.Util;
import com.jonghyun.koneps.api.chart.query.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {
    private final DatabaseRepository databaseRepository;
    private final DatasetRepository datasetRepository;
    private final ChartRepository chartRepository;
    private final ChartQueryRepository chartQueryRepository;

    private final Util util;
    private final SeqService seqService;
    private final ChartQueryService chartQueryService;

    @Override
    public List<Map<String, Object>> searchChartList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();

        chartRepository.findByChartTitleContainsOrChartTypeContainsOrCreatedByContains(keyword, keyword, keyword)
                .stream()
                .forEach(chart -> {
                    String modifiedBy = "";
                    String modifiedDate = "";
                    if (chart.getModifiedDate() != null) {
                        modifiedBy = chart.getModifiedBy();
                        modifiedDate = chart.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } else {
                        modifiedBy = chart.getCreatedBy();
                        modifiedDate = chart.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("chartId", chart.getChartId());
                    map.put("datasetId", chart.getDatasetId());
                    map.put("chartTitle", chart.getChartTitle());
                    map.put("chartType", chart.getChartType());
                    map.put("modifiedBy", modifiedBy);
                    map.put("modifiedDate", modifiedDate);
                    map.put("createdBy", chart.getCreatedBy());
                    map.put("action", "");

                    result.add(map);
                });

        // Sorting result list by modifiedDate descending
        util.getCollectionSortByModifiedDate(result, "d");

        return result;
    }

    @Override
    public boolean createNewChart(ChartDto chartDto) {
        String chartType = chartDto.getChartType();
        String chartId = seqService.getSequenceBySeqType("chart");
        String queryId = seqService.getSequenceBySeqType("query");

        String queryRowLimit = chartDto.getQueryRowLimit();
        String querySingleSeries = chartDto.getQuerySingleSeries();    // bubble
        String queryEntity = chartDto.getQueryEntity();    // bubble
        String queryMaxBubbleSize = chartDto.getQueryMaxBubbleSize();    // bubble
        String optionsNumberFormat = chartDto.getOptionsNumberFormat();    // big Number

        Map<String, Object> queryMetric = chartDto.getQueryMetric();
        Map<String, Object> queryBubbleSize = chartDto.getQueryBubbleSize();    // bubble
        Map<String, Object> queryXaxis = chartDto.getQueryXaxis();    // bubble
        Map<String, Object> queryYaxis = chartDto.getQueryYaxis();    // bubble

        List<Map<String, Object>> queryColumns = chartDto.getQueryColumns();
        List<Map<String, Object>> queryOrdering = chartDto.getQueryOrdering();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();
        List<Map<String, Object>> queryMetrics = chartDto.getQueryMetrics();
        List<Map<String, Object>> queryGroupBy = chartDto.getQueryGroupBy();
        List<Map<String, Object>> querySeries = chartDto.getQuerySeries();
        List<Map<String, Object>> filtersConfiguration = chartDto.getFiltersConfiguration();

        List<ChartQuery> chartQueries = new ArrayList<>();
        /* Front-end: QUERY.COLUMNS - table */
        queryColumns.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.COLUMNS.getCode(),
                    e.get("value").toString(),
                    e.get("key").toString(),
                    "",
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.ORDERING */
        queryOrdering.stream().forEach(e -> {
            String columnOrder = e.get("value").toString();
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.ORDERING.getCode(),
                    columnOrder.substring(0, columnOrder.indexOf("[")),
                    e.get("key").toString(),
                    columnOrder.substring(columnOrder.indexOf("[") + 1, columnOrder.indexOf("]")),
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.FILTERS - common */
        queryFilters.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.FILTERS.getCode(),
                    e.get("column").toString(),
                    e.get("key").toString(),
                    e.get("filter").toString(),
                    e.get("input").toString(),
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.METRICS - line, bar */
        queryMetrics.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.METRICS.getCode(),
                    e.get("column").toString(),
                    e.get("key").toString(),
                    e.get("metrics").toString(),
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.GROUP BY - line */
        queryGroupBy.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.GROUP_BY.getCode(),
                    e.get("value").toString(),
                    e.get("key").toString(),
                    "",
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.SERIES - bar */
        querySeries.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.SERIES.getCode(),
                    e.get("value").toString(),
                    e.get("key").toString(),
                    "",
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: FILTERS.FILTERS CONFIGURATION - filter box */
        filtersConfiguration.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.FILTERS.getCode(),
                    QueryOptionCode.FILTER_BOX.getCode(),
                    e.get("filterBoxColumn").toString(),
                    e.get("filterBoxKey").toString(),
                    e.get("filterBoxLabel").toString(),
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });

        ChartQuery chartQuery = new ChartQuery();
        if (!queryMetric.isEmpty()) {
            /* Front-end: QUERY.METRIC - pie */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.METRIC.getCode(),
                    queryMetric.get("column").toString(),
                    "",
                    queryMetric.get("metrics").toString(),
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(querySingleSeries)) {
            /* Front-end: QUERY.SINGLE SERIES - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.SINGLE_SERIES.getCode(),
                    querySingleSeries,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(queryEntity)) {
            /* Front-end: QUERY.ENTITY - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.ENTITY.getCode(),
                    queryEntity,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!queryBubbleSize.isEmpty()) {
            /* Front-end: QUERY.BUBBLE SIZE - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.BUBBLE_SIZE.getCode(),
                    queryBubbleSize.get("column").toString(),
                    queryBubbleSize.get("metrics").toString(),
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!queryXaxis.isEmpty()) {
            /* Front-end: QUERY.X AXIS - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.XAXIS.getCode(),
                    queryXaxis.get("column").toString(),
                    queryXaxis.get("metrics").toString(),
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!queryYaxis.isEmpty()) {
            /* Front-end: QUERY.Y AXIS - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.YAXIS.getCode(),
                    queryYaxis.get("column").toString(),
                    queryYaxis.get("metrics").toString(),
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(queryMaxBubbleSize)) {
            /* Front-end: QUERY.MAX BUBBLE SIZE - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.MAX_BUBBLE_SIZE.getCode(),
                    queryMaxBubbleSize,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(optionsNumberFormat)) {
            /* Front-end: OPTIONS.NUMBER FORMAT - big Number */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.OPTIONS.getCode(),
                    QueryOptionCode.NUMBER_FORMAT.getCode(),
                    optionsNumberFormat,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(queryRowLimit)) {
            /* Front-end: QUERY.ROW LIMIT - common */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.ROW_LIMIT.getCode(),
                    queryRowLimit,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }

        /** 1st. add CHART QUERY */
        boolean addChartQueries = chartQueryService.addChartQueries(chartQueries);
        if (addChartQueries) {
            // add chartQueries success
        } else {
            throw new RuntimeException("failed to add ChartQueries");
        }

        /** 2nd. add CHART */
        String loginId = util.getLoginId();
        Chart chart = new Chart();
        chart.newChart(
                chartId,
                chartDto.getDatasetId(),
                chartType,
                "PUBLIC",
                chartDto.getChartTitle(),
                chartDto.getTimeColumn(),
                chartDto.getTimeGrain(),
                chartDto.getTimeRange(),
                queryId,
                QueryMode.RAW_RECORD.getCode(),
                loginId,
                LocalDateTime.now());

        chartRepository.save(chart);

        return true;
    }

    @Override
    public List<Map<String, Object>> getTableData(ChartDto chartDto) {
        List<Map<String, Object>> result = new ArrayList<>();

        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeGrain = chartDto.getTimeGrain();
        String timeRange = chartDto.getTimeRange();
        List<Map<String, Object>> queryColumns = chartDto.getQueryColumns();
        List<Map<String, Object>> queryOrdering = chartDto.getQueryOrdering();
        String queryRowLimit = chartDto.getQueryRowLimit();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();

        if (queryColumns.size() == 0) {
            return null;
        }

        /*List<Map<String, Object>> finalQueryColumns = queryColumns.stream().sorted((o1, o2) -> Integer.compare(Integer.parseInt(String.valueOf(o1.get("key"))), Integer.parseInt(String.valueOf(o2.get("key"))))).collect(Collectors.toList());*/
        List<Map<String, Object>> finalQueryColumns = queryColumns.stream().sorted(Comparator.comparingInt(o -> Integer.parseInt(String.valueOf(o.get("key"))))).collect(Collectors.toList());
        datasetRepository.findById(datasetId).ifPresent(c -> {

            databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = null;

                    StringBuilder sb = new StringBuilder();
                    /** SELECT Columns */
                    sb.append("SELECT 1");
                    finalQueryColumns.forEach(element -> {
                        sb.append(", " + element.get("value"));
                    });
                    /** FROM */
                    sb.append("  FROM " + c.getDatasetName());//Table Name
                    /** WHERE */
                    if (queryFilters.size() > 0) {
                        sb.append(" WHERE 1=1");
                        sb.append(this.setConditionsByQueryFilters(queryFilters));
                    }
                    /** ORDER BY */
                    if (queryOrdering.size() > 0) {
                        sb.append(" ORDER BY 1");
                        queryOrdering.forEach(element -> {
                            String columnOrder = element.get("value").toString();
                            String column = columnOrder.substring(0, columnOrder.indexOf("["));
                            String order = columnOrder.substring(columnOrder.indexOf("[") + 1, columnOrder.indexOf("]"));

                            sb.append(", " + column + " " + order);
                        });
                    }
                    /** ROW LIMIT */
                    if (queryRowLimit != "" && queryRowLimit != null) {
                        sb.append(" LIMIT " + queryRowLimit);
                    }

                    resultSet = stmt.executeQuery(sb.toString());

                    while (resultSet.next()) {
                        Map<String, Object> map = new HashMap<>();
                        ResultSet finalResultSet = resultSet;
                        finalQueryColumns.forEach(element -> {
                            try {
                                map.put(element.get("value").toString(), finalResultSet.getString(element.get("value").toString()));
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        });
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

    @Override
    public boolean deleteChart(String chartId) {
        try {
            chartRepository.deleteById(chartId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateChart(ChartDto chartDto) {
        String chartId = chartDto.getChartId();
        String chartType = chartDto.getChartType();
        String queryId = seqService.getSequenceBySeqType("query");

        String queryRowLimit = chartDto.getQueryRowLimit();
        String querySingleSeries = chartDto.getQuerySingleSeries();    // bubble
        String queryEntity = chartDto.getQueryEntity();    // bubble
        String queryMaxBubbleSize = chartDto.getQueryMaxBubbleSize();    // bubble
        String optionsNumberFormat = chartDto.getOptionsNumberFormat();    // big Number

        Map<String, Object> queryMetric = chartDto.getQueryMetric();
        Map<String, Object> queryBubbleSize = chartDto.getQueryBubbleSize();    // bubble
        Map<String, Object> queryXaxis = chartDto.getQueryXaxis();    // bubble
        Map<String, Object> queryYaxis = chartDto.getQueryYaxis();    // bubble

        List<Map<String, Object>> queryColumns = chartDto.getQueryColumns();
        List<Map<String, Object>> queryOrdering = chartDto.getQueryOrdering();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();
        List<Map<String, Object>> queryMetrics = chartDto.getQueryMetrics();
        List<Map<String, Object>> queryGroupBy = chartDto.getQueryGroupBy();
        List<Map<String, Object>> querySeries = chartDto.getQuerySeries();
        List<Map<String, Object>> filtersConfiguration = chartDto.getFiltersConfiguration();

        List<ChartQuery> chartQueries = new ArrayList<>();
        /* Front-end: QUERY.COLUMNS - table */
        queryColumns.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.COLUMNS.getCode(),
                    e.get("value").toString(),
                    e.get("key").toString(),
                    "",
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.ORDERING */
        queryOrdering.stream().forEach(e -> {
            String columnOrder = e.get("value").toString();
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.ORDERING.getCode(),
                    columnOrder.substring(0, columnOrder.indexOf("[")),
                    e.get("key").toString(),
                    columnOrder.substring(columnOrder.indexOf("[") + 1, columnOrder.indexOf("]")),
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.FILTERS - common */
        queryFilters.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.FILTERS.getCode(),
                    e.get("column").toString(),
                    e.get("key").toString(),
                    e.get("filter").toString(),
                    e.get("input").toString(),
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.METRICS - line, bar */
        queryMetrics.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.METRICS.getCode(),
                    e.get("column").toString(),
                    e.get("key").toString(),
                    e.get("metrics").toString(),
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.GROUP BY - line */
        queryGroupBy.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.GROUP_BY.getCode(),
                    e.get("value").toString(),
                    e.get("key").toString(),
                    "",
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: QUERY.SERIES - bar */
        querySeries.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.SERIES.getCode(),
                    e.get("value").toString(),
                    e.get("key").toString(),
                    "",
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });
        /* Front-end: FILTERS.FILTERS CONFIGURATION - filter box */
        filtersConfiguration.stream().forEach(e -> {
            ChartQuery chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.FILTERS.getCode(),
                    QueryOptionCode.FILTER_BOX.getCode(),
                    e.get("filterBoxColumn").toString(),
                    e.get("filterBoxKey").toString(),
                    e.get("filterBoxLabel").toString(),
                    "",
                    "",
                    "");

            chartQueries.add(chartQuery);
        });

        ChartQuery chartQuery = new ChartQuery();
        if (!queryMetric.isEmpty()) {
            /* Front-end: QUERY.METRIC - pie */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.METRIC.getCode(),
                    queryMetric.get("column").toString(),
                    "",
                    queryMetric.get("metrics").toString(),
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(querySingleSeries)) {
            /* Front-end: QUERY.SINGLE SERIES - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.SINGLE_SERIES.getCode(),
                    querySingleSeries,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(queryEntity)) {
            /* Front-end: QUERY.ENTITY - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.ENTITY.getCode(),
                    queryEntity,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!queryBubbleSize.isEmpty()) {
            /* Front-end: QUERY.BUBBLE SIZE - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.BUBBLE_SIZE.getCode(),
                    queryBubbleSize.get("column").toString(),
                    queryBubbleSize.get("metrics").toString(),
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!queryXaxis.isEmpty()) {
            /* Front-end: QUERY.X AXIS - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.XAXIS.getCode(),
                    queryXaxis.get("column").toString(),
                    queryXaxis.get("metrics").toString(),
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!queryYaxis.isEmpty()) {
            /* Front-end: QUERY.Y AXIS - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.YAXIS.getCode(),
                    queryYaxis.get("column").toString(),
                    queryYaxis.get("metrics").toString(),
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(queryMaxBubbleSize)) {
            /* Front-end: QUERY.MAX BUBBLE SIZE - bubble */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.MAX_BUBBLE_SIZE.getCode(),
                    queryMaxBubbleSize,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(optionsNumberFormat)) {
            /* Front-end: OPTIONS.NUMBER FORMAT - big Number */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.AGGREGATE.getCode(),
                    QueryType.OPTIONS.getCode(),
                    QueryOptionCode.NUMBER_FORMAT.getCode(),
                    optionsNumberFormat,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }
        if (!StringUtils.isBlank(queryRowLimit)) {
            /* Front-end: QUERY.ROW LIMIT - common */
            chartQuery = new ChartQuery(
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    QueryType.QUERY.getCode(),
                    QueryOptionCode.ROW_LIMIT.getCode(),
                    queryRowLimit,
                    "",
                    "",
                    "",
                    "",
                    "");
            chartQueries.add(chartQuery);
        }

        /** 1st. add CHART QUERY */
        boolean addChartQueries = chartQueryService.addChartQueries(chartQueries);
        if (addChartQueries) {
            // add chartQueries success
        } else {
            throw new RuntimeException("failed to add ChartQueries");
        }

        /** 2nd. update CHART */
        String loginId = util.getLoginId();
        chartRepository.findById(chartId).ifPresent(c -> {
            String bfChartQueryId = c.getQueryId();
            chartQueryRepository.deleteByQueryId(bfChartQueryId);

            c.updateChart(chartType,
                    chartDto.getChartTitle(),
                    chartDto.getTimeColumn(),
                    chartDto.getTimeGrain(),
                    chartDto.getTimeRange(),
                    queryId,
                    QueryMode.RAW_RECORD.getCode(),
                    loginId,
                    LocalDateTime.now());

            chartRepository.save(c);
        });

        return true;
    }

    @Override
    public List<Map<String, Object>> getLineChartData(ChartDto chartDto) {
        List<Map<String, Object>> result = new ArrayList<>();

        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeGrain = chartDto.getTimeGrain();
        String timeRange = chartDto.getTimeRange();
        List<Map<String, Object>> queryMetrics = chartDto.getQueryMetrics();
        List<Map<String, Object>> queryGroupBy = chartDto.getQueryGroupBy();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();
        String queryRowLimit = chartDto.getQueryRowLimit();

        if (queryMetrics.size() == 0 || StringUtils.isBlank(timeColumn)) {
            return null;
        }

        datasetRepository.findById(datasetId).ifPresent(c -> {

            databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = null;

                    /*
                     * 1st: query Group by columns
                     * */
                    List<Map<String, String>> groupBySets = new ArrayList<>();
                    StringBuilder pre = new StringBuilder();
                    if (queryGroupBy.size() > 0) {
                        pre.append("SELECT DISTINCT 1 ");
                        queryGroupBy.forEach(e -> {
                            pre.append(", " + e.get("value"));
                        });
                        pre.append("  FROM " + c.getDatasetName());
                        pre.append(" WHERE 1=1");
                        queryGroupBy.forEach(e -> {
                            pre.append(" AND " + e.get("value") + " IS NOT NULL");
                        });
                        if (queryFilters.size() > 0) {
                            pre.append(this.setConditionsByQueryFilters(queryFilters));
                        }
                        pre.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
                        /** ROW LIMIT */
                        if (queryRowLimit != "" && queryRowLimit != null) {
                            pre.append(" LIMIT " + queryRowLimit);
                        }

                        resultSet = stmt.executeQuery(pre.toString());
                        while (resultSet.next()) {
                            Map<String, String> groupBySet = new HashMap<>();
                            ResultSet finalResultSet1 = resultSet;
                            queryGroupBy.forEach(e -> {
                                try {
                                    groupBySet.put(e.get("value").toString(), finalResultSet1.getObject(Integer.parseInt(e.get("key").toString()) + 2).toString());
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            });
                            groupBySets.add(groupBySet);
                        }
                    }

                    int loopCount = 0;
                    if (groupBySets.size() == 0) {
                        loopCount = 0;
                    } else {
                        if (groupBySets.size() > 200) {
                            throw new RuntimeException("That's too many GroupBy sets... (count: " + groupBySets.size() + ")");
                        }
                        loopCount = groupBySets.size();
                    }

                    /*
                     * 2nd: main Query
                     * */
                    for (int i = 0; i < loopCount; i++) {
                        StringBuilder sb = new StringBuilder();
                        /** SELECT Columns */
                        sb.append("SELECT 1");
                        queryMetrics.forEach(element -> {
                            String columnName = element.get("column").toString();
                            String aggregate = element.get("metrics").toString();
                            if ("COUNT_DISTINCT".equals(aggregate)) {
                                sb.append(", COUNT(DISTINCT(" + columnName + ")) AS " + columnName);
                            } else {
                                sb.append(", " + aggregate + "(" + columnName + ") AS " + columnName);
                            }
                        });
                        /** add Time Column */
                        if (!StringUtils.isBlank(timeGrain)) {
                            /* this.getTimeColumnFormat by DB type */
                            sb.append(", " + this.getTimeColumnFormat(type, timeColumn, timeGrain) + " AS \"TIME_COLUMN\"");
                        } else {
                            // default TimeGrain setting
                            sb.append(", " + this.getTimeColumnFormat(type, timeColumn, "DAY") + " AS \"TIME_COLUMN\"");
                        }

                        /** FROM */
                        sb.append("  FROM " + c.getDatasetName());//Table Name

                        /** WHERE */
                        if (queryFilters.size() > 0 || !StringUtils.isBlank(timeRange)) {
                            sb.append(" WHERE 1=1");
                            sb.append(this.setConditionsByQueryFilters(queryFilters));

                            /** add GROUP BY Column */
                            if (queryGroupBy.size() > 0) {
                                int finalI = i;
                                queryGroupBy.forEach(e -> {
                                    sb.append("   AND " + e.get("value").toString() + " = '" + groupBySets.get(finalI).get(e.get("value").toString()) + "'");
                                });
                            }

                            /** set Time Range condition */
                            sb.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
                        }
                        /** GROUP BY */
                        sb.append(" GROUP BY 1");
                        sb.append(", " + this.getTimeColumnFormat(type, timeColumn, timeGrain));
                        if (queryGroupBy.size() > 0) {
                            queryGroupBy.forEach(element -> {
                                sb.append(", " + element.get("value").toString());
                            });
                        }
                        /** ORDER BY */
                        sb.append(" ORDER BY " + this.getTimeColumnFormat(type, timeColumn, timeGrain));
                        if (queryGroupBy.size() > 0) {
                            queryGroupBy.forEach(e -> {
                                sb.append(", " + e.get("value"));
                            });
                        }
                        /** ROW LIMIT */
                        if (queryRowLimit != "" && queryRowLimit != null) {
                            sb.append(" LIMIT " + queryRowLimit);
                        }

                        resultSet = stmt.executeQuery(sb.toString());

                        // ResultSet 의 MetaData를 가져온다.
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        // ResultSet 의 Column의 갯수를 가져온다.
                        int sizeOfColumn = metaData.getColumnCount();

                        /* ResultSet to List<Map<String, Object>> */
                        List<Map> queryList = this.getResultMapRows(resultSet);
                        List<Map> finalQueryList = queryList;

                        int finalI1 = i;
                        queryMetrics.forEach(e -> {
                            Map<String, Object> lineData = new HashMap<>();
                            /*
                             * set Line Chart Dataset ->
                             * Map format :
                             * {
                             *   x: [1, 2, 3],  // Time Column
                             *   y: [7, 4, 5],  // resultSet column-rows
                             *   type: "scatter",
                             *   mode: "lines+markers",
                             * },
                             * */
                            List<String> xAxis = new ArrayList<>();
                            List<String> yAxis = new ArrayList<>();

                            finalQueryList.forEach(row -> {
                                xAxis.add(row.get("TIME_COLUMN").toString());
                                yAxis.add(row.get(e.get("column").toString()).toString());
                            });

                            lineData.put("x", xAxis);
                            lineData.put("y", yAxis);
                            lineData.put("type", "scatter");
                            lineData.put("mode", "lines");

                            /** add GROUP BY Column */
                            if (queryGroupBy.size() > 0) {
                                queryGroupBy.forEach(gr -> {
                                    lineData.put("name", e.get("metrics").toString() + "(" + e.get("column").toString() + ") " + groupBySets.get(finalI1).get(gr.get("value").toString()));
                                });

                            } else {
                                lineData.put("name", e.get("metrics").toString() + "(" + e.get("column").toString() + ")");
                            }

                            result.add(lineData);
                        });
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

    @Override
    public List<Map<String, Object>> getBarChartData(ChartDto chartDto) {
        List<Map<String, Object>> result = new ArrayList<>();

        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeRange = chartDto.getTimeRange();
        List<Map<String, Object>> queryMetrics = chartDto.getQueryMetrics();
        List<Map<String, Object>> querySeries = chartDto.getQuerySeries();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();
        String queryRowLimit = chartDto.getQueryRowLimit();

        if (queryMetrics.size() == 0 || StringUtils.isBlank(timeColumn)) {
            return null;
        }

        datasetRepository.findById(datasetId).ifPresent(c -> {

            databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = null;

                    if (queryMetrics.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("SELECT 1");
                        queryMetrics.forEach(element -> {
                            String columnName = element.get("column").toString();
                            String aggregate = element.get("metrics").toString();
                            if ("COUNT_DISTINCT".equals(aggregate)) {
                                sb.append(", COUNT(DISTINCT " + columnName + ") AS " + columnName);
                            } else {
                                sb.append(", " + aggregate + "(" + columnName + ") AS " + columnName);
                            }
                        });
                        /** add Series Column to Select statements */
                        for (int i = 0; i < querySeries.size(); i++) {
                            if (i == 0) {
                                sb.append(", CONCAT(" + querySeries.get(i).get("value").toString());
                            } else {
                                sb.append(", CONCAT(', ', " + querySeries.get(i).get("value").toString() + ")");
                            }
                        }
                        sb.append(") AS xaxis");

                        /** FROM */
                        sb.append("  FROM " + c.getDatasetName());//Table Name

                        /** WHERE */
                        if (queryFilters.size() > 0 || !StringUtils.isBlank(timeRange)) {
                            sb.append(" WHERE 1=1");
                            /** set Query Filters */
                            sb.append(this.setConditionsByQueryFilters(queryFilters));

                            /** set Time Range condition */
                            sb.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
                        }

                        /** GROUP BY */
                        for (int i = 0; i < querySeries.size(); i++) {
                            if (i == 0) {
                                sb.append(" GROUP BY " + querySeries.get(i).get("value").toString());
                            } else {
                                sb.append(", " + querySeries.get(i).get("value").toString());
                            }
                        }

                        /** ORDER BY */
                        sb.append(" ORDER BY 1");
                        queryMetrics.forEach(e -> {
                            String columnName = e.get("column").toString();
                            String aggregate = e.get("metrics").toString();
                            if ("COUNT_DISTINCT".equals(aggregate)) {
                                sb.append(", COUNT(DISTINCT " + columnName + ")");
                            } else {
                                sb.append(", " + aggregate + "(" + columnName + ")");
                            }
                        });

                        /** ROW LIMIT */
                        if (queryRowLimit != "" && queryRowLimit != null) {
                            sb.append(" LIMIT " + queryRowLimit);
                        }

                        resultSet = stmt.executeQuery(sb.toString());

                        // ResultSet 의 MetaData를 가져온다.
                        ResultSetMetaData metaData = resultSet.getMetaData();

                        /* ResultSet to List<Map<String, Object>> */
                        List<Map> queryList = this.getResultMapRows(resultSet);
                        List<Map> finalQueryList = queryList;

                        queryMetrics.forEach(e -> {
                            Map<String, Object> chartData = new HashMap<>();
                            /*
                             * set Line Chart Dataset ->
                             * Map format :
                             * {
                             *   x: [1, 2, 3],  // Time Column
                             *   y: [7, 4, 5],  // resultSet column-rows
                             *   type: "bar",
                             *   name: ""
                             * },
                             * */
                            List<String> xAxis = new ArrayList<>();
                            List<String> yAxis = new ArrayList<>();

                            finalQueryList.forEach(row -> {
                                xAxis.add(row.get("xaxis").toString());
                                yAxis.add(row.get(e.get("column").toString()).toString());
                            });

                            chartData.put("x", xAxis);
                            chartData.put("y", yAxis);
                            chartData.put("type", "bar");
                            chartData.put("name", e.get("metrics").toString() + "(" + e.get("column").toString() + ")");

                            result.add(chartData);
                        });
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

    @Override
    public List<Map<String, Object>> getPieChartData(ChartDto chartDto) {
        List<Map<String, Object>> result = new ArrayList<>();

        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeRange = chartDto.getTimeRange();
        Map<String, Object> queryMetric = chartDto.getQueryMetric();
        List<Map<String, Object>> queryGroupBy = chartDto.getQueryGroupBy();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();
        String queryRowLimit = chartDto.getQueryRowLimit();

        if (queryMetric.isEmpty() || StringUtils.isBlank(timeColumn)) {
            return null;
        }

        datasetRepository.findById(datasetId).ifPresent(c -> {

            databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = null;

                    StringBuilder sb = new StringBuilder();
                    sb.append("SELECT 1");
                    String columnName = queryMetric.get("column").toString();
                    String aggregate = queryMetric.get("metrics").toString();
                    if ("COUNT_DISTINCT".equals(aggregate)) {
                        sb.append(", COUNT(DISTINCT " + columnName + ") AS " + columnName);
                    } else {
                        sb.append(", " + aggregate + "(" + columnName + ") AS " + columnName);
                    }
                    if (queryGroupBy.size() > 0) {
                        sb.append(", ");
                        if (queryGroupBy.size() > 1) {
                            for (int i = 0; i < queryGroupBy.size(); i++) {
                                if (i == 0) {
                                    sb.append("CONCAT(" + queryGroupBy.get(i).get("value").toString());
                                } else {
                                    sb.append(", CONCAT(', ', " + queryGroupBy.get(i).get("value").toString());
                                }
                            }
                            sb.append(")) AS labels");
                        } else {
                            sb.append(queryGroupBy.get(0).get("value").toString() + " AS labels");
                        }
                    }

                    /** FROM */
                    sb.append("  FROM " + c.getDatasetName());//Table Name

                    /** WHERE */
                    if (queryFilters.size() > 0 || !StringUtils.isBlank(timeRange)) {
                        sb.append(" WHERE 1=1");
                        /** set Query Filters */
                        sb.append(this.setConditionsByQueryFilters(queryFilters));

                        /** set Time Range condition */
                        sb.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
                    }

                    if (queryGroupBy.size() > 0) {
                        /** GROUP BY */
                        for (int i = 0; i < queryGroupBy.size(); i++) {
                            if (i == 0) {
                                sb.append(" GROUP BY " + queryGroupBy.get(i).get("value").toString());
                            } else {
                                sb.append(", " + queryGroupBy.get(i).get("value").toString());
                            }
                        }
                        for (int i = 0; i < queryGroupBy.size(); i++) {
                            if (i == 0) {
                                sb.append(" ORDER BY " + queryGroupBy.get(i).get("value").toString());
                            } else {
                                sb.append(", " + queryGroupBy.get(i).get("value").toString());
                            }
                        }
                    }

                    /** ROW LIMIT */
                    if (queryRowLimit != "" && queryRowLimit != null) {
                        sb.append(" LIMIT " + queryRowLimit);
                    }

                    resultSet = stmt.executeQuery(sb.toString());

                    // ResultSet 의 MetaData를 가져온다.
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    /* ResultSet to List<Map<String, Object>> */
                    List<Map> queryList = this.getResultMapRows(resultSet);
                    List<Map> finalQueryList = queryList;

                    Map<String, Object> chartData = new HashMap<>();
                    /*
                     * set Line Chart Dataset ->
                     * Map format :
                     * {
                     *   type: "pie",
                     *   values: [2, 3, 4, 4],
                     *   labels: ["Wages", "Operating expenses", "Cost of sales", "Insurance"],
                     *   textinfo: "label+percent",
                     *   textposition: "outside",
                     *   automargin: true
                     * }
                     * */
                    List<String> labels = new ArrayList<>();
                    List<String> values = new ArrayList<>();

                    finalQueryList.forEach(row -> {
                        labels.add(row.get("labels") == null ? "no Group By" : row.get("labels").toString());
                        values.add(row.get(queryMetric.get("column").toString()).toString());
                    });

                    chartData.put("labels", labels);
                    chartData.put("values", values);
                    chartData.put("type", "pie");
                    chartData.put("textinfo", "label");
                    chartData.put("textposition", "outside");
                    chartData.put("automargin", "true");

                    result.add(chartData);

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

    @Override
    public List<Map<String, Object>> getBubbleChartData(ChartDto chartDto) {
        List<Map<String, Object>> result = new ArrayList<>();

        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeRange = chartDto.getTimeRange();
        String querySingleSeries = chartDto.getQuerySingleSeries();
        String queryEntity = chartDto.getQueryEntity();
        Map<String, Object> queryBubbleSize = chartDto.getQueryBubbleSize();
        Map<String, Object> queryXaxis = chartDto.getQueryXaxis();
        Map<String, Object> queryYaxis = chartDto.getQueryYaxis();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();
        String queryMaxBubbleSize = chartDto.getQueryMaxBubbleSize();

        if (StringUtils.isBlank(queryEntity)) {
            throw new RuntimeException("Query - Entity can not be empty.");
        }
        if (queryBubbleSize.isEmpty()) {
            throw new RuntimeException("Query - Bubble Size can not be empty.");
        }
        if (queryXaxis.isEmpty()) {
            throw new RuntimeException("Query - X Axis can not be empty.");
        }
        if (queryYaxis.isEmpty()) {
            throw new RuntimeException("Query - Y Axis can not be empty.");
        }

        datasetRepository.findById(datasetId).ifPresent(c -> {

            databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = null;

                    /*
                     * Bubble chart requires a Primary key consisting of at least three columns.
                     * Example ->
                     * > Series : UPPER_CODE  // Defines the grouping of entities. Each series is shown as a specific color on the chart and has a legend toggle.
                     * > Entity : CODE  // This defines the element to be plotted on the chart.
                     * > Bubble :
                     * > X Axis :
                     * > Y Axis :
                     * */

                    /*
                     * 1st query: Grouping by Series
                     * */
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append("SELECT DISTINCT " + querySingleSeries);
                    sb1.append("  FROM " + c.getDatasetName());//Table Name
                    resultSet = stmt.executeQuery(sb1.toString());    // Execute SQL
                    List<Map> seriesList = this.getResultMapRows(resultSet);

                    int seriesCount = 0;
                    if (StringUtils.isBlank(querySingleSeries)) {
                        seriesCount = 1;
                    } else {
                        seriesCount = seriesList.size();    // Executed SQL row count
                    }

                    /*
                     * 2nd query: Main query
                     * */
                    if (seriesCount == 1) {
                        // no Series
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.setBubbleChartQueryBySeries(type, c.getDatasetName(), timeColumn, timeRange, "", "", queryEntity, queryBubbleSize, queryXaxis, queryYaxis, queryFilters));

                        resultSet = stmt.executeQuery(sb.toString());
                        List<Map> queryList = this.getResultMapRows(resultSet);

                        Map<String, Object> chartData = this.setBubbleChartDataset(queryList, querySingleSeries, queryEntity, queryBubbleSize, queryMaxBubbleSize);

                        result.add(chartData);
                    } else if (seriesCount > 1) {
                        // Group by Series
                        seriesList.forEach(e -> {
                            //
                            StringBuilder sb = new StringBuilder();
                            sb.append(this.setBubbleChartQueryBySeries(type, c.getDatasetName(), timeColumn, timeRange, querySingleSeries, e.get(querySingleSeries).toString(), queryEntity, queryBubbleSize, queryXaxis, queryYaxis, queryFilters));

                            try {
                                ResultSet resultSetBySeries = stmt.executeQuery(sb.toString());
                                List<Map> queryList = this.getResultMapRows(resultSetBySeries);

                                Map<String, Object> chartData = this.setBubbleChartDataset(queryList, querySingleSeries, queryEntity, queryBubbleSize, queryMaxBubbleSize);

                                result.add(chartData);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        });
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

    @Override
    public Map<String, Object> getBigNumberData(ChartDto chartDto) {
        Map<String, Object> chartData = new HashMap<>();

        String chartId = chartDto.getChartId();
        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeGrain = chartDto.getTimeGrain();
        String timeRange = chartDto.getTimeRange();
        Map<String, Object> queryMetric = chartDto.getQueryMetric();
        List<Map<String, Object>> queryFilters = chartDto.getQueryFilters();

        // if chartId
        if (StringUtils.isNotEmpty(chartId)) {
            chartRepository.findById(chartId).ifPresent(c -> {
                String queryId = c.getQueryId();
                chartQueryRepository.findByQueryIdAndQueryTypeAndOptionCode(queryId,
                        QueryType.OPTIONS.getCode(),
                        QueryOptionCode.NUMBER_FORMAT.getCode()).ifPresent(q -> {
                    chartData.put("numberFormat", q.getOptionValue());
                });
            });
        }

        if (queryMetric.isEmpty()) {
            return null;
        }

        datasetRepository.findById(datasetId).ifPresent(c -> {

            databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = null;

                    StringBuilder sb = new StringBuilder();
                    sb.append("SELECT ");
                    String columnName = queryMetric.get("column").toString();
                    String aggregate = queryMetric.get("metrics").toString();
                    if ("COUNT_DISTINCT".equals(aggregate)) {
                        sb.append("COUNT(DISTINCT " + columnName + ") AS " + columnName);
                    } else {
                        sb.append(aggregate + "(" + columnName + ") AS " + columnName);
                    }

                    /** FROM */
                    sb.append("  FROM " + c.getDatasetName());//Table Name

                    /** WHERE */
                    sb.append(" WHERE 1=1");
                    if (queryFilters.size() > 0) {
                        /** set Query Filters */
                        sb.append(this.setConditionsByQueryFilters(queryFilters));
                    }
                    if (!StringUtils.isBlank(timeGrain) && !StringUtils.isBlank(timeRange)) {
                        /** set Time Range condition */
                        sb.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
                    }

                    resultSet = stmt.executeQuery(sb.toString());

                    while (resultSet.next()) {
                        chartData.put("value", resultSet.getString(columnName));
                    }

                    connectionPool.releaseConnection(conn);
                    connectionPool.shutdown();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                    throw new RuntimeException(throwable.getMessage());
                }
            });
        });

        return chartData;
    }

    @Override
    public List<Map<String, Object>> getFilterBoxData(ChartDto chartDto) {
        List<Map<String, Object>> result = new ArrayList<>();

        String datasetId = chartDto.getDatasetId();
        String timeColumn = chartDto.getTimeColumn();
        String timeRange = chartDto.getTimeRange();
        List<Map<String, Object>> filtersConfiguration = chartDto.getFiltersConfiguration();

        if (filtersConfiguration.size() > 0) {
            filtersConfiguration.forEach(e -> {
                String filterBoxKey = e.get("filterBoxKey").toString();
                String filterBoxColumn = e.get("filterBoxColumn").toString();
                String filterBoxLabel = e.get("filterBoxLabel").toString();
                if (StringUtils.isBlank(filterBoxLabel)) {
                    filterBoxLabel = filterBoxColumn;
                }

                String finalFilterBoxLabel = filterBoxLabel;
                datasetRepository.findById(datasetId).ifPresent(c -> {

                    databaseRepository.findById(c.getDatabaseId()).ifPresent(database -> {
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
                            Statement stmt = conn.createStatement();
                            ResultSet resultSet = null;
                            /*
                             * 1st query: Grouping by Series
                             * */
                            StringBuilder sb = new StringBuilder();
                            sb.append("SELECT DISTINCT " + filterBoxColumn);
                            sb.append("  FROM " + c.getDatasetName());//Table Name
                            /** WHERE */
                            sb.append(" WHERE 1=1");
                            if (!StringUtils.isBlank(timeRange)) {
                                /** set Time Range condition */
                                sb.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
                            }
                            /** ORDER BY */
                            sb.append(" ORDER BY " + filterBoxColumn);
                            resultSet = stmt.executeQuery(sb.toString());    // Execute SQL
                            List<Map> queryList = this.getResultMapRows(resultSet);

                            Map<String, Object> resultMap = new HashMap<>();
                            resultMap.put("label", finalFilterBoxLabel);
                            resultMap.put("column", filterBoxColumn);
                            resultMap.put("filterData", queryList);

                            result.add(resultMap);

                            connectionPool.releaseConnection(conn);
                            connectionPool.shutdown();
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                            throw new RuntimeException(throwable.getMessage());
                        }
                    });
                });
            });
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> searchChartListFromDashboard(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();

        chartRepository.findByChartTitleContainsOrChartTypeContainsOrCreatedByContains(keyword, keyword, keyword)
                .stream()
                .forEach(chart -> {
                    String modifiedDate = "";
                    if (chart.getModifiedDate() != null) {
                        modifiedDate = chart.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } else {
                        modifiedDate = chart.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                    Optional<Dataset> dataset = datasetRepository.findById(chart.getDatasetId());

                    String dateDiffUnit = " days";
                    Long dateDiff = 0L;
                    LocalDateTime now = LocalDateTime.now();
                    if (chart.getModifiedDate() != null) {
                        dateDiff = ChronoUnit.DAYS.between(chart.getModifiedDate(), now);
                    } else {
                        dateDiff = ChronoUnit.DAYS.between(chart.getCreationDate(), now);
                    }
                    if (dateDiff > 31) {
                        dateDiffUnit = " months";
                        if (chart.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.MONTHS.between(chart.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.MONTHS.between(chart.getCreationDate(), now);
                        }
                    } else if (dateDiff > 365) {
                        dateDiffUnit = " years";
                        if (chart.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.YEARS.between(chart.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.YEARS.between(chart.getCreationDate(), now);
                        }
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("chartId", chart.getChartId());
                    map.put("datasetId", chart.getDatasetId());
                    map.put("datasource", dataset.get().getSource() + "." + dataset.get().getDatasetName());
                    map.put("chartTitle", chart.getChartTitle());
                    map.put("chartType", chart.getChartType());
                    map.put("modifiedBy", chart.getModifiedBy());
                    map.put("modifiedDate", modifiedDate);
                    map.put("modifiedFrom", dateDiff + dateDiffUnit);
                    map.put("createdBy", chart.getCreatedBy());
                    map.put("action", "");

                    result.add(map);
                });

        util.getCollectionSortByModifiedDate(result, "d");

        return result;
    }

    @Override
    public Chart getChartByChartId(String chartId) {
        return chartRepository.findById(chartId).get();
    }

    @Override
    public Map<String, Object> getChartSpecByChartId(String chartId) {
        Map<String, Object> result = new HashMap<>();
        chartRepository.findById(chartId).ifPresent(c -> {
            /* 1. get Chart data */
            result.put("chartId", chartId);
            result.put("datasetId", c.getDatasetId());
            result.put("chartType", c.getChartType());
            result.put("chartTitle", c.getChartTitle());
            result.put("timeColumn", c.getTimeColumn());
            result.put("timeGrain", c.getTimeGrain());
            result.put("timeRange", c.getTimeRange());

            /* 2. get ChartQuery data */
            String queryId = c.getQueryId();
            List<ChartQuery> chartQueries = chartQueryRepository.findByQueryId(queryId);

            List<Map<String, Object>> queryColumns = new ArrayList<>();
            List<Map<String, Object>> queryOrdering = new ArrayList<>();
            List<Map<String, Object>> queryFilters = new ArrayList<>();
            AtomicReference<String> queryRowLimit = new AtomicReference<>("");
            List<Map<String, Object>> queryMetrics = new ArrayList<>();
            List<Map<String, Object>> queryGroupBy = new ArrayList<>();
            List<Map<String, Object>> querySeries = new ArrayList<>();
            Map<String, Object> queryMetric = new HashMap<>();
            AtomicReference<String> querySingleSeries = new AtomicReference<>("");
            AtomicReference<String> queryEntity = new AtomicReference<>("");
            Map<String, Object> queryXaxis = new HashMap<>();
            Map<String, Object> queryYaxis = new HashMap<>();
            Map<String, Object> queryBubbleSize = new HashMap<>();
            AtomicReference<String> queryMaxBubbleSize = new AtomicReference<>("");
            List<Map<String, Object>> filtersConfiguration = new ArrayList<>();
            AtomicReference<String> optionsNumberFormat = new AtomicReference<>("");
            if (chartQueries.size() > 0) {
                chartQueries.forEach(chartQuery -> {
                    String optionCode = chartQuery.getOptionCode();
                    String optionValue = chartQuery.getOptionValue();
                    String optionOrder = chartQuery.getOptionOrder();
                    String optionValueRemark1 = chartQuery.getOptionValueRemark1();
                    String optionValueRemark2 = chartQuery.getOptionValueRemark2();
                    String optionValueRemark3 = chartQuery.getOptionValueRemark3();
                    if (QueryOptionCode.COLUMNS.getCode().equals(optionCode)) {
                        // QUERY.COLUMNS
                        Map<String, Object> column = new HashMap<>();
                        column.put("key", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        column.put("value", optionValue);
                        queryColumns.add(column);
                    } else if (QueryOptionCode.ORDERING.getCode().equals(optionCode)) {
                        // QUERY.ORDERING
                        Map<String, Object> order = new HashMap<>();
                        order.put("key", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        order.put("value", optionValue + "[" + optionValueRemark1 + "]");
                        queryOrdering.add(order);
                    } else if (QueryOptionCode.FILTERS.getCode().equals(optionCode)) {
                        // QUERY.FILTERS
                        Map<String, Object> filter = new HashMap<>();
                        filter.put("key", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        filter.put("column", optionValue);
                        filter.put("filter", optionValueRemark1);
                        filter.put("input", optionValueRemark2);
                        queryFilters.add(filter);
                    } else if (QueryOptionCode.ROW_LIMIT.getCode().equals(optionCode)) {
                        // QUERY.ROW_LIMIT
                        queryRowLimit.set(optionValue);
                    } else if (QueryOptionCode.METRICS.getCode().equals(optionCode)) {
                        // QUERY.METRICS
                        Map<String, Object> metric = new HashMap<>();
                        metric.put("key", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        metric.put("column", optionValue);
                        metric.put("metrics", optionValueRemark1);
                        queryMetrics.add(metric);
                    } else if (QueryOptionCode.GROUP_BY.getCode().equals(optionCode)) {
                        // QUERY.GROUP_BY
                        Map<String, Object> groupBy = new HashMap<>();
                        groupBy.put("key", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        groupBy.put("value", optionValue);
                        queryGroupBy.add(groupBy);
                    } else if (QueryOptionCode.SERIES.getCode().equals(optionCode)) {
                        // QUERY.SERIES
                        Map<String, Object> series = new HashMap<>();
                        series.put("key", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        series.put("value", optionValue);
                        querySeries.add(series);
                    } else if (QueryOptionCode.METRIC.getCode().equals(optionCode)) {
                        // QUERY.METRIC
                        queryMetric.put("column", optionValue);
                        queryMetric.put("metrics", optionValueRemark1);
                    } else if (QueryOptionCode.SINGLE_SERIES.getCode().equals(optionCode)) {
                        // QUERY.SINGLE_SERIES
                        querySingleSeries.set(optionValue);
                    } else if (QueryOptionCode.ENTITY.getCode().equals(optionCode)) {
                        // QUERY.ENTITY
                        queryEntity.set(optionValue);
                    } else if (QueryOptionCode.XAXIS.getCode().equals(optionCode)) {
                        // QUERY.XAXIS
                        queryXaxis.put("column", optionValue);
                        queryXaxis.put("metrics", optionValueRemark1);
                    } else if (QueryOptionCode.YAXIS.getCode().equals(optionCode)) {
                        // QUERY.YAXIS
                        queryYaxis.put("column", optionValue);
                        queryYaxis.put("metrics", optionValueRemark1);
                    } else if (QueryOptionCode.BUBBLE_SIZE.getCode().equals(optionCode)) {
                        // QUERY.BUBBLE_SIZE
                        queryBubbleSize.put("column", optionValue);
                        queryBubbleSize.put("metrics", optionValueRemark1);
                    } else if (QueryOptionCode.MAX_BUBBLE_SIZE.getCode().equals(optionCode)) {
                        // QUERY.MAX_BUBBLE_SIZE
                        queryMaxBubbleSize.set(optionValue);
                    } else if (QueryOptionCode.FILTER_BOX.getCode().equals(optionCode)) {
                        // FILTER_BOX
                        Map<String, Object> filterBox = new HashMap<>();
                        filterBox.put("filterBoxKey", Integer.parseInt(StringUtils.isBlank(optionOrder) ? "0" : optionOrder));
                        filterBox.put("filterBoxColumn", optionValue);
                        filterBox.put("filterBoxLabel", optionValueRemark1);
                        filtersConfiguration.add(filterBox);
                    } else if (QueryOptionCode.NUMBER_FORMAT.getCode().equals(optionCode)) {
                        // NUMBER_FORMAT
                        optionsNumberFormat.set(optionValue);
                    }
                });
                // Columns 순서대로
                getCollectionSort(queryColumns);
                result.put("queryColumns", queryColumns);

                // ORDERING 순서대로
                getCollectionSort(queryOrdering);
                result.put("queryOrdering", queryOrdering);

                // FILTERS
                result.put("queryFilters", queryFilters);

                // ROW LIMIT
                result.put("queryRowLimit", queryRowLimit.toString());

                // METRICS 순서대로
                getCollectionSort(queryMetrics);
                result.put("queryMetrics", queryMetrics);

                // GROUP_BY 순서대로
                getCollectionSort(queryGroupBy);
                result.put("queryGroupBy", queryGroupBy);

                // SERIES 순서대로
                getCollectionSort(querySeries);
                result.put("querySeries", querySeries);

                // METRIC (PIE Chart)
                result.put("queryMetric", queryMetric);

                // SINGLE_SERIES (Bubble Chart)
                result.put("querySingleSeries", querySingleSeries.toString());

                // ENTITY (Bubble Chart)
                result.put("queryEntity", queryEntity.toString());

                // XAXIS (Bubble Chart)
                result.put("queryXaxis", queryXaxis);

                // YAXIS (Bubble Chart)
                result.put("queryYaxis", queryYaxis);

                // BUBBLE_SIZE (Bubble Chart)
                result.put("queryBubbleSize", queryBubbleSize);

                // ENTITY (Bubble Chart)
                result.put("queryMaxBubbleSize", queryMaxBubbleSize.toString());

                // FILTER_BOX 순서대로
                getCollectionSort(filtersConfiguration);
                result.put("filtersConfiguration", filtersConfiguration);

                // NUMBER_FORMAT
                result.put("optionsNumberFormat", optionsNumberFormat.toString());
            }
        });
        return result;
    }

    private void getCollectionSort(List<Map<String, Object>> data) {
        Collections.sort(data, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String name1 = String.valueOf(o1.get("key"));
                String name2 = String.valueOf(o2.get("key"));
                return name1.compareTo(name2);
            }
        });
    }

    private Map<String, Object> setBubbleChartDataset(List<Map> queryList, String querySingleSeries, String queryEntity, Map<String, Object> queryBubbleSize, String queryMaxBubbleSize) {
        /*
         * set Bubble Chart Dataset ->
         * Map format(trace4) :
         * // sizeref using above forumla
         * var desired_maximum_marker_size = 40;
         * var size = [400, 600, 800, 1000];
         * var trace4 = {
         *   x: [1, 2, 3, 4],
         *   y: [26, 27, 28, 29],
         *   text: ['A</br>size: 40</br>sixeref: 1.25', 'B</br>size: 60</br>sixeref: 1.25', 'C</br>size: 80</br>sixeref: 1.25', 'D</br>size: 100</br>sixeref: 1.25'],
         *   mode: 'markers',
         *   marker: {
         *     size: size,
         *     //set 'sizeref' to an 'ideal' size given by the formula sizeref = 2. * max(array_of_size_values) / (desired_maximum_marker_size ** 2)
         *     sizeref: 2.0 * Math.max(...size) / (desired_maximum_marker_size**2),
         *     sizemode: 'area'
         *   }
         * };
         * */
        Map<String, Object> chartData = new HashMap<>();

        List<String> bubbleSize = new ArrayList<>();
        List<String> xAxis = new ArrayList<>();
        List<String> yAxis = new ArrayList<>();
        List<String> text = new ArrayList<>();

        String columnName = queryBubbleSize.get("column").toString();
        String aggregate = queryBubbleSize.get("metrics").toString();
        StringBuilder toggleText = new StringBuilder();
        toggleText.append(aggregate + "(" + columnName + ")");

        queryList.forEach(row -> {
            bubbleSize.add(String.valueOf(Math.round(Double.parseDouble(row.get("bubble").toString()) * 100) / 100.0));
            xAxis.add(row.get("xaxis").toString());
            yAxis.add(row.get("yaxis").toString());
            text.add(queryEntity + "(" + querySingleSeries + ")</br>" + toggleText + " " + row.get("bubble").toString());
        });

        chartData.put("name", queryList.get(0).get("series"));
        chartData.put("x", xAxis);
        chartData.put("y", yAxis);
        chartData.put("text", text);
        chartData.put("mode", "markers");

        int desired_maximum_marker_size = Integer.parseInt(queryMaxBubbleSize);
        String maxBubbleSize = Collections.max(bubbleSize);
        double bubbleSizeRef = 2.0 * Double.parseDouble(maxBubbleSize) / Math.pow(desired_maximum_marker_size, 2);
        Map<String, Object> marker = new HashMap<>();
        marker.put("size", bubbleSize);
        marker.put("sizeref", Math.round(bubbleSizeRef * 100) / 100.0);
        marker.put("sizemode", "area");

        chartData.put("marker", marker);

        return chartData;
    }

    private StringBuilder setBubbleChartQueryBySeries(String type, String datasetName, String timeColumn, String timeRange, String querySingleSeries, String seriesData, String queryEntity, Map<String, Object> queryBubbleSize, Map<String, Object> queryXaxis, Map<String, Object> queryYaxis, List<Map<String, Object>> queryFilters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        // 1) set Column by queryBubbleSize
        String columnName = queryBubbleSize.get("column").toString();
        String aggregate = queryBubbleSize.get("metrics").toString();
        if ("COUNT_DISTINCT".equals(aggregate)) {
            sb.append("COUNT(DISTINCT " + columnName + ") AS bubble");
        } else {
            sb.append(aggregate + "(" + columnName + ") AS bubble");
        }
        // 2) set Column by queryXaxis
        columnName = queryXaxis.get("column").toString();
        aggregate = queryXaxis.get("metrics").toString();
        if ("COUNT_DISTINCT".equals(aggregate)) {
            sb.append(", COUNT(DISTINCT " + columnName + ") AS xaxis");
        } else {
            sb.append(", " + aggregate + "(" + columnName + ") AS xaxis");
        }
        // 3) set Column by queryYaxis
        columnName = queryYaxis.get("column").toString();
        aggregate = queryYaxis.get("metrics").toString();
        if ("COUNT_DISTINCT".equals(aggregate)) {
            sb.append(", COUNT(DISTINCT " + columnName + ") AS yaxis");
        } else {
            sb.append(", " + aggregate + "(" + columnName + ") AS yaxis");
        }
        // 4) set Column by querySingleSeries
        if (StringUtils.isBlank(seriesData)) {
            sb.append(", '' AS series");
        } else {
            sb.append(", '" + seriesData + "' AS series");
        }
        // 5) set Column by queryEntity
        sb.append(", " + queryEntity);
        /** FROM */
        sb.append("  FROM " + datasetName);//Table Name
        /** WHERE */
        sb.append(" WHERE 1=1");
        if (StringUtils.isBlank(querySingleSeries)) {
            // no condition
        } else {
            sb.append("  AND " + querySingleSeries + " = '" + seriesData + "'");
        }
        if (queryFilters.size() > 0 || !StringUtils.isBlank(timeRange)) {
            /** set Query Filters */
            sb.append(this.setConditionsByQueryFilters(queryFilters));

            /** set Time Range condition */
            sb.append(this.setConditionsByTimeRange(type, timeColumn, timeRange));
        }
        /** GROUP BY */
        sb.append(" GROUP BY " + queryEntity);
        /** ORDER BY */

        return sb;
    }

    private String getTimeColumnFormat(String type, String timeColumn, String timeGrain) {
        String timeColumnFormat = "";
        if (TimeGrain.SECOND.getCode().equals(timeGrain.toUpperCase())) {
            //
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y-%m-%d %T')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY-MM-DD HH24:MI:SS')";
            }
        } else if (TimeGrain.MINUTE.getCode().equals(timeGrain.toUpperCase())) {
            //
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y-%m-%d %H:%i')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY-MM-DD HH24:MI')";
            }
        } else if (TimeGrain.HOUR.getCode().equals(timeGrain.toUpperCase())) {
            //
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y-%m-%d %H')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY-MM-DD HH24')";
            }
        } else if (TimeGrain.DAY.getCode().equals(timeGrain.toUpperCase())) {
            //
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y-%m-%d')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY-MM-DD')";
            }
        } else if (TimeGrain.WEEK.getCode().equals(timeGrain.toUpperCase())) {
            //
        } else if (TimeGrain.MONTH.getCode().equals(timeGrain.toUpperCase())) {
            //
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y-%m')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY-MM')";
            }
        } else if (TimeGrain.QUARTER.getCode().equals(timeGrain.toUpperCase())) {
            //
        } else if (TimeGrain.YEAR.getCode().equals(timeGrain.toUpperCase())) {
            //
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY')";
            }
        } else {
            if ("mariadb".equals(type)) {
                timeColumnFormat = "DATE_FORMAT(" + timeColumn + ", '%Y-%m-%d')";
            } else if ("postgresql".equals(type)) {
                //
                timeColumnFormat = "TO_CHAR(" + timeColumn + ", 'YYYY-MM-DD')";
            }
        }

        return timeColumnFormat;
    }

    /**
     * ResultSet을 Row마다 Map에 저장후 List에 다시 저장.
     *
     * @param rs DB에서 가져온 ResultSet
     * @return Listt<map> 형태로 리턴
     * @throws Exception Collection
     */
    private List<Map> getResultMapRows(ResultSet rs) throws SQLException {
        // ResultSet 의 MetaData를 가져온다.
        ResultSetMetaData metaData = rs.getMetaData();
        // ResultSet 의 Column의 갯수를 가져온다.
        int sizeOfColumn = metaData.getColumnCount();

        List<Map> list = new ArrayList<Map>();
        Map<String, Object> map;
        String column;
        // rs의 내용을 돌려준다.
        while (rs.next()) {
            // 내부에서 map을 초기화
            map = new HashMap<String, Object>();
            // Column의 갯수만큼 회전
            for (int indexOfcolumn = 0; indexOfcolumn < sizeOfColumn; indexOfcolumn++) {
                column = metaData.getColumnName(indexOfcolumn + 1);
                // map에 값을 입력 map.put(columnName, columnName으로 getString)
                map.put(column, rs.getString(column));
            }
            // list에 저장
            list.add(map);
        }
        return list;
    }

    /**
     * Where condition by queryFilters
     */
    private StringBuilder setConditionsByQueryFilters(List<Map<String, Object>> queryFilters) {
        StringBuilder sb = new StringBuilder();

        queryFilters.forEach(element -> {
            String column = element.get("column").toString();
            String filter = element.get("filter").toString();
            String input = element.get("input").toString();

            sb.append("   AND UPPER(" + column + ")");
            if (filter.equals(QueryFilters.EQUALS.getCode())) {
                // "="
                sb.append(" " + QueryFilters.EQUALS.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.NOT_EQUAL.getCode())) {
                // "<>"
                sb.append(" " + QueryFilters.NOT_EQUAL.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.GREATER_THAN.getCode())) {
                // ">"
                sb.append(" " + QueryFilters.GREATER_THAN.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.LESS_THAN.getCode())) {
                // "<"
                sb.append(" " + QueryFilters.LESS_THAN.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.GREATER_THAN_EQUAL.getCode())) {
                // ">="
                sb.append(" " + QueryFilters.GREATER_THAN_EQUAL.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.LESS_THAN_EQUAL.getCode())) {
                // "<="
                sb.append(" " + QueryFilters.LESS_THAN_EQUAL.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.IN.getCode())) {
                // IN ()
                sb.append(" " + QueryFilters.IN.getValue());
                input = input.replaceAll(" ", "");
                List<String> list = List.of(input.split(","));
                final int[] i = {0};
                sb.append(" (");
                list.stream()
                        .forEach(item -> {
                            if (i[0] > 0) {
                                sb.append(",");
                            }
                            if (util.isNumeric(item)) {
                                sb.append(item);
                            } else {
                                sb.append(" UPPER('" + item + "')");
                            }
                            i[0]++;
                        });
                sb.append(")");
            } else if (filter.equals(QueryFilters.NOT_IN.getCode())) {
                // NOT IN ()
                sb.append(" " + QueryFilters.NOT_IN.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.LIKE.getCode())) {
                // LIKE '%' input '%'
                sb.append(" " + QueryFilters.LIKE.getValue());
                if (util.isNumeric(input)) {
                    sb.append(" '" + input + "'");
                } else {
                    sb.append(" UPPER('" + input + "')");
                }
            } else if (filter.equals(QueryFilters.IS_NOT_NULL.getCode())) {
                // IS NOT NULL
                sb.append(" " + QueryFilters.IS_NOT_NULL.getValue());
            } else if (filter.equals(QueryFilters.IS_NULL.getCode())) {
                // IS NULL
                sb.append(" " + QueryFilters.IS_NULL.getValue());
            }
        });

        return sb;
    }

    /**
     * Where condition by timeRange
     */
    private String setConditionsByTimeRange(String type, String timeColumn, String timeRange) {
        StringBuilder sb = new StringBuilder();

        if (TimeRange.DAY.getValue().equals(timeRange)) {
            //
            if ("mariadb".equals(type)) {
                sb.append("   AND " + timeColumn + " > DATE_SUB(NOW(), INTERVAL 1 DAY)");
                sb.append("   AND " + timeColumn + " <= NOW()");
            } else if ("postgresql".equals(type)) {
                //
                sb.append("   AND " + timeColumn + " > NOW() - INTERVAL '1 day'");
                sb.append("   AND " + timeColumn + " <= NOW()");
            }
        } else if (TimeRange.WEEK.getValue().equals(timeRange)) {
            //
            if ("mariadb".equals(type)) {
                sb.append("   AND " + timeColumn + " > DATE_SUB(NOW(), INTERVAL 1 WEEK)");
                sb.append("   AND " + timeColumn + " <= NOW()");
            } else if ("postgresql".equals(type)) {
                //
                sb.append("   AND " + timeColumn + " > NOW() - INTERVAL '1 week'");
                sb.append("   AND " + timeColumn + " <= NOW()");
            }
        } else if (TimeRange.MONTH.getValue().equals(timeRange)) {
            //
            if ("mariadb".equals(type)) {
                sb.append("   AND " + timeColumn + " > DATE_SUB(NOW(), INTERVAL 1 MONTH)");
                sb.append("   AND " + timeColumn + " <= NOW()");
            } else if ("postgresql".equals(type)) {
                //
                sb.append("   AND " + timeColumn + " > NOW() - INTERVAL '1 month'");
                sb.append("   AND " + timeColumn + " <= NOW()");
            }
        } else if (TimeRange.QUARTER.getValue().equals(timeRange)) {
            //
            if ("mariadb".equals(type)) {
                sb.append("   AND " + timeColumn + " > DATE_SUB(NOW(), INTERVAL 3 MONTH)");
                sb.append("   AND " + timeColumn + " <= NOW()");
            } else if ("postgresql".equals(type)) {
                //
                sb.append("   AND " + timeColumn + " > NOW() - INTERVAL '3 month'");
                sb.append("   AND " + timeColumn + " <= NOW()");
            }
        } else if (TimeRange.YEAR.getValue().equals(timeRange)) {
            //
            if ("mariadb".equals(type)) {
                sb.append("   AND " + timeColumn + " > DATE_SUB(NOW(), INTERVAL 1 YEAR)");
                sb.append("   AND " + timeColumn + " <= NOW()");
            } else if ("postgresql".equals(type)) {
                //
                sb.append("   AND " + timeColumn + " > NOW() - INTERVAL '1 year'");
                sb.append("   AND " + timeColumn + " <= NOW()");
            }
        }

        return sb.toString();
    }
}
