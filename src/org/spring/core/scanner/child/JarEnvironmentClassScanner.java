package org.spring.core.scanner.child;

import org.spring.Application;
import org.spring.core.container.files.SpringFile;
import org.spring.core.loader.SpringFrameWorkClassLoader;
import org.spring.core.scanner.SpringWebClassScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描jar环境下的class文件
 */
public class JarEnvironmentClassScanner implements SpringWebClassScanner {
    @Override
    public void scan(Class<?> runClass) {
        try {
            String basePack = runClass.getPackage().getName();

            //通过当前线程得到类加载器从而得到URL的枚举
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(basePack.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();//得到的结果大概是：jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit
                String protocol = url.getProtocol();//大概是jar
                if ("jar".equalsIgnoreCase(protocol)) {
                    //转换为JarURLConnection
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    if (connection != null) {
                        JarFile jarFile = connection.getJarFile();
                        if (jarFile != null) {
                            //得到该jar文件下面的类实体
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                //这里我们需要过滤不是class文件和不在basePack包名下的类
                                if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/",".").startsWith(basePack)) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                    Class cls = Class.forName(className);
                                    SpringFrameWorkClassLoader.loadClass(cls);
                                }else {
                                    try {
                                        Application.getApplicationContext().getFactory().getFileFactory().add(new SpringFile(jarEntryName,jarFile.getInputStream(entry)));
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("异常信息：" + e.getMessage());
        }
    }
}
