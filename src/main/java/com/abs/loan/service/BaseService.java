package com.abs.loan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.bean.AssetPO;
import com.abs.bean.Stage;
import com.abs.bean.TransactionPO;
import com.abs.config.CategoryHelper;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.fabric.FabricPublisher;
import com.abs.loan.bean.RequestData;
import com.abs.loan.dao.MongoDao;
import com.abs.util.SHAHashUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service(value = "baseService")
@Scope("prototype")
public class BaseService {
    protected Logger log = LoggerFactory.getLogger(BaseService.class);
    protected Logger logError = LoggerFactory.getLogger("operation");
    @Autowired
    protected MongoDao mongoDaoImpl;
    @Autowired
    private FabricPublisher fabricPublisher;

    private String serviceType;// 交易类型

    public BaseService() {}

    public BaseService(String serviceType) {
        this.serviceType = serviceType;
    }

    protected String getTxType(RequestData requestData) throws BusinessException {
        return serviceType;
    }


    public String invoke(RequestData requestData) throws BusinessException, SystemException {
        // 校验字段，不通过则 throw e
        checkParameter(requestData);

        // 获取txType
        String txType = getTxType(requestData);

        // 获取businessHash
        String businessHash = getBusinessHash(requestData, txType);

        // 校验businessHash，重复则返回 throw e
        checkBusinessHash(businessHash);

        // 获取当前category
        String currentCategory = getCurrentCategory(requestData, txType);

        // 获取前序交易
        String previousTxID = getPreviousTransactionId(requestData, txType, currentCategory);

        // 检查前序交易status是否为0(正常)
        checkPreviouxTxID(previousTxID);

        // 发交易
        String txID =
                sendTransaction(requestData, businessHash, previousTxID, txType, currentCategory);
        return txID;
    }

    protected String getCurrentCategory(RequestData requestData, String txType)
            throws BusinessException {
        CategoryHelper categoryHelper = CategoryHelper.getInstance();
        String category = categoryHelper.getCategory(txType);
        return category;
    }

    protected String getPreviousCategory(RequestData requestData, String txType)
            throws BusinessException {
        CategoryHelper categoryHelper = CategoryHelper.getInstance();
        String category = categoryHelper.getPreviousCategoryExceptSelf(txType);
        return category;
    }

