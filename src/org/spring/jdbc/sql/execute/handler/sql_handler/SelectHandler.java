package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.annotation.Select;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.DaoMethodReturnTypeHandler;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.child.DaoMethodBasicReturnType;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.child.DaoMethodListReturnType;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.child.DaoMethodMapReturnType;
import org.spring.jdbc.sql.execute.handler.sql_handler.select.child.DaoMethodObjectReturnType;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SelectHandler extends AbstractSQLHandler {

    @Override
    protected void xmlSQL(DaoMethod daoMethod) {
        if (!ObjectUtils.isEmpty(daoMethod.getRoot())){
            Element root = daoMethod.getRoot();
            String resultMapId = root.getAttributes("resultMap");
            if (!ObjectUtils.isEmpty(resultMapId)){
                //如果指定了映射规则
                Element parent = root.getParent();
                Element resultMap = parent.getChild("resultMap", resultMapId);
                if (!ObjectUtils.isEmpty(resultMap)) {
                    Map<String, String> mapper = new HashMap<>();
                    List<Element> result = resultMap.getChild("result");
                    if (!ObjectUtils.isEmpty(result)) {
                        for (Element item : result) {
                            String property = item.getAttributes("property");
                            String column = item.getAttributes("column");
                            mapper.put(property, column);
                        }
                    }
                    daoMethod.setMapper(mapper);
                }
            }

        }
    }

    @Override
    public Object executeSQL(DaoMethod method, PreparedStatement ps) throws SQLException {
        return handler(method,ps.executeQuery());
    }

    private Object handler(DaoMethod daoMethod,ResultSet resultSet){
        Method method = daoMethod.getMethod();
        Class<?> returnType = method.getReturnType();
        DaoMethodReturnTypeHandler handler;
        BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
        if (List.class.isAssignableFrom(returnType)){
            //如果返回值是一个List类型
            handler = beanFactory.getBean(DaoMethodListReturnType.class.getName());
        }else if (Map.class.isAssignableFrom(returnType)){
            handler = beanFactory.getBean(DaoMethodMapReturnType.class.getName());
        }else if (returnType.isPrimitive()){
            //返回的是一个基本的数据类型
            handler = beanFactory.getBean(DaoMethodBasicReturnType.class.getName());
        }else {
            //返回的是一个引用类型
            if (String.class.isAssignableFrom(returnType)){
                //如果返回的是String类型
                throw new RuntimeException(method.getName() + "() 方法上的返回值是一个 String 类型，这种类型是不允许的");
            }
            handler = beanFactory.getBean(DaoMethodObjectReturnType.class.getName());
        }
        return handler.handler(daoMethod,resultSet);
    }
}
