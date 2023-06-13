package org.spring.web.autoconfig;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.web.annotations.ControllerAdvice;
import org.spring.web.annotations.ExceptionHandler;
import org.spring.web.json.JsonUtils;

import javax.el.MethodNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@ConditionalOnMissingBean(name = "SpringWebExceptionHandler")
@ControllerAdvice
public class SpringWebExceptionHandler {

    @ExceptionHandler(exceptionClass = MethodNotFoundException.class)
    public void methodNotFoundHandler(HttpServletRequest request, HttpServletResponse response,Exception e) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        Map<String,Object> map = new HashMap<>();
        map.put("code","200");
        map.put("msg",request.getRequestURI() + e.getMessage());
        writer.println(JsonUtils.toString(map));
    }
}
