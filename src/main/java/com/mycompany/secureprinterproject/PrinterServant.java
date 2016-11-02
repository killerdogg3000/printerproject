/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.secureprinterproject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 *
 * @author eh
 */
public class PrinterServant extends UnicastRemoteObject implements PrinterService{
    public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
    public static final String ALGORITHM = "RSA";
    public PrinterServant() throws RemoteException{
        super();
    }

    @Override
    public String echo(String input) throws RemoteException {
        
        return "From server: "+input; 
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        System.out.println("Client have called print, with filename = "+filename+"and printer="+printer);
        return "printing "+filename+" on printer "+ printer; 
    }

    @Override
    public String queue() throws RemoteException {
        System.out.println("Client have called queue");
        return "<job number>   <file name>"; 
    }

    @Override
    public String topQueue(int job) throws RemoteException {
        System.out.println("Client have called topQueue");
        return "moves job "+job+" to the top of the queue"; 
    }

    @Override
    public String start() throws RemoteException {
        System.out.println("Client have called start");
        return "starts the print server"; 
    }

    @Override
    public String stop() throws RemoteException {
        System.out.println("Client have called stop");
        return"stops the print server"; 
    }

    @Override
    public String restart() throws RemoteException {
        System.out.println("Client have called restart");
        return"stop server, clear queue and starts the server again"; 
    }

    @Override
    public String status() throws RemoteException {
        System.out.println("Client have called status");
        return "status of printer: amazing"; //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String readConfig(String parameter) throws RemoteException {
        System.out.println("Client have called readConfig,with parameter = "+parameter);
        return "printing "+parameter; //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String setConfig(String parameter, String value) throws RemoteException {
        System.out.println("Client have called setConfig,with parameter = "+parameter+" and value="+value);
        return "parameter "+ parameter+" is set to the value "+value; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String login(String username, byte[] cipherText) {
      ObjectInputStream inputStream = null;
      String password = "null";
      String time = "null";
      String hashedPasword = null;
      boolean matchedPassword = false;
      System.out.println("cipherText:"+cipherText);
      System.out.println("username:"+username);
        try {
            // decrypt the string using the public key
            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final String plainText = decrypt(cipherText, publicKey);
            password = plainText.split(",")[1];
            time = plainText.split(",")[0];
            //get hash value
            FileReader reader = new FileReader("SecureFolder/UserInfo.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            //find hash based on username
            String line;
            String passwordhashLine="null";
            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println("line 112"+line.split(",")[0]);
                if(line.split(",")[0].matches(username)){
                    //System.out.println("line 112"+line.split(",")[1]);
                    passwordhashLine=line.split(",")[1];
                }
            }
            System.out.println("Hash: "+passwordhashLine);
            //String generatedSecuredPasswordHash = PasswordHash.generateStorngPasswordHash(password);
            //System.out.println(generatedSecuredPasswordHash);
            //hent hash fra fil, søg på brugernavn 
            matchedPassword = PasswordHash.validatePassword(password, passwordhashLine);
            System.out.println(matchedPassword);
            
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PrinterServant.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        if(matchedPassword){
            return "Username: "+username+" /n password: "+password+" /n time: "+time;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }else{
            return "userInfo was false";
        }
      
      
        //System.out.println(hashedPasword);
        //System.out.println("Username: "+username+" /n password: "+password+" /n time: "+time);
        //return "Username: "+username+" /n password: "+password+" /n time: "+time;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static String decrypt(byte[] text, PublicKey key) {
      byte[] dectyptedText = null;
      try {
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);

        // decrypt the text using the private key
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);

      } catch (Exception ex) {
        ex.printStackTrace();
      }

      return new String(dectyptedText);
    }
    public static byte[] encrypt(String text, PublicKey key) {
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

}
/*
print(String filename, String printer);   // prints file filename on the specified printer
queue();   // lists the print queue on the user's display in lines of the form <job number>   <file name>
topQueue(int job);   // moves job to the top of the queue
start();   // starts the print server
stop();   // stops the print server
restart();   // stops the print server, clears the print queue and starts the print server again
status();  // prints status of printer on the user's display
readConfig(String parameter);   // prints the value of the parameter on the user's display
setConfig(String parameter, String value);   // sets the parameter to value
*/