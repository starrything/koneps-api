package com.jonghyun.koneps.domain.mainpage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping("/recent/dashboard")
    public List<Map<String, Object>> getRecentDashboardList(String loginId) {
        return mainPageService.getRecentDashboardList(loginId);
    }

    @GetMapping("/recent/chart")
    public List<Map<String, Object>> getRecentChartList(String loginId) {
        return mainPageService.getRecentChartList(loginId);
    }
}
