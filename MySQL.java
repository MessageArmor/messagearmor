package com.example.mewolot.messagearmor;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.microsoft.sqlserver.jdbc.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by mewolot on 4/3/15.
 */
public class MySQL {

    String connectionString = "jdbc:sqlserver://y1kwechev4.database.windows.net:1433;database=MessageArmor Identity Server;user=Polysec@y1kwechev4;password=Poly$ec!;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    Connection connection = null;  // For making the connection
    Statement statement = null;    // For the SQL statement
    ResultSet resultSet = null;    // For the result set, if applicable


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

public void createTable() {
    try
    {
        // Ensure the SQL Server driver class is available.
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Establish the connection.
        connection = DriverManager.getConnection(connectionString);

        // Define the SQL string.
        String sqlString =
                "CREATE TABLE IdentityServer8 (" +
                        "[Number] [int] IDENTITY(1,1) NOT NULL," +
                        "[RSA] [nvarchar](4000) NOT NULL," +
                        "[DSA] [nvarchar](4000) NOT NULL)";

        // Use the connection to create the SQL statement.
        statement = connection.createStatement();

        // Execute the statement.
        statement.executeUpdate(sqlString);

        // Provide a message when processing is complete.
        System.out.println("Table-Creation complete.");

    }
    // Exception handling
    catch (ClassNotFoundException cnfe)
    {

        System.out.println("ClassNotFoundException " +
                cnfe.getMessage());
    }
    catch (Exception e)
    {
        System.out.println("Exception " + e.getMessage());
        e.printStackTrace();
    }
    finally
    {
        try
        {
            // Close resources.
            if (null != connection) connection.close();
            if (null != statement) statement.close();
            if (null != resultSet) resultSet.close();
        }
        catch (SQLException sqlException)
        {
            // No additional action if close() statements fail.
        }
    }
}

public void postKeys(String number, PublicKey RSA, PublicKey DSA) {
    try
    {
        // Ensure the SQL Server driver class is available.
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Establish the connection.
        connection = DriverManager.getConnection(connectionString);

       byte[] RSA_byte = RSA.getEncoded();
       byte[] DSA_byte = DSA.getEncoded();
       String RSA_String = byte2String(RSA_byte);
       String DSA_String = byte2String(DSA_byte);

        int num = number.hashCode();
        // Define the SQL string.
        String sqlString =
                "SET IDENTITY_INSERT IdentityServer8 ON " +
                        "INSERT INTO IdentityServer8 " +
                        "(Number, RSA, DSA) " +
                        "VALUES(" + num + ", '" + RSA_String + "', '" + DSA_String + "')";
        System.out.println("\n" + sqlString + "\n");
        System.out.println(RSA_String);
        System.out.println(DSA_String);

        // Use the connection to create the SQL statement.
        statement = connection.createStatement();

        // Execute the statement.
        statement.executeUpdate(sqlString);

        // Provide a message when processing is complete.
        System.out.println("Processing complete.");

    }
    catch (ClassNotFoundException cnfe) {

        System.out.println("ClassNotFoundException " +
                cnfe.getMessage());
    } catch (Exception e) {
        System.out.println("Exception " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            // Close resources.
            if (null != connection) connection.close();
            if (null != statement) statement.close();
            if (null != resultSet) resultSet.close();
        } catch (SQLException sqlException) {
            // No additional action if close() statements fail.
        }
    }
}
public PublicKey getRSA(String number) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
    String RSA = null;
    try {
        // Ensure the SQL Server driver class is available.
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Establish the connection.
        connection = DriverManager.getConnection(connectionString);
        int num = number.hashCode();
        // Define the SQL string.
        String sqlString = "SELECT * FROM IdentityServer8 WHERE Number=" + num;

        // Use the connection to create the SQL statement.
        statement = connection.createStatement();

        // Execute the statement.
        resultSet = statement.executeQuery(sqlString);
        while (resultSet.next()) {
            RSA = resultSet.getString("RSA");
            System.out.println(RSA);
        }

        // Provide a message when processing is complete.
        System.out.println("Get-RSA complete.");

    }
    // Exception handling
    catch (ClassNotFoundException cnfe) {

        System.out.println("ClassNotFoundException " +
                cnfe.getMessage());
    } catch (Exception e) {
        System.out.println("Exception " + e.getMessage());
        e.printStackTrace();
        return null;
    } finally {
        try {
            // Close resources.
            if (null != connection) connection.close();
            if (null != statement) statement.close();
            if (null != resultSet) resultSet.close();
        } catch (SQLException sqlException) {
            // No additional action if close() statements fail.
        }
    }
    //Desearialize
    byte[] RSA_byte = string2Byte(RSA);
    PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(RSA_byte));
    return key;
}

    public PublicKey getDSA(String number) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
            String DSA = null;
            try {
                // Ensure the SQL Server driver class is available.
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Establish the connection.
                connection = DriverManager.getConnection(connectionString);
                int num = number.hashCode();
                // Define the SQL string.
                String sqlString = "SELECT * FROM IdentityServer8 WHERE Number=" + num;

                // Use the connection to create the SQL statement.
                statement = connection.createStatement();

                // Execute the statement.
                resultSet = statement.executeQuery(sqlString);
                while (resultSet.next()) {
                    DSA = resultSet.getString("DSA");
                    System.out.println(DSA);
                }

                // Provide a message when processing is complete.
                System.out.println("Get-DSA complete.");

            }
            // Exception handling
            catch (ClassNotFoundException cnfe) {

                System.out.println("ClassNotFoundException " +
                        cnfe.getMessage());
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage());
                e.printStackTrace();
                return null;
            } finally {
                try {
                    // Close resources.
                    if (null != connection) connection.close();
                    if (null != statement) statement.close();
                    if (null != resultSet) resultSet.close();
                } catch (SQLException sqlException) {
                    // No additional action if close() statements fail.
                }
            }
            //Desearialize
        byte[] DSA_byte = string2Byte(DSA);
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(DSA_byte));
            return key;
        }


}
