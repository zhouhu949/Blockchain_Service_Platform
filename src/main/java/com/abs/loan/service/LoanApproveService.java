package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.loan.bean.RequestData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 3.1.2 贷款审批
 */
@Service(value = "loanApproveService")
@Scope("prototype")
public class LoanApproveService extends BaseService {
    public static final String LOAN_APPROVE = "01";
    public static final String LOAN_REJECT = "02";

    public final static String TX_TYPE_PASS = "LOAN_APPROVE";
    public final static String TX_TYPE_REJECT = "LOAN_REJECT";
    @Override
    public String getTxType(RequestData requestData) throws BusinessException {
        String txType = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            String approveResult = jsonObj.getString("approveResult");
            if (LOAN_APPROVE.equals(approveResult)) {
                // 审批通过
                txType = TX_TYPE_PASS;
            } else if (LOAN_REJECT.equals(approveResult)) {
                // 审批拒绝
                txType = TX_TYPE_REJECT;
            } else {
                logError.error("审批参数（approveResult）非法");
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_APPROVAL_RESULT_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        return txType;
    }
}

