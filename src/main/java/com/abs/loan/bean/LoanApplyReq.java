package com.abs.loan.bean;

import java.math.BigDecimal;

public class LoanApplyReq {
    // private String outTradeNo; //外部流水号 String ans40 M 外部交易流水号，必填保证唯一

    // 贷款单信息(loanApply)
    private String contractNo;// 1 合同号 String ans40 M 借款的标的号，必须保证唯一
    private String pkgNo;// 2 大包编号 String ans40 C applyType为存量贷款单时必填，需保证统一买入的大包编号唯一
    private String prodNo;// 3 与京东合作项目编号 String an30 M 合作项目编号，上前线有京东提供
    private String productNo;// 4 产品编号 String an30 M 网贷真实信息编号
    private String productName;// 5 产品名称 String C40 M 网贷真实项目名称
    private String applicationPlace;// 6 申请地点 String n4..10 M 申请地点代码
    private String applicationNo;// 7 申请号 String ans40 M 同合同号保持一致
    private String sourceChannels;// 8 渠道来源 String n2..4 M 01-外贸信托 02-渤海信托 03-易鑫租赁 04-华昌租赁 05-中航信托
                                  // 06-云南信托
    private String application;// 9 申请用途 String C100 M 详细描述
    private BigDecimal contractAmount;// 10 合同金额 BigDecimal n12,2 M 以元为单位，2位小数
    private BigDecimal amountPayed;// 11 实付金额 BigDecimal n12,2 M 以元为单位，2位小数
    private String currencyType;// 12 申请币种 String n2 M 01-人民币
    private String periodType;// 13 期限类型 String n2 M 01-按天；02-按月；
    private Integer expiresMonth;// 14 申请期限 Integer n2 M
    private String refundAccountType;// 15 还款账户类型 String n4 M 0100-对公账户 0101-对公存款账户 0200-个人账户
                                     // 0201-个人借记卡账户 0202-个人准贷记卡账户 0203-个人贷记卡账户 0204-个人存折账户 9999-其他
    private String refundAccount;// 16 还款账号 String an32 M 用户还款账号，放款亦是此帐号。
    private String refundMethod;// 17 还款方式 String n2 M 01-等额本息 02-等额本金 03-一次还本付息 04-等本等息
                                // 05-按月付息，到期一次性还本 99-其他
    private String chargeDateType;// 18 扣款日类型 String n2 M 01-放款日为扣款日 02-固定扣款日
    private String chargeDateType2;// 19 扣款日类别 String n2 M 01-对日 02-对月 03-对季 04-对年 99-其他
    private String chargeDate;// 20 扣款日期 String n2 M
    private String managementInstitution;// 21 经办机构 String C30 M 填写合作结构简称（中/英）, JD根据机构码获取合作机构名称
    private String paymentWay;// 22 缴费方式 String n2 M 01 期缴--按月还 由合作机构扣，没有写成其他即可 02 趸缴--一次性还清 99 其他
    private String loanType;// 23 贷款类型 String n2 M 01 房贷 02 车贷 03 消费贷 04 经营贷 05 人品贷 06 现金贷 99 其他
    private String borrowerType;// 24 借款人类型 String n2 M 01 法定代表人 02 自然人股 04 个体工商户 05 合伙企业合伙人 06
                                // 个人独资企业出资人 99 其他
    private Long handlingCharge;// 25 手续费 Long n12 O 常规手续费，没有填写0
    private BigDecimal handlingChargeRate;// 26 手续费率 BigDecimal n11,4 O 费率可为四位小数，没有填写0
    private BigDecimal monthlyRate;// 27 利率（月） BigDecimal n11,4 M 传送月利率，直接将年化利率除以12。
    // 举例：例如实际月利率为2.1%，则传送为2.1。
    private BigDecimal penaltyRate;// 28 提前还款违约金比率 BigDecimal n11,4 M 不要百分号，乘以100的值
    private BigDecimal monthlyPenaltyRate;// 29 罚息率(月) BigDecimal n11,4 M 具体规则同利率(月) 不要百分号，乘以100的值
    private Long secureDay;// 30 履行担保天数 Long n4 M 为0示不执行履行担保没有可填写0，客户和合作机构。
    private BigDecimal serviceCharge;// 31 服务费 BigDecimal N11,2 M
    private BigDecimal serviceChargeRate;// 32 服务费率 BigDecimal N11,4 M 不要百分号，乘以100的值，保留两位小数
    private BigDecimal secureCharge;// 33 担保费 BigDecimal N11,2 M 阳光为保费，他机构设计，没有传0
    private BigDecimal secureChargeRate;// 34 担保费率 BigDecimal N11,4 M 阳光为保费率，其他机构设计，没有传0
    private String bankCode;// 35 银行代码 String a3…5 M 还款银行代码 意：真实放款时，不可传送默认值，此字段为放款要
                            // 素之一，若传送‘000-未知’，会影响放款，请悉知
    private String openAccountArea;// 36 开户省市 String n6 M 若无法取到，将公司门店所在城市对应编码进行赋值
    private BigDecimal earnestMoney;// 37 定金 BigDecimal n12,2 M 用户组申请贷款时缴纳的定金，默认为0
    private String loanDate;// 38 放款日期 String D C 如果不为空，则取该字段作为放款日期，可传送放款操作发生（可为空），YYYY-MM-DD格式
    private Integer applyType;// 39 贷款申请类型 Integer n1…2 M 1：增量2：存量 平台新生成的贷款称为增量；网贷平台积累的贷款为存量

