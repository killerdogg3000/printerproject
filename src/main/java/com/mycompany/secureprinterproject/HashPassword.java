/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.secureprinterproject;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author eh
 */
public class HashPassword {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException 
    {
        List<List<String>> usersList = new ArrayList<List<String>>();

        usersList.add(Arrays.asList("emil","Apassword"));
        usersList.add(Arrays.asList("Auser","kepItReal"));

        String  originalPassword = "Apassword";
        String generatedSecuredPasswordHash;
        FileWriter writer;
        try {
            writer = new FileWriter("SecureFolder/UserInfo.txt", true);
            for(int i = 0; i < usersList.size(); i++) {
                System.out.println(); //prints element i       
                generatedSecuredPasswordHash = generateStorngPasswordHash(usersList.get(i).get(1));
                System.out.println(generatedSecuredPasswordHash);               
                writer.write(usersList.get(i).get(0)+","+generatedSecuredPasswordHash);
                writer.write("\r\n");   // write new line
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(HashPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
     
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
