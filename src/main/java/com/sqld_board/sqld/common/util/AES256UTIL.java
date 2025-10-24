package com.sqld_board.sqld.common.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AES256UTIL {

    private static final String ALGORITHM = "AES"; // AES 알고리즘
    private static final String TRANSFORMATION = "AES/CBC/PKCS5PADDING"; // AES 알고리즘 모드 및 패딩
    private static final int KEY_SIZE = 256; // AES 키 크기 (256비트)
    private static final int IV_SIZE = 16; // 초기화 벡터 크기 (16바이트)

    private SecretKeySpec secretKey; // 비밀 키
    private IvParameterSpec iv; // 초기화 벡터


    /**
     * 주어진 키로 AES256 유틸리티를 초기화.
     * @param key 암호화에 사용할 키
     * @throws Exception 키 생성 실패 시 예외 발생
     */
    public AES256UTIL(String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        secretKey = new SecretKeySpec(keyBytes, 0, KEY_SIZE / 8, ALGORITHM);
        iv = generateIv();
    }

    /**
     * 초기화 벡터(IV)를 생성
     * @return 생성된 초기화 벡터
     */

    private IvParameterSpec generateIv() {
        byte[] ivBytes = new byte[IV_SIZE];
        new SecureRandom().nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }


    /**
     * 주어진 문자열을 AES256으로 암호화
     * @param value 암호화할 문자열
     * @return 암호화된 문자열 (Base64 인코딩)
     * @throws Exception 암호화 실패 시 예외 발생
     */
    public String encrypt(String value) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedIVAndText = new byte[IV_SIZE + encrypted.length];
        System.arraycopy(iv.getIV(), 0, encryptedIVAndText, 0, IV_SIZE);
        System.arraycopy(encrypted, 0, encryptedIVAndText, IV_SIZE, encrypted.length);
        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }


    /**
     * 주어진 암호화된 문자열을 AES256으로 복호화합니다.
     * @param encrypted 암호화된 문자열 (Base64 인코딩)
     * @return 복호화된 문자열
     * @throws Exception 복호화 실패 시 예외 발생
     */
    public String decrypt(String encrypted) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encrypted);
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, IV_SIZE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        int encryptedSize = encryptedIvTextBytes.length - IV_SIZE;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, IV_SIZE, encryptedBytes, 0, encryptedSize);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}


