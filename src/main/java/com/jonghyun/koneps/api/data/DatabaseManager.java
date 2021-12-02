package com.jonghyun.koneps.api.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.sql.*;

@Slf4j
public class DatabaseManager {
    private static DatabaseManager instance;

    synchronized public static DatabaseManager getInstance(String type, String url, String username, String password) {
        try {
            if (instance == null) {
               instance = new DatabaseManager(type, url, username, password);
               log.info("DatabaseManger initialize : {}", instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private DatabaseManager(String type, String url, String username, String password) {
        /* Connection 초기화 */
        initConnection(type, url, username, password);
    }

    private void initConnection(String type, String url, String username, String password) {
        /* set Configuration */
        try {
            setupDriver(type, url, username, password);
        } catch (Exception e) {
            log.info("Exception : {}", e.getMessage());
        }
        log.info("Connection created.");
    }

    public void setupDriver(String type, String url, String username, String password) throws Exception {
        /* Load each JDBC Driver by Database type */
        if ("mariadb".equals(type)) {
            Class.forName("org.mariadb.jdbc.Driver");
        } else if ("postgres".equals(type)) {
            Class.forName("org.postgresql.Driver");
        } else if ("mssql".equals(type)) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } else if ("oracle".equals(type)) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }

        /*
        * 1. Create Connection Pool
        * 2. Set options
        * */
        GenericObjectPool connectionPool = new GenericObjectPool(null);
        connectionPool.setMaxTotal(45);
        connectionPool.setMinIdle(4);
        connectionPool.setMaxIdle(15000);
        connectionPool.setTimeBetweenEvictionRunsMillis(3600000);
        connectionPool.setMinEvictableIdleTimeMillis(1800000);
        connectionPool.setMaxIdle(45);
        connectionPool.setTestOnBorrow(true);

        /* Create ConnectionFactory */
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                url,
                username,
                password);

        // Connection Pool이 PoolableConnection 객체를 생성할 때 사용할
        // PoolableConnectionFactory 생성
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                connectionFactory,
                null
        );
        poolableConnectionFactory.setPool(connectionPool);

        // Pooling을 위한 JDBC 드라이버 생성 및 등록
        PoolingDriver driver = new PoolingDriver();

        // JDBC 드라이버에 커넥션 풀 등록
        driver.registerPool("mariadb", connectionPool);
    }

    /*public Connection getConnection(String type, String url, String username, String password) {
        Connection con = null;

        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            log.error("SQLException : {}", ex.getMessage());
        }
        // TODO : Connection Type에 따라 분기.

        return con;
    }*/

    public Connection getConnection(String url) {
        Connection con = null;

        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            log.error("SQLException : {}", ex.getMessage());
        }
        // TODO : Connection Type에 따라 분기.

        return con;
    }

    /*
        Connection Pool에 free 및 객체 소멸 함수들
     */
    public void freeConnection(Connection con, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            freeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection con, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            freeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection con, PreparedStatement pstmt) {
        try {
            if (pstmt != null) pstmt.close();
            freeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection con, Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            freeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection con) {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(PreparedStatement pstmt) {
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
