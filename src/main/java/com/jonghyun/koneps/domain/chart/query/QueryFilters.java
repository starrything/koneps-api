package com.jonghyun.koneps.domain.chart.query;

import com.jonghyun.koneps.global.util.Pair;
import lombok.Getter;

public enum QueryFilters {
    EQUALS("EQUALS", "="),
    NOT_EQUAL("NOT_EQUAL", "<>"),
    GREATER_THAN("GREATER_THAN", ">"),
    LESS_THAN("LESS_THAN", "<"),
    GREATER_THAN_EQUAL("GREATER_THAN_EQUAL", ">="),
    LESS_THAN_EQUAL("LESS_THAN_EQUAL", "<="),
    IN("IN", "IN"),
    NOT_IN("NOT_IN", "NOT IN"),
    LIKE("LIKE", "LIKE"),
    IS_NOT_NULL("IS_NOT_NULL", "IS NOT NULL"),
    IS_NULL("IS_NULL", "IS NULL");

    @Getter
    final private String code;
    @Getter
    final private String value;

    QueryFilters(String code, String value) {
        this.code = code;
        this.value = value;
    }

    Pair<String, String> getCodeAndValue() { return new Pair<>(code, value); }

}
