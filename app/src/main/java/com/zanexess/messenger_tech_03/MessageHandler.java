package com.zanexess.messenger_tech_03;

import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.zanexess.messenger_tech_03.Messages.ChannelListResponceData;
import com.zanexess.messenger_tech_03.Messages.CreateChannelResponceData;
import com.zanexess.messenger_tech_03.Messages.EnterResponceData;
import com.zanexess.messenger_tech_03.Messages.EvEnterResponceData;
import com.zanexess.messenger_tech_03.Messages.EvLeaveResponceData;
import com.zanexess.messenger_tech_03.Messages.EvMessageResponceData;
import com.zanexess.messenger_tech_03.Messages.LeaveResponceData;
import com.zanexess.messenger_tech_03.Messages.LoginResponceData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.Messages.RegistrationResponceData;
import com.zanexess.messenger_tech_03.Messages.SetUserInfoResponceData;
import com.zanexess.messenger_tech_03.Messages.UserInfoResponceData;
import com.zanexess.messenger_tech_03.Messages.WelcomeResponceData;
import com.zanexess.messenger_tech_03.Objects.AuthData;
import com.zanexess.messenger_tech_03.Objects.Channel;
import com.zanexess.messenger_tech_03.Objects.LastMsg;
import com.zanexess.messenger_tech_03.Objects.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageHandler {
    private final String LOG_TAG = getClass().getSimpleName();
    private Socket socket;
    private MessageSocketService messageSocketService;
    private Gson gson;
    private AuthData authData;
    private List<Channel> channels;
    private List<LastMsg> messages;
    private List<User> users;
    private String channel;
    private User user;

    public MessageHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageDeserializer());
        gson = gsonBuilder.create();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setMessageSocketService(MessageSocketService messageSocketService) {
        this.messageSocketService = messageSocketService;
    }

    //Отправка сообщений на сервер
    public void sendMessage (final String message) {
        //TODO Check
        try {
            if (socket != null) {
                socket.getOutputStream().write(message.getBytes());
                socket.getOutputStream().flush();
            } else {
                messageSocketService.onMessage("Нет соединения. Проверьте интернет соединение", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                messageSocketService.onMessage("Нет соединения. Проверьте интернет соединение", true);
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //Обработка полученных сообщений из строки в объекты
    public void handleToMessage(String result) {
        Log.v(LOG_TAG, result);
        String[] res = result.split("\\}\\}\\{");
        if (res.length == 1) {
            Message message = gson.fromJson(result, Message.class);
            handleMessage(message);
        } else {
            Message message = gson.fromJson(res[0]+"}}", Message.class);
            Log.v(LOG_TAG, res[0]+"}}");
            handleMessage(message);
            Message message1 = gson.fromJson("{"+res[1], Message.class);
            handleMessage(message1);
            Log.v(LOG_TAG, "{"+res[1]);
        }
    }

    //Выполнение логики в зависимости от пришедшей информации
    public void handleMessage(Message message) {
        if (message != null) {
            String action = message.getAction();
            switch (action) {
                case "register":
                    RegistrationResponceData registrationResponceData = (RegistrationResponceData) message.getData();
                    messageSocketService.onMessage(registrationResponceData.getError(), true);
                    break;
                case "auth":
                    LoginResponceData loginResponceData = (LoginResponceData) message.getData();
                    if (loginResponceData.getStatus().equals("0")) {
                        authData = new AuthData(loginResponceData.getSid(),
                                loginResponceData.getCid(),
                                loginResponceData.getNick());
                    }
                    if (!loginResponceData.getError().equals("OK")) {
                        try {
                            messageSocketService.mServiceProxy.mActivityCallback.onNewMessage("LOGIN_FAILED_ERR");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            messageSocketService.mServiceProxy.mActivityCallback.onNewMessage("LOGIN_SUCCESS");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    messageSocketService.onMessage(loginResponceData.getError(), true);
                    break;
                case "channellist":
                    ChannelListResponceData channelListResponceData = (ChannelListResponceData) message.getData();
                    channels = channelListResponceData.getChannels();
                    if (channelListResponceData.getError().equals("Need auth")) {
                        messageSocketService.onMessage("Соединение было прервано, попытка восстановления", true);
                        reAuth();
                    }
                    break;
                case "createchannel":
                    CreateChannelResponceData createChannelResponceData = (CreateChannelResponceData) message.getData();
                    messageSocketService.onMessage(createChannelResponceData.getError(), true);
                    break;
                case "setuserinfo":
                    SetUserInfoResponceData setUserInfoResponceData = (SetUserInfoResponceData) message.getData();
                    messageSocketService.onMessage(setUserInfoResponceData.getError(), true);
                    break;
                case "enter":
                    EnterResponceData enterResponceData = (EnterResponceData) message.getData();
                    messages = enterResponceData.getLast_msg();
                    users = enterResponceData.getUsers();
                    messageSocketService.onMessage(enterResponceData.getError(), true);
                    break;
                case "leave":
                    LeaveResponceData leaveResponceData = (LeaveResponceData) message.getData();
                    messages.clear();
                    users.clear();
                    channel = null;
                    break;
                case "ev_message":
                    Date now = new Date();
                    EvMessageResponceData evMessageResponceData = (EvMessageResponceData) message.getData();
                    if (evMessageResponceData.getChid().equals(channel)) {
                        messages.add(new LastMsg("0", evMessageResponceData.getFrom(),
                                evMessageResponceData.getNick(),
                                evMessageResponceData.getBody(),
                                DateFormat.getTimeInstance(DateFormat.MEDIUM).format(now)));
                        try {
                            messageSocketService.mServiceProxy.mActivityCallback.onNewMessage("REFRESH_CHANNEL");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "ev_enter":
                    EvEnterResponceData evEnterResponceData = (EvEnterResponceData) message.getData();
                    if (evEnterResponceData.getChid().equals(channel)) {
                        messages.add(new LastMsg("-1", "-1", "-1", evEnterResponceData.getNick() + " entered chat", "-1"));
                    }
                    try {
                        messageSocketService.mServiceProxy.mActivityCallback.onNewMessage("REFRESH_CHANNEL");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ev_leave":
                    EvLeaveResponceData evLeaveResponceData = (EvLeaveResponceData) message.getData();
                    if (evLeaveResponceData.getChid().equals(channel)) {
                        messages.add(new LastMsg("-1", "-1", "-1", evLeaveResponceData.getNick() + " left chat", "-1"));
                    }
                    try {
                        messageSocketService.mServiceProxy.mActivityCallback.onNewMessage("REFRESH_CHANNEL");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case "userinfo":
                    UserInfoResponceData userInfoResponceData = (UserInfoResponceData) message.getData();
                    if (userInfoResponceData.getError().equals("OK")) {
                        try {
                            messageSocketService.mServiceProxy.mActivityCallback.onNewUser(userInfoResponceData.getNick(), userInfoResponceData.getUser_status());
                        } catch (RemoteException e) {

                        }
                    }
                    break;
            }
        }
    }

    public List getChannels() {
        return channels;
    }

    public List getMessages() {
        return messages;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<User> getUsers() {
        return users;
    }

    public void reAuth() {
        try {
            messageSocketService.mServiceProxy.mActivityCallback.onNewMessage("REAUTH");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class MessageDeserializer implements JsonDeserializer<Message> {
        public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Message message = null;
            String string0 = null, string1 = null, string2 = null, string3 = null, string4 = null, string5 = null;
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject data = jsonObject.getAsJsonObject("data");

            String action = jsonObject.get("action").getAsString();
            switch (action) {
                case "welcome" :
                    message = new Message(action, new WelcomeResponceData());
                    break;
                case "register":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    message = new Message(action, new RegistrationResponceData(string0, string1));
                    break;
                case "auth":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();

                    if (data.has("sid")) {
                        string2 = data.get("sid").getAsString();
                        string3 = data.get("cid").getAsString();
                        string4 = data.get("nick").getAsString();
                    } else {
                        string2 = "";
                        string3 = "";
                        string4 = "";
                    }
                    message = new Message(action, new LoginResponceData(string0, string1, string2, string3, string4));
                    break;
                case "userinfo":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();

                    if (data.has("nick")) {
                        string2 = data.get("nick").getAsString();
                        string3 = data.get("user_status").getAsString();
                    } else {
                        string2 = "";
                        string3 = "";
                    }
                    message = new Message(action, new UserInfoResponceData(string0, string1, string2, string3));
                    break;
                case "channellist":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    List<Channel> channels = null;
                    if (data.has("channels")) {
                        channels = new ArrayList<>();
                        JsonArray jsonArray = data.getAsJsonArray("channels");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                            Channel channel = new Channel(asJsonObject.get("chid").getAsString(),
                                    asJsonObject.get("name").getAsString(),
                                    asJsonObject.get("descr").getAsString(),
                                    asJsonObject.get("online").getAsString());
                            channels.add(channel);
                        }
                    }
                    message = new Message(action, new ChannelListResponceData(string0, string1, channels));
                    break;
                case "createchannel":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    if (data.has("chid")) {
                        string2 = data.get("chid").getAsString();
                    } else {
                        string2 = "";
                    }
                    message = new Message(action, new CreateChannelResponceData(string0, string1, string2));
                    break;
                case "setuserinfo":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    message = new Message(action, new SetUserInfoResponceData(string0, string1));
                    break;
                case "enter":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    List<LastMsg> lastMsgs = null;
                    if (data.has("last_msg")) {
                        lastMsgs = new ArrayList<>();
                        JsonArray jsonArray = data.getAsJsonArray("last_msg");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                            LastMsg lastMsg = new LastMsg(asJsonObject.get("mid").getAsString(),
                                    asJsonObject.get("from").getAsString(),
                                    asJsonObject.get("nick").getAsString(),
                                    asJsonObject.get("body").getAsString(),
                                    asJsonObject.get("time").getAsString());
                            lastMsgs.add(lastMsg);
                        }
                    }
                    List<User> users = null;
                    if (data.has("users")) {
                        users = new ArrayList<>();
                        JsonArray jsonArray = data.getAsJsonArray("users");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                            User user = new User(asJsonObject.get("uid").getAsString(),
                                    asJsonObject.get("nick").getAsString());
                            users.add(user);
                        }
                    }
                    message = new Message(action, new EnterResponceData(string0, string1, users, lastMsgs));
                    break;
                case "ev_message":
                    string0 = data.get("chid").getAsString();
                    string1 = data.get("from").getAsString();
                    string2 = data.get("nick").getAsString();
                    string3 = data.get("body").getAsString();
                    message = new Message(action, new EvMessageResponceData(string0, string1, string2, string3));
                    break;
                case "ev_enter":
                    string0 = data.get("chid").getAsString();
                    string1 = data.get("uid").getAsString();
                    string2 = data.get("nick").getAsString();
                    message = new Message(action, new EvEnterResponceData(string0, string1, string2));
                    break;
                case "ev_leave":
                    string0 = data.get("chid").getAsString();
                    string1 = data.get("uid").getAsString();
                    string2 = data.get("nick").getAsString();
                    message = new Message(action, new EvLeaveResponceData(string0, string1, string2));
                    break;
            }
            return message;
        }
    }

    public AuthData getAuthData() {
        return authData;
    }
}
