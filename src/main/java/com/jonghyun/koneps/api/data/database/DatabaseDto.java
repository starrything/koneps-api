package com.jonghyun.koneps.api.data.database;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class DatabaseDto implements Serializable {
    String databaseId;
    String databaseName;
    String databaseType;
    String databaseUri;
    String datasourceUrl;
    String createdBy;
    LocalDateTime modifiedDate;

    /*
    * Parameters from browser
    * Screen : Add Database
    * */
    String type;
    String username;
    String password;
    String database;
}
