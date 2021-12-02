package com.jonghyun.koneps.api.chart;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_chart")
public class Chart {

    @Id
    @Column(name = "chart_id")
    String chartId;

    @Column(name = "dataset_id")
    String datasetId;

    @Column(name = "chart_type")
    String chartType;

    @Column(name = "auth_level")
    String authLevel;

    @Column(name = "chart_title")
    String chartTitle;

    @Column(name = "time_column")
    String timeColumn;

    @Column(name = "time_grain")
    String timeGrain;

    @Column(name = "time_range")
    String timeRange;

    @Column(name = "time_range_start")
    String timeRangeStart;

    @Column(name = "time_range_end")
    String timeRangeEnd;

    @Column(name = "query_id")
    String queryId;

    @Column(name = "query_mode")
    String queryMode;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    public Chart newChart(String chartId, String datasetId, String chartType, String authLevel, String chartTitle, String timeColumn, String timeGrain, String timeRange, String queryId, String queryMode, String createdBy, LocalDateTime creationDate) {
        this.chartId = chartId;
        this.datasetId = datasetId;
        this.chartType = chartType;
        this.authLevel = authLevel;
        this.chartTitle = chartTitle;
        this.timeColumn = timeColumn;
        this.timeGrain = timeGrain;
        this.timeRange = timeRange;
        this.queryId = queryId;
        this.queryMode = queryMode;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }

    public Chart updateChart(String chartType, String chartTitle, String timeColumn, String timeGrain, String timeRange, String queryId, String queryMode, String modifiedBy, LocalDateTime modifiedDate) {
        this.chartType = chartType;
        this.chartTitle = chartTitle;
        this.timeColumn = timeColumn;
        this.timeGrain = timeGrain;
        this.timeRange = timeRange;
        this.queryId = queryId;
        this.queryMode = queryMode;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;

        return this;
    }
}
