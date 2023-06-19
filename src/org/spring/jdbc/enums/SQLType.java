package org.spring.jdbc.enums;

import java.sql.SQLException;

public enum SQLType {
    INSERT,DELETE,UPDATE,SELECT;

    public static SQLType type(String type) {
        switch (type){
            case "insert":
                return INSERT;
            case "update":
                return UPDATE;
            case "delete":
                return DELETE;
            case "select":
                return SELECT;
            default:
                throw new RuntimeException("未识别的SQL类型，请检查XML文件标签是否正确，或者Dao接口上是否使用正确注解？");
        }
    }
}
