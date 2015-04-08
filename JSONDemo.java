package com.example.mewolot.messagearmor;

import org.json.JSONException;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by mewolot on 4/3/15.
 */
public class JSONDemo{
    public static void main(String[] args) throws IOException, JSONException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, SignatureException {
        JSON json = new JSON();
        json.clear("keystore");
        json.clear("messagestore");
        RSA rsa = new RSA();
        RSA_Key key = rsa.newKey();
        PrivateKey r_priv = key.getPrivKey();
        PublicKey r_pub = key.getPubKey();
        key = rsa.newKey();
        PrivateKey d_priv = key.getPrivKey();
        PublicKey d_pub = key.getPubKey();

        json.writeKeys("9492953023",r_pub,d_pub);
        json.storePrivates(r_priv,d_priv);

        PublicKey sr_pub = json.getRSAPub("9492953023");
        PublicKey sd_pub = json.getDSAPub("9492953023");
        PrivateKey sr_priv = json.getRSAPriv();
        PrivateKey sd_priv = json.getDSAPriv();
        PublicKey fail = json.getDSAPub("9492953024");
        if (fail == null)
            System.out.println("Doesn't Exist");
        else
            System.out.println("Wrong");

        System.out.println(r_pub.equals(sr_pub));
        System.out.println(d_pub.equals(sd_pub));
        System.out.println(r_priv.equals(sr_priv));
        System.out.println(d_priv.equals(sd_priv));

        String test = "Hello World I WANT TO SEE WHAT WILL HAPPEN IF I MAKE THIS A REALLY LARGE STRING, IT IS NOT QUITE" +
                "LONG ENOUGH YET, I AM GOING TO HAVE TO KEEP WORKING ON IT UNTIL IT IS GREATER THAN 300.";
        System.out.println(test.length());
        byte[] cipher = rsa.encrypt(test,r_pub);
        String cipherText = new String(cipher,"UTF8");
        System.out.println(cipherText.length());
        String first_message = cipherText.substring(0,150);
        String second_message = cipherText.substring(150,cipherText.length());
        json.constructMessage("9492953024",first_message,'S');
        json.constructMessage("9492953024",second_message,'L');
        byte[] signature = rsa.sign(cipherText,d_priv);
        String sig = json.byte2String(signature);
        json.writeSig("9492953024",sig);
        System.out.println(json.getSig("9492953024").equals(sig));
        System.out.println(json.getCipher("9492953024").equals(cipherText));
    }
}