    // 用户信息（LoanUser）
     private String loanUser;//40 个人用户信息 LoanUser C 借款人个人信息（或企业法人信息）。具体内容见个人用户信息

    // 关系人列表（List<RelationUser>）
     private String relationUsers;//41 关系人[多个] List<RelationUser> C 有联系人、保证人时填写

    // 企业信息（LoanEnterprise）
     private String loanEnterprise;//42 企业信息 LoanEnterprise C 企业贷款时填写。具体见企业信息

    // 放款账户信息(loanAccount)
    private String loanAccountType;// 43 放款账户类型 String n2 M 11-个人账户 12-企业账户
    private String loanBankCode;// 44 放款银行代码 String a3…5 M 码表见附录
    private String loanAccountName;// 45 放款账户名称 String C100 M
    private String loanAccountNo;// 46 放款账户号码 String ans32 M
    private String loanAccountBank;// 47 放款账户开户支行 String C100 O
                                   // 为招行设计的字段，客户选择放款渠道为招行则必填。如招商银行北京万达广场支行
    private String loanAccountProvince;// 48 放款账户开户所在省 String C20 C
                                       // 通过导出招商银行渠道放款的机构，该字段为必填项，填汉字，如北京市、河北省
    private String loanAccountCity;// 49 放款账户开户所在市 String C50 C 通过导出招商银行渠道放款的机构，该字段为必填项，填汉字，如北京市、武汉市
    
