package com.zanexess.messenger_tech_03;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class MessageSocketService extends Service {
    private final String LOG_TAG = getClass().getSimpleName();
    private MessageHandler messageHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceProxy;
    }

    public class ServiceProxy extends IService.Stub {
        public ICallback mActivityCallback;

        @Override
        public void bindActivity(IBinder callback) throws RemoteException {
            mActivityCallback = ICallback.Stub.asInterface(callback);
            if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
                mActivityCallback.sendToUI("INTERNET");
            } else {
                mActivityCallback.sendToUI("NO INTERNET");
            }
        }

        @Override
        public void sendMessage(String message) throws RemoteException {
            messageHandler.sendMessage(message);
        }

        @Override
        public String getCid() throws RemoteException {
            return messageHandler.getAuthData().getCid();
        }

        @Override
        public String getSid() throws RemoteException {
            return messageHandler.getAuthData().getSid();
        }

        @Override
        public String getNick() throws RemoteException {
            return messageHandler.getAuthData().getNick();
        }

        @Override
        public List getChannels() throws RemoteException {
            return messageHandler.getChannels();
        }

        @Override
        public List getMessages() throws RemoteException {
            return messageHandler.getMessages();
        }

        @Override
        public List getUsers() throws RemoteException {
            return messageHandler.getUsers();
        }

        @Override
        public void setChannelId(String id) throws RemoteException {
            messageHandler.setChannel(id);
        }
    }

    public ServiceProxy mServiceProxy = new ServiceProxy();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(LOG_TAG, "Сервис запускается");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "Сервис создан");
        //Инициализация сервиса для работы с сообщениями
        messageHandler = new MessageHandler();
        messageHandler.setMessageSocketService(MessageSocketService.this);
        //Подключение к сокету
        ContextThread getSocketThread = new ContextThread();
        getSocketThread.setService(MessageSocketService.this);
        getSocketThread.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, LOG_TAG + " завершен");
        super.onDestroy();
    }

    public void onMessage(String message, boolean asResultToUI) {
        if (asResultToUI) {
            try {
                mServiceProxy.mActivityCallback.sendToUI(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            messageHandler.handleToMessage(message);
        }
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
