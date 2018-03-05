package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.4.2还款计划确认(/loan/repayPlanConfirm)
 */
@Service(value = "repayPlanConfirmService")
@Scope("prototype")
public class RepayPlanConfirmService extends BaseService {
    public static final String REPAY_PLAN_CONFIRM = "01";
    public static final String REPAY_PLAN_REJECT = "02";

    public final static String TX_TYPE_PASS = "REPAY_PLAN_CONFIRM";
    public final static String TX_TYPE_REJECT = "REPAY_PLAN_REJECT";
    @Override
    public String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String confirmResult = jsonObj.getString("confirmResult");
            if (REPAY_PLAN_CONFIRM.equals(confirmResult)) {
                // 确认
                txType = TX_TYPE_PASS;
            } else if (REPAY_PLAN_REJECT.equals(confirmResult)) {
                // 不确认
                txType = TX_TYPE_REJECT;
            } else {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_CONFIRM_RESULT_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        return txType;
    }
}