    private String mortgages;// 50 抵押物[多个]  List<Mortgage>      C   抵押物信息，有抵押物时填写。
    
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getPkgNo() {
        return pkgNo;
    }
    public void setPkgNo(String pkgNo) {
        this.pkgNo = pkgNo;
    }
    public String getProdNo() {
        return prodNo;
    }
    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }
    public String getProductNo() {
        return productNo;
    }
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getApplicationPlace() {
        return applicationPlace;
    }
    public void setApplicationPlace(String applicationPlace) {
        this.applicationPlace = applicationPlace;
    }
    public String getApplicationNo() {
        return applicationNo;
    }
    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }
    public String getSourceChannels() {
        return sourceChannels;
    }
    public void setSourceChannels(String sourceChannels) {
        this.sourceChannels = sourceChannels;
    }
    public String getApplication() {
        return application;
    }
    public void setApplication(String application) {
        this.application = application;
    }
    public BigDecimal getContractAmount() {
        return contractAmount;
    }
    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }
    public BigDecimal getAmountPayed() {
        return amountPayed;
    }
    public void setAmountPayed(BigDecimal amountPayed) {
        this.amountPayed = amountPayed;
    }
    public String getCurrencyType() {
        return currencyType;
    }
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
    public String getPeriodType() {
        return periodType;
    }
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }
    public Integer getExpiresMonth() {
        return expiresMonth;
    }
    public void setExpiresMonth(Integer expiresMonth) {
        this.expiresMonth = expiresMonth;
    }
    public String getRefundAccountType() {
        return refundAccountType;
    }
    public void setRefundAccountType(String refundAccountType) {
        this.refundAccountType = refundAccountType;
    }
    public String getRefundAccount() {
        return refundAccount;
    }
    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }
    public String getRefundMethod() {
        return refundMethod;
    }
    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }
    public String getChargeDateType() {
        return chargeDateType;
    }
    public void setChargeDateType(String chargeDateType) {
        this.chargeDateType = chargeDateType;
    }
    public String getChargeDateType2() {
        return chargeDateType2;
    }
    public void setChargeDateType2(String chargeDateType2) {
        this.chargeDateType2 = chargeDateType2;
    }
    public String getChargeDate() {
        return chargeDate;
    }
    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }
    public String getManagementInstitution() {
        return managementInstitution;
    }
    public void setManagementInstitution(String managementInstitution) {
        this.managementInstitution = managementInstitution;
    }
    public String getPaymentWay() {
        return paymentWay;
    }
    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }
    public String getLoanType() {
        return loanType;
    }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    public String getBorrowerType() {
        return borrowerType;
    }
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }
    public Long getHandlingCharge() {
        return handlingCharge;
    }
    public void setHandlingCharge(Long handlingCharge) {
        this.handlingCharge = handlingCharge;
    }
    public BigDecimal getHandlingChargeRate() {
        return handlingChargeRate;
    }
    public void setHandlingChargeRate(BigDecimal handlingChargeRate) {
        this.handlingChargeRate = handlingChargeRate;
    }
    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }
    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }
    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }
    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
    }
    public BigDecimal getMonthlyPenaltyRate() {
        return monthlyPenaltyRate;
    }
    public void setMonthlyPenaltyRate(BigDecimal monthlyPenaltyRate) {
        this.monthlyPenaltyRate = monthlyPenaltyRate;
    }
    public Long getSecureDay() {
        return secureDay;
    }
    public void setSecureDay(Long secureDay) {
        this.secureDay = secureDay;
    }
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }
    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
    public BigDecimal getServiceChargeRate() {
        return serviceChargeRate;
    }
    public void setServiceChargeRate(BigDecimal serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }
    public BigDecimal getSecureCharge() {
        return secureCharge;
    }
    public void setSecureCharge(BigDecimal secureCharge) {
        this.secureCharge = secureCharge;
    }
    public BigDecimal getSecureChargeRate() {
        return secureChargeRate;
    }
    public void setSecureChargeRate(BigDecimal secureChargeRate) {
        this.secureChargeRate = secureChargeRate;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getOpenAccountArea() {
        return openAccountArea;
    }
    public void setOpenAccountArea(String openAccountArea) {
        this.openAccountArea = openAccountArea;
    }
    public BigDecimal getEarnestMoney() {
        return earnestMoney;
    }
    public void setEarnestMoney(BigDecimal earnestMoney) {
        this.earnestMoney = earnestMoney;
    }
    public String getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }
    public Integer getApplyType() {
        return applyType;
    }
    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }
    public String getLoanAccountType() {
        return loanAccountType;
    }
    public void setLoanAccountType(String loanAccountType) {
        this.loanAccountType = loanAccountType;
    }
    public String getLoanBankCode() {
        return loanBankCode;
    }
    public void setLoanBankCode(String loanBankCode) {
        this.loanBankCode = loanBankCode;
    }
    public String getLoanAccountName() {
        return loanAccountName;
    }
    public void setLoanAccountName(String loanAccountName) {
        this.loanAccountName = loanAccountName;
    }
    public String getLoanAccountNo() {
        return loanAccountNo;
    }
    public void setLoanAccountNo(String loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }
    public String getLoanAccountBank() {
        return loanAccountBank;
    }
    public void setLoanAccountBank(String loanAccountBank) {
        this.loanAccountBank = loanAccountBank;
    }
    public String getLoanAccountProvince() {
        return loanAccountProvince;
    }
    public void setLoanAccountProvince(String loanAccountProvince) {
        this.loanAccountProvince = loanAccountProvince;
    }
    public String getLoanAccountCity() {
        return loanAccountCity;
    }
    public void setLoanAccountCity(String loanAccountCity) {
        this.loanAccountCity = loanAccountCity;
    }
    public String getMortgages() {
        return mortgages;
    }
    public void setMortgages(String mortgages) {
        this.mortgages = mortgages;
    }
    public String getLoanUser() {
        return loanUser;
    }
    public void setLoanUser(String loanUser) {
        this.loanUser = loanUser;
    }
    public String getRelationUsers() {
        return relationUsers;
    }
    public void setRelationUsers(String relationUsers) {
        this.relationUsers = relationUsers;
    }
    public String getLoanEnterprise() {
        return loanEnterprise;
    }
    public void setLoanEnterprise(String loanEnterprise) {
        this.loanEnterprise = loanEnterprise;
    }
    
}

