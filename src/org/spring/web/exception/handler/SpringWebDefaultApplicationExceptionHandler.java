package org.spring.web.exception.handler;

import org.spring.annotations.ConditionalOnMissingBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ConditionalOnMissingBean(SpringWebApplicationExceptionHandler.class)
public class SpringWebDefaultApplicationExceptionHandler implements SpringWebApplicationExceptionHandler{
    @Override
    public void exception(HttpServletRequest request, HttpServletResponse response, Throwable exception, int errorCode) {
        try {
            response.sendError(errorCode,exception.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
