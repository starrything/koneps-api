package com.jonghyun.koneps.api.chart.query;

import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ChartQueryService {
    @Modifying
    boolean addChartQueries(List<ChartQuery> chartQueries);
}
