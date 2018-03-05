package com.abs.loan.bean;

public class SimpleResp {
    private String txID;// 交易ID String M 区块链中该交易的ID
    private String errorMessages;// 错误信息 List<String> C 返回code非CF0000时用于填充异常性描述

    public String getTxID() {
        return txID;
    }

    public void setTxID(String txID) {
        this.txID = txID;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

}

