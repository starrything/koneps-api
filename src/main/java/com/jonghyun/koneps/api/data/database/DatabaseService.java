package com.jonghyun.koneps.api.data.database;

import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface DatabaseService {
    boolean testConnection(String type, String username, String password, String database);

    List<Map<String, Object>> searchDatabaseList(String keyword);

    @Modifying
    boolean addConnection(DatabaseDto databaseDto);

    @Modifying
    boolean updateConnection(DatabaseDto databaseDto);

    @Modifying
    boolean deleteConnection(String databaseId);

}
