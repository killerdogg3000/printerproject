/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.secureprinterproject;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author eh
 */
public interface PrinterService extends Remote{
    public String echo(String input) throws RemoteException;
    
    public String login(String username, byte[] cipherText) throws RemoteException;
    public String print(String filename, String printer) throws RemoteException;// prints file filename on the specified printer
    public String queue() throws RemoteException;   // lists the print queue on the user's display in lines of the form <job number>   <file name>
    public String topQueue(int job) throws RemoteException;   // moves job to the top of the queue
    public String start() throws RemoteException;   // starts the print server
    public String stop() throws RemoteException;   // stops the print server
    public String restart() throws RemoteException;   // stops the print server, clears the print queue and starts the print server again
    public String status() throws RemoteException;  // prints status of printer on the user's display
    public String readConfig(String parameter) throws RemoteException;
    public String setConfig(String parameter, String value) throws RemoteException;   // sets the parameter to value*/
}
