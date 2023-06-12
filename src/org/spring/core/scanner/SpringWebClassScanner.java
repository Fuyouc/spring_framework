package org.spring.core.scanner;

/**
 * 类加载器
 */
public interface SpringWebClassScanner {
    /**
     * 扫描当前启动类下的所有class文件
     */
    void scan(Class<?> runClass);
}
