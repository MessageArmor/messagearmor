package com.example.mewolot.messagearmor;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.SecureRandom;

/**
 * Created by mewolot on 4/2/15.
 */
public class RSA_Private implements Serializable {
    private PrivateKey key;
    private SecureRandom random;

    public RSA_Private(PrivateKey p, SecureRandom r) {
        key = p;
        random = r;
    }

    public PrivateKey getKey() {
        return key;
    }

    public SecureRandom getRandom() {
        return random;
    }
}
