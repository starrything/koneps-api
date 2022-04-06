package com.jonghyun.koneps.domain.dashboards.dashboardChart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class DashboardChartKey implements Serializable {
    String dashboardId;
    String chartId;

    @Builder
    public DashboardChartKey(String dashboardId, String chartId) {
        this.dashboardId = dashboardId;
        this.chartId = chartId;
    }
}
