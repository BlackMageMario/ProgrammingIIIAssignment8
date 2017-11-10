/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment8;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {
    private int imageSize = 100;
    private int minWindowXSize = 400;
    private int minWindowYSize = 800;
    private String clientName;
    private String IP = "128.1.1.1";
    private JButton sendMessage, settingColour, selectImage, uploadImage;
    private JLabel iconLabel;
    private JTextField inputText;
    private JTextArea outputText;
    private JPanel optionsPanel, typingPanel, messagePanel;
    public Client() throws IOException
    {
        super("Client of Server: Assignment 8");
        //figure out how to get user name
        //possibly another class that accepts a user name
        String hostname = "";
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch(UnknownHostException ex)
        {
            System.out.println(ex.getMessage());
        }
        outputText = new JTextArea();
        inputText = new JTextField();
        sendMessage = new JButton();
        Socket sock = new Socket(hostname, Server.portNumber);
        PrintStream messageSender = new PrintStream(sock.getOutputStream());
        sendMessage.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent event)
                    {
                        String messageToSend = inputText.getText();
                        System.out.println(inputText.getText());
                        inputText.setText("");
                        messageSender.println(messageToSend);
                        messageSender.flush();
                    }
                }
        );
        Container container = getContentPane();
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridLayout(1, 3));
        messagePanel.add(inputText);
        messagePanel.add(sendMessage);
        container.add(messagePanel);
        setSize(400, 400);
        setVisible(true);
        //use append to add messages;
    }
    public String getIP()
    {
        return IP;
    }
    public void printToClient(String message)
    {
        
    }
    public static void main(String... args) throws IOException
    {
        Client client = new Client();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       /* String hostname = "Rofl";
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch(UnknownHostException ex)
        {
            System.out.println(ex.getMessage());
        }
        Socket sock;
        DataInputStream dis;
        PrintStream dat;
        sock = new Socket(hostname, 4444);
        dis = new DataInputStream(sock.getInputStream());
        dat = new PrintStream(sock.getOutputStream());
        String fromServer = dis.readLine();
        System.out.println("Got this from server " + fromServer);
        dat.println("hello");
        dat.flush();
        sock.close();*/
    }
}
