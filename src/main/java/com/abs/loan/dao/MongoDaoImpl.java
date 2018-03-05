package com.abs.loan.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.abs.bean.AssetPO;
import com.abs.bean.BlockPO;
import com.abs.bean.ChannelPO;
import com.abs.bean.Stage;
import com.abs.bean.TransactionPO;
import com.abs.config.CategoryHelper;
import com.abs.loan.bean.GetTxsByComboConditionsReq;
import com.abs.loan.bean.QueryBlockByBlockHash.BlockDetail;
import com.abs.loan.bean.QueryTxDetails.TxDetail;
import com.abs.loan.bean.QueryTxsByAssetUID.TxSummary;
import com.abs.loan.service.DiffResultConfirmService;
import com.abs.loan.service.MortgageDocConfirmService;
import com.abs.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Service(value = "mongoDaoImpl")
public class MongoDaoImpl implements MongoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final static int SUCCESS_STATUS = 0;
    private final static int QUERY_ASSETS_WITH_NO_MORTGAGE_DOC_LIMIT_DAY = 60;
    private final static String TIME_FORMAT = "yyyyMMddHHmmss";

    @Override
    public void addChannel(ChannelPO c) {
        Query q = new Query();
        q.addCriteria(Criteria.where("channelID").is(c.getChannelId()));
        Update u = new Update();
        u.set("channelID", c.getChannelId());
        mongoTemplate.upsert(q, u, ChannelPO.class);
    }

    @Override
    public ChannelPO findChannelById(String id) {
        Query q = new Query();
        q.addCriteria(Criteria.where("channelID").is(id));
        return mongoTemplate.findOne(q, ChannelPO.class);
    }


    @Override
    public void addBlock(BlockPO b) {
        mongoTemplate.insert(b);
    }

    @Override
    public void addBlockList(List<BlockPO> b) {
        mongoTemplate.insert(b, BlockPO.class);
    }

    @Override
    public void addTransaction(TransactionPO t) {
        mongoTemplate.insert(t);
    }

    @Override
    public void addTransactionList(List<TransactionPO> t) {
        // String collectionName = mongoTemplate.getCollectionName(TransactionPO.class);
        // mongoTemplate.insert(t, "transaction");
        mongoTemplate.insert(t, TransactionPO.class);
    }

    @Override
    public void addAsset(AssetPO asset) {
        mongoTemplate.insert(asset);

    }

    @Override
    public void addAssetList(List<AssetPO> assets) {
        mongoTemplate.insert(assets, "assets");
    }


    @Override
    public List<TransactionPO> findTxByBusinessHash(String businessHash) {

        Query q = new Query(Criteria.where("businessHash").is(businessHash));
        return mongoTemplate.find(q, TransactionPO.class);
    }

    @Override
    public List<TransactionPO> findTxByAssetUID(String assetUID) {

        Query q = new Query(Criteria.where("assetUID").is(assetUID));

        return mongoTemplate.find(q, TransactionPO.class);
    }

    @Override
    public TransactionPO findTxByTxID(String txID) {

        Query q = new Query(Criteria.where("txID").is(txID));
        q.addCriteria(Criteria.where("txStatus").is(SUCCESS_STATUS));
        return mongoTemplate.findOne(q, TransactionPO.class);

    }

    @Override
    public AssetPO findAssetByAssetUid(String assetUID) {
        Query q = new Query(Criteria.where("assetUID").is(assetUID));
        return mongoTemplate.findOne(q, AssetPO.class);
    }

    @Override
    public AssetPO findAssetByAssetUIDCategory(String assetUID, String category) {
        Query q = new Query(Criteria.where("stages.category").in(category, category));
        q.addCriteria(Criteria.where("assetUID").is(assetUID));
        return mongoTemplate.findOne(q, AssetPO.class);
    }

    @Override
    public AssetPO findAssetByAssetUIDCategorys(String assetUID, Object[] category) {
        Query q = new Query(Criteria.where("stages.category").in(category));
        q.addCriteria(Criteria.where("assetUID").is(assetUID));
        return mongoTemplate.findOne(q, AssetPO.class);
    }

    @Override
    public WriteResult updateAssetStagesByAssetUId(String assetUID, List<Stage> stages) {
        Query q = new Query();
        q.addCriteria(Criteria.where("assetUID").is(assetUID));
        Update update = Update.update("stages", stages);
        return mongoTemplate.updateFirst(q, update, AssetPO.class);
    }



    @Override
    public List<TxSummary> findTxsByAssetUID(String assetUID) {
        Query q = new Query(Criteria.where("assetUID").is(assetUID));
        return mongoTemplate.find(q, TxSummary.class, TransactionPO.COLLECTION_NAME);
    }

    @Override
    public Long findMaxBlockHeight() {
        Query q = new Query();
        q.with(new Sort(Direction.DESC, "blockHeight")).limit(1);
        BlockPO block = mongoTemplate.findOne(q, BlockPO.class);
        return block == null ? -1 : block.getBlockHeight();
    }

    @Override
    public List<TxDetail> findTxByAssetUIDTxType(String assetUID, String txType) {
        Query q = new Query();
        q.addCriteria(Criteria.where("assetUID").is(assetUID));
        q.addCriteria(Criteria.where("txType").is(txType));
        return mongoTemplate.find(q, TxDetail.class, TransactionPO.COLLECTION_NAME);
    }

    @Override
    public List<AssetPO> findAssetsWithNoMortgageDoc() {
        String startTime = DateUtil.format(
                System.currentTimeMillis()
                        - QUERY_ASSETS_WITH_NO_MORTGAGE_DOC_LIMIT_DAY * 24 * 60 * 60 * 1000,
                TIME_FORMAT);
        // category = CAT_MORT
        String category =
                CategoryHelper.getInstance().getCategory(MortgageDocConfirmService.TX_TYPE_PASS);
        Query q = new Query();
        q.addCriteria(Criteria.where("stages.category").ne(category));
        q.addCriteria(Criteria.where("stages.txTime").gt(startTime));

        return mongoTemplate.find(q, AssetPO.class);
    }

    @Override
    public List<AssetPO> findAssetsWithNoConfirmMortgageDoc() {
        String startTime = DateUtil.format(
                System.currentTimeMillis()
                        - QUERY_ASSETS_WITH_NO_MORTGAGE_DOC_LIMIT_DAY * 24 * 60 * 60 * 1000,
                TIME_FORMAT);
        // category = CAT_MORT stageType != MORTGAGE_DOC_CONFIRM
        String category =
                CategoryHelper.getInstance().getCategory(MortgageDocConfirmService.TX_TYPE_PASS);
        Query q = new Query();
        q.addCriteria(Criteria.where("stages.category").is(category));
        q.addCriteria(
                Criteria.where("stages.stageType").ne(MortgageDocConfirmService.TX_TYPE_PASS));
        q.addCriteria(Criteria.where("stages.txTime").gt(startTime));
        return mongoTemplate.find(q, AssetPO.class);
    }

    @Override
    public List<AssetPO> findAssetsWithDiffCnfrmByTimeSpan(String timeStart, String timeEnd) {
        Query q = new Query();
        q.addCriteria(Criteria.where("stages.txTime").gte(timeStart).lt(timeEnd));
        q.addCriteria(Criteria.where("stages.stageType").is(DiffResultConfirmService.TX_TYPE_PASS));
        return mongoTemplate.find(q, AssetPO.class, AssetPO.COLLECTION_NAME);
    }

    @Override
    public List<TxDetail> findTxDetailsByBlockHeight(String channelID, long blockHeight) {
        List<TxDetail> txDetails = new ArrayList<TxDetail>();
        DBObject dbObject = new BasicDBObject();
        dbObject.put("channelID", channelID);
        DBObject fieldObject = new BasicDBObject();
        fieldObject.put("blockHash", true);
        Query query = new BasicQuery(dbObject, fieldObject);
        List<BlockPO> blocks = mongoTemplate.find(query, BlockPO.class);
        List<String> blockHashs = new ArrayList<String>();
        for (BlockPO block : blocks) {
            blockHashs.add(block.getBlockHash());
        }
        Query q = new Query();
        q.addCriteria(Criteria.where("blockHash").in(blockHashs));
        q.addCriteria(Criteria.where("blockHeight").is(blockHeight));
        txDetails.addAll(mongoTemplate.find(q, TxDetail.class, TransactionPO.COLLECTION_NAME));
        return txDetails;
    }

    @Override
    public List<TxDetail> findTxDetailsByBlockHash(String blockHash) {
        Query q = new Query();
        q.addCriteria(Criteria.where("blockHash").is(blockHash));
        return mongoTemplate.find(q, TxDetail.class, TransactionPO.COLLECTION_NAME);

    }

    @Override
    public TxDetail findTxDetailsByTxID(String txID) {
        Query q = new Query();
        q.addCriteria(Criteria.where("txID").is(txID));
        return mongoTemplate.findOne(q, TxDetail.class, TransactionPO.COLLECTION_NAME);
    }

    @Override
    public List<ChannelPO> findChannelList() {
        return mongoTemplate.findAll(ChannelPO.class);
    }

    @Override
    public Long findBlockCountByChannelId(String channelID) {
        Query q = new Query();
        q.addCriteria(Criteria.where("channelID").is(channelID));
        return mongoTemplate.count(q, BlockPO.COLLECTION_NAME);
    }

    @Override
    public BlockDetail findBlockByBlockHash(String blockHash) {
        Query q = new Query();
        q.addCriteria(Criteria.where("blockHash").is(blockHash));
        return mongoTemplate.findOne(q, BlockDetail.class, BlockPO.COLLECTION_NAME);
    }

    @Override
    public List<BlockPO> findBlockCount(String channelID, String timeStart, String timeEnd) {
        Query q = new Query();
        q.addCriteria(Criteria.where("channelID").is(channelID));
        q.addCriteria(Criteria.where("blockTime").gte(timeStart).lt(timeEnd));
        return mongoTemplate.find(q, BlockPO.class);
    }

    @Override
    public List<TransactionPO> findTxCount(String channelID, String timeStart, String timeEnd) {
        List<BlockPO> blocks = findBlockByChannelId(channelID);
        List<String> blockHashs = new ArrayList<String>();
        for (BlockPO block : blocks) {
            blockHashs.add(block.getBlockHash());
        }
        Query q = new Query();
        q.addCriteria(Criteria.where("blockHash").in(blockHashs));
        q.addCriteria(Criteria.where("txTime").gte(timeStart).lt(timeEnd));
        return mongoTemplate.find(q, TransactionPO.class);
    }

    @Override
    public List<TransactionPO> findTxByBlockHash(List<String> blockHash, String timeStart,
            String timeEnd) {
        Query q = new Query();
        q.addCriteria(Criteria.where("blockHash").in(blockHash));
        q.addCriteria(Criteria.where("txTime").gte(timeStart).lt(timeEnd));
        return mongoTemplate.find(q, TransactionPO.class);
    }

    @Override
    public Long findTxsByComboConditionsCounts(GetTxsByComboConditionsReq req) {
        Query q = new Query();
        if (req.getOrgCode() != null && req.getOrgCode().length() != 0)
            q.addCriteria(Criteria.where("orgCode").is(req.getOrgCode()));
        if (req.getAssetUid() != null && req.getAssetUid().length() != 0)
            q.addCriteria(Criteria.where("assetUID").is(req.getAssetUid()));
        if (req.getOutTradeNo() != null && req.getOutTradeNo().length() != 0)
            q.addCriteria(Criteria.where("outTradeNo").is(req.getOutTradeNo()));
        if (req.getTxId() != null && req.getTxId().length() != 0)
            q.addCriteria(Criteria.where("txID").is(req.getTxId()));
        if (req.getTxType() != null && req.getTxType().length() != 0)
            q.addCriteria(Criteria.where("txType").is(req.getTxType()));
        if (req.getTimeStart() != null && req.getTimeStart().length() != 0
                && req.getTimeEnd() != null && req.getTimeEnd().length() != 0)
            q.addCriteria(Criteria.where("txTime").gte(req.getTimeStart()).lt(req.getTimeEnd()));
        q.addCriteria(Criteria.where("channelID").is(req.getChannelId()));
        
        return mongoTemplate.count(q, TransactionPO.COLLECTION_NAME);
    }

    @Override
    public List<TxDetail> findTxsByComboConditions(GetTxsByComboConditionsReq req) {
        Query q = new Query();
        if (req.getOrgCode() != null && req.getOrgCode().length() != 0)
            q.addCriteria(Criteria.where("orgCode").is(req.getOrgCode()));
        if (req.getAssetUid() != null && req.getAssetUid().length() != 0)
            q.addCriteria(Criteria.where("assetUID").is(req.getAssetUid()));
        if (req.getOutTradeNo() != null && req.getOutTradeNo().length() != 0)
            q.addCriteria(Criteria.where("outTradeNo").is(req.getOutTradeNo()));
        if (req.getTxId() != null && req.getTxId().length() != 0)
            q.addCriteria(Criteria.where("txID").is(req.getTxId()));
        if (req.getTxType() != null && req.getTxType().length() != 0)
            q.addCriteria(Criteria.where("txType").is(req.getTxType()));
        if (req.getTimeStart() != null && req.getTimeStart().length() != 0
                && req.getTimeEnd() != null && req.getTimeEnd().length() != 0)
            q.addCriteria(Criteria.where("txTime").gte(req.getTimeStart()).lt(req.getTimeEnd()));
        q.addCriteria(Criteria.where("channelID").is(req.getChannelId()));
        return mongoTemplate.find(q, TxDetail.class, TransactionPO.COLLECTION_NAME);
    }

    @Override
    public List<BlockPO> findBlockByChannelId(String channelID) {
        Query q = new Query();
        q.addCriteria(Criteria.where("channelID").is(channelID));
        return mongoTemplate.find(q, BlockPO.class);
    }



    // use mongoTemplate
    // this.mongoTemplate.insert(p);// 默认保存在person集合中(与类名称一致)
    // this.mongoTemplate.insert(p2, "person2");// 指定保存在person2集合中
    // this.mongoTemplate.insertAll(list);// 默认保存在person集合中(与类名称一致)
    // mongoTemplate.insert(list, collectionName);//指定保存的集合
    // mongoTemplate.insert(list, Person.class);// 默认保存在person集合中(与类名称一致)

}

