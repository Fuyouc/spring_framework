package org.spring.jdbc.sql.execute.handler.dynamic;

import org.spring.core.parser.expression.Expression;
import org.spring.core.parser.expression.SpringExpression;
import org.spring.core.parser.expression.exception.ExpressionException;
import org.spring.jdbc.annotation.Param;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import javax.management.modelmbean.XMLParseException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public abstract class AbstractDynamicHandler implements DynamicSQLHandler{
    /**
     * SQL标签上的表达式解析器
     */
    protected static Expression expressionParser = new SpringExpression();


    /**
     * 解析表达式，如果校验成功返回true，否则false
     * @param expression
     * @param method
     * @param values
     * @return
     */
    protected boolean expression(String expression,Method method,Object[] values){
        try {
            if (!ObjectUtils.isEmpty(expression)){
                Object value = null;
                expressionParser.setExpression(expression);
                List<String> params = expressionParser.getParams();
                for (int i = 0; i < params.size(); i++) {
                    String paramName = params.get(i);
                    if (paramName.startsWith("$")){
                        //如果参数开头$号，表示该变量为引用变量，使用$xxx.变量名来获取Value
                        paramName = paramName.replaceAll("\\$",""); //删除前面的$
                        if ((i + 1) >= params.size()){
                            throw new XMLParseException("请检查$" + paramName + ".xxx是否拼写成功");
                        }
                        String attributes = params.get(i + 1);
                        value = objectValue(paramName,attributes,values[getValueIndex(paramName,method)]);
                        paramName = "$" + paramName + "." + attributes;
                        ++i;
                    }else {
                        value = values[getValueIndex(paramName, method)];
                    }
                    expressionParser.addParam(paramName,value);
                }
                return (boolean) expressionParser.evaluate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 如果是引用变量，则根据.后边的变量名来获取数据
     */
    private Object objectValue(String prefix,String paramName,Object value) throws Exception {
        Class<?> clazz = value.getClass();
        if (Map.class.isInstance(value)){
            //如果是一个map
            Map<String,Object> map = (Map<String, Object>) value;
            if (!map.containsKey(paramName)){
                throw new XMLParseException("找不到 " + prefix + "." + paramName + "的变量");
            }else return map.get(paramName);
        }else if (!ClassUtils.isWrapClass(clazz)){
            //如果是一个普通类
            Field field = clazz.getDeclaredField(paramName);
            field.setAccessible(true);
            if (ObjectUtils.isEmpty(field)){
                throw new XMLParseException("找不到 " + prefix + "." + paramName + "的变量");
            }else {
                return field.get(value);
            }
        }
        return null;
    }

    protected int getValueIndex(String paramName, Method method){
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = ObjectUtils.isEmpty(parameter.getAnnotation(Param.class)) ? parameter.getName() : parameter.getAnnotation(Param.class).value();
            if (name.equals(paramName)){
                return i;
            }
        }
        throw new ExpressionException("表达式中的 " + paramName + " 参数找不到对应的Value");
    }
}
