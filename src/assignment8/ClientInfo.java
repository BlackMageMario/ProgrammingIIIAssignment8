/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Eamonn Hannon
 */
public class ClientInfo extends Thread {
    private Socket ourSocket;
    private BufferedReader in;
    private PrintWriter out;
    public ClientInfo(Socket clientSocket)
    {
        super("Fuck this shit im out");
        this.ourSocket = clientSocket;
    }
    
    @Override
    public void run()
    {
        Server ourServer = Server.returnCurrentServer();
        try
        {
            String reply;
            out = new PrintWriter(ourSocket.getOutputStream(), true);
            in = new BufferedReader(
                new InputStreamReader(
                    ourSocket.getInputStream()));
            ourServer.addWriter(out);
            out.println(ourServer.welcomeMessage);
            out.flush();
            while(true)//this needs to be multithreaded
            {
                reply = in.readLine();
                String messageType = reply.split("\\s+")[0];
                reply = reply.replace(messageType, "");
                for(PrintWriter writer : ourServer.getWriters())
                {
                    writer.println(reply);
                    writer.flush();//flush the writing
                }
                if(messageType.equalsIgnoreCase("NAME:"))
                {
                    ourServer.appendToOutput(reply);
                }
                
            }
        }
        catch(IOException ex)
        {
           ex.printStackTrace();
        }
        finally
        {
            if(out != null)
            {
                ourServer.removeWriter(out);
            }
            try
            {
                ourSocket.close();
            }
            catch(IOException e)
            {
                System.out.println("CRASH ON CLIENT: " + e.getMessage());
            }
        }
    }
}
