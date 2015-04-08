package com.example.mewolot.messagearmor;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Created by mewolot on 4/2/15.
 */
public class RSA_Key implements Serializable{

    private PublicKey pubKey;
    private PrivateKey privKey;
    private SecureRandom random;

    public RSA_Key(PublicKey pub, PrivateKey priv, SecureRandom r) {
        pubKey = pub;
        privKey = priv;
        random = r;
    }

    public PublicKey getPubKey() {
        return pubKey;
    }

    public PrivateKey getPrivKey() {
        return privKey;
    }

    public SecureRandom getRandom() {
        return random;
    }
}
