package com.jonghyun.koneps.domain.chart.query;

import com.jonghyun.koneps.global.util.Pair;
import lombok.Getter;

public enum QueryOptionCode {
    COLUMNS("COLUMNS", "columns"),
    ORDERING("ORDERING", "ordering"),
    FILTERS("FILTERS", "filters"),
    ROW_LIMIT("ROW_LIMIT", "rowLimit"),
    METRICS("METRICS", "metrics"),
    GROUP_BY("GROUP_BY", "groupBy"),
    SERIES("SERIES", "series"),
    METRIC("METRIC", "metric"),
    SINGLE_SERIES("SINGLE_SERIES", "singleSeries"),
    ENTITY("ENTITY", "entity"),
    BUBBLE_SIZE("BUBBLE_SIZE", "bubbleSize"),
    XAXIS("XAXIS", "xAxis"),
    YAXIS("YAXIS", "yAxis"),
    MAX_BUBBLE_SIZE("MAX_BUBBLE_SIZE", "maxBubbleSize"),
    FILTER_BOX("FILTER_BOX", "filterBox"),
    NUMBER_FORMAT("NUMBER_FORMAT", "numberFormat");

    @Getter
    final private String code;
    @Getter
    final private String value;

    QueryOptionCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    Pair<String, String> getCodeAndValue() { return new Pair<>(code, value); }
}
