package com.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abs.bean.AssetPO;
import com.abs.bean.BlockPO;
import com.abs.bean.TransactionPO;
import com.abs.config.CategoryHelper;
import com.abs.loan.bean.QueryTxDetails.TxDetail;
import com.abs.loan.dao.MongoDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mongodb.xml"})
public class MongoDaoTest {
    Logger log = LoggerFactory.getLogger(MongoDaoTest.class);
    Logger logError = LoggerFactory.getLogger("operation");
    @Autowired
    private MongoDao mongoDaoImpl;
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Test
    public void findMaxBlockHeight() {
        Long height = mongoDaoImpl.findMaxBlockHeight();
        System.out.println(height);
    }
    @Test
    public void findAssetsWithNoMortgageDoc() {
        List<AssetPO> list = mongoDaoImpl.findAssetsWithNoMortgageDoc();
        System.out.println(list.size()+"-------------"+JSON.toJSONString(list));
    }

    @Test
    public void findTxCount() {
        List<TransactionPO> list = mongoDaoImpl.findTxCount("TestChannel1","20170523073045","20170523152645");
        System.out.println(list.size()+"-------------"+JSON.toJSONString(list));
    }
    @Test
    public void findTxDetailsByTxID() {
        TxDetail list = mongoDaoImpl.findTxDetailsByTxID(
                "1ce4780da10c63eaab2dc0db4fd2b97d23efb180e2cca8efb6b37a2558e75504");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void queryTxDetailsByBlockHeight() {
        List<TxDetail> list = mongoDaoImpl.findTxDetailsByBlockHeight("TestChannel1", 12);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void queryTxDetailsByBlockHash() {
        List<TxDetail> list = mongoDaoImpl.findTxDetailsByBlockHash(
                "86EE40CCFA4D73DC51E130F0795DDD4ED4B0A3203164282C03A05F06408FAB94");
        System.out.println(JSON.toJSONString(list));

    }

    @Test
    public void queryAssetsWithDiffCnfrmByTimeSpan() {
        String timeStart = "20170522154829";
        String timeEnd = "20170622154829";
        List<?> list =
                mongoDaoImpl.findAssetsWithDiffCnfrmByTimeSpan(timeStart, timeEnd);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void query() {
        CategoryHelper categoryHelper = CategoryHelper.getInstance();
        JSONArray cs = categoryHelper.getPreviousCategories("BUYBACK_APPLY");

        for (int i = 0; i < cs.size(); i++) {
            System.out.println(cs.get(i).equals("CAT_REPAY:t"));
        }
        Object[] o = cs.toArray();
        AssetPO asset = mongoDaoImpl.findAssetByAssetUIDCategorys("assetUid", o);
        if (asset != null) {
            System.out.println(asset.getAssetUid());
        } else {
            System.out.println(asset + "null+++++++++++");
        }
    }

    @Test
    public void findTxByTxID() {
        TransactionPO tx = mongoDaoImpl
                .findTxByTxID("5a3bf3c966c39667925c2698fb8426f6f6a835cda5798bf31f754d32bf1b4088");
        System.out.println(tx);
    }

    @Test
    public void addAsset() {
        AssetPO a = new AssetPO();
        a.setAssetUid("ss");
        mongoDaoImpl.addAsset(a);
    }



    @Test
    public void testAddDoc() {
        log.debug("进入接口test");
        log.info("进入接口test");
        log.warn("进入接口test");
        log.error("进入接口test");
        logError.debug("进入接口test1");
        logError.info("进入接口test1");
        logError.warn("进入接口test1");
        logError.error("进入接口test1");

        List<BlockPO> bpos = new ArrayList<BlockPO>();

        BlockPO bpo = new BlockPO();
        bpo.setBlockHash("blockhash+");

        this.mongoTemplate.insert(bpo);// 默认保存在person集合中(与类名称一致)
        this.mongoTemplate.insert(bpo, "block");// 指定保存在person2集合中

        bpos.add(bpo);
        bpos.add(bpo);
        bpos.add(bpo);
        this.mongoTemplate.insert(bpos, "block");// 指定保存在person2集合中

        // mongoTemplate.insert(list, collectionName);//指定保存的集合
        // mongoTemplate.insert(list, Person.class);// 默认保存在person集合中(与类名称一致)
    }


}

