package com.abs.loan.bean;

public class LoanApproveReq {
//    private String outTradeNo;// 外部流水号 String ans40 M 外部交易流水号，必填保证唯一
    private String contractNo;// 合同号 String ans40 M
    private String approveResult;// 审批结果 String n2 M 01-审批通过 02-审批拒绝
    private String approveRejectReason;// 审批拒绝原因 String C approveResult为02时必填
    
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getApproveResult() {
        return approveResult;
    }
    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }
    public String getApproveRejectReason() {
        return approveRejectReason;
    }
    public void setApproveRejectReason(String approveRejectReason) {
        this.approveRejectReason = approveRejectReason;
    }
    
    
}

