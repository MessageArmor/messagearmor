package com.example.mewolot.messagearmor;

import java.io.Serializable;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created by mewolot on 4/2/15.
 */
public class AES_Data implements Serializable {
    private IvParameterSpec iv;
    private byte[] cipherText;

    public AES_Data(IvParameterSpec i, byte[] text) {
        iv = i;
        cipherText = text;
    }

    public IvParameterSpec getIV() {
        return iv;
    }

    public byte[] getCipherText() {
        return cipherText;
    }
}
