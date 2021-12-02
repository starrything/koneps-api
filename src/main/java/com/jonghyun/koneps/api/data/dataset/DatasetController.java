package com.jonghyun.koneps.api.data.dataset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data/dataset")
public class DatasetController {
    private final DatasetService datasetService;

    /**
     * Subject : Data Source List
     * Response : {Type: "", DatasourceName: ""}
     */
    @GetMapping("/datasource/list")
    public List<Map<String, Object>> getDatasourceList() {
        return datasetService.getDatasourceList();
    }

    /**
     * Subject : Database > schema
     * Description: mariadb의 경우 schema가 나오지 않아 catalog로 표시
     */
    @GetMapping("/catalog-schema")
    public List<String> getCatalogSchemaByDatabase(@RequestParam String databaseId) {
        return datasetService.getCatalogSchemaByDatabase(databaseId);
    }

    /**
     * Subject : Database > schema > tables
     */
    @GetMapping("/tables")
    public List<String> getTablesByDatabase(@RequestParam String databaseId, @RequestParam String catalogSchema) {
        return datasetService.getTablesByDatabase(databaseId, catalogSchema);
    }

    /**
     * Subject : Dataset 찾기
     */
    @GetMapping("/search")
    public List<Map<String, Object>> searchRegisteredDataset(@RequestParam String keyword) {
        return datasetService.searchDatasetList(keyword);
    }

    /**
     * Subject : add Dataset
     */
    @PostMapping
    public boolean addDataset(@RequestBody DatasetDto datasetDto) {
        if (datasetService.addDataset(datasetDto)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Subject : delete Dataset
     */
    @DeleteMapping
    public boolean deleteDataset(@RequestParam String datasetId) {
        if (datasetService.deleteDataset(datasetId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Subject : update Dataset
     * */
    @PutMapping
    public boolean updateDataset(@RequestBody DatasetDto datasetDto) {
        if (datasetService.updateDataset(datasetDto)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Subject : Dataset findById
     * used by Chart page (Left Panel)
     * */
    @GetMapping("/{datasetId}")
    public Dataset getDatasetByDatasetId(@PathVariable(required = true) String datasetId) {
        return datasetService.getDatasetByDatasetId(datasetId);
    }

    /**
     * Subject : Spec of dataset (Column Name, Column Type, Column Size, ...)
     * used by Chart page (Left Panel)
     * */
    @GetMapping("/spec/{datasetId}")
    public List<Map<String, Object>> getDatasetSpecByDatasetId(@PathVariable(required = true) String datasetId) {
        return datasetService.getDatasetSpecByDatasetId(datasetId);
    }
}
