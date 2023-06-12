package org.spring.web.server;

/**
 * 框架服务管理器
 */
public interface SpringWebServerManager {
    /**
     * 启动服务
     */
    void start();

    /**
     * 关闭服务
     */
    void close();

    /**
     * 获取服务名称
     */
    String getServerName();
}
