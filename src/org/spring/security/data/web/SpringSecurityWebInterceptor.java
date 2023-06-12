package org.spring.security.data.web;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.web.json.JsonUtils;
import org.spring.security.annotations.SecurityIgnore;
import org.spring.security.data.encryptor.SpringSecurityEncryptor;
import org.spring.security.data.exception.SpringSecurityException;
import org.spring.utils.global.ObjectUtils;
import org.spring.web.server.servlet.SpringWebServletRequestWrapper;
import org.spring.web.server.servlet.interceptor.SpringWebInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SpringSecurityWebInterceptor implements SpringWebInterceptor {

    @Autowired
    private SpringSecurityEncryptor dataEncryptor;

    @Override
    public boolean beforeHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Method method, Object controller) {
        if (check(httpServletRequest,method)){
            SpringWebServletRequestWrapper requestWrapper = (SpringWebServletRequestWrapper) httpServletRequest;
            try {
                String decrypt = dataEncryptor.decrypt(requestWrapper.getBodyString());
                requestWrapper.setBody(decrypt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean afterHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Method method, Object result) {
        if (check(httpServletRequest,method)){
            PrintWriter writer = null;
            try {
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                writer = httpServletResponse.getWriter();
                String value = ObjectUtils.isEmpty(result) ? null : dataEncryptor.encrypt(JsonUtils.toString(result));
                Map<String,Object> map = new HashMap<>();
                map.put("code",200);
                map.put("msg","访问成功");
                map.put("flag",true);
                map.put("data",value);
                writer.println(JsonUtils.toString(map));
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (writer != null){
                    writer.flush();
                    writer.close();
                }
            }

            return true;
        }
        return false;
    }

    private boolean check(HttpServletRequest request,Method method){
        if ("POST".equals(request.getMethod()) && request.getContentType().startsWith("application/json")) {
            if (dataEncryptor == null) {
                throw new SpringSecurityException("找不到相应的加密器，无法使用加密功能，请尝试向IOC容器中注入一个 SpringSecurityDataEncryptor");
            }
            if (!ObjectUtils.isEmpty(method.getAnnotation(SecurityIgnore.class))) {
                return false;
            }
            return true;
        }
        return false;
    }
}
