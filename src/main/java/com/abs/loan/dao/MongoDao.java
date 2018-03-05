package com.abs.loan.dao;

import java.util.List;

import com.abs.bean.AssetPO;
import com.abs.bean.BlockPO;
import com.abs.bean.ChannelPO;
import com.abs.bean.Stage;
import com.abs.bean.TransactionPO;
import com.abs.loan.bean.GetTxsByComboConditionsReq;
import com.abs.loan.bean.QueryBlockByBlockHash.BlockDetail;
import com.abs.loan.bean.QueryTxDetails.TxDetail;
import com.abs.loan.bean.QueryTxsByAssetUID.TxSummary;
import com.mongodb.WriteResult;

public interface MongoDao {

    /* channel */
    void addChannel(ChannelPO c);
    ChannelPO findChannelById(String id);
    
    /* block */
    void addBlock(BlockPO b);

    void addBlockList(List<BlockPO> b);


    /* transaction */
    void addTransaction(TransactionPO t);

    void addTransactionList(List<TransactionPO> t);

    List<TransactionPO> findTxByBusinessHash(String businessHash);

    List<TransactionPO> findTxByAssetUID(String assetUID);

    TransactionPO findTxByTxID(String txID);

    /* asset */
    void addAsset(AssetPO asset);

    void addAssetList(List<AssetPO> assets);


    AssetPO findAssetByAssetUid(String assetUID);

    AssetPO findAssetByAssetUIDCategorys(String assetUID, Object[] category);

    AssetPO findAssetByAssetUIDCategory(String assetUID, String category);

    WriteResult updateAssetStagesByAssetUId(String assetUID, List<Stage> stages);

    /* query */
    Long findMaxBlockHeight();

    List<TxSummary> findTxsByAssetUID(String assetUID);

    List<TxDetail> findTxByAssetUIDTxType(String assetUID, String txType);
    List<TxDetail> findTxDetailsByBlockHeight(String channelID, long blockHeight);
    List<TxDetail> findTxDetailsByBlockHash(String blockHash);
    TxDetail findTxDetailsByTxID(String txID);
    List<AssetPO> findAssetsWithDiffCnfrmByTimeSpan(String timeStart, String timeEnd);
    List<AssetPO>  findAssetsWithNoMortgageDoc();
    List<AssetPO>  findAssetsWithNoConfirmMortgageDoc();
    List<ChannelPO> findChannelList();
    Long findBlockCountByChannelId(String channelID);
    List<BlockPO> findBlockByChannelId(String channelID);
    List<BlockPO> findBlockCount(String channelID,String timeStart, String timeEnd);
    BlockDetail findBlockByBlockHash(String blockHash);
    List<TransactionPO> findTxCount(String channelID,String timeStart, String timeEnd);
    List<TransactionPO> findTxByBlockHash(List<String> blockHash,String timeStart, String timeEnd);
    List<TxDetail> findTxsByComboConditions(GetTxsByComboConditionsReq req);
    Long findTxsByComboConditionsCounts(GetTxsByComboConditionsReq req);
    
}

