package com.abs.loan.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.bean.AssetPO;
import com.abs.bean.Stage;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.5.2回购审批 (/loan/buybackApprove)
 */
@Service(value = "buybackApplyService")
@Scope("prototype")
public class BuybackApplyService extends BaseService {

    public static final String TX_TYPE = "BUYBACK_APPLY";

    public BuybackApplyService() {
        super(TX_TYPE);
    }

    /** 01-还款流程不合规 */
    public static final String REPAY_FLOW_ILLEGAL = "01";
    /** 02-质押文件上传流程不合规 */
    public static final String MORTGAGE_DOC_UPLOAD_FLOW_ILLEGAL = "02";

    public static final String CURRENT_CATEGORY = "CAT_BUYBACK";

    public static final String PREVIOUS_CATEGORY_SQU_00 = CURRENT_CATEGORY;
    public static final String PREVIOUS_CATEGORY_SQU_99 = "CAT_LOAN";
    public static final String PREVIOUS_CATEGORY_SQU_11 = "CAT_REPAY:";// CAT_REPAY:1、CAT_REPAY:2...
    public static final String PREVIOUS_CATEGORY_SQU_12 = "CAT_REPAY_PLAN";
    public static final String PREVIOUS_CATEGORY_SQU_21 = "CAT_MORT";


    @Override
    protected String getCurrentCategory(RequestData requestData, String txType)
            throws BusinessException {
        return CURRENT_CATEGORY;
    }

    @Override
    protected String getPreviousTransactionId(RequestData requestData, String txType,
            String currentCategory) throws BusinessException, SystemException {
        String previousTxID = null;
        try {
            List<String> previousCategorys = new LinkedList<String>();
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String buyBackCategory = jsonObj.getString("buybackCategory");
            if (REPAY_FLOW_ILLEGAL.equals(buyBackCategory)) {
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_00);// 不允许重提则此项可以不要
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_11);
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_12);
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_99);
            } else if (MORTGAGE_DOC_UPLOAD_FLOW_ILLEGAL.equals(buyBackCategory)) {
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_00);// 不允许重提则此项可以不要
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_21);
                previousCategorys.add(PREVIOUS_CATEGORY_SQU_99);
            } else {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_BUYBACK_CATEGORY_ERROR);
            }


            AssetPO asset = mongoDaoImpl.findAssetByAssetUid(requestData.getAssetUid());
            if (asset == null) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ASSETUID_NOT_FOUND);
            }
            for (int i = 0; i < previousCategorys.size() && previousTxID == null; i++) {
                String category = previousCategorys.get(i);
                if (PREVIOUS_CATEGORY_SQU_11.equals(category)) {
                    // 遍历stages，获取所有的CAT_REPAY:1、CAT_REPAY:2、CAT_REPAY:3...并找到最大的赋值txid
                    int issue = 0;
                    for (Stage stage : asset.getStages()) {
                        String c = stage.getCategory();
                        if (c.startsWith(PREVIOUS_CATEGORY_SQU_11)) {
                            int issueTem = Integer.parseInt(c.split(":")[1]);
                            if (issueTem > issue) {
                                issue = issueTem;
                                previousTxID = stage.getLastestStage();
                            }
                        }
                    }
                } else {
                    for (Stage stage : asset.getStages()) {
                        if (category.equals(stage.getCategory())) {
                            previousTxID = stage.getLastestStage();
                            break;
                        }
                    }
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
        log.info("前序交易查询成功previousTxID:{}",previousTxID);
        return previousTxID;
    }
}

