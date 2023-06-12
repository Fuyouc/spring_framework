package org.spring.web.file;

/**
 * 监听解析 form-data 请求体过程中的数据回调
 */
public interface PostFormDataBodyListener {
    /**
     *  如果当前参数为普通参数，则调用该方法
     *  key：参数名
     *  value：参数值
     *  position：表示这是第几个参数
     */
    void parameter(int position,String key,String value);
    void file(int position,String bodyString); //如果是文件类型，则将整个body字符串返回
}
