package org.spring.jdbc.sql.execute.handler.dynamic.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.dynamic.AbstractDynamicHandler;
import org.spring.utils.global.ObjectUtils;

import javax.management.modelmbean.XMLParseException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * 处理if标签
 */
@Component
public class DynamicSwitchHandler extends AbstractDynamicHandler {
    @Override
    public void dynamicSQL(DaoMethod daoMethod) throws XMLParseException {
        Element root = daoMethod.getRoot();
        Method method = daoMethod.getMethod();
        Object[] values = daoMethod.getValues();
        List<Element> whereChild = root.getChild("switch");

        if (!ObjectUtils.isEmpty(whereChild)){
            for (Element whereItem : whereChild) {
                List<Element> caseChild = whereItem.getChild("case");
                List<Element> defaultChild = whereItem.getChild("default");

                if (ObjectUtils.isEmpty(caseChild)){
                    throw new XMLParseException("<where> 标签中必须包含 <case> 标签");
                }

                if (ObjectUtils.isEmpty(defaultChild) || defaultChild.size() > 1){
                    throw new XMLParseException("<where> 标签只能存在一个 <default> 标签");
                }

                p:for (;;){
                    for (Element caseItem : caseChild) {
                        String expression = caseItem.getAttributes("text");
                        if (expression(expression,method,values)){
                            //如果当中有一个 case 满足条件，直接返回即可
                            whereItem.need(caseItem);
                            break p;
                        }
                    }
                    //如果没有一个case满足条件，执行执行default标签即可
                    whereItem.need(defaultChild.get(0));
                    break p;
                }
                root.addContext(whereItem.buildContext());
            }
        }
    }
}
