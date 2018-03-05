package com.abs.loan.bean;

import java.util.List;

public class QueryChannelList extends QueryBaseRespBean {

    private List<String> channels;// channelID列表 List<string> M

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}

