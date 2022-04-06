package com.jonghyun.koneps.domain.mainpage;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface MainPageService {
    List<Map<String, Object>> getRecentDashboardList(String loginId);

    List<Map<String, Object>> getRecentChartList(String loginId);
}
