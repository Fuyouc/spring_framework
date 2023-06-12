package org.spring.jdbc.proxy;


import org.spring.Application;
import org.spring.jdbc.annotation.*;
import org.spring.jdbc.sql.DaoHandler;
import org.spring.jdbc.sql.JDBCDaoHandler;
import org.spring.utils.global.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Dao 接口动态代理，动态创建接口实现类
 */
public class DaoInterfaceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return run(method,args);
    }

    /**
     * 代理类在调用方法后，会执行这里
     * @param method      调用的目标方法
     * @param parameterValue   方法参数列表值
     * @return 方法执行返回类型
     */
    public Object run(Method method,Object[] parameterValue) throws InstantiationException, IllegalAccessException {
        for (Annotation annotation : method.getAnnotations()) {
            if (!ObjectUtils.isEmpty(annotation.annotationType().getAnnotation(SQL.class))){
                DaoHandler daoHandler = Application.getApplicationContext().getFactory().getBeanFactory().getBean(JDBCDaoHandler.class.getName());
                return daoHandler.executeSQL(method,parameterValue);
            }
        }
        return null;
    }
}
