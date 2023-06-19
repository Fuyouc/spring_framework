package org.spring.jdbc.sql.execute.handler.sql_handler.select.child;


import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.TableField;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.DaoMethodReturnTypeHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理List类型的返回值
 */
@Component
public class DaoMethodListReturnType implements DaoMethodReturnTypeHandler {

    @Override
    public Object handler(DaoMethod daoMethod, ResultSet resultSet) {
        Method method =daoMethod.getMethod();
        Type returnType = method.getGenericReturnType();
        Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
        Type elementType = actualTypeArguments[0];
        if (elementType instanceof Class){
            //如果是
            return object(daoMethod,resultSet);
        }else {
            ParameterizedType parameterizedType = ((ParameterizedType) elementType);
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class && Map.class.isAssignableFrom((Class<?>)rawType)){
                Type[] mapTypeArguments = parameterizedType.getActualTypeArguments();
                return map(method,resultSet,(Class<?>) mapTypeArguments[0], (Class<?>) mapTypeArguments[1]);
            }
        }
        return null;
    }

    private Object map(Method method,ResultSet resultSet,Class<?> keyClass,Class<?> valueClass){
        List<Map<String,Object>> result = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取字段的长度
            int columnCount = metaData.getColumnCount();
            //获取所有字段名称
            String[] columnNames = new String[columnCount];
            for (int i = 1; i < columnCount + 1; i++) {
                /**
                 * 要从1开始，所以需要在末尾补1
                 */
                columnNames[i - 1] = metaData.getColumnLabel(i);
            }
            while (resultSet.next()){
                Map<String,Object> map = new HashMap<>();
                for (int i = 0; i < columnNames.length; i++) {
                    String fieldName = columnNames[i];
                    map.put(fieldName,resultSet.getObject(resultSet.findColumn(fieldName)));
                }
                result.add(map);
            }
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private Object object(DaoMethod daoMethod,ResultSet resultSet){
        Method method = daoMethod.getMethod();
        List<Object> result = new ArrayList<>();
        Type[] actualTypeArguments = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
        try {
            /**
             * 获取List泛型中的目标class
             */
            Class<?> targetClass = Class.forName(actualTypeArguments[0].getTypeName());
            while (resultSet.next()){
                if (!ObjectUtils.isEmpty(daoMethod.getMapper())){
                    result.add(DaoMethodObjectReturnType.object(targetClass,daoMethod,resultSet)); //添加到结果集中
                }else {
                    result.add(DaoMethodObjectReturnType.object(targetClass, method, resultSet)); //添加到结果集中
                }
            }
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return result;
    }
}
