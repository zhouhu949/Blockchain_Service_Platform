package com.abs.bean;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "block")
public class BlockPO {
    public static final String COLLECTION_NAME = "block";

    @Indexed(unique=true)
    private String blockHash;// PK index char(64)
    @Indexed
    private Long blockHeight;// index Integer
    private String previousBlockHash;// 前序 char(64)
    @Field("channelID")
    private String channelId;// FK varchar(100)
    private Integer txNum;// int
    @Field("txIDs")
    private List<String> txIds;// List<char(64)>
    private String insertTime;// timestamp
    private String blockTime;// timestamp

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelID) {
        this.channelId = channelID;
    }

    public Integer getTxNum() {
        return txNum;
    }

    public void setTxNum(Integer txNum) {
        this.txNum = txNum;
    }

    public List<String> getTxIds() {
        return txIds;
    }

    public void setTxIds(List<String> txs) {
        this.txIds = txs;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String toString() {
        return "BlockPO [blockHash=" + blockHash + ", blockHeight=" + blockHeight
                + ", previousBlockHash=" + previousBlockHash + ", channelID=" + channelId
                + ", txNum=" + txNum + ", txs=" + txIds + ", insertTime=" + insertTime + "]";
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }


}

