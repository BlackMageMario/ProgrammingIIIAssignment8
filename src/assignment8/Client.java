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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame {
    private int imageSize = 100;
    private int minWindowXSize = 400;
    private int minWindowYSize = 800;
    private String clientName;
    private String IP = "128.1.1.1";
    private JButton sendMessage, settingColour, uploadImage;
    private JLabel iconLabel;
    private JTextField inputText;
    private JTextArea outputText;
    private JPanel optionsPanel, typingPanel, messagePanel;
    private JScrollPane scrollPane;
    private BufferedReader in;
    private PrintWriter out;
    public Client() throws IOException
    {
        super("Client of Server: Assignment 8");
        outputText = new JTextArea();
        outputText.setEditable(false);
        DefaultCaret caret = (DefaultCaret)outputText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        inputText = new JTextField();
        sendMessage = new JButton("Send a message");
        settingColour = new JButton("Settings");
        uploadImage = new JButton("Upload an iamge");
        scrollPane = new JScrollPane(outputText);
        scrollPane.setSize(400, 400);
        sendMessage.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent event)
                    {
                        String messageToSend = inputText.getText();
                        inputText.setText("");
                        out.println("MESSAGE: " + messageToSend);
                    }
                }
        );
        Container container = getContentPane();
        container.setLayout(new GridLayout(3, 3));
        typingPanel = new JPanel();
        typingPanel.add(inputText);
        typingPanel.add(sendMessage);
        typingPanel.setLayout(new GridLayout(1, 3));
        messagePanel = new JPanel(new GridLayout(1, 1));
        messagePanel.add(scrollPane);
        optionsPanel = new JPanel();
        optionsPanel.add(settingColour);
        optionsPanel.add(uploadImage);
        container.add(messagePanel);
        container.add(typingPanel);
        container.add(optionsPanel);
        setSize(400, 400);
        setVisible(true);
        //use append to add messages;
    }
    public static void main(String... args) throws IOException
    {
        Client client = new Client();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.run();
    }
    public void run() throws IOException
    {
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
        clientName = "";
        //input a way to check if the client name is valid lol
        clientName = (String)JOptionPane.showInputDialog(this, "Please enter your name: ", "Enter your name", JOptionPane.PLAIN_MESSAGE, null, null, "Bob");
        Socket sock = new Socket(hostname, Server.portNumber);
        in = new BufferedReader(new InputStreamReader
            (sock.getInputStream()));
        out= new PrintWriter(sock.getOutputStream(), true);
        out.println("NAME: " + clientName + " at /" + IP + " has connected");
        String reply = "";
        while(true)
        {
            reply = in.readLine();//blocking on this line - not getting input
            outputText.append(reply + "\n");
        }
    }
}
