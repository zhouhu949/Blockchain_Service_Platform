package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.5.2回购审批 (/loan/buybackApprove)
 */
@Service(value = "buybackApproveService")
@Scope("prototype")
public class BuybackApproveService extends BaseService {
    public final static String BUYBACK_APPROVE = "10";
    public final static String BUYBACK_REJECT = "20";

    public final static String TX_TYPE_PASS = "BUYBACK_APPROVE";
    public final static String TX_TYPE_REJECT = "BUYBACK_REJECT";

    @Override
    protected String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String buybackStatus = jsonObj.getString("buybackStatus");
            if (BUYBACK_APPROVE.equals(buybackStatus)) {
                // 审批通过
                txType = TX_TYPE_PASS;
            } else if (BUYBACK_REJECT.equals(buybackStatus)) {
                // 审批不通过
                txType = TX_TYPE_REJECT;
            } else {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_CONFIRM_RESULT_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("",e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        return txType;
    }
}

