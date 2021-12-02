package com.jonghyun.koneps.api.dashboards;

import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface DashboardService {
    List<Map<String, Object>> searchDashboardList(String keyword);

    @Modifying
    boolean deleteDashboard(String dashboardId);

    @Modifying
    String createDashboard(DashboardDto dashboardDto);

    Map<String, Object> getDashboardById(String dashboardId);

    String getDashboardTitle(String dashboardId);

    @Modifying
    @Transactional
    String updateDashboard(DashboardDto dashboardDto);
}
