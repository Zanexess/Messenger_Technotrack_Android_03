// IService.aidl
package com.zanexess.messenger_tech_03;

// Declare any non-default types here with import statements

interface IService {
    void bindActivity(IBinder callback);
    void sendMessage(String message);
//    Авторизационные данные
    String getCid();
    String getSid();
    String getNick();
    //Список чатов List<Channel>
    List getChannels();
    //Список сообщений List<LastMsg>
    List getMessages();
    //Список участников чата List<User>
    List getUsers();
    void setChannelId(String id);
}

