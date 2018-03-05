package com.abs.loan.bean;

public class GetTxsByComboConditionsReq {
    
    private String channelId;// channel ID String M
    private String orgCode;// 合作机构编号 String ans2…10 C 接入前需要申请分配
    private String assetUid;// 资产ID string C 资产的唯一ID
    private String outTradeNo;// 外部流水号 String ans40 C 外部交易流水号，必填保证唯一
    private String txId;// 交易ID String M 区块链中该交易的ID
    private String txType;// 交易类型 String M 见章节4.2中交易类型列表
    private String timeStart;// 起始时间（包含） String ans20 C 起始时间（包含），yyyyMMddHHmmss
    private String timeEnd;// 结束时间（不包含） String ans20 C 结束时间（不包含），yyyyMMddHHmmss

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

}

