package com.example.mewolot.messagearmor;



import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import it.sauronsoftware.base64.Base64;

/**
 * Created by mewolot on 4/8/15.
 */
public class KeyEncoder {
    public static String publicKeyEncoder(PublicKey key) {
        byte[] pub = key.getEncoded();
        String output = new String(Base64.encode(pub));
        return output;
    }
    public static PublicKey publicKeyDecoder(String input) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] pub = Base64.decode(input.getBytes());
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pub));
        return key;
    }

    public static String privateKeyEncoder(PublicKey key) {
        byte[] priv = key.getEncoded();
        String output = new String(Base64.encode(priv));
        return output;
    }
    public static PrivateKey privateKeyDecoder(String input) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] priv = Base64.decode(input.getBytes());
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(priv));
        return key;
    }

    public static String signatureEncoder(byte[] sig) {
        String output = new String(Base64.encode(sig));
        return output;
    }
    public static byte[] signatureDecoder(String input) {
        byte[] sig = Base64.decode(input.getBytes());
        return sig;
    }
}
