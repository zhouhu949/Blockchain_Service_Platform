package com.abs.loan.bean;

/**
 * 响应数据 
 * 是区块链服务系统面向业务调用方（包括资金方、资产方、数据查询方）的接口所提供的数据字段
 */
public class ResponseData {
    
    private String code;// 业务返回码 String an6…10 M
    private String msg;// 业务返回描述 String C50 M
    private String outTradeNo;// 外部流水号 String ans40 M 外部交易流水号，必填保证唯一
    private String bizContent;// 业务数据 String M

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
        return "ResponseData [code=" + code + ", msg=" + msg + ", outTradeNo=" + outTradeNo
                + ", bizContent=" + bizContent + "]";
    }



}

