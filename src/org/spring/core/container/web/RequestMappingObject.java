package org.spring.core.container.web;

import org.spring.web.annotations.request.HttpRequestMethod;

import java.lang.reflect.Method;

/**
 * Http Request 的映射对象
 */
public interface RequestMappingObject {

    Object getController();

    Method invokeMethod();

    HttpRequestMethod getRequestMethod();
}
