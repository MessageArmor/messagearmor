package com.example.mewolot.messagearmor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
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
 * Created by mewolot on 3/31/15.
 */
public class SMSReceiver extends BroadcastReceiver{
    private String filename = "messagestore";
    public void parseMessage(String number, String message) throws IOException, ParseException {
        JSON json = new JSON();
        json.constructMessage(number, message.substring(9), message.charAt(7));
    }
    public void parseSignature(String number, String sig) throws IOException, ParseException {
        JSON json = new JSON();
        json.writeSig(number, sig.substring(4));
    }
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String str = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                if (messages[i].getMessageBody().toString().substring(0,7).equals("MESSAGE")) {
                    try {
                        parseMessage(messages[i].getOriginatingAddress(), messages[i].getMessageBody());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if (messages[i].getMessageBody().toString().substring(0,3).equals("SIG")) {
                    try {
                        parseSignature(messages[i].getOriginatingAddress(), messages[i].getMessageBody());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    str += "Messages from " + messages[i].getOriginatingAddress();
                    str += " :";
                    str += messages[i].getMessageBody().toString();
                    str += "\n";
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                }
                JSON json = new JSON();
                try {
                    if(json.getCipher(messages[i].getOriginatingAddress()) != null && json.getSig(messages[i].getOriginatingAddress())!= null) {
                        str += "Messages from " + messages[i].getOriginatingAddress();
                        str += " :";
                        String cipherText = json.getCipher(messages[i].getOriginatingAddress());
                        String sig = json.getSig(messages[i].getOriginatingAddress());
                        RSA rsa = new RSA();
                        byte[] signature = json.string2Byte(sig);
                        byte[] cipher = cipherText.getBytes("UTF8");
                        PublicKey key = null;
                        try {
                            key = json.getDSAPub(messages[i].getOriginatingAddress());
                            if (key == null) {
                                MySQL sql = new MySQL();
                                key = sql.getDSA(messages[i].getOriginatingAddress());
                                if (key == null) {
                                    Log.e("SMSReceiver", "Key Doesn't Exist");
                                    throw new InvalidKeyException();
                                }
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }catch(InvalidKeyException e) {
                            e.printStackTrace();
                        }
                        catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (rsa.verify(cipherText, signature,key)) {
                            Log.i("SMSReceiver","Key Verified");
                            PrivateKey pKey = json.getRSAPriv();
                            if (pKey == null) {
                                Log.e("SMSReceiver","Private Key Not Found");
                                throw new InvalidKeyException();
                            }
                            String plainText = rsa.decrypt(cipher,pKey);
                            str += plainText;
                        }

                        //str += messages[i].getMessageBody().toString();
                        str += "\n";
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
