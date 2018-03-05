package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.5.4回购结果确认 (/loan/buybackResultConfirm)
 */
@Service(value = "buybackResultConfirmService")
@Scope("prototype")
public class BuybackResultConfirmService extends BaseService {
    public static final String BUYBACK_RESULT_CONFIRM = "01";
    public static final String BUYBACK_RESULT_REJECT = "02";

    public final static String TX_TYPE_PASS = "BUYBACK_RESULT_CONFIRM";
    public final static String TX_TYPE_REJECT = "BUYBACK_RESULT_REJECT";
    @Override
    protected String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String confirmResult = jsonObj.getString("confirmResult");
            if (BUYBACK_RESULT_CONFIRM.equals(confirmResult)) {
                // 确认
                txType = TX_TYPE_PASS;
            } else if (BUYBACK_RESULT_REJECT.equals(confirmResult)) {
                // 不确认
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

