package org.spring.annotations.handler.clazz;

import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Component;
import org.spring.annotations.autoconfig.ConfigurationProperties;
import org.spring.annotations.autoconfig.Value;
import org.spring.core.handler.SpringBeanClassHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Field;

@Component
public class SpringConfigurationPropertiesHandler implements SpringBeanClassHandler {
    @Override
    public void handler(String beanName, Object bean,Class<?> beanClass) {
        ConfigurationProperties properties = beanClass.getAnnotation(ConfigurationProperties.class);
        if (properties != null){
            String prefix = properties.prefix();
            if (StringUtils.isEmpty(prefix)) return;
            for (Field field : beanClass.getDeclaredFields()) {
                field.setAccessible(true);
                Value annotationValue = field.getAnnotation(Value.class);
                String key = prefix + ".";
                if (!ObjectUtils.isEmpty(annotationValue)){
                    key += annotationValue.value();
                }else {
                    key += StringUtils.convertKey(field.getName());
                }
                Object value = Application.getApplicationContext().getFactory().getProfileFactory().get(key);
                if (!ObjectUtils.isEmpty(value)) {
                    ClassUtils.filedSetValue(field, value, bean);
                }
            }
        }
    }
}
