package org.spring.jdbc.xml;

import org.spring.core.parser.xml.Element;
import org.spring.jdbc.enums.SQLType;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dao映射xml文件管理器
 */
public final class DaoXMLMapperManager {

    private static Map<String,Class<?>> daoClassMap = new HashMap<>();

    private static Map<String,DaoXMLMethodMapperManager> xmlElementMap = new ConcurrentHashMap<>();

    public static void addDao(Class<?> daoClass){
        daoClassMap.put(daoClass.getName(),daoClass);
    }

    public static boolean containsDao(String className){
        return daoClassMap.containsKey(className);
    }

    /**
     * 解析xml文件，将Dao接口方法与xml文件绑定
     */
    public static void addXML(String className,Element root) {
        Class<?> clazz = daoClassMap.get(className);
        DaoXMLMethodMapperManager mapperManager;
        if (xmlElementMap.containsKey(className)){
            mapperManager = xmlElementMap.get(className);
        }else {
            mapperManager = new DaoXMLMethodMapperManager();
            xmlElementMap.put(className,mapperManager);
        }
        for (Method method : clazz.getDeclaredMethods()) {
            String name = method.getName();
            Collection<Map<String, Element>> values = root.getChild().values();
            e:for (Map<String, Element> value : values) {
                for (Element element : value.values()) {
                    String id = element.getAttributes("id");
                    if (name.equals(id)){
                        mapperManager.addElement(method.getName(),element,SQLType.type(element.getName()));
                        //如果找到
                        break e;
                    }
                }
            }
        }
    }

    public static DaoXMLElement getElement(String className,String method){
        if (xmlElementMap.containsKey(className)){
            return xmlElementMap.get(className).getElement(method);
        }
        return null;
    }

    private static class DaoXMLMethodMapperManager{
        private Map<String,DaoXMLElement> methodMap = new ConcurrentHashMap<>();

        public void addElement(String method, Element element, SQLType sqlType){
            methodMap.put(method,new JDBCDaoXXMLElement(element,sqlType));
        }

        public DaoXMLElement getElement(String method){
            return methodMap.get(method);
        }
    }
}
