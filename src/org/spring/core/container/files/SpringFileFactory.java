package org.spring.core.container.files;

import java.util.ArrayList;
import java.util.List;

public class SpringFileFactory implements FileFactory{

    private List<SpringFile> files = new ArrayList<>();

    @Override
    public void add(SpringFile file) {
        files.add(file);
    }

    @Override
    public List<SpringFile> getFiles() {
        return files;
    }
}
