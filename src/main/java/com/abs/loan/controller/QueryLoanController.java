package com.abs.loan.controller;

public interface QueryLoanController {
    /**
     * 3.6.1 根据资产ID查询交易概要
     *
     * @param json
     */
    String queryTxsByAssetUID(String json);

    /**
     * 3.6.2 根据资产ID和交易类型查询交易
     *
     * @param json
     */
    String queryTxDetailsByAssetUIDTxType(String json);

    /**
     * 3.6.3 查询60天内所有未上传质押文件或质押文件未被确认的资产
     *
     * @param json
     */
    String queryAssetsWithNoMortgageDoc();

    /**
     * 3.6.4 查询给定时间范围内的所有已完成差额划拨资产
     *
     * @param json
     */
    String queryAssetsWithDiffCnfrmByTimeSpan(String json);

    /**
     * 3.6.5 根据区块高度查询区块内所有交易
     *
     * @param json
     */
    String queryTxDetailsByBlockHeight(String json);

    /**
     * 3.6.6 根据区块哈希查询区块内所有交易
     *
     * @param json
     */
    String queryTxDetailsByBlockHash(String json);

    /**
     * 3.6.7 根据交易ID查询交易
     *
     * @param json
     */
    String queryTxDetailsByTxID(String json);

    /**
     * 3.6.8 查询channel列表
     *
     * @param json
     */
    String queryChannelList();

    /**
     * 3.6.9 根据channel ID查询channel详细信息
     *
     * @param json
     */
    String queryChannelByChannelID(String json);

    /**
     * 3.6.10 根据区块哈希查询区块详细信息
     *
     * @param json
     */
    String queryBlockByBlockHash(String json);

    /**
     * 3.6.11 按时间统计区块数量
     *
     * @param json
     */
    String getBlockCount(String json);

    /**
     * 3.6.12 按时间统计交易数量
     *
     * @param json
     */
    String getTxCount(String json);

    /**
     * 3.6.13 按时间统计平均区块交易数量
     *
     * @param json
     */
    String getAvgTxPerBlock(String json);

    /**
     * 3.6.14 通用查询接口
     *
     * @param json
     */
    String getTxsByComboConditions(String json);
    
    String findMaxBlockHeight();
}

