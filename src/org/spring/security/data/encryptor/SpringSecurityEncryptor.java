package org.spring.security.data.encryptor;

/**
 * 数据加密器
 */
public interface SpringSecurityEncryptor {
    /**
     * 将data数据进行加密，并返回密文
     */
    String encrypt(String data) throws Exception;

    /**
     * 将密文解密，转换成实际数据
     */
    String decrypt(String encryptString) throws Exception;
}
