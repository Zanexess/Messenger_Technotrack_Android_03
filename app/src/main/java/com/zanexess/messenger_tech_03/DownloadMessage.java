package com.zanexess.messenger_tech_03;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DownloadMessage extends AsyncTask<Void, Void, String> {
    //Ссылка на уже существующий соккет
    private Socket socket;

    public DownloadMessage(Socket socket) {
        this.socket = socket;
    }

    @Override
    protected String doInBackground(Void... params) {
        // Ответ данной конкретной задачи.
        String response = "";
        //Считываение пачками по 4096
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[4096];
        int bytesRead;
        try {
            InputStream inputStream = socket.getInputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
                //Проверка на корректность пришедшего JSON;
                //Если корректен - останавливаемся
                try {
                    new JSONObject(response);
                    break;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

