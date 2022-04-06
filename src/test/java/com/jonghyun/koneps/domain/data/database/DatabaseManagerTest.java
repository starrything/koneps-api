package com.jonghyun.koneps.domain.data.database;

import com.jonghyun.koneps.domain.data.ConnectionPool;
import com.jonghyun.koneps.domain.data.DatabaseManager;
import com.jonghyun.koneps.domain.data.VisualConnectionPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class DatabaseManagerTest {
     public DatabaseManager databaseManager;

    @Test
    public void testGetConnection() throws SQLException {
        String type = "mariadb";
        String url = "jdbc:log4jdbc:mariadb://197.200.1.48:3317/framework";
        String username = "framework";
        String password = "Framework1!";
        databaseManager = DatabaseManager.getInstance(type, url, username, password);
        //Connection connection = databaseManager.getConnection(type, url, username, password);
        Connection connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mariadb");

        Assert.isTrue(connection.isValid(5));

        databaseManager.freeConnection(connection);
    }

    @Test
    public void whenCalledgetConnection_thenCorrect() throws SQLException {
        ConnectionPool connectionPool = VisualConnectionPool
                .create("jdbc:mariadb://197.200.1.48:3317/framework", "framework", "Framework1!");

        assertTrue(connectionPool.getConnection().isValid(1));


        ConnectionPool connectionPool_postgres = VisualConnectionPool
                .create("jdbc:postgresql://197.200.1.52:5432/visual", "visual", "visual1234!!");

        assertTrue(connectionPool_postgres.getConnection().isValid(1));
    }
}
