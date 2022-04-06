package com.jonghyun.koneps.domain.dashboards;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DashboardDto implements Serializable {
    String dashboardId;
    String dashboardTitle;
    String status;
    String createdBy;
    LocalDateTime creationDate;
    String modifiedBy;
    LocalDateTime modifiedDate;

    List<Map<String, Object>> dashboardLayout;
}
