package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abs.loan.bean.QueryBlockByBlockHash;
import com.abs.loan.bean.GetAvgTxPerBlock;
import com.abs.loan.bean.GetBlockCount;
import com.abs.loan.bean.QueryChannelByChannelId;
import com.abs.loan.bean.QueryChannelList;
import com.abs.loan.bean.QueryTxDetails;
import com.abs.loan.bean.GetTxCount;
import com.abs.loan.bean.QueryAssetsWithDiffCnfrmByTimeSpan;
import com.abs.loan.bean.QueryAssetsWithNoMortgageDoc;
import com.abs.loan.service.QueryLoanService;
import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mongodb.xml"})
public class ServiceTest {

    @Autowired
    private QueryLoanService queryLoanServiceImpl;

    @Test
    public void queryAssetsWithDiffCnfrmByTimeSpan() throws Exception {
        String 
        json = "{\"timeStart\":\"20170523142245\","
                + "    \"timeEnd\":\"20170523152645\"}";
        QueryAssetsWithDiffCnfrmByTimeSpan q = queryLoanServiceImpl.queryAssetsWithDiffCnfrmByTimeSpan(json);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void queryAssetsWithNoMortgageDoc() throws Exception {
        QueryAssetsWithNoMortgageDoc q = queryLoanServiceImpl.queryAssetsWithNoMortgageDoc();
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void getTxsByComboConditions() throws Exception {
        String json = "{\"channelID\":\"TestChannel1\",\"orgCode\":\"orgJY\","
                + "    \"assetUid\":\"asset_client_2000036\","
                + "    \"outTradeNo\":\"JYJD20170000000309\","
                + "    \"txID\":\"34bce1138439a75b3140f9af7b770b07bae5f7073cda455b066a1f20f0f55ea3\","
                + "    \"txType\":\"REPAY_RESULT_NOTIFY:1\",\"timeStart\":\"20170523142245\","
                + "    \"timeEnd\":\"20170523152645\"}";
        json = "{\"channelID\":\"TestChannel1\",\"timeStart\":\"20170523142245\","
                + "    \"timeEnd\":\"20170523152645\"}";
        QueryTxDetails q = queryLoanServiceImpl.getTxsByComboConditions(json);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void getAvgTxPerBlock() throws Exception {
        String json = "{\"channelId\":\"TestChannel1\",\"timeStart\":\"20170523142245\","
                + "    \"timeEnd\":\"20170523152645\",\"interval\":4}";
        GetAvgTxPerBlock q = queryLoanServiceImpl.getAvgTxPerBlock(json);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void queryTxCount() throws Exception {
        String json = "{\"channelId\":\"TestChannel1\",\"timeStart\":\"20170523142245\","
                + "    \"timeEnd\":\"20170523152645\",\"interval\":4}";
        GetTxCount q = queryLoanServiceImpl.getTxCount(json);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void queryBlockCount() throws Exception {
        String json = "{\"channelId\":\"TestChannel1\",\"timeStart\":\"20170523142245\","
                + "    \"timeEnd\":\"20170523152645\",\"interval\":8}";
        GetBlockCount q = queryLoanServiceImpl.getBlockCount(json);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void queryBlockByBlockHash() throws Exception {
        String json = "{blockHash:\"86EE40CCFA4D73DC51E130F0795DDD4ED4B0A3203164282C03A05F06408FAB94\"}";
        QueryBlockByBlockHash q = queryLoanServiceImpl.queryBlockByBlockHash(json);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void queryChannelByChannelId() throws Exception {
        String channelId = "{channelId:\"TestChannel1\"}";
        QueryChannelByChannelId q = queryLoanServiceImpl.queryChannelByChannelId(channelId);
        System.out.println(JSON.toJSONString(q));
    }
    @Test
    public void queryChannelList() throws Exception {
        QueryChannelList q = queryLoanServiceImpl.queryChannelList();
        System.out.println(JSON.toJSONString(q));
    }
}

