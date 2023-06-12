package org.spring.web.annotations.request;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

    /**
     * 让 Servlet 与 controller 的方法进行映射绑定
     * 网络请求在被对应的 Servlet 接收后，会调用绑定的 controller 方法进行逻辑处理
     * 方法返回值会响应给客户端，采用JSON格式
     * 方法参数列表：
     *  - 允许方法参数上使用 HttpServletRequest、HttpServletResponse、HttpSession，框架会自动注入
     *  - 允许使用封装类来接收，使用 @RequestParam & @RequestPart 告诉框架获取的数据源的方式
     *  - 允许使用基本类型来接收，同以上
     */

    /**
     * 访问的URI，客户端在访问 http://服务器IP:端口/URI时，如果URI相同，则会被该方法接收进行处理
     */
    String value() default "/";

    /**
     * 指定请求类型，默认接收GET、POST请求
     */
    HttpRequestMethod method() default HttpRequestMethod.GET_AND_POST;
}
