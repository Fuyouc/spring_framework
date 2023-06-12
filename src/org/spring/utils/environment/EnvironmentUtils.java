package org.spring.utils.environment;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class EnvironmentUtils {
    //判断当前环境是否是jar包环境
    public static boolean isRunningFromJar(Class<?> targetClass){
        // 获取当前类的ProtectionDomain
        ProtectionDomain protectionDomain = targetClass.getProtectionDomain();
        if (protectionDomain == null) {
            return false;
        }

        // 获取当前类的CodeSource
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            return false;
        }

        // 获取当前类的URL
        URL location = codeSource.getLocation();

        // 判断URL是否以".jar"结尾
        String path = location.getPath();
        boolean isJar = path.endsWith(".jar");

        return isJar;
    }
}
