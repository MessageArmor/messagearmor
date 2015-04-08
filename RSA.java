package com.example.mewolot.messagearmor;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Created by mewolot on 4/2/15.
 */
public class RSA {

public RSA_Key newKey() throws NoSuchAlgorithmException {
    SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG");
    KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
    kg.initialize(2048, secRandom);
    KeyPair key = kg.generateKeyPair();
    RSA_Key rKey = new RSA_Key(key.getPublic(),key.getPrivate(), secRandom);
    return rKey;
}

public byte[] encrypt(String message, PublicKey pubKey) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    byte[] plainText = message.getBytes("UTF8");
    Cipher RSACipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    RSACipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] cipherText = RSACipher.doFinal(plainText);
    return cipherText;
}

public String decrypt(byte[] cipherText, PrivateKey privKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
    Cipher RSACipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    RSACipher.init(Cipher.DECRYPT_MODE, privKey);
    byte[] plainText = RSACipher.doFinal(cipherText);
    return new String(plainText, "UTF8");
}

public byte[] sign(String message, PrivateKey privKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
    byte[] plainText = message.getBytes("UTF8");
    Signature sig = Signature.getInstance("MD5withRSA");
    sig.initSign(privKey);
    sig.update(plainText);
    byte[] signature = sig.sign();
    return signature;

}
public boolean verify(String message, byte[] signature, PublicKey pubKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    byte[] plainText = message.getBytes("UTF8");
    Signature sig = Signature.getInstance("MD5withRSA");
    sig.initVerify(pubKey);
    sig.update(plainText);
    if (sig.verify(signature)) {
        return true;
    }
    return false;
}
}

