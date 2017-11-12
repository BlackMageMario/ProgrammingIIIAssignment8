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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server extends JFrame {
    public static int portNumber = 4444;
    private static Server instance;
    public String welcomeMessage = "Welcome to the server. Beginning message history.";
    private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
    private JTextArea outputText;
    protected Server()
    {
        super("Server: Assignment 8");
        if(instance == null)
        {
            instance = this;
        }
        outputText = new JTextArea();
        outputText.setText("Server started.");
        outputText.setEditable(false);
        Container container = getContentPane();
        container.add(outputText);
        setSize(400, 400);
        setVisible(true);
    }
    public static Server returnCurrentServer()
    {
        return instance;
    }
    public void appendToOutput(String output)
    {
        outputText.append("\n" + output);
    }
    public ArrayList<PrintWriter> getWriters()
    {
        return writers;
    }
    public void removeWriter(PrintWriter writer)
    {
        writers.remove(writer);
    }
    public void addWriter(PrintWriter writer)
    {
        writers.add(writer);
    }
    public static void main(String... args) throws IOException
    {
        Server theServer = new Server();
        theServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ServerSocket server = new ServerSocket(portNumber);
        try {
            while(true)
            {
                new ClientInfo(server.accept()).start();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
 }
