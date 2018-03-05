package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.1.4放款结果确认 (/loan/loanResultConfirm)
 */
@Service(value = "loanResultConfirmService")
@Scope("prototype")
public class LoanResultConfirmService extends BaseService {
    public static final String LOAN_RESULT_CONFIRM = "01";
    public static final String LOAN_RESULT_REJECT = "02";

    public final static String TX_TYPE_PASS = "LOAN_RESULT_CONFIRM";
    public final static String TX_TYPE_REJECT = "LOAN_RESULT_REJECT";

    @Override
    public String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String confirmResult = jsonObj.getString("confirmResult");
            if (LOAN_RESULT_CONFIRM.equals(confirmResult)) {
                // 确认
                txType = TX_TYPE_PASS;
            } else if (LOAN_RESULT_REJECT.equals(confirmResult)) {
                // 不确认
                txType = TX_TYPE_REJECT;
            } else {
                logError.error("放款结果确认confirmResult字段非法");
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

