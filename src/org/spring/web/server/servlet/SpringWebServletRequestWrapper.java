package org.spring.web.server.servlet;

import org.apache.catalina.connector.Request;
import org.apache.tomcat.util.http.Parameters;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SpringWebServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 存储body数据的容器
     */
    private byte[] body;

    private String requestURI;

    public SpringWebServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream inputStream = request.getInputStream();
        StringBuilder sb = new StringBuilder();
        int read;
        byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1){
            sb.append(new String(bytes,0,read));
        }
        body = sb.toString().getBytes(StandardCharsets.UTF_8);
        requestURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");
    }


    @Override
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * 修改请求体的内容
     */
    public void setBody(String body){
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 设置参数
     */
    public void setParameter(String name,String newValue){
        try {
            ServletRequest servletRequest = getRequest();
            Field field = servletRequest.getClass().getDeclaredField("request");
            field.setAccessible(true);
            Request request = (Request) field.get(servletRequest);
            Field coyoteField = request.getClass().getDeclaredField("coyoteRequest");
            coyoteField.setAccessible(true);
            org.apache.coyote.Request coyoteRequest = (org.apache.coyote.Request) coyoteField.get(request);
            Parameters parameters = coyoteRequest.getParameters();
            parameters.addParameter(name,newValue);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * 获取请求Body
     *
     * @return String
     */
    public String getBodyString() {
        final InputStream inputStream = new ByteArrayInputStream(body);
        return inputStream2String(inputStream);
    }

    /**
     * 将inputStream里的数据读取出来并转换成字符串
     *
     * @param inputStream inputStream
     * @return String
     */
    private String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return sb.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
