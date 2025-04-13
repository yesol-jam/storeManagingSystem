package com.bucket.storeManagingSystem.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {
    /*
    * 양방향 암호화 AES-256 관련 유틸
    * 설명 : 사용자 민감정보(핸드폰번호,집주소) 암호화 필요할 시 사용
    * */

    private static final String AES = "AES";
    private static final String CHARSET = "UTF-8";

    // Secret Key 생성 (이 키를 안전하게 저장해야 함)
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(256); // AES-128 (128, 192, 256 중 선택 가능)
        return keyGen.generateKey();
    }

    // Secret Key를 Base64로 인코딩/디코딩
    public static String encodeKey(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, AES);
    }

    // AES 암호화
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // AES 복호화
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, CHARSET);
    }
}
