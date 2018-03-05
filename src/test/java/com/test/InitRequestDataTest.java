package com.test;

import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;

public class InitRequestDataTest {
    
    private static String assetUid = "\"jyzb0000000001Z20170425000113\"";
    private static String orgCode = "\"orgJD\"";
    private static String normal = "true";
    
    // 1贷款申请相关
    // 1.1loanApply 贷款申请
    public static String getJsonLoanApply() {
        String json = "{" +
//                "   \"orgCode\":"+orgCode+"," +
                "   \"orgCode\":"+orgCode+"," +
                "   \"timestamp\":\"20170425153030\"," +
                "   \"assetUid\":"+assetUid+"," +
                "   \"outTradeNo\":\"JYJD2017000000030233\"," +
                "   \"bizContent\":{" +
                "       \"loanApply\":{" +
                "           \"contractNo\":\"Z201704250001\"," +
                "           \"pkgNo\":\"ql12\"," +
                "           \"prodNo\":\"jyzb-one\"," +
                "           \"productNo\":\"0000000001\"," +
                "           \"productName\":\"测试资产\"," +
                "           \"applicationPlace\":\"110000\"," +
                "           \"applicationNo\":\"Z201704250001\"," +
                "           \"sourceChannels\":\"05\"," +
                "           \"application\":\"借贷\"," +
                "           \"contractAmount\":10000," +
                "           \"amountPayed\":\"300\"," +
                "           \"currencyType\":\"01\"," +
                "           \"periodType\":\"02\"," +
                "           \"expiresMonth\":12," +
                "           \"refundAccountType\":\"0201\"," +
                "           \"refundAccount\":\"111111111111111\"," +
                "           \"refundMethod\":\"01\"," +
                "           \"chargeDateType\":\"01\"," +
                "           \"chargeDateType2\":\"02\"," +
                "           \"chargeDate\":\"31\"," +
                "           \"managementInstitution\":\"JD\"," +
                "           \"paymentWay\":\"01\"," +
                "           \"loanType\":\"02\"," +
                "           \"borrowerType\":\"01\"," +
                "           \"handlingCharge\":1," +
                "           \"monthlyRate\":12," +
                "           \"penaltyRate\":\"0\"," +
                "           \"monthlyPenaltyRate\":\"0\"," +
                "           \"secureDay\":\"0\"," +
                "           \"serviceCharge\":\"0\"," +
                "           \"serviceChargeRate\":\"0\"," +
                "           \"secureCharge\":\"0\"," +
                "           \"secureChargeRate\":\"0\"," +
                "           \"bankCode\":\"003\"," +
                "           \"openAccountArea\":\"110000\"," +
                "           \"earnestMoney\":\"0\"," +
                "           \"applyType\":2" +
                "        }," +
                "       \"loanUser\":{" +
                "           \"name\":\"艾元\"," +
                "           \"ceritificateType\":\"0\"," +
                "           \"ceritificateNo\":\"130183198710041002\"," +
                "           \"telephone\":\"12321234534\"," +
                "           \"cellphone\":\"12323433454\"," +
                "           \"maritalStatus\":\"01\"," +
                "           \"education\":\"10\"" +
                "        }," +
                "       \"loanAccount\":{" +
                "           \"loanAccountType\":\"11\"," +
                "           \"loanBankCode\":\"003\"," +
                "           \"loanAccountName\":\"abc\"," +
                "           \"loanAccountNo\":\"123123123\"" +
                "        }" +
                "    }" +
                "}";
        System.out.println(json);
        return json;
    }
    
