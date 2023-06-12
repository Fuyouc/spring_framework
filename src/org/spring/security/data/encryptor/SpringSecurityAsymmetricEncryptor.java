package org.spring.security.data.encryptor;

/**
 * 非对称加密器
 */
public interface SpringSecurityAsymmetricEncryptor extends SpringSecurityEncryptor{
    /**
     * 公钥加密
     */
    String publicEncrypt(String data) throws Exception;

    /**
     * 私钥解密
     */
    String privateDecrypt(String encryptString) throws Exception;

    /**
     * 私钥加密
     */
    String privateEncrypt(String data) throws Exception;

    /**
     * 公钥解密
     */
    String publicEncryptDecrypt(String encryptString) throws Exception;
}
