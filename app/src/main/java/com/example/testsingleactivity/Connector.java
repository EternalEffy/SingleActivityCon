package com.example.testsingleactivity;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connector {
    private Socket socket;
    private String host,message;
    private int port,l;
    private static DataInputStream inStream;
    private static DataOutputStream outStream;

    public static final String LOG_TAG = "SOCKET";

    public Connector (final String host, final int port, int l) {
        this.host = host;
        this.port = port;
        this.l = l;
    }

    public String openConnection(){
        try {
            socket = new Socket(host, port);
            inStream= new DataInputStream(socket.getInputStream());
            outStream=new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            switch (l % 2){
                case 0:
                    return SystemMessages.ERROR_CONNECTION_RUS;
                case 1:
                    return SystemMessages.ERROR_CONNECTION;
            }
        }
        return null;
    }

    public void closeConnection(){
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            } finally {
                socket = null;
            }
        }
        socket = null;
    }

    private String reg(String request){
        try {
            outStream.writeUTF(request);
            outStream.flush();
            return inStream.readUTF();
        } catch (IOException e) {
            switch (l % 2){
                case 0:
                    return SystemMessages.ERROR_REGISTRATION_RUS;
                case 1:
                    return SystemMessages.ERROR_REGISTRATION;
            }
        }
        return null;
    }

    private String log(String request){
        try {
            outStream.writeUTF(request);
            outStream.flush();
            message = inStream.readUTF();
            if(!message.startsWith(SystemMessages.CODE_ACCESS_NO)) {
                message = inStream.readUTF();
            }
          return message;
        } catch (IOException e) {
            switch (l % 2){
                case 0:
                    return SystemMessages.ERROR_LOGIN_RUS;
                case 1:
                    return SystemMessages.ERROR_LOGIN;
            }
        }
        return null;
    }

}
