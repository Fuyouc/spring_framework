package org.spring.core.container.web;

import org.spring.web.server.servlet.interceptor.SpringWebInterceptor;

import java.util.List;

public interface SpringWebFactory {

    /**
     * 获取Http Request URI 映射工厂
     */
    HttpRequestMappingFactory getHttpRequestMappingFactory();

    /**
     * 添加Web拦截器
     */
    void addWebInterceptor(String prefix,SpringWebInterceptor webInterceptor);

    /**
     * 获取Web拦截器
     */
    List<SpringWebInterceptor> getWebInterceptor(String prefix);

}
