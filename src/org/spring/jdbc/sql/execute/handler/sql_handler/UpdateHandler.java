package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.utils.global.ClassUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UpdateHandler extends AbstractSQLHandler {
    @Override
    public Object executeSQL(DaoMethod daoMethod, PreparedStatement ps) throws SQLException {
        return result(daoMethod,ps.executeUpdate());
    }

    private Object result(DaoMethod daoMethod,int result){
        Class<?> returnType = daoMethod.getMethod().getReturnType();
        if (ClassUtils.isBoolean(returnType)){
            return result > 0;
        }else if (ClassUtils.isInt(returnType)){
            return result;
        }else {
            return null;
        }
    }

    @Override
    protected void xmlSQL(DaoMethod daoMethod) {

    }
}
