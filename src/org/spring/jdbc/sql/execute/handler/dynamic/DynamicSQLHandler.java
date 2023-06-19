package org.spring.jdbc.sql.execute.handler.dynamic;

import org.spring.core.parser.xml.Element;
import org.spring.jdbc.sql.DaoMethod;

import java.lang.reflect.Method;

/**
 * 动态SQL处理器
 */
public interface DynamicSQLHandler {
    void dynamicSQL(DaoMethod daoMethod) throws Exception;
}
