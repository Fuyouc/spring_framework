package org.spring.core.container.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringWebHttpRequestMappingFactory implements HttpRequestMappingFactory{

    private static Map<String,RequestMappingObject> map = new ConcurrentHashMap<>();

    @Override
    public void add(String uri, RequestMappingObject object) {
        map.put(uri,object);
    }

    public RequestMappingObject get(String uri) {
        return map.get(uri);
    }

    @Override
    public boolean containsURI(String uri) {
        return map.containsKey(uri);
    }
}
