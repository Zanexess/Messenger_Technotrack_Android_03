package com.zanexess.messenger_tech_03;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.Messages.ChannelListData;
import com.zanexess.messenger_tech_03.Messages.LoginData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.Messages.RegistrationData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class MessageTests {
    @Test
    public void AuthMessage() {
        Message message = new Message("auth", new LoginData("111", "111"));
        String msg = new Gson().toJson(message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(msg);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        //Проверка корректности Json строки
        Assert.assertNotEquals(jsonObject, null);
    }

    @Test
    public void registerMessage() {
        Message message = new Message("register", new RegistrationData("1111", "1111", "1111"));
        String msg = new Gson().toJson(message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(msg);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        //Проверка корректности Json строки
        Assert.assertNotEquals(jsonObject, null);
    }

    @Test
    public void channelListMessage() {
        Message message = new Message("channellist", new ChannelListData("MY_LOGIN1", "a54b5e9a5b0a9ca09a769e3ab294b698"));
        String msg = new Gson().toJson(message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(msg);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        //Проверка корректности Json строки
        Assert.assertNotEquals(jsonObject, null);
    }

    //**И.т.д**//
}
