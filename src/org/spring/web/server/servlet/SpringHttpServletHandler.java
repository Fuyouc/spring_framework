package org.spring.web.server.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet请求处理器
 *  - WebHttpServletHandler：处理Http接口请求处理器
 *  - WebStaticResourcesServletHandler：处理http静态资源请求处理器
 */
public interface SpringHttpServletHandler {
    boolean handler(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
