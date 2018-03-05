package com.abs.bean;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "channel")
public class ChannelPO {
    public static final String COLLECTION_NAME = "channel";

    public ChannelPO() {}

    public ChannelPO(String channelID) {
        this.channelId = channelID;
    }

    @Indexed(unique=true)
    @Field("channelID")
    private String channelId;// varchar(100)

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelID) {
        this.channelId = channelID;
    }

    public String toString() {
        return "ChannelPO [channelID=" + channelId + "]";
    }


}

