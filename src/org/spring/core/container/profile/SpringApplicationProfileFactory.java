package org.spring.core.container.profile;

import org.spring.core.container.profile.ProfileFactory;
import org.spring.utils.global.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public final class SpringApplicationProfileFactory implements ProfileFactory {
    private Map<String,Object> factory = new HashMap<>();

    @Override
    public void add(String key, Object value) {
        factory.put(key,value);
    }

    public Object get(String key){
        return factory.get(key);
    }

    public void remove(String key){
        factory.remove(key);
    }
}
