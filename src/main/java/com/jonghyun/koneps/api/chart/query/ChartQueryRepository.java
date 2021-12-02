package com.jonghyun.koneps.api.chart.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartQueryRepository extends JpaRepository<ChartQuery, ChartQueryKey> {
    List<ChartQuery> findByQueryId(String queryId);

    void deleteByQueryId(String bfChartQueryId);

    Optional<ChartQuery> findByQueryIdAndQueryTypeAndOptionCode(String queryId, String queryType, String optionCode);
}
