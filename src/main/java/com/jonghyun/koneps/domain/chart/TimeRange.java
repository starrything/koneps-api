package com.jonghyun.koneps.domain.chart;

import com.jonghyun.koneps.global.util.Pair;
import lombok.Getter;

public enum TimeRange {
    NO_FILTER("NO_FILTER", "no filter"),
    DAY("DAY", "day"),
    WEEK("WEEK", "week"),
    MONTH("MONTH", "month"),
    QUARTER("QUARTER", "quarter"),
    YEAR("YEAR", "year");

    @Getter
    final private String code;
    @Getter
    final private String value;

    TimeRange(String code, String value) {
        this.code = code;
        this.value = value;
    }

    Pair<String, String> getCodeAndValue() { return new Pair<>(code, value); }
}
