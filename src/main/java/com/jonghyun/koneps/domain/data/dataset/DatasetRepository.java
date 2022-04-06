package com.jonghyun.koneps.domain.data.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, String> {
    List<Dataset> findByDatasetNameContainsOrDatasetTypeContainsOrSourceContainsOrSchemaContains(String datasetName, String datasetType, String source, String schema);
}
