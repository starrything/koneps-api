package com.jonghyun.koneps.api.dashboards;

import com.jonghyun.koneps.api.chart.Chart;
import com.jonghyun.koneps.api.chart.ChartRepository;
import com.jonghyun.koneps.api.dashboards.dashboardChart.DashboardChart;
import com.jonghyun.koneps.api.dashboards.dashboardChart.DashboardChartRepository;
import com.jonghyun.koneps.api.dashboards.dashboardComponent.DashboardComponent;
import com.jonghyun.koneps.api.dashboards.dashboardComponent.DashboardComponentRepository;
import com.jonghyun.koneps.api.system.seq.SeqService;
import com.jonghyun.koneps.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final Util util;
    private final SeqService seqService;
    private final ChartRepository chartRepository;
    private final DashboardRepository dashboardRepository;
    private final DashboardComponentRepository dashboardComponentRepository;
    private final DashboardChartRepository dashboardChartRepository;

    @Override
    public List<Map<String, Object>> searchDashboardList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();
        dashboardRepository.findByDashboardIdContainsOrDashboardTitleContainsOrStatusContainsOrCreatedBy(keyword, keyword, keyword, keyword)
                .stream()
                .forEach(dashboard -> {
                    String modifiedBy = "";
                    String modifiedDate = "";
                    if (dashboard.getModifiedDate() != null) {
                        modifiedBy = dashboard.getModifiedBy();
                        modifiedDate = dashboard.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } else {
                        modifiedBy = dashboard.getCreatedBy();
                        modifiedDate = dashboard.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("dashboardId", dashboard.getDashboardId());
                    map.put("dashboardTitle", dashboard.getDashboardTitle());
                    map.put("status", dashboard.getStatus());
                    map.put("modifiedBy", modifiedBy);
                    map.put("modifiedDate", modifiedDate);
                    map.put("createdBy", dashboard.getCreatedBy());
                    map.put("action", "");

                    result.add(map);
                });

        // Sorting result list by modifiedDate descending
        util.getCollectionSortByModifiedDate(result, "d");

        return result;
    }

    @Override
    @Transactional
    public boolean deleteDashboard(String dashboardId) {
        try {
            dashboardChartRepository.deleteAllByDashboardId(dashboardId);
            dashboardComponentRepository.deleteAllByDashboardId(dashboardId);
            dashboardRepository.deleteById(dashboardId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String createDashboard(DashboardDto dashboardDto) {
        String dashboardId = seqService.getSequenceBySeqPrefix("dashboard");
        String dashboardTitle = dashboardDto.getDashboardTitle();
        List<Map<String, Object>> dashboardLayout = dashboardDto.getDashboardLayout();

        /*
         * 1. add Dashboard (Parent table)
         * */
        String loginId = util.getLoginId();
        Dashboard dashboard = new Dashboard();
        dashboard.newDashboard(
                dashboardId,
                dashboardTitle,
                "draft",
                loginId,
                LocalDateTime.now(),
                loginId,
                LocalDateTime.now()
        );
        dashboardRepository.save(dashboard);

        /*
         * 2. add Dashboard panels
         * - Components : Header, Divider
         * - Charts : Bar, Line, Bubble, ...
         * */
        dashboardLayout.forEach(layout -> {
            String feature = layout.get("feature").toString();
            String builderType = "component";
            boolean chartYn = feature.contains("chart");
            if (chartYn) {
                builderType = "chart";
            }
            String panelId = layout.get("panelId").toString();
            String index = layout.get("i").toString();
            String inputValue = layout.get("value").toString();
            Integer x = Integer.parseInt(layout.get("x").toString());
            Integer y = Integer.parseInt(layout.get("y").toString());
            Integer w = Integer.parseInt(layout.get("w").toString());
            Integer h = Integer.parseInt(layout.get("h").toString());
            Integer minW = Integer.parseInt("null".equals(String.valueOf(layout.get("minW"))) ? "-1" : String.valueOf(layout.get("minW")));
            Integer maxW = Integer.parseInt("null".equals(String.valueOf(layout.get("maxW"))) ? "-1" : String.valueOf(layout.get("maxW")));
            Integer minH = Integer.parseInt("null".equals(String.valueOf(layout.get("minH"))) ? "-1" : String.valueOf(layout.get("minH")));
            Integer maxH = Integer.parseInt("null".equals(String.valueOf(layout.get("maxH"))) ? "-1" : String.valueOf(layout.get("maxH")));

            if ("chart".equals(builderType)) {
                Optional<Chart> chart = chartRepository.findById(feature);
                if (chart.isPresent()) {
                    String chartId = chart.get().getChartId();
                    String chartType = chart.get().getChartType();
                    DashboardChart dashboardChart = new DashboardChart(
                            dashboardId,
                            chartId,
                            chartType,
                            panelId,
                            index,
                            x,
                            y,
                            w,
                            h,
                            minW,
                            maxW,
                            minH,
                            maxH);

                    dashboardChartRepository.save(dashboardChart);
                }
            } else if ("component".equals(builderType)) {
                String componentId = seqService.getSequenceBySeqPrefix("component");
                String componentType = feature;
                DashboardComponent dashboardComponent = new DashboardComponent(
                        dashboardId,
                        componentId,
                        componentType,
                        inputValue,
                        panelId,
                        index,
                        x,
                        y,
                        w,
                        h,
                        minW,
                        maxW,
                        minH,
                        maxH);

                dashboardComponentRepository.save(dashboardComponent);
            }
        });

        return dashboardId;
    }

    @Override
    public Map<String, Object> getDashboardById(String dashboardId) {
        Map<String, Object> result = new HashMap<>();

        /*
        * 1. get title and status
        * */
        dashboardRepository.findById(dashboardId).ifPresent(c -> {
            String dashboardTitle = c.getDashboardTitle();
            String dashboardStatus = c.getStatus();

            result.put("title",dashboardTitle);
            result.put("status", dashboardStatus);
        });

        /*
        * 2. get Dashboard Components
        * */
        List<Map<String, Object>> componentList = new ArrayList<>();
        List<DashboardComponent> dashboardComponents = dashboardComponentRepository.findByDashboardId(dashboardId);
        if(dashboardComponents.size() > 0) {
          dashboardComponents.forEach(c -> {
              Map<String, Object> dataGrid = new HashMap<>();
              dataGrid.put("x", c.getElementX());
              dataGrid.put("y", c.getElementY());
              dataGrid.put("w", c.getElementW());
              dataGrid.put("h", c.getElementH());
              dataGrid.put("minW", c.getMinW() == -1 ? null : c.getMinW());
              dataGrid.put("maxW", c.getMaxW() == -1 ? null : c.getMaxW());
              dataGrid.put("minH", c.getMinH() == -1 ? null : c.getMinH());
              dataGrid.put("maxH", c.getMaxH() == -1 ? null : c.getMaxH());
              dataGrid.put("isResizable", c.getRemark1());

              Map<String, Object> component = new HashMap<>();
              component.put("panelId", c.getPanelId());
              component.put("index", c.getElementI());
              component.put("type", "component");
              component.put("component", c.getComponentType());
              component.put("value", c.getComponentValue());
              component.put("dataGrid", dataGrid);

              componentList.add(component);
          });
        }
        result.put("component", componentList);

        /*
         * 3. get Dashboard Components
         * */
        List<Map<String, Object>> chartList = new ArrayList<>();
        List<DashboardChart> dashboardCharts = dashboardChartRepository.findByDashboardId(dashboardId);
        if(dashboardCharts.size() > 0) {
            dashboardCharts.forEach(c -> {
                Map<String, Object> dataGrid = new HashMap<>();
                dataGrid.put("x", c.getElementX());
                dataGrid.put("y", c.getElementY());
                dataGrid.put("w", c.getElementW());
                dataGrid.put("h", c.getElementH());
                dataGrid.put("minW", c.getMinW() == -1 ? null : c.getMinW());
                dataGrid.put("maxW", c.getMaxW() == -1 ? null : c.getMaxW());
                dataGrid.put("minH", c.getMinH() == -1 ? null : c.getMinH());
                dataGrid.put("maxH", c.getMaxH() == -1 ? null : c.getMaxH());
                dataGrid.put("isResizable", c.getRemark1());

                Map<String, Object> chart = new HashMap<>();
                chart.put("panelId", c.getPanelId());
                chart.put("index", c.getElementI());
                chart.put("type", "chart");
                chart.put("chartId", c.getChartId());
                chart.put("chartType", c.getChartType());
                chart.put("dataGrid", dataGrid);

                chartRepository.findById(c.getChartId()).ifPresent(e -> {
                    chart.put("dataset", e.getDatasetId());
                });

                chartList.add(chart);
            });
        }
        result.put("chart", chartList);

        result.put("count", componentList.size() + chartList.size());

        return result;
    }

    @Override
    public String getDashboardTitle(String dashboardId) {
        return dashboardRepository.findById(dashboardId).get().getDashboardTitle();
    }

    @Override
    @Transactional
    public String updateDashboard(DashboardDto dashboardDto) {
        String dashboardId = dashboardDto.getDashboardId();
        String dashboardTitle = dashboardDto.getDashboardTitle();
        List<Map<String, Object>> dashboardLayout = dashboardDto.getDashboardLayout();

        String loginId = util.getLoginId();
        dashboardRepository.findById(dashboardId).ifPresent(c -> {
            /*
             * 1. add Dashboard (Parent table)
             * */
            c.updateDashboard(dashboardTitle,
                    dashboardDto.getStatus(),
                    loginId,
                    LocalDateTime.now()
                    );

            dashboardRepository.save(c);
        });

        dashboardChartRepository.deleteAllByDashboardId(dashboardId);
        dashboardComponentRepository.deleteAllByDashboardId(dashboardId);

        /*
         * 2. add Dashboard panels
         * - Components : Header, Divider
         * - Charts : Bar, Line, Bubble, ...
         * */
        dashboardLayout.forEach(layout -> {
            String feature = layout.get("feature").toString();
            String builderType = "component";
            boolean chartYn = feature.contains("chart");
            if (chartYn) {
                builderType = "chart";
            }
            String panelId = layout.get("panelId").toString();
            String index = layout.get("i").toString();
            String inputValue = "null".equals(String.valueOf(layout.get("value")))? "" : layout.get("value").toString();
            Integer x = Integer.parseInt(layout.get("x").toString());
            Integer y = Integer.parseInt(layout.get("y").toString());
            Integer w = Integer.parseInt(layout.get("w").toString());
            Integer h = Integer.parseInt(layout.get("h").toString());
            Integer minW = Integer.parseInt("null".equals(String.valueOf(layout.get("minW"))) ? "-1" : String.valueOf(layout.get("minW")));
            Integer maxW = Integer.parseInt("null".equals(String.valueOf(layout.get("maxW"))) ? "-1" : String.valueOf(layout.get("maxW")));
            Integer minH = Integer.parseInt("null".equals(String.valueOf(layout.get("minH"))) ? "-1" : String.valueOf(layout.get("minH")));
            Integer maxH = Integer.parseInt("null".equals(String.valueOf(layout.get("maxH"))) ? "-1" : String.valueOf(layout.get("maxH")));

            if ("chart".equals(builderType)) {
                Optional<Chart> chart = chartRepository.findById(feature);
                if (chart.isPresent()) {
                    String chartId = chart.get().getChartId();
                    String chartType = chart.get().getChartType();
                    DashboardChart dashboardChart = new DashboardChart(
                            dashboardId,
                            chartId,
                            chartType,
                            panelId,
                            index,
                            x,
                            y,
                            w,
                            h,
                            minW,
                            maxW,
                            minH,
                            maxH);

                    dashboardChartRepository.save(dashboardChart);
                }
            } else if ("component".equals(builderType)) {
                String componentId = seqService.getSequenceBySeqPrefix("component");
                String componentType = feature;
                DashboardComponent dashboardComponent = new DashboardComponent(
                        dashboardId,
                        componentId,
                        componentType,
                        inputValue,
                        panelId,
                        index,
                        x,
                        y,
                        w,
                        h,
                        minW,
                        maxW,
                        minH,
                        maxH);

                dashboardComponentRepository.save(dashboardComponent);
            }
        });

        return dashboardId;
    }
}
