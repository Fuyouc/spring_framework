package org.spring.web.server.servlet.handler.http;

import com.sun.nio.sctp.HandlerResult;
import org.spring.Application;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.web.RequestMappingObject;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.web.annotations.InterceptIgnore;
import org.spring.web.annotations.handler.SpringWebControllerExceptionHandler;
import org.spring.web.annotations.request.HttpRequestMethod;
import org.spring.web.exception.HttpRequestMethodNotSupportedException;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;
import org.spring.web.json.annotations.JsonIgnore;
import org.spring.web.server.filter.SpringHttpFilterHandler;
import org.spring.web.server.servlet.SpringHttpServletHandler;
import org.spring.web.server.servlet.interceptor.SpringWebInterceptor;
import org.spring.web.server.servlet.response.SpringWebHttpResponseParser;
import org.spring.web.server.servlet.utils.ServletUtils;

import javax.el.MethodNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringWebHttpAPIServletHandler implements SpringHttpServletHandler {

    @Autowired
    private List<SpringHttpFilterHandler> httpFilterHandlers;

    /**
     * Controller层中的方法出现异常时，会转交给该该处理器执行
     */
    @Autowired
    private SpringWebControllerExceptionHandler controllerExceptionHandler;

    @Autowired
    private List<SpringWebRequestParameterHandler> requestParameterHandlers;

    @Autowired
    private List<SpringWebHttpResponseParser> webHttpResponseParsers;


    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response){
        /**
         * 过滤器方法校验请求被接收后，会先执行该方法，检查容器中的 Filter 是否需要过滤请求
         * 以责任链的形势往下传递，如果出现一个拦截，则不会执行目标方法
         */
        for (SpringHttpFilterHandler httpFilterHandler : httpFilterHandlers) {
            if (httpFilterHandler != null){
                boolean result = httpFilterHandler.handler(request, response);
                if (!result) return true;
            }
        }

        try {
            String uri = request.getRequestURI(); //获取访问到URI后缀
            RequestMappingObject requestMappingObject = Application.getApplicationContext().getFactory().getWebFactory().getHttpRequestMappingFactory().get(uri);
            if (!ObjectUtils.isEmpty(requestMappingObject)){
                Map.Entry<Boolean,Object> result = handlerRequest(request, response, requestMappingObject);
                if (!ObjectUtils.isEmpty(result)){
                    if (result.getKey()){
                        //如果结果已经被处理
                        for (SpringWebHttpResponseParser parser : webHttpResponseParsers) {
                            if (parser.handler(request,response,result.getValue(),requestMappingObject.invokeMethod())) {
                                return true;
                            }
                        }
                    }
                    return true;
                }
            }
        }catch (Exception e){
            if (!ObjectUtils.isEmpty(e.getCause())){
                controllerExceptionHandler.handler(request,response,e.getCause());
            }else {
                controllerExceptionHandler.handler(request,response,e);
            }
            return true;
        }
        return false;
    }

    private Map.Entry<Boolean,Object> handlerRequest(HttpServletRequest request, HttpServletResponse response,RequestMappingObject object) throws Exception{
        HttpRequestMethod requestMethod = object.getRequestMethod();
        switch (requestMethod){
            case GET:
                if (!request.getMethod().equals("GET")){
                    throw new MethodNotFoundException("不支持GET请求");
                }
                break;
            case POST:
                if (!request.getMethod().equals("POST")){
                    throw new MethodNotFoundException("不支持POST请求");
                }
                break;
        }
        return handlerRequest(object,request,response);
    }

    private Map.Entry<Boolean,Object> handlerRequest(RequestMappingObject obj,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Method method = obj.invokeMethod(); //获取目标方法对象
        Object methodResult = null;
        boolean interceptor = method.getAnnotation(InterceptIgnore.class) == null ? false : true;
        if (interceptor) {
            List<SpringWebInterceptor> springWebInterceptors = Application.getApplicationContext().getFactory().getWebFactory().getWebInterceptor(request.getRequestURI());
            for (SpringWebInterceptor webInterceptor : springWebInterceptors) {
                if (webInterceptor.beforeHandler(request, response, obj.invokeMethod(), obj.getController())) {
                    //如果被拦截，则直接返回，不进行任何处理（需要用户手动通过 response 响应给客户端信息）
                    return new AbstractMap.SimpleEntry<>(false, null);
                }
            }
            methodResult = handlerServletMethod(obj, request, response);
            for (SpringWebInterceptor webInterceptor : springWebInterceptors) {
                if (webInterceptor.afterHandler(request, response, method, methodResult)) {
                    //如果被拦截，则直接返回，不进行任何处理（需要用户手动通过 response 响应给客户端信息）
                    return new AbstractMap.SimpleEntry<>(false, null);
                }
            }
        }else {
            methodResult = handlerServletMethod(obj,request,response);
        }
        return new AbstractMap.SimpleEntry<>(true,methodResult);
    }

    private Object handlerServletMethod(RequestMappingObject obj,HttpServletRequest request,HttpServletResponse response) throws Exception{
        Method method = obj.invokeMethod(); //获取目标方法对象
        Object methodResult = null;
        Map<String, Object> cache = new HashMap<>();
        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters(); //获取方法上的参数
            //以方法上的参数个数创建一个参数值数组（Java中通过反射调用方法，方法参数值必须与参数个数一直（允许为null））
            Object[] parameterValue = new Object[parameters.length];
            int index = 0; //当前处理到第几个参数
            for (Parameter parameter : parameters) {
                h:
                for (SpringWebRequestParameterHandler requestParameterHandler : requestParameterHandlers) {
                    if (requestParameterHandler.allow(parameter)) {
                        Map.Entry<Boolean, Object> result = requestParameterHandler.handler(request, response, method, parameter, cache);
                        if (!ObjectUtils.isEmpty(result) && result.getKey()) {
                            parameterValue[index] = result.getValue();
                            break h;
                        }
                    }
                }
                index++;
            }
            methodResult = method.invoke(obj.getController(), parameterValue);
        }else {
            methodResult = method.invoke(obj.getController(), null);
        }
        return method;
    }

}
