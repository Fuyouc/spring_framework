package org.spring.jdbc.sql;

import org.spring.Application;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.parser.expression.Expression;
import org.spring.core.parser.expression.SpringExpression;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.annotation.Delete;
import org.spring.jdbc.annotation.Insert;
import org.spring.jdbc.annotation.Select;
import org.spring.jdbc.annotation.Update;
import org.spring.jdbc.enums.SQLType;
import org.spring.jdbc.sql.execute.handler.sql_handler.SQLHandler;
import org.spring.jdbc.sql.execute.handler.JDBCSQLHandler;

import org.spring.jdbc.sql.execute.handler.sql_handler.DeleteHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.InsertHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.SelectHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.UpdateHandler;
import org.spring.jdbc.xml.DaoXMLElement;
import org.spring.jdbc.xml.DaoXMLMapperManager;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;

@Component
public class JDBCDaoHandler implements DaoHandler{

    @Autowired
    private JDBCSQLHandler jdbcsqlHandler;


    @Override
    public Object executeSQL(String beanName,Class<?> beanClass,Method method, Object[] paramValues) {
        String SQL = null;
        SQLType sqlType;
        Element root = null;
        DaoMethod daoMethod = new DaoMethod();
        daoMethod.setMethod(method);
        daoMethod.setValues(paramValues);
        daoMethod.setDaoClass(beanClass);
        if (!ObjectUtils.isEmpty(method.getAnnotation(Insert.class))) {
            SQL = method.getAnnotation(Insert.class).value();
            sqlType = SQLType.INSERT;
        } else if (!ObjectUtils.isEmpty(method.getAnnotation(Delete.class))) {
            SQL = method.getAnnotation(Delete.class).value();
            sqlType = SQLType.DELETE;
        } else if (!ObjectUtils.isEmpty(method.getAnnotation(Update.class))) {
            SQL = method.getAnnotation(Update.class).value();
            sqlType = SQLType.UPDATE;
        } else if (!ObjectUtils.isEmpty(method.getAnnotation(Select.class))){
            SQL = method.getAnnotation(Select.class).value();
            sqlType = SQLType.SELECT;
        }else {
            //如果在接口上没有标志注解，则查找相应的XML文件
            DaoXMLElement element = DaoXMLMapperManager.getElement(beanClass.getName(), method.getName());
            if (ObjectUtils.isEmpty(element)){
                //如果不在方法上使用注解，并且也找不到对应的xml文件
                throw new RuntimeException("找不到 " + beanClass.getName() + " 所对应的xml文件");
            }else {
                sqlType = element.getType();
                root = element.getElement();
                daoMethod.setRoot(root);
            }
        }
        daoMethod.setSQL(SQL);
        SQLHandler sqlHandler = getSQLHandler(sqlType);
        return jdbcsqlHandler.handler(daoMethod,sqlHandler);
    }

    private SQLHandler getSQLHandler(SQLType sqlType){
        BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
        switch (sqlType){
            case INSERT:
                return beanFactory.getBean(InsertHandler.class.getName());
            case DELETE:
                return beanFactory.getBean(DeleteHandler.class.getName());
            case UPDATE:
                return beanFactory.getBean(UpdateHandler.class.getName());
            case SELECT:
                return beanFactory.getBean(SelectHandler.class.getName());
        }
        return null;
    }


}
