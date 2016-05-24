// ICallback.aidl
package com.zanexess.messenger_tech_03;

// Declare any non-default types here with import statements

interface ICallback {
    void onNewMessage(String data);
    void sendToUI(String result);
    void onNewUser(String nick, String status);
}
