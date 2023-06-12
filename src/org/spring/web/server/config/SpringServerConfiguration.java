package org.spring.web.server.config;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.ConfigurationProperties;

@ConditionalOnMissingBean(SpringServerConfiguration.class)
@ConfigurationProperties(prefix = "spring.server")
public class SpringServerConfiguration {
    private int port = 80;
    private String serverName = "Tomcat";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return "SpringWebServerConfiguration{" +
                "port=" + port +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}
