package org.spring.security.data.encryptor.child;

import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.PostConstruct;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.security.data.SpringDataSecurityKeyFactory;
import org.spring.security.data.config.SpringSecurityConfiguration;
import org.spring.security.data.encryptor.SpringSecurityAsymmetricEncryptor;
import org.spring.security.data.encryptor.SpringSecurityEncryptor;
import org.spring.security.data.utils.SpringDataSecurityUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 采用RSA非对称加密算法进行加密
 */
public class SpringSecurityRSAEncryptor implements SpringSecurityAsymmetricEncryptor {

    private static final String ALGORITHM = "RSA";
    @Autowired
    private SpringSecurityConfiguration configuration;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    private static final String CHARSET = "UTF-8";
    private static final String RSA_ALGORITHM = "RSA";
    /** 针对 keysize = 1024 RSA最大加密明文大小 117 */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**  针对 keysize = 1024  RSA最大解密密文大小  128*/
    private static final int MAX_DECRYPT_BLOCK = 128;

    @PostConstruct
    protected void initEncryptor() {
        try {
            if (ObjectUtils.isEmpty(configuration) || StringUtils.isEmpty(configuration.getPublicKey())
                    || StringUtils.isEmpty(configuration.getPrivateKey())){
                //如果公钥和秘钥有一个为空，则重新创建新的公钥与秘钥
                KeyPair keyPair = SpringDataSecurityKeyFactory.RSA(ObjectUtils.isEmpty(configuration) ? 1024 : configuration.getInitialize());
                publicKey =  keyPair.getPublic();
                privateKey =  keyPair.getPrivate();
                String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
                configuration.setPublicKey(publicKeyString);
                configuration.setPrivateKey(privateKeyString);
                System.out.println("RSA公钥：" + publicKeyString);
                System.out.println("RSA私钥：" + privateKeyString);
            }else {
                //如果配置中有设置公密钥，则使用配置中的公密钥
                publicKey = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(configuration.getPublicKey())));
                privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(configuration.getPrivateKey())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String publicEncrypt(String data) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataByte = data.getBytes(CHARSET), cache;
        int dataLength = dataByte.length, offSet = 0, i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            // 数据分段加密
            while (dataLength - offSet > 0) {
                if (dataLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, dataLength - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptData = out.toByteArray();
            return Base64.getEncoder().encodeToString(encryptData);
        }
    }

    @Override
    public String privateDecrypt(String encryptString) throws Exception{
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //Base64做对称解码(一定要先把数据还原，否则解密失败---因为原始数据先geyBytes后再做Base64编码，此处getBytes后再还原回去)
        byte[] dataByte = Base64.getDecoder().decode(encryptString.getBytes()), cache;
        int dataLength = dataByte.length, offSet = 0, i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            // 数据分段解密
            while (dataLength - offSet > 0) {
                if (dataLength - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, dataLength - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, CHARSET);
        }
    }

    @Override
    public String privateEncrypt(String data) throws Exception{
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] dataByte = data.getBytes(CHARSET), cache;
        int dataLength = dataByte.length, offSet = 0, i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            // 数据分段加密
            while (dataLength - offSet > 0) {
                if (dataLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, dataLength - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.getEncoder().encodeToString(encryptedData);
        }
    }

    @Override
    public String publicEncryptDecrypt(String encryptString) throws Exception{
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        //Base64做对称解码
        byte[] dataByte = Base64.getDecoder().decode(encryptString.getBytes()), cache;
        int dataLength = dataByte.length, offSet = 0, i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            while (dataLength - offSet > 0) {
                if (dataLength - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, dataLength - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, CHARSET);
        }
    }

    /**
     * 对于服务端来说，加密与解密应该都是需要私钥进行处理，公钥要交给客户端
     */
    @Override
    public String encrypt(String data) throws Exception {
        return publicEncrypt(data);
    }

    @Override
    public String decrypt(String encryptString) throws Exception {
        return privateDecrypt(encryptString);
    }
}
