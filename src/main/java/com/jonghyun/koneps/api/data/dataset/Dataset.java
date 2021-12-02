package com.jonghyun.koneps.api.data.dataset;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_data_dataset")
public class Dataset {
    @Id
    @Column(name = "dataset_id")
    String datasetId;

    @Column(name = "dataset_name")
    String datasetName;

    @Column(name = "dataset_type")
    String datasetType;

    @Column(name = "database_id")
    String databaseId;

    @Column(name = "source")
    String source;

    @Column(name = "schema")
    String schema;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    @Builder
    public Dataset(String datasetId, String datasetName, String datasetType, String databaseId, String source, String schema, String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modifiedDate) {
        this.datasetId = datasetId;
        this.datasetName = datasetName;
        this.datasetType = datasetType;
        this.databaseId = databaseId;
        this.source = source;
        this.schema = schema;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public Dataset newDataset(String datasetId, String datasetName, String datasetType, String databaseId, String source, String schema, String createdBy, LocalDateTime creationDate) {
        this.datasetId = datasetId;
        this.datasetName = datasetName;
        this.datasetType = datasetType;
        this.databaseId = databaseId;
        this.source = source;
        this.schema = schema;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }

    public Dataset editDataset(String datasetName, String datasetType, String schema, String modifiedBy, LocalDateTime modifiedDate) {
        this.datasetName = datasetName;
        this.datasetType = datasetType;
        this.schema = schema;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;

        return this;
    }
}
