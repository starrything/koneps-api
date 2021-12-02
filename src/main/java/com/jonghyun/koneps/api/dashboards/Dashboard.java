package com.jonghyun.koneps.api.dashboards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_dashboard")
public class Dashboard {
    @Id
    @Column(name = "dashboard_id")
    String dashboardId;

    @Column(name = "dashboard_title")
    String dashboardTitle;

    @Column(name = "status")
    String status;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    @Builder
    public Dashboard(String dashboardId, String dashboardTitle, String status, String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modifiedDate) {
        this.dashboardId = dashboardId;
        this.dashboardTitle = dashboardTitle;
        this.status = status;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public Dashboard newDashboard(String dashboardId, String dashboardTitle, String status, String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modifiedDate) {
        this.dashboardId = dashboardId;
        this.dashboardTitle = dashboardTitle;
        this.status = status;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.createdBy = modifiedBy;
        this.creationDate = modifiedDate;

        return this;
    }

    public Dashboard updateDashboard(String dashboardTitle, String statue, String modifiedBy, LocalDateTime modifiedDate) {
        this.dashboardTitle = dashboardTitle;
        this.status = status;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;

        return this;
    }
}
