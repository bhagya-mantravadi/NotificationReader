package com.example.notificationreader.data;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptDecrypt{
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "MyKeyAlias";

    private static SecretKey getKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);
        if (entry == null) {
            return generateKey();
        } else {
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        }
    }

    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
        keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(false)
                .build());
        return keyGenerator.generateKey();
    }

    public static EncryptedData encrypt(String password) throws Exception {
        SecretKey secretKey = getKey();
        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_GCM + "/" + KeyProperties.ENCRYPTION_PADDING_NONE);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        byte[] encryptedPassword = cipher.doFinal(password.getBytes());

        return new EncryptedData(encryptedPassword, iv );
    }

    public static String decrypt(byte[] encryptedPassword, byte[] iv) throws Exception {
        SecretKey secretKey = getKey();
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_GCM + "/" + KeyProperties.ENCRYPTION_PADDING_NONE);
        GCMParameterSpec spec = new GCMParameterSpec(128,iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decryptedPassword = cipher.doFinal(encryptedPassword);
        return new String(decryptedPassword);
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[12]; // GCM requires 16 bytes IV
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}

