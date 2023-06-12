package org.spring.security.data.config;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.ConfigurationProperties;

@ConditionalOnMissingBean(SpringSecurityConfiguration.class)
@ConfigurationProperties(prefix = "spring.security.data")
public class SpringSecurityConfiguration {
    private String publicKey;
    private String privateKey;
    private int initialize;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getInitialize() {
        return initialize;
    }

    public void setInitialize(int initialize) {
        this.initialize = initialize;
    }

    @Override
    public String toString() {
        return "SpringSecurityConfiguration{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", initialize=" + initialize +
                '}';
    }
}
