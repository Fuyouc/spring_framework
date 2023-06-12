package org.spring.jdbc.connection;

import java.sql.Connection;

/**
 * JDBC的连接池的管理对象
 */
public interface JDBCConnectionPoolManager {

    /**
     * 获得连接对象
     */
    Connection getConnection();

}
