package org.spring.jdbc.sql;

import java.lang.reflect.Method;

public interface DaoHandler {
    /**
     * 执行SQL语句并返回结果
     */
    Object executeSQL(Method method,Object[] paramValues);
}
