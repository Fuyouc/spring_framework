package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.core.parser.xml.Element;
import org.spring.jdbc.config.ParameterBindValue;
import org.spring.jdbc.sql.DaoMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface SQLHandler {

    /**
     * 是否需要对原SQL语句进行加工处理
     */
    void processSQL(DaoMethod method) throws Exception;

    /**
     * 执行SQL语句
     */
    Object executeSQL(DaoMethod method,PreparedStatement ps) throws SQLException;

    default void checkMappingXML(Element root,Method method) throws Exception{
        if (root == null){
            throw new SQLException("未在XML文件中找到指定映射的：" + method.getName());
        }
    }
}
