package org.spring.security.data;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.AbstractMap;
import java.util.Map;

public class SpringDataSecurityKeyFactory {

    public static SecretKey AES(int initialize) throws NoSuchAlgorithmException {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(initialize <= 0 ? 128 : initialize);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static SecretKey AES(int initialize,String salt) throws NoSuchAlgorithmException {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(initialize <= 0 ? 128 : initialize,getSecureRandom(salt));
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static KeyPair RSA(int initialize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(initialize <= 0 ? 1024 : initialize);
        return generator.generateKeyPair();
    }

    public static KeyPair RSA(int initialize,String salt) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(initialize <= 0 ? 1024 : initialize,getSecureRandom(salt));
        return generator.generateKeyPair();
    }

    private static SecureRandom getSecureRandom(String salt){
        return new SecureRandom(salt.getBytes());
    }
}
