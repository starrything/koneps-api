package com.jonghyun.koneps.domain.dashboards.dashboardComponent;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class DashboardComponentKey implements Serializable {
    String dashboardId;
    String componentId;

    @Builder
    public DashboardComponentKey(String dashboardId, String componentId) {
        this.dashboardId = dashboardId;
        this.componentId = componentId;
    }
}
