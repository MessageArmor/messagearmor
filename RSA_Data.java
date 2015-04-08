package com.example.mewolot.messagearmor;

import java.io.Serializable;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created by mewolot on 4/2/15.
 */
public class RSA_Data implements Serializable {
    private byte[] cipherText;
    private IvParameterSpec iv;

    public RSA_Data(byte[] c, IvParameterSpec i) {
        cipherText = c;
        iv = i;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public IvParameterSpec getIv() {
        return iv;
    }
}
