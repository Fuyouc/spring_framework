package org.spring.jdbc.sql;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.bean.BeanFactory;
import org.spring.jdbc.annotation.Delete;
import org.spring.jdbc.annotation.Insert;
import org.spring.jdbc.annotation.Select;
import org.spring.jdbc.annotation.Update;
import org.spring.jdbc.sql.execute.handler.sql_handler.SQLHandler;
import org.spring.jdbc.sql.execute.handler.JDBCSQLHandler;

import org.spring.jdbc.sql.execute.handler.sql_handler.DeleteHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.InsertHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.SelectHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.UpdateHandler;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;

@Component
public class JDBCDaoHandler implements DaoHandler{
    @Override
    public Object executeSQL(Method method, Object[] paramValues) {
        String SQL;
        SQLHandler sqlHandler = null;
        BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
        if (!ObjectUtils.isEmpty(method.getAnnotation(Insert.class))) {
            SQL = method.getAnnotation(Insert.class).value();
            sqlHandler = beanFactory.getBean(InsertHandler.class.getName());
        } else if (!ObjectUtils.isEmpty(method.getAnnotation(Delete.class))) {
            SQL = method.getAnnotation(Delete.class).value();
            sqlHandler = beanFactory.getBean(DeleteHandler.class.getName());
        } else if (!ObjectUtils.isEmpty(method.getAnnotation(Update.class))) {
            SQL = method.getAnnotation(Update.class).value();
            sqlHandler = beanFactory.getBean(UpdateHandler.class.getName());
        } else {
            SQL = method.getAnnotation(Select.class).value();
            sqlHandler = beanFactory.getBean(SelectHandler.class.getName());
        }
        JDBCSQLHandler handler = beanFactory.getBean(JDBCSQLHandler.class.getName());
        return handler.handler(method, paramValues, SQL, sqlHandler);
    }
}
