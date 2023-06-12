package org.spring.core.container.web;

import org.spring.web.annotations.request.HttpRequestMethod;

import java.lang.reflect.Method;

public class SpringWebRequestMappingObject implements RequestMappingObject{

    private Object controller;
    private Method invokeMethod;
    private HttpRequestMethod httpRequestMethod;

    public SpringWebRequestMappingObject(Object controller, Method invokeMethod, HttpRequestMethod httpRequestMethod) {
        this.controller = controller;
        this.invokeMethod = invokeMethod;
        this.httpRequestMethod = httpRequestMethod;
    }

    @Override
    public Object getController() {
        return controller;
    }

    @Override
    public Method invokeMethod() {
        return invokeMethod;
    }

    @Override
    public HttpRequestMethod getRequestMethod() {
        return httpRequestMethod;
    }
}
