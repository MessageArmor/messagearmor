package com.example.mewolot.messagearmor;


import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

/**
 * Created by mewolot on 4/3/15.
 */
public class JSON {

private String filename = "keystore";
private String message_file = "messagestore";

    public String byte2String(byte[] array) {
        String output = "";
        output += "" + array[0] + " ";
        for (int i = 1; i < array.length -1; i++) {
            output += "" + array[i] + " ";
        }
        output += "" + array[array.length - 1];
        return output;
    }
    public byte[] string2Byte(String bytes) {
        String[] splitBytes = bytes.split(" ");
        byte[] output = new byte[splitBytes.length];
        for (int i = 0; i < splitBytes.length; i++) {
            output[i] = Byte.parseByte(splitBytes[i]);
        }
        return output;
    }

    public void writeKeys(String number, PublicKey RSAKey, PublicKey DSAKey) throws JSONException, IOException, ParseException {
        byte[] r = RSAKey.getEncoded();
        byte[] d = DSAKey.getEncoded();
        String RSA = byte2String(r);
        String DSA = byte2String(d);

        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(filename));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(filename));
            jsonObj = (JSONObject) obj;
        }
        else {
            jsonObj = new JSONObject();
        }
        scan.close();

        if (jsonObj.get(number) == null) {
            JSONObject current = new JSONObject();
            current.put("Number", number);
            current.put("RSA", RSA);
            current.put("DSA", DSA);
            jsonObj.put(number, current);
        }
        FileWriter fw = new FileWriter(new File(filename));
        System.out.println(jsonObj.toJSONString());
        fw.write(jsonObj.toJSONString());
        fw.close();

    }
    public void clear(String filename) throws IOException {
        JSONObject jsonObj = new JSONObject();
        FileWriter fw = new FileWriter(new File(filename));
        System.out.println(jsonObj.toJSONString());
        fw.write(jsonObj.toJSONString());
        fw.close();
    }
   public void storePrivates(PrivateKey RSAKey, PrivateKey DSAKey) throws IOException, ParseException {
       byte[] r = RSAKey.getEncoded();
       byte[] d = DSAKey.getEncoded();
       String RSA = byte2String(r);
       String DSA = byte2String(d);

       JSONParser parser = new JSONParser();
       JSONObject jsonObj;
       Scanner scan = new Scanner(new File(filename));
       if (scan.hasNext()) {
           Object obj = parser.parse(new FileReader(filename));
           jsonObj = (JSONObject) obj;
       }
       else {
           jsonObj = new JSONObject();
       }
       scan.close();

       if (jsonObj.get("PRIVATES") == null) {
           JSONObject current = new JSONObject();
           current.put("RSA", RSA);
           current.put("DSA", DSA);
           jsonObj.put("PRIVATES", current);
       }
       else {
           JSONObject current = (JSONObject) jsonObj.get("PRIVATES");
           current.clear();
           current.put("RSA", RSA);
           current.put("DSA", DSA);
           jsonObj.put("PRIVATES", current);
       }
       FileWriter fw = new FileWriter(new File(filename));
       System.out.println(jsonObj.toJSONString());
       fw.write(jsonObj.toJSONString());
       fw.close();
   }

    public PublicKey getRSAPub(String number) throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(filename));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(filename));
            jsonObj = (JSONObject) obj;
        }
        else {
            System.out.println("No Key Found");
            return null;
        }
        try {
            JSONObject current = (JSONObject) jsonObj.get(number);

        String RSA = (String) current.get("RSA");
        byte[] r = string2Byte(RSA);
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(r));
        return key;
        } catch(Exception e) {
            return null;
        }
    }

    public PublicKey getDSAPub(String number) throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(filename));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(filename));
            jsonObj = (JSONObject) obj;
        }
        else {
            System.out.println("No Key Found");
            return null;
        }
        try {
        JSONObject current = (JSONObject) jsonObj.get(number);
        String DSA = (String) current.get("DSA");
        byte[] d = string2Byte(DSA);
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(d));
        return key;
        } catch(Exception e) {
            return null;
        }
    }

    public PrivateKey getRSAPriv() throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(filename));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(filename));
            jsonObj = (JSONObject) obj;
        }
        else {
            System.out.println("No Key Found");
            return null;
        }
        try {
        JSONObject current = (JSONObject) jsonObj.get("PRIVATES");
        String RSA = (String) current.get("RSA");
        byte[] r = string2Byte(RSA);
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(r));
        return key;
        } catch(Exception e) {
            return null;
        }
    }

    public PrivateKey getDSAPriv() throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(filename));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(filename));
            jsonObj = (JSONObject) obj;
        }
        else {
            System.out.println("No Key Found");
            return null;
        }
        try {
        JSONObject current = (JSONObject) jsonObj.get("PRIVATES");
        String DSA = (String) current.get("DSA");
        byte[] d = string2Byte(DSA);
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(d));
        return key;
        } catch(Exception e) {
            return null;
        }
    }

    //Untested
    public void constructMessage(String number, String message, char id) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
       try {
           Scanner scan = new Scanner(new File(message_file));
           if (scan.hasNext()) {
               Object obj = parser.parse(new FileReader(message_file));
               jsonObj = (JSONObject) obj;
           } else {
               jsonObj = new JSONObject();
           }
           scan.close();
       } catch(FileNotFoundException e) {
           jsonObj = new JSONObject();
       }
        if (id == 'S') {
            JSONObject current = new JSONObject();
            current.put("END",false);
            current.put("INDEX", 0);
            current.put("MESSAGE",message);
            jsonObj.put(number,current);
        }
        else if (id == 'X') {
            JSONObject current = new JSONObject();
            current.put("END",true);
            current.put("INDEX",0);
            current.put("MESSAGE",message);
            jsonObj.put(number,current);
        }
        //This one is wrong when Last comes before middle, however it is currently an impossible case
        else if (id == 'L') {
            JSONObject current = new JSONObject();
            current.put("END",true);
            JSONObject temp = (JSONObject) jsonObj.get(number);
            String c_message = (String) temp.get("MESSAGE");
            jsonObj.remove(number);
            current.put("MESSAGE", c_message + message);
            jsonObj.put(number,current);
        }
        else {
            JSONObject current = new JSONObject();
            current.put("END",false);
            JSONObject temp = (JSONObject) jsonObj.get(number);
            String c_message = (String) temp.get("MESSAGE");
            int index = (int) temp.get("INDEX");
            jsonObj.remove(number);
            //Pass into function as an integer value
            if (id == index + 1) {
                current.put("MESSAGE", c_message + message);
                current.put("INDEX",id);
                int i = id + 1;
                while(temp.get(i) != null) {
                    c_message = (String) current.get("MESSAGE");
                    String p_message = (String) temp.get(i);
                    current.remove("MESSAGE");
                    current.put("MESSAGE", c_message + p_message);
                    current.remove("INDEX");
                    current.put("INDEX",i);
                    i++;
                }
            }
            else {
                current.put(id, message);
                current.put("INDEX",index);
                current.put("MESSAGE", c_message);
            }
            jsonObj.put(number, current);
        }
        FileWriter fw = new FileWriter(new File(message_file));
        System.out.println(jsonObj.toJSONString());
        fw.write(jsonObj.toJSONString());
        fw.close();
    }

    public void writeSig(String number, String sig) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(message_file));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(message_file));
            jsonObj = (JSONObject) obj;
        }
        else {
            jsonObj = new JSONObject();
        }
        scan.close();
        JSONObject current = (JSONObject) jsonObj.get(number);
        current.put("SIG",sig);
        FileWriter fw = new FileWriter(new File(message_file));
        System.out.println(jsonObj.toJSONString());
        fw.write(jsonObj.toJSONString());
        fw.close();
    }

    public String getCipher(String number) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(message_file));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(message_file));
            jsonObj = (JSONObject) obj;
        }
        else {
            return null;
        }
        scan.close();
        JSONObject current = (JSONObject) jsonObj.get(number);
        if (current.get("END") == false)
            return null;
        String output = (String) current.get("MESSAGE");
        current.remove("MESSAGE");
        current.remove("END");
        current.remove("INDEX");
        FileWriter fw = new FileWriter(new File(message_file));
        System.out.println(jsonObj.toJSONString());
        fw.write(jsonObj.toJSONString());
        fw.close();
        return output;
    }

    public String getSig(String number) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        Scanner scan = new Scanner(new File(message_file));
        if (scan.hasNext()) {
            Object obj = parser.parse(new FileReader(message_file));
            jsonObj = (JSONObject) obj;
        }
        else {
            return null;
        }
        scan.close();
        JSONObject current = (JSONObject) jsonObj.get(number);
        if (current.get("SIG") == null)
            return null;
        String output = (String)current.get("SIG");
        current.remove("SIG");
        FileWriter fw = new FileWriter(new File(message_file));
        System.out.println(jsonObj.toJSONString());
        fw.write(jsonObj.toJSONString());
        fw.close();
        return output;
    }


}
