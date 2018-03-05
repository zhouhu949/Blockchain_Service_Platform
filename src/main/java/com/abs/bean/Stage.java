package com.abs.bean;

public class Stage {

    private String category;// String
    private String lastestStage;// String 该Category下最新Stage对应的TxID 即前序交易
    private String stageType;// String Stage/交易类型
    private String txTime;// String 入链时间

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastestStage() {
        return lastestStage;
    }

    public void setLastestStage(String lastestStage) {
        this.lastestStage = lastestStage;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

}

