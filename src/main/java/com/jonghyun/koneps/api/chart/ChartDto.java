package com.jonghyun.koneps.api.chart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ChartDto implements Serializable {
    String chartId;

    String chartTitle;

    String datasetId;

    String chartType;

    String timeColumn;

    String timeGrain;

    String timeRange;

    String timeRangeStart;

    String timeRangeEnd;

    String queryId;

    String queryMode;

    String createdBy;

    LocalDateTime creationDate;

    String modifiedBy;

    LocalDateTime modifiedDate;

    /*
    * request-parameters for Query Options of MakeChart.js(Front-end Chart Screen)
    * */
    List<Map<String, Object>> queryColumns = new ArrayList<>();
    List<Map<String, Object>> queryOrdering = new ArrayList<>();
    String queryRowLimit;
    List<Map<String, Object>> queryFilters = new ArrayList<>();
    Map<String, Object> queryMetric = new HashMap<>();
    List<Map<String, Object>> queryMetrics = new ArrayList<>();
    List<Map<String, Object>> querySeries = new ArrayList<>();
    List<Map<String, Object>> queryGroupBy = new ArrayList<>();
    String querySingleSeries;
    String queryEntity;
    Map<String, Object> queryBubbleSize = new HashMap<>();
    Map<String, Object> queryXaxis = new HashMap<>();
    Map<String, Object> queryYaxis = new HashMap<>();
    String queryMaxBubbleSize;
    List<Map<String, Object>> filtersConfiguration = new ArrayList<>();
    String optionsNumberFormat;
}
