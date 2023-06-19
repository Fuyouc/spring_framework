package org.spring.core.parser.expression;

import org.apache.commons.jexl3.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SpringExpression implements Expression{

    private JexlEngine engine;

    private JexlContext context;
    private JexlScript script;


    public SpringExpression() {
        this(null);
    }

    public SpringExpression(String expression){
        engine = new JexlBuilder().create();
        if (null != expression){
            script = engine.createScript(expression);
        }
    }

    @Override
    public void setExpression(String expression) {
        script = engine.createScript(expression);
    }

    @Override
    public Expression addParam(String param, Object value) {
        if (context == null){
            context = new MapContext();
        }
        context.set(param,value);
        return this;
    }

    @Override
    public List<String> getParams() {
        List<String> params = new ArrayList<>();
        for (List<String> variable : script.getVariables()) {
            for (String param : variable) {
                params.add(param);
            }
        }
        return params;
    }

    @Override
    public Object evaluate() {
        Object result = script.execute(context);
        context = null;
        script = null;
        return result;
    }
}
