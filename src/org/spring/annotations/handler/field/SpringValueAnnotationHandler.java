package org.spring.annotations.handler.field;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.annotations.autoconfig.ConfigurationProperties;
import org.spring.annotations.autoconfig.Value;
import org.spring.core.handler.SpringBeanFieldHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Field;

@Component
public class SpringValueAnnotationHandler implements SpringBeanFieldHandler {

    @Override
    public void handler(String beanName, Object bean, Field field) {
        Value values = field.getAnnotation(Value.class);
        if (values != null){
            ConfigurationProperties properties = bean.getClass().getAnnotation(ConfigurationProperties.class);
            String key = values.value();
            if (properties != null && !StringUtils.isEmpty(properties.prefix()) && values.prefix()){
                key = properties.prefix() + "." + key;
            }
            Object value = Application.getApplicationContext().getFactory().getProfileFactory().get(key);
            if (!ObjectUtils.isEmpty(value)) {
                field.setAccessible(true);
                ClassUtils.filedSetValue(field, value, bean);
            }
        }
    }
}
