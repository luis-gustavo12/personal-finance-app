package com.github.Finance.services;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private String encryptionKey;

    private final byte[] decodedKey;
    private final SecretKeySpec spec;

    public EncryptionService(@Value("${aes.secret.key}") String value) {

        this.encryptionKey = value;

        this.decodedKey = Base64.getDecoder().decode(encryptionKey);
        this.spec = new SecretKeySpec(decodedKey, "AES");

    }


    public String encrypt(String valueString)  {

        try {

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            byte[] encrypted = cipher.doFinal(valueString.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            return null;
        }
    }

    public String decrypt(String valueString) {

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, spec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(valueString));
            return new String(original);
        } catch (Exception e) {
            return null;
        }

    }



    
}
