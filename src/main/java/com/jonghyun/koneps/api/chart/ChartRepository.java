package com.jonghyun.koneps.api.chart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<Chart, String> {
    List<Chart> findByChartTitleContainsOrChartTypeContainsOrCreatedByContains(String chartTitle, String chartType, String createdBy);

    List<Chart> findTop3ByOrderByModifiedDateDescCreationDateDesc();
}
