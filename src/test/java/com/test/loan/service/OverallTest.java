package com.test.loan.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abs.util.DateUtil;
import com.abs.util.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OverallTest {
    private static final Logger logger = LoggerFactory.getLogger("test");

    private static final String BS_URL_BASE_PATH_JD =
            "http://9.186.56.164:8080/Blockchain_Service_Platform/loan/";
    private static final String BS_URL_BASE_PATH_JY =
            "http://9.186.56.35:8080/Blockchain_Service_Platform/loan/";

    private static final String ORG_CODE_JD = "orgJD";
    private static final String ORG_CODE_JY = "orgJY";

    private static final String ORG_CODE_JD_OLD = "cloudFactory";
    private static final String ORG_CODE_JY_OLD = "jyzb";

    private static final String ASSET_UID_OLD = "orgJY0000000001Z201704250001";

    private static final String KEY_URL = "url";
    private static final String KEY_OPERATOR = "operator";
    private static final String KEY_REQUEST_DATA = "requestData";

    /** 发送交易时间间隔，以秒为单位 */
    private static final int SEND_REQUEST_INTERVAL = 5;

    /**
     * 正常流程测试 其中，回购原因为01，链接至还款
     * 
     * @throws IOException
     * 
     */
    @Test
    public void testPassNormal() throws IOException {
        String testDataFile = "test_pass_normal.json";
        String assetUID = "asset-068";
        sendTestData(testDataFile, assetUID);
    }

    @Test
    public void testPassNormalThread() throws IOException {
        final String testDataFile = "test_pass_normal.json";
        String assetUID = "asset-client-";
        for (int i = 0; i < 4; i++) {
            final String UID = assetUID + i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        OverallTest o = new OverallTest();
                        System.out.println(testDataFile + UID);
                        o.sendTestData(testDataFile, UID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        DateUtil.sleep(1000 * 60 * 60 * 24);
    }

    /**
     * 拒绝重试流程测试 其中，回购原因为02，链接至质押文件上传
     * 
     * @throws IOException
     * 
     */
    @Test
    public void testPassRetry() throws IOException {
        String testDataFile = "test_pass_retry.json";
        String assetUID = "asset-024";
        sendTestData(testDataFile, assetUID);
    }

    /**
     * 回购流程测试1 其中，回购原因为01，链接至还款计划上传
     * 
     * @throws IOException
     * 
     */
    @Test
    public void testPassBuyback1() throws IOException {
        String testDataFile = "test_pass_buyback_1.json";
        String assetUID = "asset-031";
        sendTestData(testDataFile, assetUID);
    }

    /**
     * 回购流程测试2 其中，回购原因为01，链接至贷款结果确认
     * 
     * @throws IOException
     * 
     */
    @Test
    public void testPassBuyback2() throws IOException {
        String testDataFile = "test_pass_buyback_2.json";
        String assetUID = "asset-041";
        sendTestData(testDataFile, assetUID);
    }

    /**
     * 回购流程测试3 其中，回购原因为02，链接至贷款结果确认
     * 
     * @throws IOException
     * 
     */
    @Test
    public void testPassBuyback3() throws IOException {
        String testDataFile = "test_pass_buyback_3.json";
        String assetUID = "asset-042";
        sendTestData(testDataFile, assetUID);
    }

    /**
     * 疲劳测试
     * 
     * @throws IOException
     */
    @Test
    public void testFatigue() throws IOException {
        // 测试数据每个assetUID发送20个交易，间隔设置为15s，共计5分钟发完
        // 48小时需要发送576个asset
        String testDataFile = "test_pass_normal.json";
        int maxAssetIndex = 1000;
        String assetUIDFormat = "00000000";
        DecimalFormat df = new DecimalFormat(assetUIDFormat);
        for (int i = 0; i < maxAssetIndex; i++) {
            String assetUID = "asset_" + df.format(i + 1);
            sendTestData(testDataFile, assetUID);
        }
    }

    /**
     * 双客户端疲劳测试
     * 
     * @throws IOException
     */
    @Test
    public void testFatigueMulti() throws IOException {
        long start = System.currentTimeMillis();
        // 测试数据每个assetUID发送20个交易，间隔设置为15s，共计5分钟发完
        // 48小时需要发送576个asset
        String testDataFile = "test_pass_normal.json";
        String clientId = "asset_client_800000";
        int maxAssetIndex = 150000;// 12小时左右
        int clientCount = 10;
        int interval = 50;//ms 多客户端发送间隔时间
        for (int i = 0; i < clientCount - 1; i++) {
            int id = i;
            new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < maxAssetIndex; j++) {
                        String assetUID = id + clientId + j;
                        try {
                            sendTestData(testDataFile, assetUID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            DateUtil.sleep(interval);
        }
        for (int i = 0; i < maxAssetIndex; i++) {
            String assetUID = clientCount - 1 + clientId + i;
            sendTestData(testDataFile, assetUID);
            long end = System.currentTimeMillis();
            logger.info("客户端运行时间：{}h", (end - start) / 1000.0 / 60.0 / 60);
        }
    }

    /**
     * 向区块链服务系统发送请求
     * 
     * @param url
     * @param operator
     * @param req
     * @return
     */
    private String sendHttpPostToBS(String url, String operator, String req) {
        HttpClientUtil httpClient = HttpClientUtil.getInstance();
        String resp;
        switch (operator) {
            case ORG_CODE_JD:
                resp = httpClient.sendHttpJsonPost(BS_URL_BASE_PATH_JD + url, req);
                break;
            case ORG_CODE_JY:
                resp = httpClient.sendHttpJsonPost(BS_URL_BASE_PATH_JY + url, req);
                break;
            default:
                System.out.println("操作者未定义");
                resp = null;
        }
        return resp;
    }

    /**
     * 给定文件名，读取测试数据
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    private String readTestData(String fileName) throws IOException {
        String fileBasePath = "src/test/resource/";
        File file = new File(fileBasePath + fileName);
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        br = new BufferedReader(new FileReader(file));
        String tem = "";
        while ((tem = br.readLine()) != null) {
            sb.append(tem);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 读取并发送测试数据
     * 
     * @param fileName
     * @param assetUID
     * @throws IOException
     */
    private void sendTestData(String fileName, String assetUID) throws IOException {
        System.out.println("assetUID: " + assetUID);
        String testDataJSONStr = readTestData(fileName);

        testDataJSONStr = testDataJSONStr.replaceAll(ORG_CODE_JD_OLD, ORG_CODE_JD);
        testDataJSONStr = testDataJSONStr.replaceAll(ORG_CODE_JY_OLD, ORG_CODE_JY);
        testDataJSONStr = testDataJSONStr.replaceAll(ASSET_UID_OLD, assetUID);

        JSONArray testDataJSONArr = JSON.parseArray(testDataJSONStr);

        for (int i = 0; i < testDataJSONArr.size(); i++) {
            JSONObject testDataJSON = testDataJSONArr.getJSONObject(i);
            String url = testDataJSON.getString(KEY_URL);
            String operator = testDataJSON.getString(KEY_OPERATOR);
            String requestData = testDataJSON.getString(KEY_REQUEST_DATA);

            logger.info("测试接口: " + url + "  发送者：" + operator);
            logger.info("测试数据：" + requestData);

            String resp = sendHttpPostToBS(url, operator, requestData);

            logger.info("返回数据：" + resp);

            try {
                TimeUnit.SECONDS.sleep(SEND_REQUEST_INTERVAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
