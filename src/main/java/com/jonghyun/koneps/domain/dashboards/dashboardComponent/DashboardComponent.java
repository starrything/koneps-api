package com.jonghyun.koneps.domain.dashboards.dashboardComponent;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@IdClass(DashboardComponentKey.class)
@Table(name = "tb_dashboard_component")
public class DashboardComponent {
    @Id
    @Column(name = "dashboard_id")
    String dashboardId;

    @Id
    @Column(name = "component_id")
    String componentId;

    @Column(name = "component_type")
    String componentType;

    @Column(name = "component_value")
    String componentValue;

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
    public DashboardComponent(String dashboardId, String componentId, String componentType, String componentValue, String panelId, String elementI, Integer elementX, Integer elementY, Integer elementW, Integer elementH, Integer minW, Integer maxW, Integer minH, Integer maxH) {
        this.dashboardId = dashboardId;
        this.componentId = componentId;
        this.componentType = componentType;
        this.componentValue = componentValue;
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
