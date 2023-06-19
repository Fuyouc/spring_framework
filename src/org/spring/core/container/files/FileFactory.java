package org.spring.core.container.files;

import java.util.List;

public interface FileFactory {

    void add(SpringFile file);

    List<SpringFile> getFiles();
}
