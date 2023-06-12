package org.spring.utils.global;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtils {

    public static File create(String path){
        return new File(path);
    }

    /**
     * 获取文件的class类路径
     * @param file        文件
     * @return
     */
    public static String getFileClassPath(Class<?> applicationClass,File file){
       if (!isClassFile(file)) return null; //如果不是.class 结尾，则直接返回
        String classPath = file.getPath().substring(applicationClass.getResource("/").getPath().length() - 1,file.getPath().length()); //获取类路径
        classPath = classPath.substring(0,classPath.indexOf(".")); //截取掉.class
        switch (EnvironmentUtils.getSystem()){
            case WINDOWS:
                classPath = classPath.replaceAll("\\\\","."); //将 \ 转换 .
                break;
            case MAC:
                classPath = classPath.substring(1,classPath.length()); //将最前面的 / 去掉
                classPath = classPath.replaceAll("\\/","."); //将 / 转换 .
                break;
        }
        return classPath;
    }

    /**
     * 获取文件的class名称
     * @return
     */
    public static String getFileClassName(File file){
        if (isClassFile(file)) {
            String fileName = file.getName();
            String objName = fileName.substring(0, fileName.indexOf(".")); //获取类名称（默认使用类名称当做Key）
            return objName;
        }
        return null;
    }

    /**
     * 是否是一个class文件
     * @return
     */
    public static boolean isClassFile(File file){
        String fileName = file.getName();
        if (!fileName.endsWith(".class")) return false; //如果不是.class 结尾，则返回false
        return true;
    }
}
