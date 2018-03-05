package com.abs.bean;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "transaction")
public class TransactionPO {
    public static final String COLLECTION_NAME = "transaction";
    @Indexed(unique=true)
    @Field("txID")
    private String txId;// PK index char(32)
    @Field("chaincodeID")
    private String chaincodeId;// varchar(100)
    private Integer txStatus;// int 交易入链状态
    private String txTime;// timestamp 交易时间
    private String blockHash;// FK char(64)
    private Long blockHeight;// int

    private String orgCode;// varchar(10) 合作机构编号
    @Indexed
    @Field("assetUID")
    private String assetUid;// varchar(50) index 资产唯一ID
    @Indexed
    private String outTradeNo;// varchar(40) √ 外部交易流水号
    private String bizContent;// varchar(2000) 交易内容
    private String category;// varchar(100) 交易类别
    private String txType;// varchar(30) 交易类型，如 loanApply, loanApprove
    @Field("previousTxID")
    private String previousTxId;// char(32) 前序交易ID
    @Indexed
    private String businessHash;// char(64) √ 业务Hash，根据业务请求数据生成，用于接口幂等性控制。

    @Field("channelID")
    private String channelId;// FK varchar(100)

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getChaincodeId() {
        return chaincodeId;
    }

    public void setChaincodeId(String chainCodeId) {
        this.chaincodeId = chainCodeId;
    }

    public Integer getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(Integer txStatus) {
        this.txStatus = txStatus;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getAssetUid() {
        return assetUid;
    }

    public void setAssetUid(String assetUid) {
        this.assetUid = assetUid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getPreviousTxId() {
        return previousTxId;
    }

    public void setPreviousTxId(String previousTxId) {
        this.previousTxId = previousTxId;
    }

    public String getBusinessHash() {
        return businessHash;
    }

    public void setBusinessHash(String businessHash) {
        this.businessHash = businessHash;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }



}