    /**
     * 获取前序交易 1、查询asset：从previousCategory中肯定能查出来，如果无数据则参数错误
     * 2、遍历asset中的stages，获取真正的前序category：查看currentCategory是否存在，若存在则取它所在的txId的值。
     * 若不存在则取previousCategory所在的txId。
     * 
     * @param requestData
     * @param txType
     * @param currentCategory 当前流程所在的category
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    protected String getPreviousTransactionId(RequestData requestData, String txType,
            String currentCategory) throws BusinessException, SystemException {
        String previousTxID = "";
        try {
            String previousCategory = getPreviousCategory(requestData, txType);
            AssetPO asset = mongoDaoImpl.findAssetByAssetUIDCategory(requestData.getAssetUid(),
                    previousCategory);
            if (asset == null) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ASSETUID_NOT_FOUND);
            }
            for (Stage stage : asset.getStages()) {
                String category = stage.getCategory();
                if (currentCategory.equals(category)) {
                    previousTxID = stage.getLastestStage();
                    break;
                }
                if (previousCategory.equals(category)) {
                    previousTxID = stage.getLastestStage();
                }
            }
            if (previousTxID == null || previousTxID.length() == 0) {
                throw new BusinessException(ExceptionEnum.TX_BUSINESS_FLOW_ERROR);
            }
        } catch (BusinessException e) {
            log.error("前序交易查询失败,{}", e.getErrorMsg());
            throw e;
        } catch (Exception e) {
            log.error("前序交易查询失败", e);
            throw new SystemException(ExceptionEnum.DATABASE_ERROR);
        }
        log.info("前序交易查询成功previousTxID:{}", previousTxID);
        return previousTxID;
    }

    private void checkPreviouxTxID(String previousTxId) throws BusinessException, SystemException {
        if ("".equals(previousTxId)) {
            return;
        }
        TransactionPO tx = null;
        try {
            tx = mongoDaoImpl.findTxByTxID(previousTxId);
        } catch (Exception e) {
            logError.error("根据previousTxId查找tx失败", e);
            throw new SystemException(ExceptionEnum.DATABASE_ERROR);
        }
        if (tx == null) {
            // 此处什么错误
            throw new BusinessException(ExceptionEnum.TX_BUSINESS_FLOW_ERROR);
        }
        if (tx.getTxStatus() != 0) {
            // 此处什么错误
            throw new BusinessException(ExceptionEnum.TX_BUSINESS_FLOW_ERROR);
        }
    }

    /**
     * 对当前交易生成唯一hash 计算businessHash时，不包括 请求发起时间 timestamp
     * 
     * @throws BusinessException
     */
    private String getBusinessHash(RequestData requestData, String transactionType)
            throws BusinessException {
        String businessHash = "";
        try {
            JSONObject jsonBiz = (JSONObject) JSON.parse(requestData.getBizContent());
            businessHash = SHAHashUtil.encryptSHA256(
                    transactionType + requestData.getAssetUid() + requestData.getOrgCode()
                            + requestData.getOutTradeNo() + jsonBiz.toJSONString());
            log.info("生成businessHash:{}", businessHash);
        } catch (Exception e) {
            logError.error("生成businessHash失败", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }
        return businessHash;
    }

    /**
     * 检查 BusinessHash是否重合，若有则返回错误 通过交易表查询该businessHash对应的交易是否存在；
     * 
     * @throws BusinessException,SystemException
     */
    private void checkBusinessHash(String businessHash) throws BusinessException, SystemException {
        List<TransactionPO> tx = null;
        try {
            tx = mongoDaoImpl.findTxByBusinessHash(businessHash);
        } catch (Exception e) {
            logError.error("根据businessHash查找tx失败", e);
            throw new SystemException(ExceptionEnum.DATABASE_ERROR);
        }
        if (tx == null || tx.size() == 0) {
            // pass, nothing todo
            log.info("数据库无此businessHash：{}，校验通过", businessHash);
        } else {
            logError.error("数据库有此businessHash：{}，校验不通过", businessHash);
            throw new BusinessException(ExceptionEnum.TX_DUPLICATE_ERROR);
        }
    }

    /**
     * @param requestData
     * @param businessHash
     * @param previousTxID
     * @param txType
     * @param currentCategory
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    private String sendTransaction(RequestData requestData, String businessHash,
            String previousTxID, String txType, String currentCategory)
            throws SystemException, BusinessException {

        if (txType.indexOf(":") > -1) {
            txType = txType.split(":")[0] + ":" + currentCategory.split(":")[1];
        }
        log.info(
                "sendTransaction请求参数： businessHash:{}, previousTxID:{}, txType:{}, currentCategory:{}",
                businessHash, previousTxID, txType, currentCategory);
        String txID = "";
        // 发交易
        txID = fabricPublisher.sendTransaction(txType, requestData.getOrgCode(),
                requestData.getAssetUid(), requestData.getOutTradeNo(), currentCategory,
                previousTxID, businessHash, requestData.getBizContent());
        return txID;
    }

    /**
     * 请求数据校验 校验请求数据中除bizcontent之外的其他字段； 后续需要在具体接口方法中实现对bizcontent中的字段校验，比如分期还款中的期数等。
     * 
     * @param requestData
     */
    private void checkParameter(RequestData requestData) throws BusinessException {
        // null check
        String assetUid = requestData.getAssetUid();
        String outTrandeNo = requestData.getOutTradeNo();
        String timestamp = requestData.getTimestamp();
        String orgCode = requestData.getOrgCode();
        if (assetUid == null || assetUid.length() == 0 || outTrandeNo == null
                || outTrandeNo.length() == 0 || timestamp == null || timestamp.length() == 0
                || orgCode == null || orgCode.length() == 0) {
            logError.error("请求数据字段为空，校验不通过");
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
//        if (assetUid.length() > 40 || assetUid.length() < 2) {
//            logError.error(ExceptionEnum.REQUEST_PARAMETER_ASSETUID_LENGTH_ERROR.getErrorMsg());
//            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ASSETUID_LENGTH_ERROR);
//        }
        if (outTrandeNo.length() > 40) {
            logError.error(ExceptionEnum.REQUEST_PARAMETER_OUTTRADENO_LENGTH_ERROR.getErrorMsg());
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_OUTTRADENO_LENGTH_ERROR);
        }
        if(timestamp.length() > 20) {
            logError.error(ExceptionEnum.REQUEST_PARAMETER_OUTTRADENO_LENGTH_ERROR.getErrorMsg());
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_OUTTRADENO_LENGTH_ERROR);
        }
        if(orgCode.length() > 20 || orgCode.length() < 2) {
            logError.error(ExceptionEnum.REQUEST_PARAMETER_ORGCODE_LENGTH_ERROR.getErrorMsg());
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ORGCODE_LENGTH_ERROR);
        }
        log.info("请求数据基础校验通过");
    }
}

