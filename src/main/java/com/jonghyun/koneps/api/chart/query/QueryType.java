package com.jonghyun.koneps.api.chart.query;

import com.jonghyun.koneps.core.util.Pair;
import lombok.Getter;

public enum QueryType {
    QUERY("QUERY", "Query"),
    OPTIONS("OPTIONS", "Options"),
    FILTERS("FILTERS", "Filters");

    @Getter
    final private String code;
    @Getter
    final private String value;

    QueryType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    Pair<String, String> getCodeAndValue() { return new Pair<>(code, value); }
}
