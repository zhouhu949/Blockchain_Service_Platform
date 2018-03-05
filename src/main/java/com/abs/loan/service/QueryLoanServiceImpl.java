package com.abs.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.bean.AssetPO;
import com.abs.bean.BlockPO;
import com.abs.bean.ChannelPO;
import com.abs.bean.Stage;
import com.abs.bean.TransactionPO;
import com.abs.config.CategoryHelper;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.loan.bean.GetAvgTxPerBlock;
import com.abs.loan.bean.GetAvgTxPerBlock.AvgTxPerBlock;
import com.abs.loan.bean.GetBlockCount;
import com.abs.loan.bean.GetBlockCount.BlockCount;
import com.abs.loan.bean.GetTxCount;
import com.abs.loan.bean.GetTxCount.TxCount;
import com.abs.loan.bean.GetTxsByComboConditionsReq;
import com.abs.loan.bean.QueryAssetsWithDiffCnfrmByTimeSpan;
import com.abs.loan.bean.QueryAssetsWithDiffCnfrmByTimeSpan.AssetWithDiffCnfrm;
import com.abs.loan.bean.QueryAssetsWithNoMortgageDoc;
import com.abs.loan.bean.QueryAssetsWithNoMortgageDoc.AssetWithNoMortgageDoc;
import com.abs.loan.bean.QueryBlockByBlockHash;
import com.abs.loan.bean.QueryBlockByBlockHash.BlockDetail;
import com.abs.loan.bean.QueryChannelByChannelId;
import com.abs.loan.bean.QueryChannelList;
import com.abs.loan.bean.QueryTxDetail;
import com.abs.loan.bean.QueryTxDetails;
import com.abs.loan.bean.QueryTxDetails.TxDetail;
import com.abs.loan.bean.QueryTxsByAssetUID;
import com.abs.loan.bean.QueryTxsByAssetUID.TxSummary;
import com.abs.loan.dao.MongoDao;
import com.abs.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class QueryLoanServiceImpl implements QueryLoanService {
    // private Logger log = LoggerFactory.getLogger(QueryLoanServiceImpl.class);
    private Logger logError = LoggerFactory.getLogger("operation");
    @Autowired
    private MongoDao mongoDaoImpl;

    private final static Integer RESULT_COUNT = 100;
    private final static String TIME_FORMAT = "yyyyMMddHHmmss";
    private final String NO_MORTGEAGE_DOC = "01";// 未上传质押文件
    private final String NO_CONFIRM_MORTGEAGE_DOC = "02";// 已上传质押文件但未确认


    @Override
    public Long findMaxBlockHeight() {
        return mongoDaoImpl.findMaxBlockHeight();
    }

    @Override
    public QueryTxsByAssetUID queryTxsByAssetUID(String json)
            throws BusinessException, SystemException {
        QueryTxsByAssetUID resp = new QueryTxsByAssetUID();
        String assetUID = "";
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            assetUID = getAssetUID(jsonObj);
        } catch (BusinessException e) {
            logError.error(e.getErrorMsg());
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            List<TxSummary> txContents = mongoDaoImpl.findTxsByAssetUID(assetUID);
            resp.setTxContents(txContents);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryTxDetails queryTxDetailsByAssetUIDTxType(String json)
            throws BusinessException, SystemException {
        QueryTxDetails resp = new QueryTxDetails();
        String assetUID = "";
        String txType = "";
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            assetUID = getAssetUID(jsonObj);
            txType = getTxType(jsonObj);
        } catch (BusinessException e) {
            logError.error(e.getErrorMsg());
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            List<TxDetail> txContents = mongoDaoImpl.findTxByAssetUIDTxType(assetUID, txType);
            resp.setTxContents(txContents);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryAssetsWithDiffCnfrmByTimeSpan queryAssetsWithDiffCnfrmByTimeSpan(String json)
            throws BusinessException, SystemException {
        QueryAssetsWithDiffCnfrmByTimeSpan resp = new QueryAssetsWithDiffCnfrmByTimeSpan();
        String timeStart = "";// 包含
        String timeEnd = "";// 不包含
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            timeStart = getTimeStart(jsonObj);
            timeEnd = getTimeEnd(jsonObj, timeStart);
        } catch (BusinessException e) {
            logError.error(e.getErrorMsg());
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            List<AssetPO> assets =
                    mongoDaoImpl.findAssetsWithDiffCnfrmByTimeSpan(timeStart, timeEnd);
            checkResultCount(assets);

            List<AssetWithDiffCnfrm> list = new ArrayList<AssetWithDiffCnfrm>();
            for (AssetPO asset : assets) {
                AssetWithDiffCnfrm a = new AssetWithDiffCnfrm();
                a.setAssetUid(asset.getAssetUid());
                boolean flag = false;
                for (Stage s : asset.getStages()) {
                    if (DiffResultConfirmService.TX_TYPE_PASS.equals(s.getStageType())) {
                        a.setDiffCnfrmTime(s.getTxTime());
                        flag = true;
                    }
                }
                if (flag) {
                    list.add(a);
                }
            }
            resp.setAssets(list);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryAssetsWithNoMortgageDoc queryAssetsWithNoMortgageDoc()
            throws BusinessException, SystemException {
        QueryAssetsWithNoMortgageDoc resp = new QueryAssetsWithNoMortgageDoc();
        try {
            List<AssetPO> assetsNoCategory = mongoDaoImpl.findAssetsWithNoMortgageDoc();
            List<AssetPO> assetsNoConfirm = mongoDaoImpl.findAssetsWithNoConfirmMortgageDoc();
            List<AssetWithNoMortgageDoc> assets = new ArrayList<AssetWithNoMortgageDoc>();

            String txType = LoanResultConfirmService.TX_TYPE_PASS;
            // category = CAT_MORT
            String category = CategoryHelper.getInstance().getCategory(txType);
            for (AssetPO asset : assetsNoCategory) {
                AssetWithNoMortgageDoc a = new AssetWithNoMortgageDoc();
                a.setAssetUid(asset.getAssetUid());
                boolean flag = false;
                for (Stage stage : asset.getStages()) {
                    if (category.equals(stage.getCategory())
                            && txType.equals(stage.getStageType())) {
                        a.setLoanResultConfirmTime(stage.getTxTime());
                        a.setLoanResultConfirmTxId(stage.getLastestStage());
                        flag = true;
                    }
                }
                a.setMortgageDocStatus(NO_MORTGEAGE_DOC);
                if (flag) {
                    assets.add(a);
                }
            }
            for (AssetPO asset : assetsNoConfirm) {
                AssetWithNoMortgageDoc a = new AssetWithNoMortgageDoc();
                a.setAssetUid(asset.getAssetUid());
                boolean flag = false;
                for (Stage stage : asset.getStages()) {
                    if (category.equals(stage.getCategory())
                            && txType.equals(stage.getStageType())) {
                        a.setLoanResultConfirmTime(stage.getTxTime());
                        a.setLoanResultConfirmTxId(stage.getLastestStage());
                        flag = true;
                    }
                }
                a.setMortgageDocStatus(NO_CONFIRM_MORTGEAGE_DOC);
                if (flag) {
                    assets.add(a);
                }
            }
            resp.setAssets(assets);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryTxDetails queryTxDetailsByBlockHeight(String json)
            throws BusinessException, SystemException {
        QueryTxDetails resp = new QueryTxDetails();
        String channelID = "";
        long blockHeight = -1;
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            channelID = getChannelID(jsonObj);
            blockHeight = getBlockHeight(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            List<TxDetail> txContents =
                    mongoDaoImpl.findTxDetailsByBlockHeight(channelID, blockHeight);
            resp.setTxContents(txContents);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryTxDetails queryTxDetailsByBlockHash(String json)
            throws BusinessException, SystemException {
        QueryTxDetails resp = new QueryTxDetails();
        String blockHash = "";
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            blockHash = getBlockHash(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            List<TxDetail> txContents = mongoDaoImpl.findTxDetailsByBlockHash(blockHash);
            resp.setTxContents(txContents);
        } catch (Exception e) {
            e.printStackTrace();
            logError.error("", e);
            throw new SystemException(ExceptionEnum.DATABASE_ERROR);
        }
        return resp;
    }

    @Override
    public QueryTxDetail queryTxDetailsByTxID(String json)
            throws BusinessException, SystemException {
        QueryTxDetail resp = new QueryTxDetail();
        String txId = "";
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            txId = getTxId(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            TxDetail txDetail = mongoDaoImpl.findTxDetailsByTxID(txId);
            resp.setTxContent(txDetail);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryChannelList queryChannelList() throws BusinessException, SystemException {
        QueryChannelList resp = new QueryChannelList();
        try {
            List<ChannelPO> channels = mongoDaoImpl.findChannelList();
            List<String> list = new ArrayList<String>();
            for (ChannelPO channel : channels) {
                list.add(channel.getChannelId());
            }
            resp.setChannels(list);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryChannelByChannelId queryChannelByChannelId(String json)
            throws BusinessException, SystemException {
        QueryChannelByChannelId resp = new QueryChannelByChannelId();
        String channelId = "";
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            channelId = getChannelID(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            Long blocks = mongoDaoImpl.findBlockCountByChannelId(channelId);
            resp.setBlockNum(blocks);
            resp.setChannelId(channelId);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryBlockByBlockHash queryBlockByBlockHash(String json)
            throws BusinessException, SystemException {
        QueryBlockByBlockHash resp = new QueryBlockByBlockHash();
        String blockHash = "";
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            blockHash = getBlockHash(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            BlockDetail blockDetail = mongoDaoImpl.findBlockByBlockHash(blockHash);
            resp.setBlockContent(blockDetail);
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    /**
     * 1、解析字段 2、用时间间隔确定查询的起止时间 3、根据channelId查询block表所有区块 4、更新查询起止时间，直到超过结束时间。超过结束时间则用结束时间
     */
    @Override
    public GetBlockCount getBlockCount(String json) throws BusinessException, SystemException {
        GetBlockCount resp = new GetBlockCount();
        String channelId = "";
        String timeStart = "";// 起始时间（包含） String ans20 M 起始时间（包含），yyyyMMddHHmmss
        String timeEnd = "";// 结束时间（不包含） String ans20 M 结束时间（不包含），yyyyMMddHHmmss
        Integer interval = 0;// 时间间隔 Integer M 时间间隔，单位为分钟
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            channelId = getChannelID(jsonObj);
            timeStart = getTimeStart(jsonObj);
            timeEnd = getTimeEnd(jsonObj, timeStart);
            interval = getInterval(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            Calendar calTem = Calendar.getInstance();
            calStart.setTime(DateUtil.parseDate(timeStart, TIME_FORMAT));
            calEnd.setTime(DateUtil.parseDate(timeEnd, TIME_FORMAT));
            calTem.setTime(DateUtil.parseDate(timeStart, TIME_FORMAT));
            calTem.add(Calendar.MINUTE, interval);
            List<BlockCount> blockCounts = new ArrayList<BlockCount>();

            long timeTotle = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
            long timeInterval = calTem.getTimeInMillis() - calStart.getTimeInMillis();
            Double resultCount = (double) timeTotle / timeInterval;
            checkResultCount(resultCount);

            while (true) {
                boolean isbreak = false;
                List<BlockPO> blocks = null;
                String start = DateUtil.format(calStart.getTimeInMillis(), TIME_FORMAT);
                String tem = "";
                if (calTem.before(calEnd)) {
                    tem = DateUtil.format(calTem.getTimeInMillis(), TIME_FORMAT);
                } else {
                    tem = DateUtil.format(calEnd.getTimeInMillis(), TIME_FORMAT);
                    isbreak = true;
                }
                blocks = mongoDaoImpl.findBlockCount(channelId, start, tem);
                BlockCount blockCount = new BlockCount();
                blockCount.setBlockNum(blocks.size());
                blockCount.setTimeStart(start);
                blockCount.setTimeEnd(tem);
                blockCounts.add(blockCount);
                if (isbreak) {
                    break;
                }
                calStart.setTimeInMillis(calTem.getTimeInMillis());
                calTem.add(Calendar.MINUTE, interval);
            }
            resp.setBlockCounts(blockCounts);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    /**
     * 1、解析字段 2、用时间间隔确定查询的起止时间 3、根据channelId查询transaction表所有交易 4、更新查询起止时间，直到超过结束时间。超过结束时间则用结束时间
     */
    @Override
    public GetTxCount getTxCount(String json) throws BusinessException, SystemException {
        GetTxCount resp = new GetTxCount();
        String channelId = "";
        String timeStart = "";// 起始时间（包含） String ans20 M 起始时间（包含），yyyyMMddHHmmss
        String timeEnd = "";// 结束时间（不包含） String ans20 M 结束时间（不包含），yyyyMMddHHmmss
        Integer interval = 0;// 时间间隔 Integer M 时间间隔，单位为分钟
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            channelId = getChannelID(jsonObj);
            timeStart = getTimeStart(jsonObj);
            timeEnd = getTimeEnd(jsonObj, timeStart);
            interval = getInterval(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            Calendar calTem = Calendar.getInstance();
            calStart.setTime(DateUtil.parseDate(timeStart, TIME_FORMAT));
            calEnd.setTime(DateUtil.parseDate(timeEnd, TIME_FORMAT));
            calTem.setTime(DateUtil.parseDate(timeStart, TIME_FORMAT));
            calTem.add(Calendar.MINUTE, interval);
            List<TxCount> txCounts = new ArrayList<TxCount>();

            long timeTotle = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
            long timeInterval = calTem.getTimeInMillis() - calStart.getTimeInMillis();
            Double resultCount = (double) timeTotle / timeInterval;
            checkResultCount(resultCount);
            while (true) {
                boolean isbreak = false;
                List<TransactionPO> txs = null;
                String start = DateUtil.format(calStart.getTimeInMillis(), TIME_FORMAT);
                String tem = "";
                if (calTem.before(calEnd)) {
                    tem = DateUtil.format(calTem.getTimeInMillis(), TIME_FORMAT);
                } else {
                    tem = DateUtil.format(calEnd.getTimeInMillis(), TIME_FORMAT);
                    isbreak = true;
                }
                txs = mongoDaoImpl.findTxCount(channelId, start, tem);
                TxCount txCount = new TxCount();
                txCount.setTxNum(txs.size());
                txCount.setTimeStart(start);
                txCount.setTimeEnd(tem);
                txCounts.add(txCount);
                if (isbreak) {
                    break;
                }
                calStart.setTimeInMillis(calTem.getTimeInMillis());
                calTem.add(Calendar.MINUTE, interval);
            }
            resp.setTxCounts(txCounts);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    /**
     * 1、解析字段 2、用时间间隔确定查询的起止时间 3、根据channelId查询block表所有区块 4、遍历区块，获取所需信息
     * 5、更新查询起止时间，直到超过结束时间。超过结束时间则用结束时间
     */
    @Override
    public GetAvgTxPerBlock getAvgTxPerBlock(String json)
            throws BusinessException, SystemException {
        GetAvgTxPerBlock resp = new GetAvgTxPerBlock();
        String channelId = "";
        String timeStart = "";// 起始时间（包含） String ans20 M 起始时间（包含），yyyyMMddHHmmss
        String timeEnd = "";// 结束时间（不包含） String ans20 M 结束时间（不包含），yyyyMMddHHmmss
        Integer interval = 0;// 时间间隔 Integer M 时间间隔，单位为分钟
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            channelId = getChannelID(jsonObj);
            timeStart = getTimeStart(jsonObj);
            timeEnd = getTimeEnd(jsonObj, timeStart);
            interval = getInterval(jsonObj);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            Calendar calTem = Calendar.getInstance();
            calStart.setTime(DateUtil.parseDate(timeStart, TIME_FORMAT));
            calEnd.setTime(DateUtil.parseDate(timeEnd, TIME_FORMAT));
            calTem.setTime(DateUtil.parseDate(timeStart, TIME_FORMAT));
            calTem.add(Calendar.MINUTE, interval);
            List<AvgTxPerBlock> avgTxPerBlocks = new ArrayList<AvgTxPerBlock>();

            long timeTotle = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
            long timeInterval = calTem.getTimeInMillis() - calStart.getTimeInMillis();
            Double resultCount = (double) timeTotle / timeInterval;
            checkResultCount(resultCount);

            while (true) {
                boolean isbreak = false;
                String start = DateUtil.format(calStart.getTimeInMillis(), TIME_FORMAT);
                String tem = "";
                if (calTem.before(calEnd)) {
                    tem = DateUtil.format(calTem.getTimeInMillis(), TIME_FORMAT);
                } else {
                    tem = DateUtil.format(calEnd.getTimeInMillis(), TIME_FORMAT);
                    isbreak = true;
                }
                List<BlockPO> blocks = mongoDaoImpl.findBlockCount(channelId, start, tem);
                int txNumSum = 0;
                for (BlockPO block : blocks) {
                    txNumSum += block.getTxNum();
                }
                double avg = 0.0;
                if (blocks.size() != 0) {
                    avg = (double) txNumSum / blocks.size();
                }
                AvgTxPerBlock avgTxPerBlock = new AvgTxPerBlock();
                avgTxPerBlock
                        .setTxPerBlock(new BigDecimal(avg).setScale(2, BigDecimal.ROUND_HALF_UP));
                avgTxPerBlock.setTimeStart(start);
                avgTxPerBlock.setTimeEnd(tem);
                avgTxPerBlocks.add(avgTxPerBlock);
                if (isbreak) {
                    break;
                }
                calStart.setTimeInMillis(calTem.getTimeInMillis());
                calTem.add(Calendar.MINUTE, interval);
            }
            resp.setAvgTxPerBlocks(avgTxPerBlocks);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    @Override
    public QueryTxDetails getTxsByComboConditions(String json)
            throws BusinessException, SystemException {
        // channelId M 其他可以没有
        QueryTxDetails resp = new QueryTxDetails();
        GetTxsByComboConditionsReq req = new GetTxsByComboConditionsReq();
        try {
            req = JSON.parseObject(json, GetTxsByComboConditionsReq.class);
            if (req == null) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
            }
            if (req.getChannelId() == null || req.getChannelId().length() == 0) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_FORMAT_ERROR);
        }
        try {
            Long resultCount = mongoDaoImpl.findTxsByComboConditionsCounts(req);
            checkResultCount(resultCount);

            List<TxDetail> txDetails = new ArrayList<TxDetail>();
            txDetails = mongoDaoImpl.findTxsByComboConditions(req);
            resp.setTxContents(txDetails);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logError.error("", e);
            if (e.getMessage().contains("Timed out")) {
                logError.error("数据库连接超时");
                throw new SystemException(ExceptionEnum.DATABASE_CONNECT_ERROR);
            } else {
                throw new SystemException(ExceptionEnum.DATABASE_ERROR);
            }
        }
        return resp;
    }

    private String getTxId(JSONObject jsonObj) throws BusinessException {
        String txId = jsonObj.getString("txId");
        if (txId == null || txId.length() == 0) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        if (txId.length() != 64) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_TX_ID_LENGTH_ERROR);
        }
        return txId;
    }

    private String getTxType(JSONObject jsonObj) throws BusinessException {
        String txType = jsonObj.getString("txType");
        if (txType == null || txType.length() == 0) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        CategoryHelper helper = CategoryHelper.getInstance();
        if (!helper.getTxTypes().contains(txType)) {
            if (txType.indexOf(":") == -1) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_TXTYPE_NOT_FOUND);
            } else {
                if(!helper.getTxTypes().contains(txType.split(":")[0])) {
                    throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_TXTYPE_NOT_FOUND);
                }
                Integer count = Integer.parseInt(txType.split(":")[1]);
                if (count < 0) {
                    throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_TXTYPE_NOT_FOUND);
                }
            }
        }
        return txType;
    }

    private Long getBlockHeight(JSONObject jsonObj) throws BusinessException {
        Long blockHeight = jsonObj.getLong("blockHeight");
        if (blockHeight == null) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        if (blockHeight < 0 || blockHeight.toString().length() > 12) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_BLOCK_HEIGHT_ERROR);
        }
        return blockHeight;
    }

    private String getBlockHash(JSONObject jsonObj) throws BusinessException {
        String blockHash = jsonObj.getString("blockHash");
        if (blockHash == null || blockHash.length() == 0) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        if (blockHash.length() != 64) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_BLOCK_HASH_LENGTH_ERROR);
        }
        return blockHash;
    }

    private String getChannelID(JSONObject jsonObj) throws BusinessException {
        String channelID = jsonObj.getString("channelId");
        if (channelID == null || channelID.length() == 0) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        return channelID;
    }

    private String getAssetUID(JSONObject jsonObj) throws BusinessException {
        String assetUID = jsonObj.getString("assetUid");
        if (assetUID == null || assetUID.length() == 0) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        return assetUID;
    }

    private Integer getInterval(JSONObject jsonObj) throws BusinessException {
        Integer interval = -1;
        try {
            interval = jsonObj.getInteger("interval");
            if (interval == null) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
            }
            if (interval <= 0) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_INTERVAL_VALUE_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_INTERVAL_VALUE_ERROR);
        }
        return interval;
    }

    private String getTimeStart(JSONObject jsonObj) throws BusinessException {
        String timeStart = "";
        timeStart = jsonObj.getString("timeStart");
        if (timeStart == null || timeStart.length() == 0) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
        }
        return timeStart;
    }

    private String getTimeEnd(JSONObject jsonObj, String timeStart) throws BusinessException {
        String timeEnd = "";
        try {
            timeEnd = jsonObj.getString("timeEnd");
            if (timeEnd == null || timeEnd.length() == 0) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_NULL_ERROR);
            }
            if (timeEnd.length() != TIME_FORMAT.length()) {
                throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_TIME_END_FORMAT_ERROR);
            }
            if (timeStart.length() != TIME_FORMAT.length()) {
                throw new BusinessException(
                        ExceptionEnum.REQUEST_PARAMETER_TIME_START_FORMAT_ERROR);
            }
            Date start = DateUtil.parseDate(timeStart, TIME_FORMAT);
            Date end = DateUtil.parseDate(timeEnd, TIME_FORMAT);
            if (end.before(start)) {
                throw new BusinessException(
                        ExceptionEnum.REQUEST_PARAMETER_TIME_END_BEFORE_START_ERROR);
            }
            if (end.getTime() > System.currentTimeMillis()) {
                throw new BusinessException(
                        ExceptionEnum.REQUEST_PARAMETER_TIME_END_AFTER_NOW_ERROR);
            }
            if (end.getTime() == start.getTime()) {
                throw new BusinessException(
                        ExceptionEnum.REQUEST_PARAMETER_TIME_END_EQ_START_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_TIME_END_FORMAT_ERROR);
        }
        return timeEnd;
    }


    @SuppressWarnings("rawtypes")
    private void checkResultCount(Object obj) throws BusinessException {
        if (obj instanceof Collection) {
            Collection c = (Collection) obj;
            if (c.size() > RESULT_COUNT) {
                logError.error("结果条数{},{}", c.size(),
                        ExceptionEnum.QUERY_RESULT_EXCEED_100_ITEMS.getErrorMsg());
                throw new BusinessException(ExceptionEnum.QUERY_RESULT_EXCEED_100_ITEMS);
            }
        }
        if (obj instanceof Integer || obj instanceof Long) {
            Long resultCount = (Long) obj;
            if (resultCount > RESULT_COUNT) {
                logError.error("返回条数{}", RESULT_COUNT,
                        ExceptionEnum.QUERY_RESULT_EXCEED_100_ITEMS.getErrorMsg());
                throw new BusinessException(ExceptionEnum.QUERY_RESULT_EXCEED_100_ITEMS);
            }

        }
        if (obj instanceof Double) {
            Double resultCount = (Double) obj;
            if (resultCount > RESULT_COUNT) {
                logError.error("返回条数{}", RESULT_COUNT,
                        ExceptionEnum.QUERY_RESULT_EXCEED_100_ITEMS.getErrorMsg());
                throw new BusinessException(ExceptionEnum.QUERY_RESULT_EXCEED_100_ITEMS);
            }

        }

    }
}

