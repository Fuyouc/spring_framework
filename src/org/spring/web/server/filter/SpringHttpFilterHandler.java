package org.spring.web.server.filter;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP Filter 过滤器接口（伪Filter）
 */
public interface SpringHttpFilterHandler {
    boolean handler(HttpServletRequest request, HttpServletResponse response);
}
