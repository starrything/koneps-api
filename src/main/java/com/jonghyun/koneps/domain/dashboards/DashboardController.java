package com.jonghyun.koneps.domain.dashboards;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/search")
    public List<Map<String, Object>> searchRegisteredDashboard(@RequestParam String keyword) {
        return dashboardService.searchDashboardList(keyword);
    }

    @DeleteMapping
    public boolean deleteDashboard(@RequestParam String dashboardId) {
        if (dashboardService.deleteDashboard(dashboardId)) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping
    public String saveDashboard(@RequestBody DashboardDto dashboardDto) {
        String dashboardId = dashboardDto.getDashboardId();
        if(StringUtils.isEmpty(dashboardId)) {
            return dashboardService.createDashboard(dashboardDto);
        } else {
            return dashboardService.updateDashboard(dashboardDto);
        }
    }

    @GetMapping("/{dashboardId}")
    public Map<String, Object> getDashboardById(@PathVariable(required = true) String dashboardId) {
        return dashboardService.getDashboardById(dashboardId);
    }

    @GetMapping("/title")
    public String getDashboardTitle(@RequestParam String dashboardId) {
        return dashboardService.getDashboardTitle(dashboardId);
    }
}
