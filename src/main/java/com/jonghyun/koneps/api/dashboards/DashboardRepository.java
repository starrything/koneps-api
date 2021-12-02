package com.jonghyun.koneps.api.dashboards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, String> {
    List<Dashboard> findByDashboardIdContainsOrDashboardTitleContainsOrStatusContainsOrCreatedBy(String dashboardId, String dashboardTitle, String status, String createdBy);

    List<Dashboard> findTop3ByOrderByModifiedDateDescCreationDateDesc();

    List<Dashboard> findByDashboardTitleContainsIgnoreCaseOrderByModifiedDateDescCreationDateDesc(String dashboardId);
}
