package com.jonghyun.koneps.api.chart.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@IdClass(ChartQueryKey.class)
@Table(name = "tb_chart_query")
public class ChartQuery {
    @Id
    @Column(name = "query_id")
    String queryId;

    @Column(name = "query_mode")
    String queryMode;

    @Column(name = "query_type")
    String queryType;

    @Id
    @Column(name = "option_code")
    String optionCode;

    @Id
    @Column(name = "option_value")
    String optionValue;

    @Column(name = "option_order")
    String optionOrder;

    @Column(name = "option_value_remark1")
    String optionValueRemark1;

    @Column(name = "option_value_remark2")
    String optionValueRemark2;

    @Column(name = "option_value_remark3")
    String optionValueRemark3;

    @Column(name = "option_value_remark4")
    String optionValueRemark4;

    @Builder
    public ChartQuery(String queryId, String queryMode, String queryType, String optionCode, String optionValue, String optionOrder, String optionValueRemark1, String optionValueRemark2, String optionValueRemark3, String optionValueRemark4) {
        this.queryId = queryId;
        this.queryMode = queryMode;
        this.queryType = queryType;
        this.optionCode = optionCode;
        this.optionValue = optionValue;
        this.optionOrder = optionOrder;
        this.optionValueRemark1 = optionValueRemark1;
        this.optionValueRemark2 = optionValueRemark2;
        this.optionValueRemark3 = optionValueRemark3;
        this.optionValueRemark4 = optionValueRemark4;
    }
}
