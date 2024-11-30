/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idreameducation.ipreppal.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kakkadn
 */
public class SocketService extends Service {
    private ServerSocket serverSocket;
    private Socket socket;

    void I_l() {
        try {
            try {
//                serverSocket = assignPort(); // Creating a server socket, bound to the specified port
                serverSocket = new ServerSocket(7453); // Creating a server socket, bound to the specified port
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                socket = serverSocket.accept();
                new DecryptNUnTweakFile(serverSocket, socket, threadPool);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
                if(socket != null){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ExecutorService threadPool = Executors.newFixedThreadPool(20);

    private ServerSocket assignPort() {

        ServerSocket serverSocket = null;
        for (int j = 1111; j < 9999; j++) {
            try {
                serverSocket = new ServerSocket(j);// Creating a server socket, bound to the specified port
                PreferenceConnector.writeInteger(this, PreferenceConnector.PORT, j);
                break;
            } catch (Exception e) {
            }
        }
        return serverSocket;
    }

    Thread l = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                I_l();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            l.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            serverSocket.close();
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
