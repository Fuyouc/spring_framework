package org.spring.core.parser.expression;

import java.util.List;

public interface Expression {
    /**
     * 设置表达式
     */
    void setExpression(String expression);

    /**
     * 如果表达式中使用变量，则设置变量的值
     */
    Expression addParam(String param,Object value);

    /**
     * 获取参数列表
     */
    List<String> getParams();

    /**
     * 估算表达式，将结果返回
     */
    Object evaluate();
}
