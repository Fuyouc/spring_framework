package org.spring.web.annotations.handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.web.annotations.ControllerAdvice;
import org.spring.web.annotations.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Component
/**
 * 扫描用户是否存在 @ControllerAdvice 注解的类，该类为异常处理类
 * 在类中的方法上使用 @ExceptionHandler，并指定需要处理的异常
 * 在捕获到异常后，会执行对应的方法
 * 方法允许没有参数，如果需要参数，必须在方法上声明以下三个参数
 *  - 1、HttpServletRequest
 *  - 2、HttpServletResponse
 *  - 3、Exception（异常信息）
 */
public final class SpringWebControllerExceptionHandler {
    public void handler(HttpServletRequest request, HttpServletResponse response, Throwable e){
        List<Object> exceptionHandlers = SpringWebControllerAdviceAnnotationHandler.getExceptionHandler();
        for (int i = 0; i < exceptionHandlers.size(); i++) {
            Object target = exceptionHandlers.get(i);
            Class<?> targetClass = target.getClass();
            if (targetClass.getAnnotation(ControllerAdvice.class) != null){
                for (Method method : targetClass.getMethods()) {
                    ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
                    if (exceptionHandler != null){
                        //如果方法上存在 @ExceptionHandler 注解
                        Class<?>[] classes = exceptionHandler.exceptionClass(); //获取方法上需要处理的class
                        if (isHandler(classes,e)){
                            try {
                                //如果该方法支持这个异常的处理
                                int parameterCount = method.getParameterCount();
                                if (parameterCount == 0){
                                    method.invoke(target,null);
                                }else {
                                    Object[] value = new Object[3];
                                    value[0] = request;
                                    value[1] = response;
                                    value[2] = e;
                                    method.invoke(target,value);
                                }
                            }catch (Exception es){
                                es.printStackTrace();
                            }
                            return;
                        }
                    }
                }
            }
        }
        //如果没有人处理这个异常，则抛出程序外
        e.printStackTrace();
    }


    private boolean isHandler(Class<?>[] classes,Throwable e){
        for (Class<?> aClass : classes) {
            if (aClass.isInstance(e)){
                return true;
            }
        }
        return false;
    }
}
