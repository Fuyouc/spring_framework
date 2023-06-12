package org.spring.utils.global;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.CORBA.SystemException;
import org.spring.utils.environment.EnvironmentUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

public class ClassUtils {

    public static <T> T object(Class<?> targetClass){
        try {
            if (!targetClass.isInterface()){
                return (T) targetClass.newInstance();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isString(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return String.class.isAssignableFrom(value.getClass());
    }

    public static boolean isInt(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return int.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass());
    }

    public static boolean isDouble(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return double.class.isAssignableFrom(value.getClass()) || Double.class.isAssignableFrom(value.getClass());
    }

    public static boolean isFloat(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return float.class.isAssignableFrom(value.getClass()) || Float.class.isAssignableFrom(value.getClass());
    }

    public static boolean isBoolean(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return boolean.class.isAssignableFrom(value.getClass()) || Boolean.class.isAssignableFrom(value.getClass());
    }

    public static boolean isEnum(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return value.getClass().isEnum();
    }

    public static boolean isInterface(Object value){
        if (ObjectUtils.isEmpty(value)) return false;
        return value.getClass().isInterface();
    }

    public static boolean isString(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return String.class.isAssignableFrom(clazz);
    }

    public static boolean isInt(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz);
    }

    public static boolean isDouble(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz);
    }

    public static boolean isFloat(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return float.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz);
    }

    public static boolean isBoolean(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz);
    }

    public static boolean isEnum(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return clazz.isEnum();
    }

    public static boolean isInterface(Class<?> clazz){
        if (ObjectUtils.isEmpty(clazz)) return false;
        return clazz.isInterface();
    }

    public static void filedSetValue(Field field,Object value,Object targetObj){
        if (ObjectUtils.isEmpty(field) || ObjectUtils.isEmpty(targetObj)) return;
        try {
            if (isInt(value) || isDouble(value) || isFloat(value)){
                if (String.class.isAssignableFrom(field.getType())){
                    //如果字段是String，但是类型是其他类型，则转换成Stirng
                    value = String.valueOf(value);
                }
                field.set(targetObj,value);
                return;
            }
            //如果来到这里，说明是一个字符串
            if (isEnum(field.getType())){
                //如果是一个枚举类型
                setEnumFiledValue(field,value,targetObj);
            }else {
                field.set(targetObj, value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setEnumFiledValue(Field field,Object value,Object target){
        Class<?> enumClass = field.getType();
        for (Object enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant instanceof Enum<?>){
                Enum<?> e = (Enum<?>) enumConstant;
                if (e.name().equals(value)){
                    /**
                     * 枚举类型是一种特殊的类，它的所有实例都是在类加载时被创建的并分配好了内存空间，因此不能通过反射创建新的枚举实例。
                     * 但是，我们可以通过修改枚举常量的属性来模拟创建新的枚举实例
                     */
                    Object newEnum = Enum.valueOf((Class<? extends Enum>) enumClass, (String) value);
                    try {
                        field.set(target,newEnum);
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                    return;
                }
            }
        }
    }

    /**
     * 获得class的绝对路径
     * @param objClass
     * @return
     */
    public static String getClassURL(Class<?> objClass) throws UnsupportedEncodingException {
        String path = objClass.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = java.net.URLDecoder.decode(path, "UTF-8");
        String packName = objClass.getPackage().getName().replaceAll("\\.","/");
        String scanPath = path + packName;
        switch (EnvironmentUtils.getSystem()){
            case WINDOWS:
                scanPath = scanPath.charAt(0) == '/' ? scanPath.substring(1,scanPath.length()) : scanPath;
                scanPath = scanPath.replaceAll("/","\\\\");
                break;
            case MAC:
                break;
            default:
                throw new UnknownError("框架暂时不支持当前操作系统，请与开发人员反馈。");
        }
        return scanPath;
    }

    /**
     * 判断类型是否实基本类型或包装类型
     *
     * @param clz 类
     * @return true: 是   false: 不是
     */
    public static boolean isWrapClass(Class<?> clz) {
        try {
            return String.class.isAssignableFrom (clz) || (clz.isPrimitive() || ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive());
        } catch (Exception e) {
            return false;
        }
    }


}
