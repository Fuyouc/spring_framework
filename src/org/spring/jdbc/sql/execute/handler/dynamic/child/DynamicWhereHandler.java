package org.spring.jdbc.sql.execute.handler.dynamic.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.dynamic.AbstractDynamicHandler;
import org.spring.utils.global.ObjectUtils;

import javax.management.modelmbean.XMLParseException;
import java.util.List;

/**
 * WHERE语句处理器
 */
@Component
public class DynamicWhereHandler extends AbstractDynamicHandler {
    @Override
    public void dynamicSQL(DaoMethod daoMethod) throws Exception {
        Element root = daoMethod.getRoot();
        List<Element> whereChild = root.getChild("where");
        if (!ObjectUtils.isEmpty(whereChild)){
            if (whereChild.size() == 1){
                Element where = whereChild.get(0);
                List<Element> ifChild = where.getChild("if");
                if (!ObjectUtils.isEmpty(ifChild)){
                    StringBuilder sb = new StringBuilder();
                    //如果where中有if标签，才进行处理
                    for (Element item : ifChild) {
                        if (expression(item.getAttributes("text"),daoMethod.getMethod(),daoMethod.getValues())){
                            String context = item.buildContext();
                            if (sb.length() == 0){
                                //如果是第一个条件，则去掉前面的逻辑符
                                context = context.replaceAll("(?i)\\b(and|or|&&|\\|\\|)\\b", "").trim();
                            }else {
                                sb.append(" ");
                            }
                            sb.append(context);
                        }
                    }
                    if (sb.length() > 0){
                        root.addContext("WHERE" + " " + sb.toString());
                    }
                }
            }else {
                throw new XMLParseException("<where>标签只能存在一个");
            }
        }
    }
}
