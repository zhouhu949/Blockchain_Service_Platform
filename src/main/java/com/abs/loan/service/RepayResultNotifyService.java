package com.abs.loan.service;

import java.util.Map;
import java.util.Set;

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
 * 3.4.3扣款结果通知 (/loan/repayResultNotify)
 */
@Service(value = "repayResultNotifyService")
@Scope("prototype")
public class RepayResultNotifyService extends BaseService {
    public static final String TX_TYPE_ADD = "ADD_REPAY_RESULT_NOTIFY:t>0";
    public static final String TX_TYPE_NORMAL = "REPAY_RESULT_NOTIFY:";

    @Override
    protected String getTxType(RequestData requestData) throws BusinessException {

        String txType = "";;
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            Integer currentIssue = jsonObj.getInteger("currentIssue");
            Boolean normal = jsonObj.getBoolean("normal");
            if (normal) {
                if (currentIssue < 1) {
                    throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_CURRENT_ISSUE_ERROR);
                }
                CategoryHelper categoryHelper = CategoryHelper.getInstance();
                Map<String, JSONObject> map = categoryHelper.getMap();
                Set<String> set = map.keySet();
                // 迭代所有txType，找到对应txType并判断表达式，例如REPAY_RESULT_NOTIFY:t>1
                txType = TX_TYPE_NORMAL;
                for (String name : set) {
                    if (name.startsWith(txType)) {
                        // 判断表达式 t>1是否成立，成立则取其txType(t为期数)
                        Boolean b = ScriptUtil.getBooleanValue(currentIssue, name.split(":")[1]);
                        if (b) {
                            txType = name;
                            break;
                        }
                    }
                }
            } else {
                txType = TX_TYPE_ADD;
            }
            if(txType.equals(TX_TYPE_NORMAL) || txType.equals("")){
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("",e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }
        return txType;
    }

    @Override
    protected String getCurrentCategory(RequestData requestData, String txType)
            throws BusinessException {
        String currentCategory = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            Integer currentIssue = jsonObj.getInteger("currentIssue");
            CategoryHelper categoryHelper = CategoryHelper.getInstance();
            String c = categoryHelper.getCategory(txType);
            currentCategory = ScriptUtil.getCategory(currentIssue, c);
        } catch (Exception e) {
            logError.error("",e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }
        return currentCategory;
    }

    @Override
    protected String getPreviousCategory(RequestData requestData, String txType)
            throws BusinessException {
        String previousCategory = "";
        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(requestData.getBizContent());
            Integer currentIssue = jsonObj.getInteger("currentIssue");
            CategoryHelper categoryHelper = CategoryHelper.getInstance();
            String pc = categoryHelper.getPreviousCategoryExceptSelf(txType);
            previousCategory = ScriptUtil.getCategory(currentIssue, pc);
        } catch (Exception e) {
            logError.error("",e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
        }
        return previousCategory;
    }
}

