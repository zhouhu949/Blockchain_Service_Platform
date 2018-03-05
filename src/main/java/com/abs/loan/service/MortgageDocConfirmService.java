package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.2.2质押文件确认 (/loan/mortgageDocConfirm)
 */
@Service(value = "mortgageDocConfirmService")
@Scope("prototype")
public class MortgageDocConfirmService extends BaseService {

    public static final String LOAN_RESULT_CONFIRM = "01";
    public static final String LOAN_RESULT_REJECT = "02";

    public final static String TX_TYPE_PASS = "MORTGAGE_DOC_CONFIRM";
    public final static String TX_TYPE_REJECT = "MORTGAGE_DOC_REJECT";
    @Override
    public String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String confirmResult = jsonObj.getString("confirmResult");
            if ("01".equals(confirmResult)) {
                // 确认
                txType = TX_TYPE_PASS;
            } else if ("02".equals(confirmResult)) {
                txType = TX_TYPE_REJECT;
                // 不确认
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

