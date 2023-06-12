package org.spring.jdbc.config;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.ConfigurationProperties;
import org.spring.annotations.autoconfig.Value;
import org.spring.jdbc.enums.DataSourceType;

/**
 * 数据源配置
 */
@ConditionalOnMissingBean(DataSourceConfiguration.class)
@ConfigurationProperties(prefix = "spring.datasource")
public final class DataSourceConfiguration {
    private DataSourceType dataSource = DataSourceType.MYSQL;
    private String driver;
    private String url;
    private String username;
    private String password;
    private int capacity = 4; //连接池容量
    private boolean enable = false;


    public void setDataSource(DataSourceType dataSource) {
        this.dataSource = dataSource;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public DataSourceType getDataSource() {
        return dataSource;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "DataSourceConfiguration{" +
                "dataSource=" + dataSource +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
