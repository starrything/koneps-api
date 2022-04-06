package com.jonghyun.koneps.domain.dashboards.dashboardChart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardChartRepository extends JpaRepository<DashboardChart, DashboardChartKey> {
    List<DashboardChart> findByDashboardId(String dashboardId);

    void deleteAllByDashboardId(String dashboardId);
}
