package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.3.2差额划拨结果确认 (/loan/diffResultConfirm)
 */
@Service(value = "diffResultConfirmService")
@Scope("prototype")
public class DiffResultConfirmService extends BaseService {
    public static final String DIFF_RESULT_CONFIRM = "01";
    public static final String DIFF_RESULT_REJECT = "02";

    public final static String TX_TYPE_PASS = "DIFF_RESULT_CONFIRM";
    public final static String TX_TYPE_REJECT = "DIFF_RESULT_REJECT";
    @Override
    public String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String confirmResult = jsonObj.getString("confirmResult");
            if (DIFF_RESULT_CONFIRM.equals(confirmResult)) {
                // 确认
                txType = TX_TYPE_PASS;
            } else if (DIFF_RESULT_REJECT.equals(confirmResult)) {
                txType = TX_TYPE_REJECT;
                // 不确认
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

