package com.zanexess.messenger_tech_03.Messages;

import com.zanexess.messenger_tech_03.Objects.Channel;
import com.zanexess.messenger_tech_03.Objects.LastMsg;
import com.zanexess.messenger_tech_03.Objects.User;

import java.util.List;

public class ChannelListResponceData implements Data {
    private String status;
    private String error;
    private List<Channel> channels;
    private List<LastMsg> messages;
    private List<User> users;
    private Channel currentChannel;

    public ChannelListResponceData() {
    }

    public ChannelListResponceData(String status, String error, List<Channel> channels) {
        this.status = status;
        this.error = error;
        this.channels = channels;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<Channel> getChannels() {
        return channels;
    }
}
