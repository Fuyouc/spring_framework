package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.annotation.*;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.dynamic.DynamicSQLHandler;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractSQLHandler implements SQLHandler{

    @Autowired
    public static List<DynamicSQLHandler> dynamicSQLHandlers;

    @Override
    public void processSQL(DaoMethod daoMethod) throws Exception {
        if (xml(daoMethod.getMethod())){
            for (DynamicSQLHandler handler : dynamicSQLHandlers) {
                handler.dynamicSQL(daoMethod);
            }
            daoMethod.setSQL(daoMethod.getRoot().buildContext());
        }
        xmlSQL(daoMethod);
    }

    protected abstract void xmlSQL(DaoMethod daoMethod);

    private boolean xml(Method method){
        return ObjectUtils.isEmpty(method.getAnnotation(Insert.class))
                && ObjectUtils.isEmpty(method.getAnnotation(Delete.class))
                && ObjectUtils.isEmpty(method.getAnnotation(Update.class))
                && ObjectUtils.isEmpty(method.getAnnotation(Select.class));
    }
}
