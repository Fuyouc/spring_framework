package org.spring.web.config;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.ConfigurationProperties;

@ConditionalOnMissingBean(SpringWebResourceConfiguration.class)
@ConfigurationProperties(prefix = "spring.web.resources")
public class SpringWebResourceConfiguration {
    private String prefix = "/static";
    private boolean enable = true;
    private String location = "/resources/static/";
    private int fileMaxSize = 1;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(int fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }
}
