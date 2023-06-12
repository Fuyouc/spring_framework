package org.spring.web.annotations.request.parameter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestPart {
    /**
     * 从请求体中获得参数值，要求请求类型必须为 POST 请求
     */
    //参数Key，如果为空，则使用字段名
    String value() default "";

    //是否允许该参数值为空，默认为true
    boolean required() default true;

    /**
     * 是否允许上次多个文件，上传的文件会被封装成 MultipartFile 对象
     * 允许参数中使用 List<MultipartFile> | MultipartFile 进行接收文件
     */
    boolean uploadMultipleFile() default true;
}
