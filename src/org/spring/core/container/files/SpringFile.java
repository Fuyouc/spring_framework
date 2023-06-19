package org.spring.core.container.files;

import java.io.InputStream;

public class SpringFile {
    private String name;
    private InputStream inputStream;

    public SpringFile(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String toString() {
        return "SpringFile{" +
                "name='" + name + '\'' +
                ", inputStream=" + inputStream +
                '}';
    }
}
