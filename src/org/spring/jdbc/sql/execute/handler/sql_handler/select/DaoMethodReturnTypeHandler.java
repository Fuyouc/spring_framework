package org.spring.jdbc.sql.execute.handler.sql_handler.select;

import org.spring.jdbc.sql.DaoMethod;

import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * Dao方法的返回值处理器
 * 支持：
 *  - List<目标类> List<Map<String,Object>
 *  - 目标类
 *  - int
 */
public interface DaoMethodReturnTypeHandler {
    /**
     * 处理Dao方法各种类型的返回值
     */
    Object handler(DaoMethod daoMethod, ResultSet resultSet);
}
