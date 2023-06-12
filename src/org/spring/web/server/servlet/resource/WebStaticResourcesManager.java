package org.spring.web.server.servlet.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 静态资源管理器，用于响应静态资源的处理
 */
public interface WebStaticResourcesManager {
    boolean handler(HttpServletRequest request, HttpServletResponse response);
    boolean allow(String uri);
}
