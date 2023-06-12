package org.spring.core.init;

import org.spring.Application;
import org.spring.core.handler.SpringBeanClassHandler;
import org.spring.core.handler.SpringBeanFieldHandler;
import org.spring.core.handler.SpringBeanMethodHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public final class SpringBeanInit {

    private static Set<String> set = new HashSet<>();
    private static List<SpringBeanClassHandler> classHandlers;
    private static List<SpringBeanFieldHandler> fieldHandlers;
    private static List<SpringBeanMethodHandler> methodHandlers;
    private static boolean first = true;

    public static void initBean(String beanName,Object bean){
        initClass(beanName,bean);
    }

    public static void initClass(String beanName,Object bean){

        if (first){
            classHandlers = Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringBeanClassHandler.class);
            fieldHandlers = Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringBeanFieldHandler.class);
            methodHandlers = Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringBeanMethodHandler.class);
            first = false;
        }

        for (SpringBeanClassHandler classHandler : classHandlers) {
            classHandler.handler(beanName,bean,bean.getClass());
        }
        initField(beanName,bean);
        initMethod(beanName,bean);
    }

    public static void initField(String beanName, Object bean){
        /**
         * 获取父类的字段（只获取公开的字段，否则会有不必要的浪费）
         */
        Field[] objSuperFields = bean.getClass().getSuperclass().getFields();
        /**
         * 获取子类自己的字段
         */
        Field[] objFields = bean.getClass().getDeclaredFields();

        /**
         * 将父类的字段与子类的字段融合，并且赋值
         */
        Field[] declaredFields = Arrays.copyOf(objSuperFields,objSuperFields.length + objFields.length);

        System.arraycopy(objFields,0,declaredFields,objSuperFields.length,objFields.length);

        for (int j = 0; j < declaredFields.length; ++j){
            Field field = declaredFields[j];
            String featureCode = field.getDeclaringClass().getTypeName() + "-" + field.getName();
            if (!set.contains(featureCode)) {
                set.add(featureCode);
                for (SpringBeanFieldHandler fieldHandler : fieldHandlers) {
                    fieldHandler.handler(beanName,bean,field);
                }
            }
        }
    }

    public static void initMethod(String beanName, Object bean){
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            for (SpringBeanMethodHandler methodHandler : methodHandlers) {
                methodHandler.handler(beanName,bean,method);
            }
        }
    }
}
