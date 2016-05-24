package com.zanexess.messenger_tech_03;


import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class ContextThread extends Thread {
    private final String LOG_TAG = getClass().getSimpleName();
    private Socket socket;
    private MessageSocketService service;
    private final String address = "188.166.49.215";
    private final Integer port = 7777;
    private static volatile boolean state = false;

    @Override
    public void run() {
        while (true) {
            if (socket == null) {
                try {
                    if (NetworkManager.isNetworkAvailable(service.getApplicationContext())) {
                        socket = new Socket(address, port);
                        service.getMessageHandler().setSocket(socket);
                        Log.v(LOG_TAG, "Успешно");
                        try {
                            service.getMessageHandler().reAuth();
                        } catch (Exception e) {

                        }
                    } else {
                        //Log.v(LOG_TAG, "Нет интернет соединения");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!socket.isConnected()) {
                    try {
                        socket.close();
                        socket = new Socket(address, port);
                        service.getMessageHandler().setSocket(socket);
                        Log.v(LOG_TAG, "Соединение восстановлено");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    DownloadMessage downloadMessage = new DownloadMessage(socket);
                    try {
                        String result = downloadMessage.execute().get();
                        //Log.v(LOG_TAG, result);
                        try {
                            service.onMessage(result, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setService(MessageSocketService service) {
        this.service = service;
    }

}
