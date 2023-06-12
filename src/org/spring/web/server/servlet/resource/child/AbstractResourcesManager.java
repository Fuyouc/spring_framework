package org.spring.web.server.servlet.resource.child;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.server.servlet.resource.WebFileResourcesManager;
import org.spring.web.server.servlet.resource.WebStaticResourcesManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public abstract class AbstractResourcesManager implements WebStaticResourcesManager {

    @Autowired
    public static WebFileResourcesManager webFileResourcesManager;

    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response) {
        String fileName = getFileName(request);
        if (fileName == null) return false;
        return result(webFileResourcesManager.getFileInputStream(fileName),fileName,response);
    }

    /**
     * 获取访问的文件名
     */
    private String getFileName(HttpServletRequest request){
        String uri = request.getRequestURI();
        if (ObjectUtils.isEmpty(uri) || StringUtils.isEmpty(uri)) return null;
        try {
            //获取第二个出现 / 的下标，如果没有。则走异常
            int startIndex = uri.indexOf("/", 1);
            return uri.substring(startIndex + 1,uri.length());
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 根据不同的文件格式响应不同的状态效果
     * @param inputStream
     * @param fileName
     * @param response
     * @return
     */
    protected abstract boolean result(InputStream inputStream, String fileName, HttpServletResponse response);
}
