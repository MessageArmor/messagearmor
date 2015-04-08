package com.example.mewolot.messagearmor;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.microsoft.sqlserver.jdbc.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*
* Java program to connect to MySQL Server database running on localhost,
* using JDBC type 4 driver.
*
* @author http://java67.blogspot.com
*/
public class MySQLDemo{

    public static void main(String args[]) throws NoSuchAlgorithmException, IOException, ClassNotFoundException, InvalidKeySpecException {
        MySQL sql = new MySQL();
        //sql.createTable();
        String number = "9492953023";
        RSA rsa = new RSA();
        RSA_Key rKey = rsa.newKey();
        PublicKey pubKey = rKey.getPubKey();
        PrivateKey privKey = rKey.getPrivKey();
        SecureRandom random = rKey.getRandom();
        RSA_Key dKey = rsa.newKey();
        PublicKey dPubKey = dKey.getPubKey();
        PrivateKey dPrivKey = dKey.getPrivKey();
        sql.postKeys(number, pubKey, dPubKey);
        PublicKey rSQL = sql.getRSA(number);
        PublicKey dSQL = sql.getDSA(number);

        System.out.println(pubKey.equals(rSQL));
        System.out.println(dPubKey.equals(dSQL));


    }

}