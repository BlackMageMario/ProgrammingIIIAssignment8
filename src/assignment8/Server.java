/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment8;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server extends JFrame implements Runnable{
    public static int portNumber = 4444;
    private static String welcomeMessage = "Welcome to the server. Beginning message history.";
    private static String historyFileName = "";
    private final ArrayBlockingQueue<Socket> clients;
    private ServerSocket server;
    private static JTextArea outputText;
    protected Server()
    {
        super("Server: Assignment 8");
        clients = new ArrayBlockingQueue<Socket>(100);
        try
        {
            server = new ServerSocket(portNumber);
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
            System.exit(1);
        }
        outputText = new JTextArea();
        outputText.setText("Server started.");
        outputText.setEditable(false);
        Container container = getContentPane();
        container.add(outputText);
        setSize(400, 400);
        setVisible(true);
        
    }
    public static String historyFileN()
    {
        return historyFileName;//maybe return the actual file
    }
    private void printMessageRecieved(String message)
    {
        outputText.append("\n" + message);//really this should go to other clients
        //but that's OK
    }
    public static void main(String... args) throws IOException
    {
        Server theServer = new Server();
        theServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theServer.run();
    }

    @Override
    public void run() {
        int exTimes = 0;
        while(true)
        {
            System.out.println("We got here");
            synchronized(clients)
            {
                System.out.println(clients.size());
                if(!clients.isEmpty())
                {
                    Socket sock = null;
                    try {
                        sock  = clients.take();
                    } catch (InterruptedException ex) {
                        System.err.println(ex.getMessage());
                    }
                    try
                    {
                        String reply;
                        PrintStream out = new PrintStream(sock.getOutputStream());
                        BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                sock.getInputStream()));
                        if((reply = in.readLine()) != null)//this needs to be multithreaded
                        {
                            System.out.println("reply");
                            printMessageRecieved(reply);
                        }   
                    }
                    catch(IOException ex)
                    {
                        System.out.println(ex.getMessage());
                    }  
                    clients.offer(sock);
                }
                else
                {
                    exTimes++;
                    try
                    {
                        System.out.println("Execution: " + exTimes);
                        Socket socket;
                        socket = server.accept();
                        clients.offer(socket);
                    }
                    catch(IOException ex)
                    {
                        System.out.println(ex.getMessage());
                    }  
                }  
            }
        }
    }
}
