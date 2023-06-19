package org.spring.jdbc.sql;

import org.spring.core.parser.xml.Element;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Queue;

public class DaoMethod {
    /**
     * daoClass对象
     */
    private Class<?> daoClass;
    /**
     * dao执行的方法
     */
    private Method method;
    /**
     * 参数值列表
     */
    private Object[] values;
    /**
     * 执行的SQL语句
     */
    private String SQL;
    /**
     * dao对应的xml文件
     */
    private Element root;

    /**
     * 映射规则
     */
    private Map<String,String> mapper;

    /**
     * 映射参数（主要用于Foreach标签使用）
     */
    private Map<String, Queue<Object>> mapperParam;

    public Map<String, Queue<Object>> getMapperParam() {
        return mapperParam;
    }

    public void setMapperParam(Map<String, Queue<Object>> mapperParam) {
        this.mapperParam = mapperParam;
    }

    public DaoMethod() {

    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getSQL() {
        return SQL;
    }

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    public Element getRoot() {
        return root;
    }

    public void setRoot(Element root) {
        this.root = root;
    }

    public Class<?> getDaoClass() {
        return daoClass;
    }

    public void setDaoClass(Class<?> daoClass) {
        this.daoClass = daoClass;
    }

    public Object getValue(int index){
        return values[index];
    }

    public void setMapper(Map<String, String> mapper) {
        this.mapper = mapper;
    }

    public Map<String, String> getMapper() {
        return mapper;
    }
}
