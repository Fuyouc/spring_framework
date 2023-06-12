package org.spring.web.file;

import java.io.IOException;
import java.io.InputStream;

public interface MultipartFile {
    String getName(); //获取参数名

    String getOriginalFilename(); //获取文件名称

    String getContentType();    //获取上传的文件类型

    boolean isEmpty();          //是否有文件

    long getSize();             //获取文件大小（字节单位）

    InputStream getInputStream() throws IOException; //获取输入流对象

    void output(String path) throws IOException; //将文件存储到指定位置
}
