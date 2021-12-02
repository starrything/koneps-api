package com.jonghyun.koneps.api.data.database;

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
@Table(name = "tb_data_database")
public class Database {
    @Id
    @Column(name = "database_id")
    String databaseId;

    @Column(name = "db_name")
    String databaseName;

    @Column(name = "db_type")
    String databaseType;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "db_uri")
    String databaseUri;

    @Column(name = "datasource_url")
    String datasourceUrl;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    @Builder
    public Database(String databaseName, String databaseType, String databaseUri, String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modifiedDate) {
        this.databaseName = databaseName;
        this.databaseType = databaseType;
        this.databaseUri = databaseUri;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public Database newDatabase(String databaseId, String databaseName, String databaseType, String username, String password, String databaseUri, String datasourceUrl, String createdBy, LocalDateTime creationDate) {
        this.databaseId = databaseId;
        this.databaseName = databaseName;
        this.databaseType = databaseType;
        this.username = username;
        this.password = password;
        this.databaseUri = databaseUri;
        this.datasourceUrl = datasourceUrl;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }

    public Database editDatabase(String databaseName, String databaseType, String username, String password, String databaseUri, String datasourceUrl, String modifiedBy, LocalDateTime modifiedDate) {
        this.databaseName = databaseName;
        this.databaseType = databaseType;
        this.username = username;
        this.password = password;
        this.databaseUri = databaseUri;
        this.datasourceUrl = datasourceUrl;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;

        return this;
    }
}
