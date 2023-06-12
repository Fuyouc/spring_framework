package org.spring.core.container.web;

import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.StringUtils;
import org.spring.web.server.servlet.interceptor.SpringWebInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SpringWebComponentFactory implements SpringWebFactory{

    private Map<String, SpringWebInterceptor> webInterceptorMap;

    private HttpRequestMappingFactory requestMappingFactory;

    public SpringWebComponentFactory() {
        requestMappingFactory = new SpringWebHttpRequestMappingFactory();
        webInterceptorMap = new ConcurrentHashMap<>();
    }

    @Override
    public HttpRequestMappingFactory getHttpRequestMappingFactory() {
        return requestMappingFactory;
    }

    @Override
    public void addWebInterceptor(String prefix, SpringWebInterceptor webInterceptor) {
        if (!webInterceptorMap.containsKey(prefix)){
            webInterceptorMap.put(prefix,webInterceptor);
        }
    }

    @Override
    public List<SpringWebInterceptor> getWebInterceptor(String prefix) {
        return  webInterceptorMap.entrySet()
                .stream()
                .filter(entry -> prefix.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
