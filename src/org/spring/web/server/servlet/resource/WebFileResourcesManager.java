package org.spring.web.server.servlet.resource;

import org.spring.Application;
import org.spring.annotations.PostConstruct;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.web.config.SpringWebResourceConfiguration;

import java.io.*;

/**
 * Web文件资源管理器
 */
@Component
public final class WebFileResourcesManager {

    private static final String DEFAULT_STATIC_RESOURCES_PATH = "/resources/static/";

    @Autowired
    private SpringWebResourceConfiguration webConfiguration;

    @PostConstruct
    private void check(){
        if ("".equals(webConfiguration.getLocation())) webConfiguration.setLocation("/resources/static/");
        else if (!webConfiguration.getLocation().endsWith("/")) webConfiguration.setLocation(webConfiguration.getLocation() + "/");
    }

    /**
     * 获取文件的输入流
     */
    public InputStream getFileInputStream(String fileName){
        if (DEFAULT_STATIC_RESOURCES_PATH.equals(webConfiguration.getLocation()) || webConfiguration.getLocation().startsWith("/resources")) {
            //如果采用的还是默认路径或者在/resources下有其他文件夹
            return getLocalFileInputStream(webConfiguration.getLocation(),fileName);
        }else {
            return getOutsideImageHandler(webConfiguration.getLocation(),fileName);
        }
    }

    /**
     * 保存文件到静态资源文件夹下（返回文件大小）
     * 不支持保存在项目的/resources/文件下，也不介意，因为如果保存在这里面，程序也没办法动态的知道这个文件被写入，也就访问不到
     * 需要重新编译才行，所以推荐使用外部的存储路径
     */
    public long saveFile(String fileName,InputStream fileInputStream){
        if (fileInputStream == null || webConfiguration.getLocation().startsWith("/resources")) return 0;
        File file = new File(webConfiguration.getLocation() + fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            fileInputStream.close();
            outputStream.flush();
            return file.length();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private InputStream getLocalFileInputStream(String location,String fileName){
        return Application.getRunClass().getResourceAsStream(location + fileName);
    }

    private InputStream getOutsideImageHandler(String location,String fileName){
        try {
            FileInputStream inputStream = new FileInputStream(location + fileName);
            return inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
