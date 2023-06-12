package org.spring.jdbc.sql.execute.handler.sql_handler.select.child;


import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.DaoMethodReturnTypeHandler;

import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * 处理基本数据类型的返回值
 */
@Component
public class DaoMethodBasicReturnType implements DaoMethodReturnTypeHandler {
    @Override
    public Object handler(Method method, ResultSet resultSet) {
        if (int.class.isAssignableFrom(method.getReturnType())){
            int count = 0;
            try {
                while (resultSet.next()){
                    count++;
                }
            }catch (Exception e){

            }
            return count;
        }
        return null;
    }
}
