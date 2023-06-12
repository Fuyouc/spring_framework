package org.spring.web.server.servlet.resource.child;


import org.spring.annotations.autoconfig.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 处理其他文件，提供给客户端下载
 */
@Component
public class FileResourcesManager extends AbstractResourcesManager {

    protected boolean result(InputStream inputStream,String fileName,HttpServletResponse response){
        if (inputStream != null){
            try {
                // 设置响应头，指定下载文件的类型
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
                try (OutputStream outputStream = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean allow(String uri) {
        return false;
    }
}
