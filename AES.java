package com.example.mewolot.messagearmor;

/**
 * Created by mewolot on 4/2/15.
 */
import java.io.UnsupportedEncodingException;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class AES {

    public AES_Key newKey() throws NoSuchAlgorithmException {
        SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG");
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, secRandom);
        Key key = kg.generateKey();
        AES_Key aKey = new AES_Key(key, secRandom);
        return aKey;
    }


    public AES_Data encrypt(String message, AES_Key aKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key key = aKey.getKey();
        SecureRandom random = aKey.getRandom();
        AESCipher.init(Cipher.ENCRYPT_MODE, key, random);
        IvParameterSpec iv = new IvParameterSpec(AESCipher.getIV());
        byte[] plainText = message.getBytes("UTF8");
        byte[] cipherText = AESCipher.doFinal(plainText);
        AES_Data data = new AES_Data(iv, cipherText);
        return data;
    }

    public String decrypt(AES_Data data, AES_Key aKey) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        byte[] cipherText = data.getCipherText();
        IvParameterSpec iv = data.getIV();
        Key key = aKey.getKey();
        SecureRandom random = aKey.getRandom();
        Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AESCipher.init(Cipher.DECRYPT_MODE, key,iv, random);
        byte[] plainText = AESCipher.doFinal(cipherText);
        return new String(plainText, "UTF8");
    }

}
