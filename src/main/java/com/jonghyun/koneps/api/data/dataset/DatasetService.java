package com.jonghyun.koneps.api.data.dataset;

import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface DatasetService {
    List<Map<String, Object>> getDatasourceList();

    List<String> getCatalogSchemaByDatabase(String databaseId);

    List<String> getTablesByDatabase(String databaseId, String catalogSchema);

    List<Map<String, Object>> searchDatasetList(String keyword);

    @Modifying
    boolean addDataset(DatasetDto datasetDto);

    @Modifying
    boolean deleteDataset(String datasetId);

    @Modifying
    boolean updateDataset(DatasetDto datasetDto);

    Dataset getDatasetByDatasetId(String datasetId);

    List<Map<String, Object>> getDatasetSpecByDatasetId(String datasetId);
}
