package com.jonghyun.koneps.api.chart.query;

import com.jonghyun.koneps.core.util.Pair;
import lombok.Getter;

public enum QueryMode {
    AGGREGATE("AGGREGATE", "aggregate"),
    RAW_RECORD("RAW_RECORD", "raw record");

    @Getter
    final private String code;
    @Getter
    final private String value;

    QueryMode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    Pair<String, String> getCodeAndValue() { return new Pair<>(code, value); }
}
