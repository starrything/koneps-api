package com.jonghyun.koneps.domain.chart;

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
@RequestMapping("/api/chart")
public class ChartController {
    private final ChartService chartService;

    /**
     * Subject : get Chart by Chart ID
     * */
    @GetMapping("/{chartId}")
    public Chart getChartByChartId(@PathVariable(required = true) String chartId) {
        return chartService.getChartByChartId(chartId);
    }

    /**
     * Subject : Chart 찾기
     */
    @GetMapping("/search")
    public List<Map<String, Object>> searchRegisteredChart(@RequestParam String keyword) {
        return chartService.searchChartList(keyword);
    }

    /**
     * Subject : Chart 찾기
     * Front : Dashboard menu
     */
    @GetMapping("/dashboard-component/search")
    public List<Map<String, Object>> searchRegisteredChartFromDashboard(@RequestParam String keyword) {
        return chartService.searchChartListFromDashboard(keyword);
    }

    /**
     * Subject : Chart 생성, Chart 수정
     */
    @PostMapping
    public boolean createChart(@RequestBody ChartDto chartDto) {
        String chartId = chartDto.getChartId();
        if(StringUtils.isEmpty(chartId)) {
            return chartService.createNewChart(chartDto);
        } else {
            return chartService.updateChart(chartDto);
        }
    }

    /**
     * Subject : delete Chart
     */
    @DeleteMapping
    public boolean deleteChart(@RequestParam String chartId) {
        if (chartService.deleteChart(chartId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Subject : Get table raw-data
     */
    @PostMapping("/get-table")
    public List<Map<String, Object>> getTableData(@RequestBody ChartDto chartDto) {
        return chartService.getTableData(chartDto);
    }

    /**
     * Subject : Get line chart raw-data
     */
    @PostMapping("/get-line")
    public List<Map<String, Object>> getLineChartData(@RequestBody ChartDto chartDto) {
        return chartService.getLineChartData(chartDto);
    }

    /**
     * Subject : Get bar chart raw-data
     */
    @PostMapping("/get-bar")
    public List<Map<String, Object>> getBarChartData(@RequestBody ChartDto chartDto) {
        return chartService.getBarChartData(chartDto);
    }

    /**
     * Subject : Get pie chart raw-data
     */
    @PostMapping("/get-pie")
    public List<Map<String, Object>> getPieChartData(@RequestBody ChartDto chartDto) {
        return chartService.getPieChartData(chartDto);
    }

    /**
     * Subject : Get bubble chart raw-data
     */
    @PostMapping("/get-bubble")
    public List<Map<String, Object>> getBubbleChartData(@RequestBody ChartDto chartDto) {
        return chartService.getBubbleChartData(chartDto);
    }

    /**
     * Subject : Get big number raw-data
     */
    @PostMapping("/get-bignumber")
    public Map<String, Object> getBigNumberData(@RequestBody ChartDto chartDto) {
        return chartService.getBigNumberData(chartDto);
    }

    /**
     * Subject : Get filter box raw-data
     */
    @PostMapping("/get-filterbox")
    public List<Map<String, Object>> getFilterBoxData(@RequestBody ChartDto chartDto) {
        return chartService.getFilterBoxData(chartDto);
    }

    @GetMapping("/spec")
    public Map<String, Object> getChartSpecByChartId(@RequestParam String chartId) {
        return chartService.getChartSpecByChartId(chartId);
    }
}
