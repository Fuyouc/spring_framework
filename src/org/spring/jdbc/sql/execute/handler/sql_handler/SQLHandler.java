package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.jdbc.config.ParameterBindValue;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface SQLHandler {

    /**
     * 是否需要对原SQL语句进行加工处理
     */
    String processSQL(Method method,String SQL);

    /**
     * 执行SQL语句
     */
    Object executeSQL(Method method,PreparedStatement ps) throws SQLException;
}
