package com.example.mewolot.messagearmor;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import it.sauronsoftware.base64.Base64;

/**
 * Created by mewolot on 4/2/15.
 */
public class CryptoTester {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, SignatureException, InvalidKeySpecException {
        AES aes = new AES();
        String message = "Hello World!";
        System.out.println("Plain Text = " + message);
        AES_Key aKey = aes.newKey();
        AES_Data data = aes.encrypt(message, aKey);
        System.out.println("Cipher Text = " + new String(data.getCipherText(),"UTF8"));
        String plainText = aes.decrypt(data, aKey);
        System.out.println("Decrypted Text = " + plainText);

        byte[] a = aKey.getKey().getEncoded();


        RSA rsa = new RSA();
        System.out.println("\nPlain Text = " + message);
        RSA_Key rKey = rsa.newKey();
        PublicKey pubKey = rKey.getPubKey();
        PrivateKey privKey = rKey.getPrivKey();
        SecureRandom random = rKey.getRandom();
        byte[] cipherText = rsa.encrypt(message, pubKey);
        System.out.println("Cipher Text = " + new String(cipherText, "UTF8"));
        String plain = rsa.decrypt(cipherText, privKey);
        System.out.println("Decrypted Text = " + plain + "\n");

        byte[] signature = rsa.sign(new String(cipherText, "UTF8"), privKey);
        System.out.println(rsa.verify(new String(cipherText, "UTF8"), signature, pubKey));

        String encoded = KeyEncoder.publicKeyEncoder(pubKey);
        PublicKey key = KeyEncoder.publicKeyDecoder(encoded);
        System.out.println(pubKey.equals(key));


    }
}
