package org.spring.web.server.servlet.response.handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.web.json.JsonUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.web.server.servlet.response.SpringWebHttpResponseParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 将结果转换成JSON返回给客户端
 */
@Component
public class SpringJsonResponseHandler implements SpringWebHttpResponseParser {
    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response, Object result, Method method) {
        String json = JsonUtils.toString(result);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            if (!ObjectUtils.isEmpty(json)){
                writer.println(json);
            }else {
                writer.println(result);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
