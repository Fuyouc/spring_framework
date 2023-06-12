package org.spring.jdbc.sql.execute.handler.sql_handler.select.child;


import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.TableField;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.DaoMethodReturnTypeHandler;
import org.spring.utils.global.ClassUtils;
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
    public Object handler(Method method, ResultSet resultSet) {
        Type returnType = method.getGenericReturnType();
        Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
        Type elementType = actualTypeArguments[0];
        if (elementType instanceof Class){
            //如果是
            return object(method,resultSet);
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

    private Object object(Method method,ResultSet resultSet){
        List<Object> result = new ArrayList<>();
        Type[] actualTypeArguments = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
        try {
            /**
             * 获取List泛型中的目标class
             */
            Class<?> targetClass = Class.forName(actualTypeArguments[0].getTypeName());
            while (resultSet.next()){
                //根据目标class对象创建目标类
                Object tagObj = ClassUtils.object(targetClass);
                Field[] declaredFields = tagObj.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    try {
                        field.setAccessible(true);
                        String fieldName = null;
                        //如果类上有注解，则使用注解的内容当做字段名
                        if (field.getAnnotation(TableField.class) != null) fieldName = field.getAnnotation(TableField.class).value();
                        if (StringUtils.isEmpty(fieldName)){
//                            if (JdbcUtils.isHump()){
//                                //如果开启驼峰命名，则将其转换
//                                fieldName = StringUtils.transLine(field.getName());
//                            }else {
//                                fieldName = field.getName(); //如果字段名，则使用类名中的字段名
//                            }
                        }
                        field.set(tagObj,resultSet.getObject(resultSet.findColumn(fieldName))); //根据字段名称与数据库列关联，并赋值到对象上
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                result.add(tagObj); //添加到结果集中
            }
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return result;
    }
}