    // 1.2 loanApprove 贷款审批
    // 审批不通过：
    public static String getJsonLoanApproveNoPass() {
        String json = "{\n" +
//                "   \"orgCode\":"+orgCode+",\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153230\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144885\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"approveResult\":\"02\",\n" +
                "       \"approveRejectReason\":\"该用户在黑名单中\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 审批通过的情况：
    public static String getJsonLoanApprovePass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153230\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144883\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"approveResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 1.3loanResultNotify 放款结果通知
    // 放款失败：
    public static String getJsonLoanResultNotifyNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153330\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000304\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"loanResult\":\"40\",\n" +
                "       \"errorMessage\":\"放款失败的原因...\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 放款成功：
    public static String getJsonLoanResultNotifyPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153330\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000304\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"loanResult\":\"30\",\n" +
                "       \"bankChargeDate\":\"1487782861000\",\n" +
                "       \"calcInterestTime\":\"1487782861000\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 1.4loanResultConfirm 放款结果确认
    // 不确认的情况：
    public static String getJsonLoanResultConfirmNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153430\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144886\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"02\",\n" +
                "       \"confirmRejectReason\":\"交易记录与放款结果不匹配\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 确认的情况：
    public static String getJsonLoanResultConfirmPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153430\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144886\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }

    // 2质押文件上传相关
    // 2.1 mortgageDocUpload 质押文件上传
    public static String getJsonMortgageDocUpload() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153530\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000306\",\n" +
                "   \"bizContent\":{\n" +
                "       \"files\":[\n" +
                "            {\n" +
                "               \"fileType\":\"001\",\n" +
                "               \"fileName\":\"aiyuan04060000001_37_001.jpg\",\n" +
                "               \"fileHash\":\"ff3f4036a1164d1ddbad5b3edf9022addb3e1961a54a922708a6c1ffc49e5489\",\n" +
                "               \"filePath\":\"/upload/conforms/20170406/\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"fileType\":\"001\",\n" +
                "               \"fileName\":\"aiyuan04060000001_60_001.pdf\",\n" +
                "               \"fileHash\":\"e029cf5860792c862980509bf9ff267aac1612422351118fe808aad0fb0caf2e\",\n" +
                "               \"filePath\":\"/upload/conforms/20170406/\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 2.2 mortgageDocUploadConfirm 质押文件确认
    // 确认的情况：
    public static String getJsonMortgageDocUploadConfirmPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153630\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144888\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 不确认的情况：
    public static String getJsonMortgageDocUploadConfirmNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153730\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144888\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"02\",\n" +
                "       \"confirmRejectReason\":\"质押文件不全\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 3差额划拨相关
    // 3.1 diffResultNotify 差额划拨通知
    public static String getJsonDiffResultNotify() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425153830\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144889\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"balanceTransferDirection\":1,\n" +
                "       \"balanceTransferAmount\":1234.00,\n" +
                "       \"balanceTransferDesc\":\"差额划拨描述...\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //3.2 diffResultConfirm 差额划拨确认
    //确认的情况：
    public static String getJsonDiffResultConfirmPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425154030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000307\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //不确认的情况：
    public static String getJsonDiffResultConfirmNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425154030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000307\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"02\",\n" +
                "       \"confirmRejectReason\":\"划拨金额不正确\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    // 4扣款相关
    // 4.1 repayPlanUpload 还款计划上传
    public static String getJsonRepayPlanUpload() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425154130\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000308\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"repayPlans\":[\n" +
                "            {\n" +
                "               \"issue\":1,\n" +
                "               \"refundDate\":20170425,\n" +
                "               \"refundInterest\":11.1,\n" +
                "               \"refundPrincipal\":222.2,\n" +
                "               \"singleMoney\":576.5,\n" +
                "               \"surplusPrincipal\":343.2\n" +
                "            },\n" +
                "            {\n" +
                "               \"issue\":2,\n" +
                "               \"refundDate\":20170525,\n" +
                "               \"refundInterest\":43.1,\n" +
                "               \"refundPrincipal\":343.2,\n" +
                "               \"singleMoney\":386.3,\n" +
                "               \"surplusPrincipal\":0\n" +
                "            },\n" +
                "            {\n" +
                "               \"issue\":3,\n" +
                "               \"refundDate\":20170625,\n" +
                "               \"refundInterest\":43.1,\n" +
                "               \"refundPrincipal\":343.2,\n" +
                "               \"singleMoney\":386.3,\n" +
                "               \"surplusPrincipal\":0\n" +
                "            },\n" +
                "            {\n" +
                "               \"issue\":4,\n" +
                "               \"refundDate\":20170725,\n" +
                "               \"refundInterest\":43.1,\n" +
                "               \"refundPrincipal\":343.2,\n" +
                "               \"singleMoney\":386.3,\n" +
                "               \"surplusPrincipal\":0\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    
    //4.2 repayPlanConfirm 还款计划确认
    //确认的情况：
    public static String getJsonRepayPlanConfirmPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425155030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144890\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //不确认的情况：
    public static String getJsonRepayPlanConfirmNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425155030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144890\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"02\",\n" +
                "       \"confirmRejectReason\":\"还款计划有误\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //4.3 repayResultNotify 扣款结果通知 
    //不同分期，只改变currentIssue字段
    //第1期：
    public static String getJsonRepayResultNotify(int currentIssue) {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425155030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000309\",\n" +
                "   \"bizContent\":{\n" +
                "       \"repayResult\":\n" +
                "            {\n" +
                "               \"contractNo\":\"Z201704250001\",\n" +
                "               \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "               \"orgRepayId\":\"A1test07\",\n" +
                "               \"currentIssue\":"+currentIssue+",\n" +
                "               \"normal\":"+normal+",\n" +
                "               \"currentIssueStatus\":\"01\",\n" +
                "               \"chargeMethod\":\"01\",\n" +
                "               \"chargeType\":\"01\",\n" +
                "               \"chargeDate\":1486310400000,\n" +
                "               \"mitigateFlag\":0,\n" +
                "               \"refundAccount\":\"111111111111111\",\n" +
                "               \"overdueDays\":3,\n" +
                "               \"bankChargeDate\":\"1487782861000\",\n" +
                "               \"isExceedBankLimit\":20,\n" +
                "               \"repayResultType\":1,\n" +
                "               \"chargeAmount\":33.33,\n" +
                "               \"chargePrincipal\":22.2,\n" +
                "               \"reducedPrincipal\":\"0\",\n" +
                "               \"subsidyPrincipal\":\"0\",\n" +
                "               \"chargeRate\":11.1,\n" +
                "               \"reducedRate\":0,\n" +
                "               \"subsidyRate\":0,\n" +
                "               \"penaltyRate\":0,\n" +
                "               \"reducedPenaltyRate\":0,\n" +
                "               \"subsidyPenaltyRate\":0,\n" +
                "               \"penaltyAmount\":0,\n" +
                "               \"chargeMoney\":35,\n" +
                "               \"refundSecureCharge\":0,\n" +
                "               \"refundServiceCharge\":0,\n" +
                "               \"pendingOverpayment\":0,\n" +
                "               \"otherFee\":0,\n" +
                "               \"otherReducedFee\":0,\n" +
                "               \"otherSubsidy\":0,\n" +
                "               \"pendingWholesalePayment\":0\n" +
                "            }\n" +
                "        \n" +
                "    }\n" +
                "}";
        json = "{\"assetUid\":\"asset-007\",\"bizContent\":{\"bankChargeDate\":\"1487782861000\",\"chargeAmount\":33.33,\"chargeDate\":1486310400000,\"chargeMethod\":\"01\",\"chargeMoney\":35,\"chargePrincipal\":22.2,\"chargeRate\":11.1,\"chargeType\":\"01\",\"contractNo\":\"Z201704250001\",\"currentIssue\":1,\"currentIssueStatus\":\"01\",\"isExceedBankLimit\":20,\"mitigateFlag\":0,\"normal\":false,\"orgRepayId\":\"A1test07\",\"otherFee\":0,\"otherReducedFee\":0,\"otherSubsidy\":0,\"overdueDays\":3,\"payTradeNo\":\"20170406100041000003023938\",\"penaltyAmount\":0,\"penaltyRate\":0,\"pendingOverpayment\":0,\"pendingWholesalePayment\":0,\"reducedPenaltyRate\":0,\"reducedPrincipal\":\"0\",\"reducedRate\":0,\"refundAccount\":\"111111111111111\",\"refundSecureCharge\":0,\"refundServiceCharge\":0,\"repayResultType\":1,\"subsidyPenaltyRate\":0,\"subsidyPrincipal\":\"0\",\"subsidyRate\":0},\"orgCode\":\"orgJY\",\"outTradeNo\":\"JYJD20170000000309\",\"timestamp\":\"20170425155030\"}";
        System.out.println(json);
        return json;
    }
    //4.4 repayResultConfirm 扣款结果确认
    //确认的情况：
    public static String getJsonRepayResultConfirmPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425162030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144891\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"orgRepayId\":\"A1test07\",\n" +
                "       \"currentIssue\":3,\n" +
                "       \"normal\":"+normal+",\n" +
                "       \"confirmResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //不确认的情况：
    public static String getJsonRepayResultConfirmNoPass(int currentIssue) {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425162030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144891\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"orgRepayId\":\"A1test07\",\n" +
                "       \"currentIssue\":"+currentIssue+",\n" +
                "       \"normal\":"+normal+",\n" +
                "       \"confirmResult\":\"02\",\n" +
                "       \"confirmRejectReason\":\"还款结果与还款计划不匹配\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //5回购相关
    //5.1 buybackApply 回购申请
    //还款流程不合规引起的回购：
    public static String getJsonBuybackApplyForRepayFlowError() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425163030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144892\",\n" +
                "   \"bizContent\":{\n" +
                "       \"buybackDetail\":{\n" +
                "           \"contractNo\":\"Z201704250001\",\n" +
                "           \"buyBackCategory\":\"01\",\n" +
                "           \"buybackReason\":\"还款流程不合规\",\n" +
                "           \"loanStartDate\":\"20170426\",\n" +
                "           \"loanTotal\":123456,\n" +
                "           \"expiry\":12,\n" +
                "           \"unexpirePrincipal\":0,\n" +
                "           \"unpaidPrincipal\":0,\n" +
                "           \"unpaidInterest\":0,\n" +
                "           \"lateChargeInterest\":0,\n" +
                "           \"overdueTotalDay\":0,\n" +
                "           \"overdueTotalCount\":0,\n" +
                "           \"buybackMoney\":123456,\n" +
                "           \"proName\":\"...\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        json = "{\n" +
                "      \"orgCode\": \"cloudFactory\",\n" +
                "      \"timestamp\": \"20170425163030\",\n" +
                "      \"assetUid\": \"jyzb0000000001Z201704250001\",\n" +
                "      \"outTradeNo\": \"jyzb2017042516243566144892\",\n" +
                "      \"bizContent\": {\n" +
                "        \"buybackDetail\": {\n" +
                "          \"contractNo\": \"Z201704250001\",\n" +
                "          \"buyBackCategory\": \"01\",\n" +
                "          \"buybackReason\": \"还款流程不合规\",\n" +
                "          \"loanStartDate\": \"20170426\",\n" +
                "          \"loanTotal\": 123456,\n" +
                "          \"expiry\": 12,\n" +
                "          \"unexpirePrincipal\": 0,\n" +
                "          \"unpaidPrincipal\": 0,\n" +
                "          \"unpaidInterest\": 0,\n" +
                "          \"lateChargeInterest\": 0,\n" +
                "          \"overdueTotalDay\": 0,\n" +
                "          \"overdueTotalCount\": 0,\n" +
                "          \"buybackMoney\": 123456,\n" +
                "          \"proName\": \"...\"\n" +
                "        }\n" +
                "      }\n" +
                "    }";
        System.out.println(json);
        return json;
    }
    //质押文件上传流程不合规引起的回购：
    public static String getJsonBuybackApplyForMortgageDocUploadFlowError() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425163030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144892\",\n" +
                "   \"bizContent\":{\n" +
                "       \"buybackDetail\":{\n" +
                "           \"contractNo\":\"Z201704250001\",\n" +
                "           \"buyBackCategory\":\"02\",\n" +
                "           \"buybackReason\":\"质押文件上传流程不合规：\",\n" +
                "           \"loanStartDate\":\"20170426\",\n" +
                "           \"loanTotal\":123456,\n" +
                "           \"expiry\":12,\n" +
                "           \"unexpirePrincipal\":0,\n" +
                "           \"unpaidPrincipal\":0,\n" +
                "           \"unpaidInterest\":0,\n" +
                "           \"lateChargeInterest\":0,\n" +
                "           \"overdueTotalDay\":0,\n" +
                "           \"overdueTotalCount\":0,\n" +
                "           \"buybackMoney\":123456,\n" +
                "           \"proName\":\"...\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //5.2 buybackApprove 回购审批
    //确认回购：
    public static String getJsonBuybackApprovePass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425172030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000313\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"buybackStatus\":\"10\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //回购拒绝：
    public static String getJsonBuybackApproveNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425172030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000314\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"buybackStatus\":\"20\",\n" +
                "       \"rejectReason\":\"...\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //5.3 buybackResultNotify 回购结果通知
    public static String getJsonBuybackResultNotify() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425173030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"jyzb2017042516243566144893\",\n" +
                "   \"bizContent\":{\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"buybackStatus\":\"30\",\n" +
                "       \"buybackSuccTime\":\"20170425153010\",\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //5.4 buybackResultConfirm 回购结果确认
    //确认的情况：
    public static String getJsonBuybackResultConfirmPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425183030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000317\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"01\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    //不确认的情况：
    public static String getJsonBuybackResultConfirmNoPass() {
        String json = "{\n" +
                "   \"orgCode\":"+orgCode+",\n" +
                "   \"timestamp\":\"20170425183030\",\n" +
                "   \"assetUid\":"+assetUid+",\n" +
                "   \"outTradeNo\":\"JYJD20170000000318\",\n" +
                "   \"bizContent\":{\n" +
                "       \"payTradeNo\":\"20170406100041000003023938\",\n" +
                "       \"bankChannelNo\":\"201704250010070250015091470\",\n" +
                "       \"contractNo\":\"Z201704250001\",\n" +
                "       \"confirmResult\":\"02\",\n" +
                "       \"confirmRejectReason\":\"confirm Reject\"\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        return json;
    }
    
    
    // 1贷款申请相关
    // 1.1loanApply 贷款申请
    public static RequestData getRequestDataLoanApply() {
        return JSON.parseObject(getJsonLoanApply(),RequestData.class);
    }
    
    // 1.2 loanApprove 贷款审批
    // 审批不通过：
    public static RequestData getRequestDataLoanApproveNoPass() {
        return JSON.parseObject(getJsonLoanApproveNoPass(),RequestData.class);
    }
    // 审批通过的情况：
    public static RequestData getRequestDataLoanApprovePass() {
        return JSON.parseObject(getJsonLoanApprovePass(),RequestData.class);
    }

    // 1.3loanResultNotify 放款结果通知
    // 放款失败：
    public static RequestData getRequestDataLoanResultNotifyNoPass() {
        return JSON.parseObject(getJsonLoanResultNotifyNoPass(), RequestData.class);
    }

    // 放款成功：
    public static RequestData getRequestDataLoanResultNotifyPass() {
        return JSON.parseObject(getJsonLoanResultNotifyPass(), RequestData.class);
    }

    // 1.4loanResultConfirm 放款结果确认
    // 不确认的情况：
    public static RequestData getRequestDataLoanResultConfirmNoPass() {
        return JSON.parseObject(getJsonLoanResultConfirmNoPass(), RequestData.class);
    }

    // 确认的情况：
    public static RequestData getRequestDataLoanResultConfirmPass() {
        return JSON.parseObject(getJsonLoanResultConfirmPass(), RequestData.class);
    }

    // 2质押文件上传相关
    // 2.1 mortgageDocUpload 质押文件上传
    public static RequestData getRequestDataMortgageDocUpload() {
        return JSON.parseObject(getJsonMortgageDocUpload(), RequestData.class);
    }

    // 2.2 mortgageDocUploadConfirm 质押文件确认
    // 确认的情况：
    public static RequestData getRequestDataMortgageDocUploadConfirmPass() {
        return JSON.parseObject(getJsonMortgageDocUploadConfirmPass(), RequestData.class);
    }

    // 不确认的情况：
    public static RequestData getRequestDataMortgageDocUploadConfirmNoPass() {
        return JSON.parseObject(getJsonMortgageDocUploadConfirmNoPass(), RequestData.class);
    }

    // 3差额划拨相关
    // 3.1 diffResultNotify 差额划拨通知
    public static RequestData getRequestDataDiffResultNotify() {
        return JSON.parseObject(getJsonDiffResultNotify(), RequestData.class);
    }

    // 3.2 diffResultConfirm 差额划拨确认
    // 确认的情况：
    public static RequestData getRequestDataDiffResultConfirmPass() {
        return JSON.parseObject(getJsonDiffResultConfirmPass(), RequestData.class);
    }

    // 不确认的情况：
    public static RequestData getRequestDataDiffResultConfirmNoPass() {
        return JSON.parseObject(getJsonDiffResultConfirmNoPass(), RequestData.class);
    }

    // 4扣款相关
    // 4.1 repayPlanUpload 还款计划上传
    public static RequestData getRequestDataRepayPlanUpload() {
        return JSON.parseObject(getJsonRepayPlanUpload(), RequestData.class);
    }

    // 4.2 repayPlanConfirm 还款计划确认
    // 确认的情况：
    public static RequestData getRequestDataRepayPlanConfirmPass() {
        return JSON.parseObject(getJsonRepayPlanConfirmPass(), RequestData.class);
    }

    // 不确认的情况：
    public static RequestData getRequestDataRepayPlanConfirmNoPass() {
        return JSON.parseObject(getJsonRepayPlanConfirmNoPass(), RequestData.class);
    }

    // 4.3 repayResultNotify 扣款结果通知
    // 不同分期，只改变currentIssue字段
    // 第1期：
    public static RequestData getRequestDataRepayResultNotify(int currentIssue) {
        return JSON.parseObject(getJsonRepayResultNotify(currentIssue), RequestData.class);
    }

    // 4.4 repayResultConfirm 扣款结果确认
    // 确认的情况：
    public static RequestData getRequestDataRepayResultConfirmPass() {
        return JSON.parseObject(getJsonRepayResultConfirmPass(), RequestData.class);
    }

    // 不确认的情况：
    public static RequestData getRequestDataRepayResultConfirmNoPass() {
        return JSON.parseObject(getJsonRepayResultConfirmNoPass(1), RequestData.class);
    }

    // 5回购相关
    // 5.1 buybackApply 回购申请
    // 还款流程不合规引起的回购：
    public static RequestData getRequestDataBuybackApplyForRepayFlowError() {
        return JSON.parseObject(getJsonBuybackApplyForRepayFlowError(), RequestData.class);
    }

    // 质押文件上传流程不合规引起的回购：
    public static RequestData getRequestDataBuybackApplyForMortgageDocUploadFlowError() {
        return JSON.parseObject(getJsonBuybackApplyForMortgageDocUploadFlowError(), RequestData.class);
    }

    // 5.2 buybackApprove 回购审批
    // 确认回购：
    public static RequestData getRequestDataBuybackApprovePass() {
        return JSON.parseObject(getJsonBuybackApprovePass(), RequestData.class);
    }

    // 回购拒绝：
    public static RequestData getRequestDataBuybackApproveNoPass() {
        return JSON.parseObject(getJsonBuybackApproveNoPass(), RequestData.class);
    }

    // 5.3 buybackResultNotify 回购结果通知
    public static RequestData getRequestDataBuybackResultNotify() {
        return JSON.parseObject(getJsonBuybackResultNotify(), RequestData.class);
    }

    // 5.4 buybackResultConfirm 回购结果确认
    // 确认的情况：
    public static RequestData getRequestDataBuybackResultConfirmPass() {
        return JSON.parseObject(getJsonBuybackResultConfirmPass(), RequestData.class);
    }

    // 不确认的情况：
    public static RequestData getRequestDataBuybackResultConfirmNoPass() {
        return JSON.parseObject(getJsonBuybackResultConfirmNoPass(), RequestData.class);
    }

    
}

