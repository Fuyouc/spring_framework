package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ClassUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UpdateHandler implements SQLHandler {
    @Override
    public String processSQL(Method method, String SQL) {
        return SQL;
    }

    @Override
    public Object executeSQL(Method method, PreparedStatement ps) throws SQLException {
        return result(method,ps.executeUpdate());
    }

    private Object result(Method method,int result){
        Class<?> returnType = method.getReturnType();
        if (ClassUtils.isBoolean(returnType)){
            return result > 0;
        }else if (ClassUtils.isInt(returnType)){
            return result;
        }else {
            return null;
        }
    }
}
