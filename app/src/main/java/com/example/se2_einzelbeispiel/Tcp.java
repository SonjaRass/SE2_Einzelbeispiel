package com.example.se2_einzelbeispiel;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Tcp {

    private String message;

    public static final String ip = "143.205.174.165"; //getracert mit domain funktioniert es einfach nicht, keine Ahnung wieso
    public static final int port = 53212;
    private OnMessageReceived mListener = null;
    private boolean run = false;

    PrintWriter pw;
    BufferedReader bf;

    public Tcp (OnMessageReceived listener){
        mListener = listener;
    }

    //Die Methode für das Interface wird in der main implementiert - asynch Task - Background
    public interface OnMessageReceived{
        public void messageReceived (String message);
    }

    public void run(){
        run = true;
        try{
            InetAddress ipAdress = InetAddress.getByName(ip);

            Log.e("Client", "connect");

            Socket s = new Socket(ipAdress, port);

            try{
                //übermitteln
                pw = new PrintWriter(new BufferedWriter((new OutputStreamWriter(s.getOutputStream()))), true);

                Log.e("Client", "sent");
                Log.e("Cient", "finsih");

                //erhalten
                bf = new BufferedReader(new InputStreamReader(s.getInputStream()));

                //antwort abwarten..
                while(run){
                    message = bf.readLine();

                    if (message != null && mListener != null){
                        mListener.messageReceived(message);
                    } // wird vom Main aufgerufen - Asynk
                    message = null;
                }
                Log.e("Response", "message: " + message);
            } catch (Exception e){
                Log.e("Client", "error ", e);
            } finally {
                s.close();;
            }
        } catch (Exception e){
            Log.e("Client", "error", e);
        }
    }

    public void send (String message){
        if (pw != null && !pw.checkError()){
            pw.println(message);
            pw.flush();
        }
    }



}
