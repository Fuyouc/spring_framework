package org.spring.web.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web服务异常处理器
 */
public interface SpringWebApplicationExceptionHandler {
    /**
     * @param request
     * @param response
     * @param exception 异常信息
     * @param errorCode 错误代码
     */
    void exception(HttpServletRequest request,
                   HttpServletResponse response,
                   Throwable exception,
                   int errorCode);
}
