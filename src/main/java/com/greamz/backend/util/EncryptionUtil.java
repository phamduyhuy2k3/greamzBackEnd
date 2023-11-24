package com.greamz.backend.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {
    private static final String SECRET_KEY = "49fecce2f68fb05945fc26d7edc25611";
    public static String encrypt(String token) {
        try {
            return Base64.getEncoder().encodeToString(getBytes(token));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String decrypt(String encryptedToken) {
        try {
            return getString(encryptedToken);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String encryptURL(String token) {
        try {
            return URLEncoder.encode(Base64.getEncoder().encodeToString(getBytes(token)), StandardCharsets.UTF_8) ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String decryptURL(String encryptedToken) {
        try {
            encryptedToken = URLDecoder.decode(encryptedToken, StandardCharsets.UTF_8);
            return getString(encryptedToken);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String getString(String encryptedToken) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedToken));
        return new String(decryptedBytes);
    }
    private static byte[] getBytes(String token) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
        return encryptedBytes;
    }
}
