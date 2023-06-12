package org.spring.core.container.profile;

import org.spring.utils.global.ObjectUtils;

public interface ProfileFactory {

    /**
     * 添加配置文件 Key 与 Value
     */
    void add(String key,Object value);

    Object get(String key);

    void remove(String key);

}
