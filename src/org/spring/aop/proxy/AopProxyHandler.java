package org.spring.aop.proxy;

import org.spring.aop.annotation.After;
import org.spring.aop.annotation.Before;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 * Aop动态代理实现类
 */
public class AopProxyHandler implements InvocationHandler {

    private Object target; //目标类

    public AopProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return run(method,args);
    }

    public Object getProxy() throws Exception {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }

    /**
     * 获取目标类的方法
     */
    public Method getTargetMethod(Method method){
        Method[] targetMethods = target.getClass().getMethods();
        for (Method m : targetMethods) {
            /**
             * 去找目标类调用的方法，否则注解只能标记在接口层
             */
            if (m.getName().equals(method.getName()) && m.getParameterCount() == method.getParameterCount()){
                return m;
            }
        }
        return null;
    }

    private boolean isIntercept(Before before, Object beforeMethodResult){
        if (before.intercept()){
            if (beforeMethodResult instanceof Boolean){
                //如果返回的是一个boolean类型
                return (boolean) beforeMethodResult;
            }
        }
        return true;
    }

    public Object run(Method method,Object[] values) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method targetMethod = getTargetMethod(method);

        boolean intercept = true; //是否拦截目标方法执行
        Before before = targetMethod.getAnnotation(Before.class);
        if (!ObjectUtils.isEmpty(before)){
            intercept = isIntercept(before,invoke(before.value(), before.methodParameterType(), values));
        }

        if (intercept) {
            Object result = method.invoke(target, values);
            After after = targetMethod.getAnnotation(After.class);
            if (!ObjectUtils.isEmpty(after)) {
                if (after.result()){
                    //说明用户需要目标方法的返回值
                    if (ObjectUtils.isEmpty(values) || after.methodParameterType()[0] == Void.class){
                        //如果目标方法是无参的情况 or 用户只想要返回结果的情况下
                        values = new Object[1];
                        values[0] = result;
                        invoke(after.value(),new Class[]{result.getClass()},values);
                    }else {
                        values = Arrays.copyOf(values, values.length + 1);
                        values[values.length - 1] = result;
                        invoke(after.value(), after.methodParameterType(), values);
                    }
                }else {
                    invoke(after.value(), after.methodParameterType(), values);
                }
            }
            return result;
        }else {
            //如果方法被拦截的情况
            if (!StringUtils.isEmpty(before.invokeMethod())) {
                invoke(before.invokeMethod(), before.methodParameterType(), values);
            }
        }
        return null;
    }


    /**
     * 执行目标方法
     * @param methodName 方法名称
     * @param parameterType 方法需要的参数类型
     * @param parameterValue 方法参数值
     */
    private Object invoke(String methodName,Class<?>[] parameterType,Object[] parameterValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        List<String> partitionString = StringUtils.partitionString(methodName, " "); //分割字符串，检查是否包含类名
        if (partitionString.size() == 2) {
            //如果存在类名
            String className = partitionString.get(0);
            methodName = partitionString.get(1);
            Object targetObj = ClassUtils.object(Class.forName(className)); //如果使用类名，则根据类名创建目标类
            return invokeTargetMethod(targetObj, methodName, parameterType, parameterValue);
        } else if (partitionString.size() == 1) {
            //不存在类名
            return invokeTargetMethod(target, methodName, parameterType, parameterValue);
        } else {
            throw new RuntimeException("字符串监测异常");
        }
    }

    /**
     * 真正执行目标方法
     * @param targetObj     目标类
     * @param methodName    目标方法名称
     * @param parameterType 参数类型
     * @param parameterValue 参数值
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invokeTargetMethod(Object targetObj,String methodName,Class<?>[] parameterType,Object[] parameterValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (!ObjectUtils.isEmpty(parameterType) && parameterType[0] == Void.class) {
            //没有指定参数列表，说明是一个无参方法
            return targetObj.getClass().getMethod(methodName, null).invoke(targetObj, null);
        } else if (!ObjectUtils.isEmpty(parameterType)) {
            Method targetInvokeMethod = targetObj.getClass().getMethod(methodName, parameterType); //说明方法有参数
            Object[] values = new Object[parameterType.length]; //目标方法参数值
            /**
             * 注意：AOP方法如果需要参数，参数下标位置必须与源方法参数一致
             */
            for (int i = 0; i < parameterValue.length; i++) {
                values[i] = parameterValue[i];
            }
            /**
             * 如果 parameterType.length >= (parameterValue.length + 1)
             * 就认为末尾最后一个参数为目标对象
             */
            if (parameterType.length >= (parameterValue.length + 1)){
                values[parameterType.length - 1] = target;
            }
            return targetInvokeMethod.invoke(targetObj, values);
        }
        return null;
    }
}