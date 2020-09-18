package com.ohdocha.cu.kprojectcu.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptHelper {
    private static EncryptHelper instance;

    public enum Algorithm {
        DEFAULT_ALGORITHM("HmacMD5"), SHA256("SHA-256"), SHA512("SHA-512"), AES256("AES");
        String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        String getAlgorithm() {
            return algorithm;
        }
    }

    private static final String KEY = "FrameworkForSharenShare";
    private static final String AES_KEY = "FRAMEWORKFORSHARENSHAREMGPARKSSS";

    public static EncryptHelper getInstance() {
        if (instance == null)
            instance = new EncryptHelper();
        return instance;
    }

    private Algorithm algorithm;

    private EncryptHelper() {
        this.algorithm = Algorithm.DEFAULT_ALGORITHM;
    }

    public EncryptHelper setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public String encrypt(String msg) {
        switch (algorithm) {
            case DEFAULT_ALGORITHM:
                return encryptWithHmacMD5(KEY, msg);
            case SHA256:
                return encryptWithSHA256(msg);
            case SHA512:
                return encryptWithSHA512(msg);
            case AES256:
                return encryptWithAES(AES_KEY, msg);
            default:
                return null;
        }
    }

    public String encrypt(String key, String msg) {
        switch (algorithm) {
            case DEFAULT_ALGORITHM:
                return encryptWithHmacMD5(key, msg);
            case AES256:
                return encryptWithAES(key, msg);
            case SHA256:
                return encryptWithSHA256(msg);
            case SHA512:
                return encryptWithSHA512(msg);
            default:
                return null;
        }
    }

    private String encryptWithHmacMD5(String encryptKey, String msg) {
        try {
            SecretKeySpec key = new SecretKeySpec((encryptKey).getBytes(StandardCharsets.UTF_8), Algorithm.DEFAULT_ALGORITHM.getAlgorithm());
            Mac mac = Mac.getInstance(Algorithm.DEFAULT_ALGORITHM.getAlgorithm());
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes(StandardCharsets.US_ASCII));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException" + e.toString());
            return null;
        } catch (InvalidKeyException e) {
            System.err.println("InvalidKeyException " + e.toString());
            return null;
        } catch (Exception e) {
            System.err.println("UnknownException " + e.toString());
            return null;
        }
    }

    private String encryptWithSHA256(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(msg.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private String encryptWithSHA512(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            md.update(msg.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private String encryptWithAES(String encryptKey, String msg) {
        try {
            SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), Algorithm.AES256.getAlgorithm());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(encryptKey.substring(0, 16).getBytes()));

            byte[] encrypted = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException e) {
            System.err.println("NoSuchPaddingException Error " + e.toString());
            return null;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException Error " + e.toString());
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("InvalidAlgorithmParameterException Error " + e.toString());
            return null;
        } catch (InvalidKeyException e) {
            System.err.println("InvalidKeyException Error " + e.toString());
            return null;
        } catch (BadPaddingException e) {
            System.err.println("BadPaddingException Error " + e.toString());
            return null;
        } catch (IllegalBlockSizeException e) {
            System.err.println("IllegalBlockSizeException Error " + e.toString());
            return null;
        } catch (Exception e) {
            System.err.println("Unknown Error");
            return null;
        }
    }

    public String decrypt(String msg) {
        switch (algorithm) {
            case AES256:
                return decryptWithAES(AES_KEY, msg);
            default:
                return null;
        }
    }

    public String decrypt(String Key, String msg) {
        switch (algorithm) {
            case AES256:
                return decryptWithAES(Key, msg);
            default:
                return null;
        }
    }


    private String decryptWithAES(String encryptedKey, String encryptedMsg) {
        try {
            byte[] keyData = encryptedKey.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyData, Algorithm.AES256.getAlgorithm());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptedKey.substring(0, 16).getBytes("UTF-8")));

            byte[] msgData = Base64.getDecoder().decode(encryptedMsg.getBytes());
            return new String(cipher.doFinal(msgData), StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException e) {
            System.err.println("NoSuchPaddingException Error " + e.toString());
            return null;
        } catch (UnsupportedEncodingException e) {
            System.err.println("UnsupportedEncodingException Error " + e.toString());
            return null;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException Error " + e.toString());
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("InvalidAlgorithmParameterException Error " + e.toString());
            return null;
        } catch (InvalidKeyException e) {
            System.err.println("InvalidKeyException Error " + e.toString());
            return null;
        } catch (BadPaddingException e) {
            System.err.println("BadPaddingException Error " + e.toString());
            return null;
        } catch (IllegalBlockSizeException e) {
            System.err.println("IllegalBlockSizeException Error " + e.toString());
            return null;
        } catch (Exception e) {
            System.err.println("Unknown Error");
            return null;
        }
    }
}
