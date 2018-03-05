package com.test;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.abs.util.DateUtil;
import com.abs.util.HttpClientUtil;

@FixMethodOrder(MethodSorters.JVM)
public class ControllerTest {

    private String sendHttp(String url, String json) {
        System.out.println("url:" + url);
        HttpClientUtil client = HttpClientUtil.getInstance();
        String resp = client.sendHttpJsonPost(
                // "http://9.186.56.164:8080/Blockchain_Service_Platform/loan/" + url, json);
                "http://localhost:8080/Blockchain_Service_Platform/loan/" + url, json);
        System.out.println("" + resp);

        return resp;
    }

    private String sendHttp(String url) {
        System.out.println("url:" + url);
        HttpClientUtil client = HttpClientUtil.getInstance();
        String resp = client
                .sendHttpPost("http://localhost:8080/Blockchain_Service_Platform/loan/" + url);
        System.out.println("" + resp);

        return resp;
    }

    @Test
    public void loanApply() {
        sendHttp("loanApply", InitRequestDataTest.getJsonLoanApply());
        DateUtil.sleep(2500);
    }

    /**
     * 3.1.2 贷款审批
     */
    @Test
    public void loanApprove() {
        // sendHttp("loanApprove", InitRequestDataTest.getJsonLoanApprovePass());
        sendHttp("loanApprove", InitRequestDataTest.getJsonLoanApproveNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.1.3 放款结果通知
     */
    @Test
    public void loanResultNotify() {
        sendHttp("loanResultNotify", InitRequestDataTest.getJsonLoanResultNotifyPass());
        // sendHttp("loanResultNotify", InitRequestDataTest.getJsonLoanResultNotifyNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.1.4 放款结果确认
     */
    @Test
    public void loanResultConfirm() {
        sendHttp("loanResultConfirm", InitRequestDataTest.getJsonLoanResultConfirmPass());
        // sendHttp("loanResultConfirm", InitRequestDataTest.getJsonLoanResultConfirmNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.2.1 质押文件上传
     */
    @Test
    public void mortgageDocUpload() {
        sendHttp("mortgageDocUpload", InitRequestDataTest.getJsonMortgageDocUpload());
        DateUtil.sleep(2500);
    }

    /**
     * 3.2.2 质押文件确认
     */
    @Test
    public void mortgageDocConfirm() {
        // sendHttp("mortgageDocConfirm",
        // InitRequestDataTest.getJsonMortgageDocUploadConfirmPass());
        sendHttp("mortgageDocConfirm", InitRequestDataTest.getJsonMortgageDocUploadConfirmNoPass());
        DateUtil.sleep(2500);
    }


    /**
     * 3.3.1差额划拨结果通知 (/loan/diffResultNotify)
     */
    @Test
    public void diffResultNotify() {
        sendHttp("diffResultNotify", InitRequestDataTest.getJsonDiffResultNotify());
        DateUtil.sleep(2500);
    }

    /**
     * 3.3.2差额划拨结果确认 (/loan/diffResultConfirm)
     */
    @Test
    public void diffResultConfirm() {
        // sendHttp("diffResultConfirm", InitRequestDataTest.getJsonDiffResultConfirmPass());
        sendHttp("diffResultConfirm", InitRequestDataTest.getJsonDiffResultConfirmNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.4.1 还款计划上传
     */
    @Test
    public void repayPlanUpload() {
        sendHttp("repayPlanUpload", InitRequestDataTest.getJsonRepayPlanUpload());
        DateUtil.sleep(2500);
    }

    /**
     * 3.4.2 还款计划确认
     */
    @Test
    public void repayPlanConfirm() {
        // sendHttp("repayPlanConfirm", InitRequestDataTest.getJsonRepayPlanConfirmPass());
        sendHttp("repayPlanConfirm", InitRequestDataTest.getJsonRepayPlanConfirmNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.4.3 扣款结果通知
     */
    @Test
    public void repayResultNotify() {
        sendHttp("repayResultNotify", InitRequestDataTest.getJsonRepayResultNotify(1));
        DateUtil.sleep(2500);
    }

    /**
     * 3.4.4 扣款结果确认
     */
    @Test
    public void repayResultConfirm() {
        // sendHttp("repayResultConfirm", InitRequestDataTest.getJsonRepayResultConfirmPass(1));
        sendHttp("repayResultConfirm", InitRequestDataTest.getJsonRepayResultConfirmNoPass(1));
        DateUtil.sleep(2500);
    }

    /**
     * 3.5.1 回购申请
     */
    @Test
    public void buybackApply() {
        // sendHttp("buybackApply",
        // InitRequestDataTest.getJsonBuybackApplyForMortgageDocUploadFlowError());
        sendHttp("buybackApply", InitRequestDataTest.getJsonBuybackApplyForRepayFlowError());
        DateUtil.sleep(2500);
    }

    /**
     * 3.5.2 回购审批
     */
    @Test
    public void buybackApprove() {
        // sendHttp("buybackApprove", InitRequestDataTest.getJsonBuybackApprovePass());
        sendHttp("buybackApprove", InitRequestDataTest.getJsonBuybackApproveNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.5.3 回购结果通知
     */
    @Test
    public void buybackResultNotify() {
        sendHttp("buybackResultNotify", InitRequestDataTest.getJsonBuybackResultNotify());
        DateUtil.sleep(2500);
    }

    /**
     * 3.5.4 回购结果确认
     */
    @Test
    public void buybackResultConfirm() {
        // sendHttp("buybackResultConfirm", InitRequestDataTest.getJsonBuybackResultConfirmPass());
        sendHttp("buybackResultConfirm", InitRequestDataTest.getJsonBuybackResultConfirmNoPass());
        DateUtil.sleep(2500);
    }

    /**
     * 3.6.1 根据资产ID查询交易概要
     */
    @Test
    public void queryTxsByAssetUID() {
        sendHttp("queryTxsByAssetUid", "{\"assetUid\":\"asset-057\"}");
        // sendHttp("queryTxsByAssetUid", "{\"assetUid\":null}");
        // sendHttp("queryTxsByAssetUid", "{\"assetUid\":\"\"}");
        DateUtil.sleep(2500);
    }

    /**
     * 3.6.2 根据资产ID和交易类型查询交易
     */
    @Test
    public void queryTxDetailsByAssetUIDTxType() {
        sendHttp("queryTxDetailsByAssetUidTxType",
                "{\"assetUid\":\"asset-057\"," + "    \"txType\":\"LOAN_RESULT_CONFIRM\"}");
        DateUtil.sleep(2500);
    }

    /**
     * 3.6.3 查询60天内所有未上传质押文件或质押文件未被确认的资产
     */
    @Test
    public void queryAssetsWithNoMortgageDoc() {
        sendHttp("queryAssetsWithNoMortgageDoc");
        DateUtil.sleep(2500);
    }

    /**
     * 3.6.4 查询给定时间范围内的所有已完成差额划拨资产
     */
    @Test
    public void queryAssetsWithDiffCnfrmByTimeSpan() {
        sendHttp("queryAssetsWithDiffCnfrmByTimeSpan",
                "{\"timeStart\":\"20170523230000\",\"timeEnd\":\"2017052230100\"}");
        DateUtil.sleep(2500);
    }

    /**
     * 3.6.5 根据区块高度查询区块内所有交易
     */
    @Test
    public void queryTxDetailsByBlockHeight() {
        sendHttp("queryTxDetailsByBlockHeight",
                "{\"channelId\":\"TestChannel1\",\"blockHeight\":3864}");
    }

    /**
     * 3.6.6 根据区块哈希查询区块内所有交易
     */
     @Test
    public void queryTxDetailsByBlockHash() {
        sendHttp("queryTxDetailsByBlockHash",
                "{\"blockHash\":\"7376045C25A09F1F6EB04DABEC4B14E60787F9CFC9A2D963405F0104D726C725\"}");
    }

    /**
     * 3.6.7 根据交易ID查询交易
     */
     @Test
    public void queryTxDetailsByTxID() {
        sendHttp("queryTxDetailsByTxID",
                "{\"txId\":\"ed9d5dcb308b188e9c1987207de6ad6d83d7fb35d5f53506a255ffc35bcd31ea\""
                        + "}");
    }

    /**
     * 3.6.8 查询channel列表
     */
    @Test
    public void queryChannelList() {
        sendHttp("queryChannelList");
    }

    /**
     * 3.6.9 根据channel ID查询channel详细信息
     */
    @Test
    public void queryChannelByChannelID() {
        sendHttp("queryChannelByChannelId", "{\"channelId\":\"TestChannel1\"}");
    }

    /**
     * 3.6.10 根据区块哈希查询区块详细信息
     */
    @Test
    public void queryBlockByBlockHash() {
        sendHttp("queryBlockByBlockHash",
                "{" + "  \"blockHash\":\"A37F539C4290DD120451388BE1BB34A03E482E24C8E6575946D404838831CCEE\""
                        + "}");
    }

    /**
     * 3.6.11 按时间统计区块数量
     */
    @Test
    public void getBlockCount() {
        sendHttp("getBlockCount", "{\"channelId\":\"TestChannel1\",\"timeStart\":\"20170524155028\","
                + "    \"timeEnd\":\"20170524185022\",\"interval\":10}");
    }

    /**
     * 3.6.12 按时间统计交易数量
     */
    @Test
    public void getTxCount() {
        sendHttp("getTxCount", "{\"channelId\":\"TestChannel1\",\"timeStart\":\"20170524155028\","
                + "    \"timeEnd\":\"20170524185022\",\"interval\":10}");
    }

    /**
     * 3.6.13 按时间统计平均区块交易数量
     */
    @Test
    public void getAvgTxPerBlock() {
        sendHttp("getAvgTxPerBlock",
                "{\"channelId\":\"TestChannel1\",\"timeStart\":\"20170524175022\","
                        + "    \"timeEnd\":\"20170524205022\",\"interval\":10}");
    }

    /**
     * 3.6.14 通用查询接口
     */
    @Test
    public void getTxsByComboConditions() {
        String json = "{\"channelID\":\"TestChannel1\",\"orgCode\":\"orgJY\","
                + "    \"assetUid\":\"asset_client_2000036\","
                + "    \"outTradeNo\":\"JYJD20170000000309\","
                + "    \"txID\":\"34bce1138439a75b3140f9af7b770b07bae5f7073cda455b066a1f20f0f55ea3\","
                + "    \"txType\":\"REPAY_RESULT_NOTIFY:1\",\"timeStart\":\"20170524154516\","
                + "    \"timeEnd\":\"20170524155016\"}";
        json = "{\"channelID\":\"TestChannel1\",\"timeStart\":\"20170524154516\","
                + "    \"timeEnd\":\"20170524155016\"}";
        sendHttp("getTxsByComboConditions",json);
    }


}

