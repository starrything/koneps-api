package com.jonghyun.koneps.domain.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisualConnectionPool implements ConnectionPool {

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 2;
    private static int MAX_POOL_SIZE = 2;
    private static int MAX_TIMEOUT = 15000;

    /* initializing Constructor */
    public VisualConnectionPool() {

    }

    /* Standard Constructor */
    public VisualConnectionPool(String url, String user, String password, List<Connection> pool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = pool;
    }

    /* Create Connection Pool */
    public static VisualConnectionPool create(
            String url, String user,
            String password) throws SQLException {

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, user, password));
        }
        return new VisualConnectionPool(url, user, password, pool);
    }

    /* Create Connection */
    private static Connection createConnection(
            String url, String user, String password)
            throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /* getConnection() */
    @Override
    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password));
            } else {
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);

        if (!connection.isValid(MAX_TIMEOUT)) {
            connection = createConnection(url, user, password);
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public void shutdown() throws SQLException {
        if (usedConnections.size() > 0) {
            usedConnections.forEach(this::releaseConnection);
        }
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
