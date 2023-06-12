package org.spring.jdbc.connection;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.PostConstruct;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.config.DataSourceConfiguration;
import org.spring.utils.global.ObjectUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ConditionalOnMissingBean(JDBCConnectionPoolManager.class)
public class SpringJDBCConnectionPollManager implements JDBCConnectionPoolManager{

    @Autowired
    public DataSourceConfiguration configuration;

    private Connection connection;

    @PostConstruct
    private void init(){
        if (!configuration.isEnable()) return;
        try {
            Class.forName(configuration.getDriver());
            connection = DriverManager.getConnection(configuration.getUrl(),configuration.getUsername(),configuration.getPassword());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        if (!configuration.isEnable()){
            throw new RuntimeException("未启动JDBC，请在配置文件中配置 spring.datasource.enable=true");
        }
        return connection;
    }

}
