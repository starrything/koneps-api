package com.jonghyun.koneps.domain.chart.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ChartQueryKey implements Serializable {
    String queryId;
    String optionCode;
    String optionValue;

    @Builder
    public ChartQueryKey(String queryId, String optionCode, String optionValue) {
        this.queryId = queryId;
        this.optionCode = optionCode;
        this.optionValue = optionValue;
    }
}
