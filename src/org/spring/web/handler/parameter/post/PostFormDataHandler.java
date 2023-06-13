package org.spring.web.handler.parameter.post;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.parameter.RequestPart;
import org.spring.web.autoconfig.SpringWebResourceConfiguration;
import org.spring.web.file.MockMultipartFile;
import org.spring.web.file.MultipartFile;
import org.spring.web.file.PostFormDataBodyListener;
import org.spring.web.file.exception.NotAllowedUploadMultipleFile;
import org.spring.web.server.servlet.SpringWebServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
public class PostFormDataHandler implements PostMethodParameterHandler {

    @Autowired
    private SpringWebResourceConfiguration configuration;

    private static int DEFAULT_FACTOR = 1024;

    @Override
    public Map.Entry<Boolean,  Map<String,Object>> handler(HttpServletRequest request, Method method, Parameter parameter) {
        Map<String,Object> map = new HashMap<>();
        String bodyString = ((SpringWebServletRequestWrapper) request).getBodyString(); //获取bodyString
        StringUtils.getPostFormBodyData(bodyString, new PostFormDataBodyListener() {
            @Override
            public void parameter(int position,String key, String value) {
                map.put(key,value);
            }

            @Override
            public void file(int position,String bodyString) {
                List<MultipartFile> bodyFile = getBodyFile(parameter, request);
                List<MultipartFile> multipartFiles;
                /**
                 * 获取所有的文件
                 */
                for (MultipartFile file : bodyFile) {
                    if (!map.containsKey(file.getName())){
                        //根据上传的name值来辨别文件的类型
                        multipartFiles = new ArrayList<>();
                        multipartFiles.add(file);
                        map.put(file.getName(),multipartFiles);
                    }else {
                        multipartFiles = (List<MultipartFile>) map.get(file.getName());
                        multipartFiles.add(file);
                    }
                }
            }
        });
        return new AbstractMap.SimpleEntry<>(true,map);
    }

    private List<MultipartFile> getBodyFile(Parameter parameter,HttpServletRequest request){
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8"); //解决中文名称乱码
        upload.setSizeMax(configuration.getFileMaxSize() * (DEFAULT_FACTOR * DEFAULT_FACTOR)); //设置上传文件大小
        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            if (fileItems.size() > 1){
                //如果上传多个文件
                if (!parameter.getAnnotation(RequestPart.class).uploadMultipleFile()) {
                    //如果不允许上次多个文件
                    throw new NotAllowedUploadMultipleFile("不允许多个文件上传，文件个数：" + fileItems.size());
                }
            }
            Iterator<FileItem> iterator = fileItems.iterator();
            List<MultipartFile> list = new ArrayList<>();
            while (iterator.hasNext()){
                FileItem item = iterator.next();
                if (!item.isFormField()) {
                    MockMultipartFile multipartFile = new MockMultipartFile();
                    multipartFile.setName(item.getFieldName());
                    multipartFile.setFileName(item.getName());
                    multipartFile.setContentType(item.getContentType());
                    multipartFile.setInputStream(item.getInputStream());
                    multipartFile.setFileSize(item.getSize());
                    list.add(multipartFile);
                }
            }
            return list;
        } catch (FileUploadException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean allow(HttpServletRequest request) {
        if (request.getContentType().startsWith("multipart/form-data")){
            return true;
        };
        return false;
    }
}
