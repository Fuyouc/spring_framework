package org.spring.security.data.encryptor.child.consistent;

import org.spring.annotations.PostConstruct;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.security.data.SpringDataSecurityKeyFactory;
import org.spring.security.data.config.SpringSecurityConfiguration;
import org.spring.security.data.encryptor.SpringSecurityEncryptor;
import org.spring.security.data.utils.SpringDataSecurityUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SpringSecurityAESEncryptor implements SpringSecurityEncryptor {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private SecretKey secretKey;

    @Autowired
    private SpringSecurityConfiguration configuration;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException {
        if (ObjectUtils.isEmpty(configuration)){
            throw new SecurityException("请向容器中注入SpringSecurityConfiguration");
        }
        if (StringUtils.isEmpty(configuration.getPublicKey())){
            if (StringUtils.isEmpty(configuration.getSalt())){
                secretKey = SpringDataSecurityKeyFactory.AES(configuration.getInitialize());
            }else {
                secretKey = SpringDataSecurityKeyFactory.AES(configuration.getInitialize(),configuration.getSalt());
            }
            String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            configuration.setPublicKey(key);
            System.out.println("AES公钥：" + key);
        }else {
            secretKey = new SecretKeySpec(Base64.getDecoder().decode(configuration.getPublicKey()),"AES");
        }
    }

    @Override
    public String encrypt(String data) throws Exception {
        // 创建加密对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化 Cipher 对象
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String decrypt(String encryptString) throws Exception {
        // 创建加密对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化 Cipher 对象
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptString));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
