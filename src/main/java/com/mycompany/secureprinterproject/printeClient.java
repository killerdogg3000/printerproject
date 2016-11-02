/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.secureprinterproject;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.util.Scanner;
import javax.crypto.Cipher;
import java.sql.Timestamp;
import java.util.Date;
/**
 *
 * @author eh
 */
public class printeClient {
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
    
    public static byte[] encryptPrivate(String text, PrivateKey key) {
        byte[] cipherText = null;
        try {
          // get an RSA cipher object and print the provider
          final Cipher cipher = Cipher.getInstance(ALGORITHM);
          // encrypt the plain text using the public key
          cipher.init(Cipher.ENCRYPT_MODE, key);
          cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
          e.printStackTrace();
        }
        return cipherText;
    }
    public static String getUsername(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        return username; 
    }
    public static String getPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        return password; 
    }
    
    

    public static String GetCurrentTimeStamp()
    {
        java.util.Date date= new java.util.Date();
        System.out.println(new Timestamp(date.getTime()));
        return new Timestamp(date.getTime()).toString();

    }
    
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException, FileNotFoundException, IOException, ClassNotFoundException {
        
        PrinterService service = (PrinterService)Naming.lookup("rmi://localhost:5099/nice");
        System.out.println("tjek den kat " + service.echo("hey server"));
        System.out.println("tjek den hund " + service.queue());
        
        //final String originalText = "Text to be encrypted ";
        ObjectInputStream inputStream = null;
        
        String username = "emil";//getUsername();
        String password = "Apassword";//getPassword();
        String time = GetCurrentTimeStamp();
        
        
        // Encrypt the cipher text using the private key.
        inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
        final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
        final byte[] cipherTextPassword = encryptPrivate(time+","+password, privateKey);
        
        
        System.out.println("sending  " + service.login(username,cipherTextPassword));
    }
}
