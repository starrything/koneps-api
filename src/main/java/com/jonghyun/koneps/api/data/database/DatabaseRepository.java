package com.jonghyun.koneps.api.data.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepository extends JpaRepository<Database, String> {
    List<Database> findByDatabaseType(String databaseType);

    List<Database> findByDatabaseNameContainsOrDatabaseTypeContainsOrCreatedByContains(String databaseName, String databaseType, String createdBy);

}
