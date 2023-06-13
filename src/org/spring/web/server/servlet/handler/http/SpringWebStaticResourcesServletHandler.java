package org.spring.web.server.servlet.handler.http;


import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ObjectUtils;
import org.spring.web.autoconfig.SpringWebResourceConfiguration;
import org.spring.web.server.servlet.SpringHttpServletHandler;
import org.spring.web.server.servlet.resource.WebStaticResourcesManager;
import org.spring.web.server.servlet.resource.child.FileResourcesManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理静态资源文件请求
 */
@Component
public class SpringWebStaticResourcesServletHandler implements SpringHttpServletHandler {

    @Autowired
    private SpringWebResourceConfiguration webConfiguration;

    @Autowired
    private List<WebStaticResourcesManager> resourcesManagers;

    @Autowired
    private FileResourcesManager fileResourcesManager;

    /**
     * 校验是否是文件请求(是否以.xxx为结尾)
     */
    private static final String REGEX = "^.*\\..+";

    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI();
        if (!webConfiguration.isEnable() || !uri.matches(REGEX)) return false;
        if (uri.startsWith(webConfiguration.getPrefix())){
            WebStaticResourcesManager manager = null;
            for (WebStaticResourcesManager resourcesManager : resourcesManagers) {
                if (resourcesManager.allow(uri)){
                    manager = resourcesManager;
                    break;
                }
            }
            manager = ObjectUtils.isEmpty(manager) ? fileResourcesManager : manager;
            return manager.handler(request,response);
        }
        return false;
    }

}
