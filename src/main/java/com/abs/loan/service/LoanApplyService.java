package com.abs.loan.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.abs.exception.BusinessException;
import com.abs.exception.SystemException;
import com.abs.loan.bean.RequestData;

/**
 * 3.1.1 贷款申请
 */
@Service(value = "loanApplyService")
@Scope("prototype")
public class LoanApplyService extends BaseService {
    public static final String TX_TYPE = "LOAN_APPLY";

    public LoanApplyService() {
        super(TX_TYPE);
    }

    @Override
    public String getPreviousTransactionId(RequestData requestData, String txType,
            String currentCategory) throws BusinessException, SystemException {
        // 获取前序交易
        // 贷款申请无前序交易
        return "";
    }
}

