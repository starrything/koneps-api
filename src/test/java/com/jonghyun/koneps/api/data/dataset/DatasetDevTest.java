package com.jonghyun.koneps.api.data.dataset;

import com.jonghyun.koneps.api.data.ConnectionPool;
import com.jonghyun.koneps.api.data.VisualConnectionPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class DatasetDevTest {
    @Test
    public void getSchemaByDatabaseTest() throws SQLException {
        ConnectionPool connectionPool = VisualConnectionPool
                //.create("jdbc:mariadb://197.200.1.48:3317/framework", "framework", "Framework1!");
                .create("jdbc:postgresql://197.200.1.52:5432/visual", "visual", "visual1234!!");

        Connection conn = connectionPool.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getCatalogs();
        //ResultSet rs = meta.getSchemas();
        while (rs.next()) {
            String cat = rs.getString("table_cat");
            System.out.println(cat);
            /*String catalog = rs.getString("TABLE_CATALOG");
            String schema = rs.getString("TABLE_SCHEM");
            System.out.println("Catalog : " + catalog + ", Schema : " + schema);*/
        }

/*        Connection conn = connectionPool.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet tables = meta.getTables(null, "public", "%", null);
        while (tables.next()) {
            System.out.println(tables.getString(3));
        }*/

        assertTrue(conn.isValid(1));
    }
}
