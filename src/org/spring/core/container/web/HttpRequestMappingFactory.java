package org.spring.core.container.web;

/**
 *
 */
public interface HttpRequestMappingFactory {

    void add(String uri,RequestMappingObject object);

    RequestMappingObject get(String uri);

    boolean containsURI(String uri);

}
