package com.jonghyun.koneps.domain.mainpage;

import com.jonghyun.koneps.domain.chart.ChartRepository;
import com.jonghyun.koneps.domain.dashboards.DashboardRepository;
import com.jonghyun.koneps.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
    private final DashboardRepository dashboardRepository;
    private final ChartRepository chartRepository;
    private final Util util;

    @Override
    public List<Map<String, Object>> getRecentDashboardList(String loginId) {
        // TODO: loginId
        String username = util.getLoginId();
        List<Map<String, Object>> result = new ArrayList<>();

        dashboardRepository.findTop3ByOrderByModifiedDateDescCreationDateDesc()
                .stream()
                .forEach(c -> {
                    String modifiedDate = "";
                    if(c.getModifiedDate() != null) {
                        modifiedDate = c.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                    String dateDiffUnit = " days";
                    Long dateDiff = 0L;
                    LocalDateTime now = LocalDateTime.now();
                    if (c.getModifiedDate() != null) {
                        dateDiff = ChronoUnit.DAYS.between(c.getModifiedDate(), now);
                    } else {
                        dateDiff = ChronoUnit.DAYS.between(c.getCreationDate(), now);
                    }
                    if (dateDiff > 31) {
                        dateDiffUnit = " months";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.MONTHS.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.MONTHS.between(c.getCreationDate(), now);
                        }
                    } else if (dateDiff > 365) {
                        dateDiffUnit = " years";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.YEARS.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.YEARS.between(c.getCreationDate(), now);
                        }
                    }

                    if (dateDiff == 0) {
                        dateDiffUnit = " hours";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.HOURS.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.HOURS.between(c.getCreationDate(), now);
                        }
                    }

                    if (dateDiff == 0) {
                        dateDiffUnit = " minutes";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.MINUTES.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.MINUTES.between(c.getCreationDate(), now);
                        }
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("dashboardId", c.getDashboardId());
                    map.put("dashboardTitle", c.getDashboardTitle());
                    map.put("modifiedFrom", dateDiff + dateDiffUnit);

                    result.add(map);
                });

        return result;
    }

    @Override
    public List<Map<String, Object>> getRecentChartList(String loginId) {
        // TODO: loginId
        String username = util.getLoginId();
        List<Map<String, Object>> result = new ArrayList<>();

        chartRepository.findTop3ByOrderByModifiedDateDescCreationDateDesc()
                .stream()
                .forEach(c -> {
                    String modifiedDate = "";
                    if(c.getModifiedDate() != null) {
                        modifiedDate = c.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                    String dateDiffUnit = " days";
                    Long dateDiff = 0L;
                    LocalDateTime now = LocalDateTime.now();
                    if (c.getModifiedDate() != null) {
                        dateDiff = ChronoUnit.DAYS.between(c.getModifiedDate(), now);
                    } else {
                        dateDiff = ChronoUnit.DAYS.between(c.getCreationDate(), now);
                    }
                    if (dateDiff > 31) {
                        dateDiffUnit = " months";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.MONTHS.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.MONTHS.between(c.getCreationDate(), now);
                        }
                    } else if (dateDiff > 365) {
                        dateDiffUnit = " years";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.YEARS.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.YEARS.between(c.getCreationDate(), now);
                        }
                    }

                    if (dateDiff == 0) {
                        dateDiffUnit = " hours";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.HOURS.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.HOURS.between(c.getCreationDate(), now);
                        }
                    }

                    if (dateDiff == 0) {
                        dateDiffUnit = " minutes";
                        if (c.getModifiedDate() != null) {
                            dateDiff = ChronoUnit.MINUTES.between(c.getModifiedDate(), now);
                        } else {
                            dateDiff = ChronoUnit.MINUTES.between(c.getCreationDate(), now);
                        }
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("chartId", c.getChartId());
                    map.put("chartTitle", c.getChartTitle());
                    map.put("modifiedFrom", dateDiff + dateDiffUnit);

                    result.add(map);
                });

        return result;
    }
}
