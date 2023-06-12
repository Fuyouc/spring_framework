package org.spring.core.resources.child;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.annotations.autoconfig.Value;
import org.spring.core.resources.SpringFrameworkResourceManager;
import org.spring.utils.global.ValueUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * 配置文件管理器
 */
@Component
public class ProfileResourceManager implements SpringFrameworkResourceManager {

    private static final String PATH = "/resources/application.propertips";

    @Override
    public void handler() {
        InputStream inputStream = Application.getRunClass().getResourceAsStream(PATH);
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Iterator<String> iterator = reader.lines().iterator();
            String string = null;
            Object value;
            while (iterator.hasNext()) {
                string = iterator.next();
                if (string.length() > 0 && string.charAt(0) != '#') { //不是注解
                    String key = string.substring(0, string.indexOf("="));
                    string = string.substring(key.length() + 1, string.length());
                    value = ValueUtils.stringConvert(string);
                    Application.getApplicationContext().getFactory().getProfileFactory().add(key,value);
                }
            }
        }else {
            throw new RuntimeException("请在当前根目录下创建 /resources/application.propertips 配置文件，这将方便您配置框架的底层功能");
        }
    }
}
