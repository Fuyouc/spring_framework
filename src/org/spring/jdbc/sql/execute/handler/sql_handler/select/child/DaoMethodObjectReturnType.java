package org.spring.jdbc.sql.execute.handler.sql_handler.select.child;


import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.annotations.autoconfig.Value;
import org.spring.jdbc.annotation.TableField;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.DaoMethodReturnTypeHandler;
import org.spring.jdbc.utils.JdbcUtils;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * 处理对象类型的返回值
 */
@Component
public class DaoMethodObjectReturnType implements DaoMethodReturnTypeHandler {

    @Value("spring.datasource.map-underscore-to-camel-case")
    private static boolean isHump;

    @Override
    public Object handler(DaoMethod daoMethod, ResultSet resultSet) {
        Method method = daoMethod.getMethod();
        try {
            if (resultSet.next()){
                if (!ObjectUtils.isEmpty(daoMethod.getMapper())){
                    return object(method.getReturnType(),daoMethod,resultSet);
                }else {
                    return object(method.getReturnType(), method, resultSet);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object object(Class<?> targetClass,DaoMethod daoMethod,ResultSet resultSet){
        Object target = ClassUtils.object(targetClass);
        try {
            Iterator<Map.Entry<String, String>> iterator = daoMethod.getMapper().entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> entry = iterator.next();
                String property = entry.getKey();
                String column = entry.getValue();
                Field field = targetClass.getDeclaredField(property);
                if (!ObjectUtils.isEmpty(field)){
                    field.setAccessible(true);
                    Object object = resultSet.getObject(resultSet.findColumn(column));
                    if (!ObjectUtils.isEmpty(object)) {
                        field.set(target, object); //根据字段名称与数据库列关联，并赋值到对象上
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return target;
    }

    public static Object object(Class<?> targetClass,Method method, ResultSet resultSet){
        Object target = ClassUtils.object(targetClass);
        try {
            Field[] declaredFields = target.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                    String fieldName = null;
                    //如果类上有注解，则使用注解的内容当做字段名
                    if (field.getAnnotation(TableField.class) != null) fieldName = field.getAnnotation(TableField.class).value();
                    if (StringUtils.isEmpty(fieldName)){
                        if (isHump){
                            //如果开启驼峰命名，则将其转换
                            fieldName = StringUtils.transLine(field.getName());
                        }else {
                            fieldName = field.getName(); //如果字段名，则使用类名中的字段名
                        }
                    }
                    Object object = resultSet.getObject(resultSet.findColumn(fieldName));
                    if (!ObjectUtils.isEmpty(object)) {
                        field.set(target, object); //根据字段名称与数据库列关联，并赋值到对象上
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return target;
    }
}
