package com.example.mewolot.messagearmor;

import java.io.Serializable;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by mewolot on 4/2/15.
 */
public class AES_Key implements Serializable{
    private Key key;
    private SecureRandom random;

    public AES_Key(Key k, SecureRandom r) {
        key = k;
        random = r;
    }

    public Key getKey() {
        return key;
    }

    public SecureRandom getRandom() {
        return random;
    }

}
