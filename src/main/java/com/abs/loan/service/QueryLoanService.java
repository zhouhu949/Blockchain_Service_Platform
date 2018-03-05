package com.abs.loan.service;

import com.abs.exception.BusinessException;
import com.abs.exception.SystemException;
import com.abs.loan.bean.GetAvgTxPerBlock;
import com.abs.loan.bean.GetBlockCount;
import com.abs.loan.bean.GetTxCount;
import com.abs.loan.bean.QueryAssetsWithDiffCnfrmByTimeSpan;
import com.abs.loan.bean.QueryAssetsWithNoMortgageDoc;
import com.abs.loan.bean.QueryBlockByBlockHash;
import com.abs.loan.bean.QueryChannelByChannelId;
import com.abs.loan.bean.QueryChannelList;
import com.abs.loan.bean.QueryTxDetail;
import com.abs.loan.bean.QueryTxDetails;
import com.abs.loan.bean.QueryTxsByAssetUID;

public interface QueryLoanService {
    Long findMaxBlockHeight();
    QueryTxsByAssetUID queryTxsByAssetUID(String json) throws BusinessException, SystemException;

    QueryTxDetails queryTxDetailsByAssetUIDTxType(String json)
            throws BusinessException, SystemException;

    QueryAssetsWithNoMortgageDoc queryAssetsWithNoMortgageDoc() throws BusinessException, SystemException;

    QueryAssetsWithDiffCnfrmByTimeSpan queryAssetsWithDiffCnfrmByTimeSpan(String json)
            throws BusinessException, SystemException;

    QueryTxDetails queryTxDetailsByBlockHeight(String json)
            throws BusinessException, SystemException;

    QueryTxDetails queryTxDetailsByBlockHash(String json) throws BusinessException, SystemException;

    QueryTxDetail queryTxDetailsByTxID(String json) throws BusinessException, SystemException;
    QueryTxDetails getTxsByComboConditions(String json) throws BusinessException, SystemException;

    QueryChannelList queryChannelList() throws BusinessException, SystemException;
    QueryChannelByChannelId queryChannelByChannelId(String json) throws BusinessException, SystemException;
    QueryBlockByBlockHash queryBlockByBlockHash(String json) throws BusinessException, SystemException;
    GetBlockCount getBlockCount(String json) throws BusinessException, SystemException;
    GetTxCount getTxCount(String json) throws BusinessException, SystemException;
    GetAvgTxPerBlock getAvgTxPerBlock(String json) throws BusinessException, SystemException;
    
}

