package com.jonghyun.koneps.domain.chart;

import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface ChartService {
    List<Map<String, Object>> searchChartList(String keyword);

    @Modifying
    boolean createNewChart(ChartDto chartDto);

    List<Map<String, Object>> getTableData(ChartDto chartDto);

    @Modifying
    boolean deleteChart(String chartId);

    @Modifying
    boolean updateChart(ChartDto chartDto);

    List<Map<String, Object>> getLineChartData(ChartDto chartDto);

    List<Map<String, Object>> getBarChartData(ChartDto chartDto);

    List<Map<String, Object>> getPieChartData(ChartDto chartDto);

    List<Map<String, Object>> getBubbleChartData(ChartDto chartDto);

    Map<String, Object> getBigNumberData(ChartDto chartDto);

    List<Map<String, Object>> getFilterBoxData(ChartDto chartDto);

    List<Map<String, Object>> searchChartListFromDashboard(String keyword);

    Chart getChartByChartId(String chartId);

    Map<String, Object> getChartSpecByChartId(String chartId);
}
