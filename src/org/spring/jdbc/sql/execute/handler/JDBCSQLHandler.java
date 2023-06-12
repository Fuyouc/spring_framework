package org.spring.jdbc.sql.execute.handler;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.annotations.autoconfig.Value;
import org.spring.jdbc.config.ParameterBindValue;
import org.spring.jdbc.connection.JDBCConnectionPoolManager;
import org.spring.jdbc.sql.execute.SQLParameterBinder;
import org.spring.jdbc.sql.execute.handler.sql_handler.SQLHandler;
import org.spring.jdbc.utils.SQLStringUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public final class JDBCSQLHandler{

    @Value("spring.datasource.log")
    private boolean log = false;

    @Autowired
    private SQLParameterBinder binder;

    @Autowired
    private JDBCConnectionPoolManager connectionPoolManager;


    public Object handler(Method method, Object[] paramValues, String SQL, SQLHandler handler){
        PreparedStatement ps = null;
        try {
            SQL = handler.processSQL(method,SQL);
            List<String> sqlParameter = SQLStringUtils.getSqlParameter(SQL);
            List<ParameterBindValue> parameterBindValue = binder.getParameterBindValue(method, paramValues, SQL,sqlParameter);
            Connection cn = connectionPoolManager.getConnection();
            String preSQL = SQLStringUtils.getSQL(SQL);
            ps = cn.prepareStatement(preSQL);
            for (int i = 0; i < sqlParameter.size(); i++) {
                ParameterBindValue value = parameterBindValue.get(i);
                ps.setObject(value.getParameterIndex() + 1,value.getParameterValue());
            }
            if (log){
                for (int i = 0; i < parameterBindValue.size(); i++) {
                    preSQL = preSQL.replaceFirst("\\?",String.valueOf(parameterBindValue.get(i).getParameterValue()));
                }
                System.out.println("执行的SQL：" + preSQL);
            }
            Object result = handler.executeSQL(method,ps);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }
}
