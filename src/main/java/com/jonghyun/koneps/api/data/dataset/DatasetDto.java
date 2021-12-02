package com.jonghyun.koneps.api.data.dataset;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class DatasetDto implements Serializable {
    String datasetId;
    String datasetName;
    String datasetType;
    String source;
    String schema;
    String createdBy;
    LocalDateTime creationDate;
    String modifiedBy;
    LocalDateTime modifiedDate;
}
