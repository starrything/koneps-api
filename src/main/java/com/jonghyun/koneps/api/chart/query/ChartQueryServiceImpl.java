package com.jonghyun.koneps.api.chart.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartQueryServiceImpl implements ChartQueryService {
    private final ChartQueryRepository chartQueryRepository;

    @Override
    public boolean addChartQueries(List<ChartQuery> chartQueries) {
        try {
            chartQueries.stream().forEach(e -> {
                chartQueryRepository.save(e);
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
