package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.Insert;
import org.spring.jdbc.utils.SQLStringUtils;
import org.spring.utils.global.ClassUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class InsertHandler implements SQLHandler {
    @Override
    public String processSQL(Method method, String SQL) {
        if (method.getAnnotation(Insert.class).autoSplicing()){
            return autoSplicing(SQL);
        }else return SQL;
    }

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
    public Object executeSQL(Method method,PreparedStatement ps) throws SQLException {
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
