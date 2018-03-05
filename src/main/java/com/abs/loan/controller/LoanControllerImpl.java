package com.abs.loan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.loan.bean.RequestData;
import com.abs.loan.bean.ResponseData;
import com.abs.loan.service.BaseService;
import com.abs.loan.service.BuybackApplyService;
import com.abs.loan.service.BuybackApproveService;
import com.abs.loan.service.BuybackResultConfirmService;
import com.abs.loan.service.DiffResultConfirmService;
import com.abs.loan.service.LoanApplyService;
import com.abs.loan.service.LoanApproveService;
import com.abs.loan.service.LoanResultConfirmService;
import com.abs.loan.service.MortgageDocConfirmService;
import com.abs.loan.service.RepayPlanConfirmService;
import com.abs.loan.service.RepayResultConfirmService;
import com.abs.loan.service.RepayResultNotifyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/loan")
public class LoanControllerImpl implements LoanController, BeanFactoryAware {

    private Logger log = LoggerFactory.getLogger(LoanControllerImpl.class);
    private Logger logError = LoggerFactory.getLogger("operation");

    private BeanFactory factory;

    @Override
    public void setBeanFactory(BeanFactory arg0) throws BeansException {
        factory = arg0;
    }

    private long count = 0;

    private ResponseData invoke(BaseService service, String jsonData) {
        long in = System.currentTimeMillis();

        RequestData reqData = new RequestData();
        ResponseData respData = new ResponseData();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("txID", "");
            try {
                reqData = JSON.parseObject(jsonData, RequestData.class);
                respData.setOutTradeNo(reqData.getOutTradeNo());
            } catch (Exception e) {
                logError.error("json转换失败",e);
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
            }
            String txID = service.invoke(reqData);
            jsonObj.put("txID", txID);
            log.info("接口{}写入成功， txID:{}", count, txID);
        } catch (BusinessException e) {
            logError.error(e.getExceptionEnum().getErrorMsg());
            respData.setBizContent(jsonObj.toJSONString());
            respData.setCode(e.getErrorCode());
            respData.setMsg(e.getErrorMsg());
            return respData;
        } catch (SystemException e) {
            logError.error(e.getExceptionEnum().getErrorMsg());
            respData.setBizContent(jsonObj.toJSONString());
            respData.setCode(e.getErrorCode());
            respData.setMsg(e.getErrorMsg());
            return respData;
        } catch (Exception e) {
            e.printStackTrace();
            logError.error("错误信息：", e);
            respData.setMsg(ExceptionEnum.UNKNOWN.getErrorMsg());
            respData.setCode(ExceptionEnum.UNKNOWN.getErrorCode());
            return respData;
        } finally {
            long out = System.currentTimeMillis();
            long useTime = out - in;
            log.info("退出接口{}，用时：{}ms", count++, useTime);
        }
        respData.setBizContent(jsonObj.toJSONString());
        respData.setCode(ExceptionEnum.SUCCESS.getErrorCode());
        respData.setMsg(ExceptionEnum.SUCCESS.getErrorMsg());
        return respData;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/loanApply")
    public String loanApply(@RequestBody String json) {
        // factory.getBean(serviceName,serviceType)
        log.info("进入接口{}：loanApply",count);
        BaseService service = (LoanApplyService) factory.getBean("loanApplyService");
        ResponseData respData = invoke(service, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/loanApprove")
    public String loanApprove(@RequestBody String json) {
        log.info("进入接口{}：loanApprove",count);
        BaseService baseService = (LoanApproveService) factory.getBean("loanApproveService");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/loanResultNotify")
    public String loanResultNotify(@RequestBody String json) {
        log.info("进入接口{}：loanResultNotify",count);
        BaseService baseService =
                (BaseService) factory.getBean("baseService", "LOAN_RESULT_NOTIFY");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/loanResultConfirm")
    public String loanResultConfirm(@RequestBody String json) {
        log.info("进入接口{}：loanResultConfirm",count);
        BaseService baseService =
                (LoanResultConfirmService) factory.getBean("loanResultConfirmService");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/mortgageDocUpload")
    public String mortgageDocUpload(@RequestBody String json) {
        log.info("进入接口{}：mortgageDocUpload",count);
        BaseService baseService =
                (BaseService) factory.getBean("baseService", "MORTGAGE_DOC_UPLOAD");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/mortgageDocConfirm")
    public String mortgageDocConfirm(@RequestBody String json) {
        log.info("进入接口{}：mortgageDocConfirm",count);
        BaseService baseService =
                (MortgageDocConfirmService) factory.getBean("mortgageDocConfirmService");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/diffResultNotify")
    public String diffResultNotify(@RequestBody String json) {
        log.info("进入接口{}：diffResultNotify",count);
        BaseService baseService =
                (BaseService) factory.getBean("baseService", "DIFF_RESULT_NOTIFY");
        ResponseData respData = invoke(baseService, json);

        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/diffResultConfirm")
    public String diffResultConfirm(@RequestBody String json) {
        log.info("进入接口{}：diffResultConfirm",count);
        BaseService baseService =
                (DiffResultConfirmService) factory.getBean("diffResultConfirmService");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/repayPlanUpload")
    public String repayPlanUpload(@RequestBody String json) {
        log.info("进入接口{}：repayPlanUpload",count);
        BaseService baseService = (BaseService) factory.getBean("baseService", "REPAY_PLAN_UPLOAD");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/repayPlanConfirm")
    public String repayPlanConfirm(@RequestBody String json) {
        log.info("进入接口{}：repayPlanConfirm",count);
        BaseService baseService = (RepayPlanConfirmService) factory
                .getBean("repayPlanConfirmService");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/repayResultNotify")
    public String repayResultNotify(@RequestBody String json) {
        log.info("进入接口{}：repayResultNotify",count);
        BaseService baseService =
                (RepayResultNotifyService) factory.getBean("repayResultNotifyService");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/repayResultConfirm")
    public String repayResultConfirm(@RequestBody String json) {
        log.info("进入接口{}：repayResultConfirm",count);
        BaseService service =
                (RepayResultConfirmService) factory.getBean("repayResultConfirmService");
        ResponseData respData = invoke(service, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/buybackApply")
    public String buybackApply(@RequestBody String json) {
        log.info("进入接口{}：buybackApply",count);
        BaseService service = (BuybackApplyService) factory.getBean("buybackApplyService");
        ResponseData respData = invoke(service, json);
        return JSON.toJSONString(respData);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/buybackApprove")
    public String buybackApprove(@RequestBody String json) {
        log.info("进入接口{}：buybackApprove",count);
        BaseService service = (BuybackApproveService) factory.getBean("buybackApproveService");
        ResponseData respData = invoke(service, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/buybackResultNotify")
    public String buybackResultNotify(@RequestBody String json) {
        log.info("进入接口{}：buybackResultNotify",count);
        BaseService baseService = (BaseService) factory.getBean("baseService","BUYBACK_RESULT_NOTIFY");
        ResponseData respData = invoke(baseService, json);
        return JSON.toJSONString(respData);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/buybackResultConfirm")
    public String buybackResultConfirm(@RequestBody String json) {
        log.info("进入接口{}：buybackResultConfirm",count);
        BaseService service =
                (BuybackResultConfirmService) factory.getBean("buybackResultConfirmService");
        ResponseData respData = invoke(service, json);
        return JSON.toJSONString(respData);
    }

}

