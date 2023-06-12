package org.spring.jdbc.utils;


import org.spring.jdbc.config.ParameterBindValue;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ParameterUtils {

    /**
     * 获取SQL上的参数与value绑定的对象
     * @param sql
     * @param methodParameterNameList  //方法上的参数列表
     * @param parameterValue           //参数值列表
     * @param customize                //是否自定义SQL
     * @return
     */
    public static List<ParameterBindValue> getSqlParameterBindValue(Method method,
                                                                    String sql,
                                                                    List<String> methodParameterNameList,
                                                                    Object[] parameterValue,
                                                                    boolean customize){
        List<ParameterBindValue> parameterBindValueList = new ArrayList<>();
        if (customize){
            //自定义SQL语句的情况下
            List<String> values = SQLStringUtils.getSqlParameter(sql); //获取sql上的#{}中的参数值
            for (int i = 0; i < values.size(); i++) {
                parameterBindValueList.add(new ParameterBindValue(i + 1,null,values.get(i)));
            }
            return parameterBindValueList;
        }else {
            if (parameterValue != null && parameterValue.length >= 1) {

            }
        }
        return parameterBindValueList;
    }
}
