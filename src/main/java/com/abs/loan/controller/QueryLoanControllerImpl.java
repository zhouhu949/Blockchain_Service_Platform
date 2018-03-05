package com.abs.loan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.loan.bean.GetAvgTxPerBlock;
import com.abs.loan.bean.GetBlockCount;
import com.abs.loan.bean.GetTxCount;
import com.abs.loan.bean.QueryAssetsWithDiffCnfrmByTimeSpan;
import com.abs.loan.bean.QueryAssetsWithNoMortgageDoc;
import com.abs.loan.bean.QueryBaseRespBean;
import com.abs.loan.bean.QueryBlockByBlockHash;
import com.abs.loan.bean.QueryChannelByChannelId;
import com.abs.loan.bean.QueryChannelList;
import com.abs.loan.bean.QueryTxDetail;
import com.abs.loan.bean.QueryTxDetails;
import com.abs.loan.bean.QueryTxsByAssetUID;
import com.abs.loan.service.QueryLoanService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Controller
@RequestMapping(value = "/loan")
public class QueryLoanControllerImpl implements QueryLoanController {
    private Logger log = LoggerFactory.getLogger(QueryLoanControllerImpl.class);
    private Logger logError = LoggerFactory.getLogger("operation");


    @Autowired
    private QueryLoanService queryLoanServiceImpl;
    private long count = 0;


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryTxsByAssetUid")
    public String queryTxsByAssetUID(@RequestBody String json) {
        log.info("进入{}接口：queryTxsByAssetUid,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryTxsByAssetUID resp = new QueryTxsByAssetUID();
        try {
            resp = queryLoanServiceImpl.queryTxsByAssetUID(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryTxsByAssetUid，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryTxDetailsByAssetUidTxType")
    public String queryTxDetailsByAssetUIDTxType(@RequestBody String json) {
        log.info("进入{}接口：queryTxDetailsByAssetUidTxType,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryTxDetails resp = new QueryTxDetails();
        try {
            resp = queryLoanServiceImpl.queryTxDetailsByAssetUIDTxType(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryTxDetailsByAssetUidTxType，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryAssetsWithNoMortgageDoc")
    public String queryAssetsWithNoMortgageDoc() {
        log.info("进入{}接口：queryAssetsWithNoMortgageDoc", count);
        long start = System.currentTimeMillis();
        QueryAssetsWithNoMortgageDoc resp = new QueryAssetsWithNoMortgageDoc();
        try {
            resp = queryLoanServiceImpl.queryAssetsWithNoMortgageDoc();
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryAssetsWithNoMortgageDoc，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryAssetsWithDiffCnfrmByTimeSpan")
    public String queryAssetsWithDiffCnfrmByTimeSpan(@RequestBody String json) {
        log.info("进入{}接口：queryAssetsWithDiffCnfrmByTimeSpan,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryAssetsWithDiffCnfrmByTimeSpan resp = new QueryAssetsWithDiffCnfrmByTimeSpan();
        try {
            resp = queryLoanServiceImpl.queryAssetsWithDiffCnfrmByTimeSpan(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryAssetsWithDiffCnfrmByTimeSpan，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryTxDetailsByBlockHeight")
    public String queryTxDetailsByBlockHeight(@RequestBody String json) {
        log.info("进入{}接口：queryTxDetailsByBlockHeight,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryTxDetails resp = new QueryTxDetails();
        try {
            resp = queryLoanServiceImpl.queryTxDetailsByBlockHeight(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryTxDetailsByBlockHeight，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryTxDetailsByBlockHash")
    public String queryTxDetailsByBlockHash(@RequestBody String json) {
        log.info("进入{}接口：queryTxDetailsByBlockHash,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryTxDetails resp = new QueryTxDetails();
        try {
            resp = queryLoanServiceImpl.queryTxDetailsByBlockHash(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryTxDetailsByBlockHash，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryTxDetailsByTxId")
    public String queryTxDetailsByTxID(@RequestBody String json) {
        log.info("进入{}接口：queryTxDetailsByTxId,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryTxDetail resp = new QueryTxDetail();
        try {
            resp = queryLoanServiceImpl.queryTxDetailsByTxID(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryTxDetailsByTxId，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);

    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryChannelList")
    public String queryChannelList() {
        log.info("进入{}接口：queryChannelList", count);
        long start = System.currentTimeMillis();
        QueryChannelList resp = new QueryChannelList();
        try {
            resp = queryLoanServiceImpl.queryChannelList();
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryChannelList，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryChannelByChannelId")
    public String queryChannelByChannelID(@RequestBody String json) {
        log.info("进入{}接口：queryChannelByChannelId,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryChannelByChannelId resp = new QueryChannelByChannelId();
        try {
            resp = queryLoanServiceImpl.queryChannelByChannelId(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryChannelByChannelId，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/queryBlockByBlockHash")
    public String queryBlockByBlockHash(@RequestBody String json) {
        log.info("进入{}接口：queryBlockByBlockHash,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryBlockByBlockHash resp = new QueryBlockByBlockHash();
        try {
            resp = queryLoanServiceImpl.queryBlockByBlockHash(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：queryBlockByBlockHash，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/getBlockCount")
    public String getBlockCount(@RequestBody String json) {
        log.info("进入{}接口：getBlockCount,参数：{}", count, json);
        long start = System.currentTimeMillis();
        GetBlockCount resp = new GetBlockCount();
        try {
            resp = queryLoanServiceImpl.getBlockCount(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：getBlockCount，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/getTxCount")
    public String getTxCount(@RequestBody String json) {
        log.info("进入{}接口：getTxCount,参数：{}", count, json);
        long start = System.currentTimeMillis();
        GetTxCount resp = new GetTxCount();
        try {
            resp = queryLoanServiceImpl.getTxCount(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：getTxCount，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/getAvgTxPerBlock")
    public String getAvgTxPerBlock(@RequestBody String json) {
        log.info("进入{}接口：getAvgTxPerBlock,参数：{}", count, json);
        long start = System.currentTimeMillis();
        GetAvgTxPerBlock resp = new GetAvgTxPerBlock();
        try {
            resp = queryLoanServiceImpl.getAvgTxPerBlock(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：getAvgTxPerBlock，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/getTxsByComboConditions")
    public String getTxsByComboConditions(@RequestBody String json) {
        log.info("进入{}接口：getTxsByComboConditions,参数：{}", count, json);
        long start = System.currentTimeMillis();
        QueryTxDetails resp = new QueryTxDetails();
        try {
            resp = queryLoanServiceImpl.getTxsByComboConditions(json);
        } catch (SystemException e) {
            fail(resp, e);
        } catch (BusinessException e) {
            fail(resp, e);
        } catch (Exception e) {
            fail(resp, e);
        }
        success(resp);

        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.info("退出{}接口：getTxsByComboConditions，耗时{}ms", count, useTime);
        return JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "/findMaxBlockHeight")
    public String findMaxBlockHeight() {
        for (int i = 0; i < 10000; i++) {
            logError.error("{}i:{}", getClass().getName(), i);
        }
        String result = "";
        try {
            result = String.valueOf(queryLoanServiceImpl.findMaxBlockHeight());
        } catch (Exception e) {
            logError.error("", e);
            return "-1";
        }
        return result;
    }

    private void success(QueryBaseRespBean resp) {
        if (resp.getMsg() == null || resp.getMsg().length() == 0) {
            resp.setCode(ExceptionEnum.SUCCESS.getErrorCode());
            resp.setMsg(ExceptionEnum.SUCCESS.getErrorMsg());
        }
    }

    private void fail(QueryBaseRespBean resp, Exception e) {
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            resp.setCode(be.getErrorCode());
            resp.setMsg(be.getErrorMsg());
            logError.error(be.getErrorMsg());
        } else if (e instanceof SystemException) {
            SystemException se = (SystemException) e;
            resp.setCode(se.getErrorCode());
            resp.setMsg(se.getErrorMsg());
            logError.error(se.getErrorMsg());
        } else {
            resp.setCode(ExceptionEnum.SYSTEM_ERROR.getErrorCode());
            resp.setMsg(ExceptionEnum.SYSTEM_ERROR.getErrorMsg());
            logError.error("", e);
        }
    }
}

