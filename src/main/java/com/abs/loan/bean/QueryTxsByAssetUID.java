package com.abs.loan.bean;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

public class QueryTxsByAssetUID extends QueryBaseRespBean {

    private List<TxSummary> txContents;// 交易内容 List<txSummary> M 交易内容概要列表
    
    public static class TxSummary {
        @Field("assetUID")
        private String assetUid;// 资产ID String M
        @Field("txID")
        private String txId;// 交易ID String M 区块链中该交易的ID
        private String txType;// 交易类型 String M 见章节4.2中交易类型列表
        @Field("previousTxID")
        private String prevTxId;// 前序交易ID String M
        private String txTime;// 时间戳 String ans20 M 交易写入区块链时间， yyyyMMddHHmmss
        private String txStatus;// 交易状态 String n2 M 01-交易有效，已被写入区块链并执行 02-交易无效，已被写入区块链但不执行
        @Field("category")
        private String txCategory;
        private String orgCode;
        private String outTradeNo;
        private String blockHash;
        private long blockHeight;
        @Field("channelID")
        private String channelId;
        @Field("chaincodeID")
        private String chaincodeId;
        
        public String getTxCategory() {
            return txCategory;
        }

        public void setTxCategory(String txCategory) {
            this.txCategory = txCategory;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChaincodeId() {
            return chaincodeId;
        }

        public void setChaincodeId(String chaincodeId) {
            this.chaincodeId = chaincodeId;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public long getBlockHeight() {
            return blockHeight;
        }

        public void setBlockHeight(long blockHeight) {
            this.blockHeight = blockHeight;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getAssetUid() {
            return assetUid;
        }

        public void setAssetUid(String assetUid) {
            this.assetUid = assetUid;
        }

        public String getTxId() {
            return txId;
        }

        public void setTxId(String txId) {
            this.txId = txId;
        }

        public String getTxType() {
            return txType;
        }

        public void setTxType(String txType) {
            this.txType = txType;
        }

        public String getPrevTxId() {
            return prevTxId;
        }

        public void setPrevTxId(String prevTxId) {
            this.prevTxId = prevTxId;
        }

        public String getTxTime() {
            return txTime;
        }

        public void setTxTime(String txTime) {
            this.txTime = txTime;
        }

        public String getTxStatus() {
            return txStatus;
        }

        public void setTxStatus(String txStatus) {
            this.txStatus = txStatus;
        }
    }

    public List<TxSummary> getTxContents() {
        return txContents;
    }

    public void setTxContents(List<TxSummary> txContents) {
        this.txContents = txContents;
    }

}

