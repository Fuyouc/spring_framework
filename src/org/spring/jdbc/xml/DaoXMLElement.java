package org.spring.jdbc.xml;

import org.spring.core.parser.xml.Element;
import org.spring.jdbc.enums.SQLType;

public interface DaoXMLElement {
    /**
     * 获取XML的Element元素
     */
    Element getElement();

    SQLType getType();
}
