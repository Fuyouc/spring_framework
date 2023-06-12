package org.spring.jdbc.config;

/**
 * 参数绑定value
 */
public class ParameterBindValue {
    private int parameterIndex;  //参数下标
    private String parameterName; //参数名称
    private Object parameterValue; //参数值

    public ParameterBindValue(int parameterIndex, String parameterName, Object parameterValue) {
        this.parameterIndex = parameterIndex;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Object getParameterValue() {
        return parameterValue;
    }

    @Override
    public String toString() {
        return "ParameterBindValue{" +
                "parameterIndex=" + parameterIndex +
                ", parameterName='" + parameterName + '\'' +
                ", parameterValue=" + parameterValue +
                '}';
    }
}
