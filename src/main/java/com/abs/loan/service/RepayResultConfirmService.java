package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.config.CategoryHelper;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.abs.util.ScriptUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.4.4扣款结果确认 (/loan/repayResultConfirm)
 */
@Service(value = "repayResultConfirmService")
@Scope("prototype")
public class RepayResultConfirmService extends BaseService {

    public static final String CONFIRM_RESULT_CONFIRM = "01";
    public static final String CONFIRM_RESULT_UNCONFIRM = "02";

    public static final String TX_TYPE_NORMAL_CONFIRM = "REPAY_RESULT_CONFIRM:t>0";
    public static final String TX_TYPE_NORMAL_UNCONFIRM = "REPAY_RESULT_UNCONFIRM:t>0";
    public static final String TX_TYPE_ADD_CONFIRM = "ADD_REPAY_RESULT_CONFIRM:t>0";
    public static final String TX_TYPE_ADD_UNCONFIRM = "ADD_REPAY_RESULT_UNCONFIRM:t>0";

    @Override
    protected String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String confirmResult = jsonObj.getString("confirmResult");
            Boolean normal = jsonObj.getBoolean("normal");
            if (normal) {
                // 正常回购
                if (CONFIRM_RESULT_CONFIRM.equals(confirmResult)) {
                    // 确认
                    txType = TX_TYPE_NORMAL_CONFIRM;
                } else if (CONFIRM_RESULT_UNCONFIRM.equals(confirmResult)) {
                    // 不确认
                    txType = TX_TYPE_NORMAL_UNCONFIRM;
                } else {
                    throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_CONFIRM_RESULT_ERROR);
                }
            } else {
                if (CONFIRM_RESULT_CONFIRM.equals(confirmResult)) {
                    txType = TX_TYPE_ADD_CONFIRM;
                } else if (CONFIRM_RESULT_UNCONFIRM.equals(confirmResult)) {
                    txType = TX_TYPE_ADD_UNCONFIRM;
                } else {
                    throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_CONFIRM_RESULT_ERROR);
                }
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("",e);
            throw new BusinessException(ExceptionEnum.SYSTEM_ERROR);
        }
        return txType;
    }

    @Override
    protected String getCurrentCategory(RequestData requestData, String txType)
            throws BusinessException {
        String category = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            Integer currentIssue = jsonObj.getInteger("currentIssue");
            CategoryHelper categoryHelper = CategoryHelper.getInstance();
            String c = categoryHelper.getCategory(txType);
            category = ScriptUtil.getCategory(currentIssue, c);
        } catch (Exception e) {
            logError.error("",e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }
        return category;
    }

    @Override
    protected String getPreviousCategory(RequestData requestData, String txType)
            throws BusinessException {
        return this.getCurrentCategory(requestData, txType);
    }
}

