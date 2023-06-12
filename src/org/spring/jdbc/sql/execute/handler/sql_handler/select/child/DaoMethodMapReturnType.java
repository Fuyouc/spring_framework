package org.spring.jdbc.sql.execute.handler.sql_handler.select.child;


import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.DaoMethodReturnTypeHandler;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理Map类型的返回值
 */
@Component
public class DaoMethodMapReturnType implements DaoMethodReturnTypeHandler {
    @Override
    public Object handler(Method method, ResultSet resultSet) {
        Map<String,Object> map = new HashMap<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()){
                for (int i = 1; i < columnCount + 1; i++) {
                    String column = metaData.getColumnLabel(i);
                    map.put(column,resultSet.getObject(resultSet.findColumn(column)));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //获取字段的长度
        return map;
    }
}
