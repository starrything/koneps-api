package com.jonghyun.koneps.domain.dashboards.dashboardComponent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface DashboardComponentRepository extends JpaRepository<DashboardComponent, DashboardComponentKey> {
    List<DashboardComponent> findByDashboardId(String dashboardId);

    void deleteAllByDashboardId(String dashboardId);
}
