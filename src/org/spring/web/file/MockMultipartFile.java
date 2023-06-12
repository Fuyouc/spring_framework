package org.spring.web.file;


import org.spring.utils.global.ObjectUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockMultipartFile implements MultipartFile {

    private String name;
    private String fileName;
    private String contentType;
    private InputStream inputStream;
    private long fileSize;

    public MockMultipartFile() {
    }

    public MockMultipartFile(String name, String fileName, String contentType) {
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return
                ObjectUtils.isEmpty(name)
                && ObjectUtils.isEmpty(fileName)
                && ObjectUtils.isEmpty(contentType)
                && ObjectUtils.isEmpty(inputStream)
                ;
    }

    @Override
    public long getSize() {
        return fileSize;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void output(String path) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,read);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public String toString() {
        return "MockMultipartFile{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", inputStream=" + inputStream +
                ", fileSize=" + fileSize +
                '}';
    }
}
