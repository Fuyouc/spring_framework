package org.spring.web.server.servlet.resource.child;


import org.spring.annotations.autoconfig.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class HtmlResourcesManager extends AbstractResourcesManager {

    protected boolean result(InputStream inputStream,String fileName,HttpServletResponse response){
        if (inputStream != null){
            response.setHeader("Content-Type", "text/html");
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
        }
        return false;
    }

    @Override
    public boolean allow(String uri) {
        return uri.endsWith(".html") || uri.endsWith(".css") || uri.endsWith(".js");
    }
}
