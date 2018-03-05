package com.abs.loan.bean;

/**
 * 请求数据 是区块链服务系统面向业务调用方（包括资金方、资产方、数据查询方）的接口所提供的数据字段
 */
public class RequestData {

    private String orgCode; // 合作机构编号 String ans2…10 M 接入前需要申请分配
    private String timestamp;// 请求发送时间 String ans20 M 发送时间，yyyy-MM-dd HH:mm:ss
    private String assetUid;// 资产ID string M 资产的唯一ID
    private String outTradeNo;// 外部流水号 String ans40 M 外部交易流水号，必填保证唯一
    private String bizContent;// 业务数据 String M

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "RequestDate [orgCode=" + orgCode + ", timestamp=" + timestamp + ", assetUid="
                + assetUid + ", outTradeNo=" + outTradeNo + ", bizContent=" + bizContent + "]";
    }

    public String toStr() {
        return orgCode + timestamp + assetUid + outTradeNo + bizContent;
    }

}

