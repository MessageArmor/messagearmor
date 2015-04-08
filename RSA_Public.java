package com.example.mewolot.messagearmor;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Created by mewolot on 4/2/15.
 */
public class RSA_Public implements Serializable {

    private PublicKey key;
    private SecureRandom random;

    public RSA_Public(PublicKey p, SecureRandom r) {
        key = p;
        random = r;
    }
    public PublicKey getKey() {
        return key;
    }
    public SecureRandom getRandom() {
        return random;
    }
}
