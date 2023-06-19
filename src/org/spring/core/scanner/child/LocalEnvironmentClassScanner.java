package org.spring.core.scanner.child;

import org.spring.Application;
import org.spring.core.container.files.SpringFile;
import org.spring.core.loader.SpringFrameWorkClassLoader;
import org.spring.core.scanner.SpringWebClassScanner;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * 扫描本地环境下的class文件
 */
public class LocalEnvironmentClassScanner implements SpringWebClassScanner {
    @Override
    public void scan(Class<?> runClass) {
        try {
            scan(runClass, ClassUtils.getClassURL(runClass));
        } catch (UnsupportedEncodingException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void scan(Class<?> applicationClass, String path) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        File[] files = FileUtils.create(path).listFiles();
        if (files != null){
            //存在子文件的情况下
            for (File file : files) {
                if (file.isDirectory()){
                    //如果它是一个目录
                    directory(file,applicationClass);
                }
            }
        }
    }

    private void directory(File directory,Class<?> applicationClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        File[] files = directory.listFiles();
        if (files != null){
            for (File file : files) {
                if (file.isDirectory()){
                    //如果它是一个目录
                    directory(file,applicationClass);
                }else {
                    //说明是一个文件
                    classFile(file,applicationClass);
                }
            }
        }
    }

    private void classFile(File classFile,Class<?> applicationClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (FileUtils.isClassFile(classFile)){
            String classPath = FileUtils.getFileClassPath(applicationClass, classFile);
            Class<?> classObj = Class.forName(classPath);
            SpringFrameWorkClassLoader.loadClass(classObj);
        }else {
            try {
                Application.getApplicationContext().getFactory().getFileFactory().add(new SpringFile(classFile.getName(),new FileInputStream(classFile)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
