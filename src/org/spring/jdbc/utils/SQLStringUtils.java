package org.spring.jdbc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLStringUtils {
    /**
     * 获取SQL中的#{}内的参数名
     * @param sql
     * @return
     */
    public static List<String> getSqlParameter(String sql){
        List<String> parameter = new ArrayList<>();
        String start = "{", end = "}";
        String patern = "(?<=\\" + start + ")[^\\" + end + "]+";
        Pattern pattern = Pattern.compile(patern);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            parameter.add(matcher.group());
        }
        return parameter;
    }

    /**
     * 将原始SQL转换成预编译SQL（#{} 转换成 ?）
     * @param sql
     * @return
     */
    public static String getSQL(String sql){
        List<String> sqlParameter = getSqlParameter(sql);
        String preSql = sql;
        for (int i = 0; i < sqlParameter.size(); i++) {
            preSql = preSql.replaceFirst("#\\{"+sqlParameter.get(i)+"}","\\?");
        }
        return preSql;
    }

    public static String autoInsert(String tableName, List<String> parameterName){
        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName + "(");
        for (String s : parameterName) {
            sb.append(s + ",");
        }
        if (parameterName.size() > 1){
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")" + " VALUES(");
        for (String s : parameterName) {
            sb.append("#{"+s+"},");
        }
        if (parameterName.size() > 1){
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }
}
