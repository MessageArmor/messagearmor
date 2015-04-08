package com.example.mewolot.messagearmor;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.*;

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

public class MessageActivity extends ActionBarActivity {
    Button sendSMS;
    EditText msgTxt, numTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sendSMS = (Button) findViewById(R.id.sendButton);
        msgTxt = (EditText) findViewById(R.id.messageText);
        numTxt = (EditText) findViewById(R.id.numberText);
        sendSMS.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                String theMessage = msgTxt.getText().toString();
                String theNumber = numTxt.getText().toString();
                try {
                    send(theNumber, theMessage);
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    protected String[] messageList(String CipherText, String Sig) {
        //I am going to get overflow counting like this, need a better method
        if (CipherText.length() == 0)
            return null;
        int length = CipherText.length() / 150;
        if (CipherText.length() % 150 == 0) {
            length--;
        }
        length += 2;
        String[] output = new String[length];
        for (int i = 0; i < output.length -1; i++) {
            int last = i * 150 + 150;
            if (last > CipherText.length()){
                    last = CipherText.length();}
            String message;
            if (i == 0 && output.length - 1 == 1) {
                message = "MESSAGEX=" + CipherText.substring(i*150,last);
            }
            else if (i == output.length -2) {
                 message = "MESSAGEL=" + CipherText.substring(i*150,last);
            }
            else if (i == 0) {
                message = "MESSAGES=" + CipherText.substring(i*150,last);
            }
            else {
                message = "MESSAGE" + i + "=" + CipherText.substring(i*150,last);
            }
            output[i] = message;
        }
        output[output.length - 1] = Sig;
        return output;
    }
    protected void send(String number, String message) throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException, IOException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, SignatureException {
        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";
        String Sig = null;
        String CipherText = null;
        JSON json = new JSON();
        PublicKey pub_key = json.getRSAPub(number);
        if (pub_key == null) {
            MySQL sql = new MySQL();
            pub_key = sql.getRSA(number);
        }
        if (pub_key != null) {
            RSA rsa = new RSA();
            byte[] cipher = rsa.encrypt(message, pub_key);
            //Identify Message by increment
            CipherText = new String(cipher,"UTF8");
            PrivateKey priv_key = json.getDSAPriv();
            if (priv_key != null) {
                byte[] signature = rsa.sign(message,priv_key);
                 Sig = "SIG=" + json.byte2String(signature);
            }
        }

        PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(MessageActivity.this, "SMS Sent", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_LONG).show();
                        break;

                }
            }
        }, new IntentFilter(SENT));
        SmsManager sms = SmsManager.getDefault();
        if (CipherText != null && Sig != null) {
            String[] messages = messageList(CipherText, Sig);
            for (int i = 0; i < messages.length - 1; i++) {
                sms.sendTextMessage(number,null,messages[i],null,null);
            }
            sms.sendTextMessage(number,null,messages[message.length() - 1],sentPI, deliveredPI);
        }
        else {

            sms.sendTextMessage(number, null, message, sentPI, deliveredPI);
        }
        Log.v("OUTPUT", "\nThe Message:\n" + message + "\nThe Number:\n" + number);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
