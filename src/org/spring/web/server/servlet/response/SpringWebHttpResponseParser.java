package org.spring.web.server.servlet.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Web HTTP API响应解析器
 */
public interface SpringWebHttpResponseParser {
    boolean handler(HttpServletRequest request, HttpServletResponse response, Object result, Method method);
}
