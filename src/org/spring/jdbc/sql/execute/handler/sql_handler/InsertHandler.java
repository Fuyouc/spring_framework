package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.annotation.Insert;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.utils.SQLStringUtils;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class InsertHandler extends AbstractSQLHandler{
    private String autoSplicing(String sql){
        StringBuilder sb = new StringBuilder(sql.substring(0,12));
        String tableName = sql.substring(12,sql.length());
        int endIndex = tableName.indexOf(" ");
        tableName = tableName.substring(0,endIndex);
        tableName = tableName.endsWith(")") ? tableName.substring(0,tableName.indexOf("(")) : tableName;
        sb.append(tableName);
        List<String> sqlParameter = SQLStringUtils.getSqlParameter(sql);
        if (sqlParameter.size() > 0){
            sb.append("(");
            for (String param : sqlParameter) {
                sb.append(param + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        sb.append(sql.substring(endIndex + 12,sql.length()));
        return sb.toString();
    }

    @Override
    protected void xmlSQL(DaoMethod daoMethod) {

    }

    @Override
    public Object executeSQL(DaoMethod daoMethod,PreparedStatement ps) throws SQLException {
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
}
