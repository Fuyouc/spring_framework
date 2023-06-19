package org.spring.jdbc.sql.execute.handler.dynamic.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.dynamic.AbstractDynamicHandler;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * 处理if标签
 */
@Component
public class DynamicIFHandler extends AbstractDynamicHandler {
    @Override
    public void dynamicSQL(DaoMethod daoMethod) {
        Element root = daoMethod.getRoot();
        Method method = daoMethod.getMethod();
        Object[] values = daoMethod.getValues();
        Collection<Element> ifChild = root.getChild("if");
        if (!ObjectUtils.isEmpty(ifChild)){
            for (Element item : ifChild) {
                String expression = item.getAttributes("text");
                if (expression(expression,method,values)){
                    root.need(item);
                }
            }
        }
    }
}
