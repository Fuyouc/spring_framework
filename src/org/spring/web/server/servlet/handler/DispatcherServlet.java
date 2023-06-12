package org.spring.web.server.servlet.handler;


import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.StringUtils;
import org.spring.web.exception.handler.SpringWebApplicationExceptionHandler;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;
import org.spring.web.server.servlet.SpringHttpServletHandler;
import org.spring.web.server.servlet.SpringWebServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.List;

/**
 * DispatcherServlet处理器
 * 所有请求都会经过此处
 */
@Component
public final class DispatcherServlet {

    @Autowired
    private SpringWebApplicationExceptionHandler applicationExceptionHandler;

    /**
     * 静态资源Servlet处理器
     */
    @Autowired
    private List<SpringHttpServletHandler> servletHandlers;

    @Autowired
    private List<SpringWebRequestParameterHandler> requestParameterHandlers;


    public void handler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /**
         * 将原始HttpServletRequest封装成HttpServletRequestWrapper（为了解决request.getInputStream方法数据流只能读取一次问题）
         */
        request = new SpringWebServletRequestWrapper(request);

        for (SpringHttpServletHandler handler : servletHandlers) {
            if (handler.handler(request,response)) {
                return;
            }
        }

        error(request,response);
    }


    /**
     * 如果本次请求没有任何处理器可以处理，返回404无法找到
     */
    private void error(HttpServletRequest request,HttpServletResponse response){
        RuntimeException exception = new RuntimeException("找不到：" + request.getRequestURI() + " 的资源");
        applicationExceptionHandler.exception(request,response,exception,404); //交给处理器处理
    }
}
