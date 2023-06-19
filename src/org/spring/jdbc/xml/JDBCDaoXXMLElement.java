package org.spring.jdbc.xml;

import org.spring.core.parser.xml.Element;
import org.spring.jdbc.enums.SQLType;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCDaoXXMLElement implements DaoXMLElement{

    private Element element;
    private SQLType type;

    public JDBCDaoXXMLElement(Element element, SQLType type) {
        this.element = element;
        this.type = type;
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public SQLType getType() {
        return type;
    }
}
