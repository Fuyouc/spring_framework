package org.spring.web.server.servlet.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * SpringWebInterceptor拦截器（在Filter之后）
 * 可用于数据加密和解密
 */
public interface SpringWebInterceptor {
    /**
     * Controller method before execution
     * @return false表示放行，true表示拦截
     */
    boolean beforeHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Method method,Object controller) throws Exception;

    /**
     * Controller method after execution
     * @param result 响应及结果
     * @return 返回false表示使用框架内部的默认响应结果，返回true表示自定义响应结果，不采用框架内部
     */
    boolean afterHandler(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Method method,Object result) throws Exception;
}
