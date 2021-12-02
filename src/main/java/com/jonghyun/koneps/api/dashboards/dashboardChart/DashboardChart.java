package com.jonghyun.koneps.api.dashboards.dashboardChart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@IdClass(DashboardChartKey.class)
@Table(name = "tb_dashboard_chart")
public class DashboardChart {
    @Id
    @Column(name = "dashboard_id")
    String dashboardId;

    @Id
    @Column(name = "chart_id")
    String chartId;

    @Column(name = "chart_type")
    String chartType;

    @Column(name = "panel_id")
    String panelId;

    @Column(name = "element_i")
    String elementI;

    @Column(name = "element_x")
    Integer elementX;

    @Column(name = "element_y")
    Integer elementY;

    @Column(name = "element_w")
    Integer elementW;

    @Column(name = "element_h")
    Integer elementH;

    @Column(name = "min_w")
    Integer minW;

    @Column(name = "max_w")
    Integer maxW;

    @Column(name = "min_h")
    Integer minH;

    @Column(name = "max_h")
    Integer maxH;

    @Column(name = "remark1")
    String remark1;

    @Column(name = "remark2")
    String remark2;

    @Column(name = "remark3")
    String remark3;

    @Column(name = "remark4")
    String remark4;

    @Column(name = "remark5")
    String remark5;

    @Builder
    public DashboardChart(String dashboardId, String chartId, String chartType, String panelId, String elementI, Integer elementX, Integer elementY, Integer elementW, Integer elementH, Integer minW, Integer maxW, Integer minH, Integer maxH) {
        this.dashboardId = dashboardId;
        this.chartId = chartId;
        this.chartType = chartType;
        this.panelId = panelId;
        this.elementI = elementI;
        this.elementX = elementX;
        this.elementY = elementY;
        this.elementW = elementW;
        this.elementH = elementH;
        this.minW = minW;
        this.maxW = maxW;
        this.minH = minH;
        this.maxH = maxH;
    }
}
