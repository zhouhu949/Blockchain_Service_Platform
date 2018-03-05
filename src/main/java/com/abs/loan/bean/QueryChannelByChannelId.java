package com.abs.loan.bean;

public class QueryChannelByChannelId extends QueryBaseRespBean {
    
    private String channelId;// channel ID String M
    private long blockNum;// 区块数 Long M channel 中区块数量

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(long blockNum) {
        this.blockNum = blockNum;
    }
}

