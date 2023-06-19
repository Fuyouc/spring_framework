package org.spring.jdbc.sql.execute.handler.dynamic.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.dynamic.AbstractDynamicHandler;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import javax.management.modelmbean.XMLParseException;
import java.util.List;

@Component
public class DynamicIncludeHandler extends AbstractDynamicHandler {
    @Override
    public void dynamicSQL(DaoMethod daoMethod) throws Exception {
        Element root = daoMethod.getRoot();
        List<Element> includeChild = root.getChild("include");
        if (!ObjectUtils.isEmpty(includeChild)){
            for (Element item : includeChild) {
                String refid = item.getAttributes("refid");
                if (StringUtils.isEmpty(refid)){
                    throw new XMLParseException("请在 <include> 标签上指定 refid 属性的 value");
                }else {
                    Element sqlElement = root.getParent().getChild("sql", refid);
                    if (!ObjectUtils.isEmpty(sqlElement)){
                        item.addContext(sqlElement.buildContext() + " ");
                        root.need(item);
                    }else
                        throw new XMLParseException("找不到 " + refid + " 所对应的<sql>标签，请尝试检查<sql>标签的id属性");
                }
            }
        }
    }
}
