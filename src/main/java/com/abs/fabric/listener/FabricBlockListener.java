package com.abs.fabric.listener;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.protos.common.Common.BlockHeader;
import org.hyperledger.fabric.protos.common.Common.ChannelHeader;
import org.hyperledger.fabric.protos.common.Common.Header;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.protos.peer.Chaincode.ChaincodeInvocationSpec;
import org.hyperledger.fabric.protos.peer.FabricProposal.ChaincodeHeaderExtension;
import org.hyperledger.fabric.protos.peer.FabricProposal.ChaincodeProposalPayload;
import org.hyperledger.fabric.protos.peer.FabricTransaction.ChaincodeActionPayload;
import org.hyperledger.fabric.protos.peer.FabricTransaction.Transaction;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.BlockListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abs.bean.AssetPO;
import com.abs.bean.BlockPO;
import com.abs.bean.ChannelPO;
import com.abs.bean.Stage;
import com.abs.bean.TransactionPO;
import com.abs.exception.SystemException;
import com.abs.loan.dao.MongoDao;
import com.abs.mq.service.MQService;
import com.abs.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;

@Component(value = "fabricBlockListener")
public class FabricBlockListener implements BlockListener {
    Logger log = LoggerFactory.getLogger(FabricBlockListener.class);
    Logger logError = LoggerFactory.getLogger("operation");
    @Autowired
    private MQService mqServiceImpl;

    @Autowired
    private MongoDao mongoDaoImpl;

    private static final int ARGUMENT_COUNT = 8;

    @Override
    public void received(BlockEvent blockEvent) {
        try {
            log.info("接收到blockEvent,解析开始");
            List<AssetPO> assets = new ArrayList<AssetPO>();
            BlockPO block = parseBlock(blockEvent);
            List<TransactionPO> transactions = parseTransaction(blockEvent, assets, block);
            insertMongo(block, transactions, assets);
            sendMQ(block, transactions);
            log.info("接收的blockEvent,解析成功");
        } catch (Exception e) {
            logError.error("",e);
        }
    }

    private BlockPO parseBlock(BlockEvent blockEvent) throws Exception {

        BlockPO bpo = new BlockPO();
        try {
            List<String> txs = new ArrayList<String>();
            for (TransactionEvent te : blockEvent.getTransactionEvents()) {
                txs.add(te.getTransactionID().toUpperCase());
            }
            String channelId = blockEvent.getChannelID();

            BlockHeader blockHeader = blockEvent.getBlock().getHeader();
            ASN1BlockHeader asn1Header = new ASN1BlockHeader(blockHeader.getNumber(),
                    blockHeader.getPreviousHash(), blockHeader.getDataHash());
            Long blockHeight = blockHeader.getNumber();
            String pBlockHash =
                    Hex.toHexString(blockHeader.getPreviousHash().toByteArray()).toUpperCase();
            int txNum = blockEvent.getTransactionEvents().size();

            bpo.setBlockHash(asn1Header.getSHA256Hash());
            bpo.setBlockHeight(blockHeight);
            bpo.setPreviousBlockHash(pBlockHash);
            bpo.setChannelId(channelId);
            bpo.setTxNum(txNum);
            bpo.setTxIds(txs);
            bpo.setInsertTime(DateUtil.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
            log.info("block解析成功");
        } catch (Exception e) {
            logError.error("解析block失败", e);
            throw e;
        }
        return bpo;
    }

    private List<TransactionPO> parseTransaction(BlockEvent blockEvent, List<AssetPO> assets,
            BlockPO block) throws Exception {

        List<TransactionPO> txPOs = new ArrayList<TransactionPO>();
        try {
            List<TransactionEvent> tes = blockEvent.getTransactionEvents();
            if (tes != null) {
                for (TransactionEvent transactionEvent : tes) {
                    String txId = transactionEvent.getTransactionID();
                    if (txId == null || txId.length() == 0) {
                        continue;
                    }
                    txId = txId.toUpperCase();
                    Integer txStatus = (int) transactionEvent.validationCode();
                    ChannelHeader channelHeader = null;
                    ChaincodeHeaderExtension che = null;
                    // List<ByteString> txinfo = null;
                    Payload payload =
                            Payload.parseFrom(transactionEvent.getEnvelope().getPayload());

                    Header plh = payload.getHeader();
                    channelHeader = ChannelHeader.parseFrom(plh.getChannelHeader());
                    che = ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());

                    ByteString bs = payload.getData();
                    Transaction tx = Transaction.parseFrom(bs);
                    ChaincodeActionPayload cap =
                            ChaincodeActionPayload.parseFrom(tx.getActions(0).getPayload());
                    ChaincodeProposalPayload cpp =
                            ChaincodeProposalPayload.parseFrom(cap.getChaincodeProposalPayload());


                    // ChaincodeSpec cs = ChaincodeSpec.parseFrom(cpp.getInput());
                    ChaincodeInvocationSpec cs = ChaincodeInvocationSpec.parseFrom(cpp.getInput());
                    List<ByteString> txArgs = cs.getChaincodeSpec().getInput().getArgsList();

                    long seconds = channelHeader.getTimestamp().getSeconds();
                    int nanos = channelHeader.getTimestamp().getNanos();
                    long millis = seconds * 1000 + nanos / 1000000;
                    String txTime = DateUtil.format(millis, "yyyyMMddHHmmss");

                    String chainCodeId = che.getChaincodeId().getName();
                    BlockHeader blockHeader = transactionEvent.getBlock().getHeader();
                    Long bh =  blockHeader.getNumber();
                    ASN1BlockHeader asn1Header = new ASN1BlockHeader(blockHeader.getNumber(),
                            blockHeader.getPreviousHash(), blockHeader.getDataHash());
                    String blockHash = asn1Header.getSHA256Hash();

                    TransactionPO txPO = new TransactionPO();

                    // 从交易数据结构获取
                    txPO.setTxId(txId);
                    txPO.setChaincodeId(chainCodeId);
                    txPO.setTxStatus(txStatus);
                    txPO.setTxTime(txTime);
                    block.setBlockTime(txTime);
                    txPO.setBlockHeight(bh);
                    txPO.setBlockHash(blockHash);
                    txPO.setChannelId(block.getChannelId());
                    if (txArgs == null || txArgs.size() != ARGUMENT_COUNT) {
                        continue;
                    }
                    // 从交易参数列表获取
                    txPO.setTxType(txArgs.get(0).toStringUtf8());
                    txPO.setOrgCode(txArgs.get(1).toStringUtf8());
                    txPO.setAssetUid(txArgs.get(2).toStringUtf8());
                    txPO.setOutTradeNo(txArgs.get(3).toStringUtf8());
                    txPO.setCategory(txArgs.get(4).toStringUtf8());
                    txPO.setPreviousTxId(txArgs.get(5).toStringUtf8());
                    txPO.setBusinessHash(txArgs.get(6).toStringUtf8());
                    txPO.setBizContent(txArgs.get(7).toStringUtf8());

                    txPOs.add(txPO);
                    AssetPO asset = new AssetPO();
                    List<Stage> stages = new ArrayList<Stage>();
                    Stage stage = new Stage();
                    stage.setCategory(txPO.getCategory());
                    stage.setLastestStage(txId);
                    stage.setStageType(txPO.getTxType());
                    stage.setTxTime(txTime);
                    stages.add(stage);
                    asset.setStages(stages);
                    asset.setAssetUid(txPO.getAssetUid());

                    assets.add(asset);
                }
            }
            log.info("transactions解析成功");
        } catch (Exception e) {
            logError.error("解析transaction失败", e);
            throw e;
        }
        return txPOs;
    }

    /**
     * 发布到MQ
     * 
     * @param block
     * @param transactions
     * @throws MQException
     */
    private final static Integer INIT_BLOCK_HEIGHT = 1;

    private void sendMQ(BlockPO block, List<TransactionPO> transactions) throws SystemException {
        try {
            // 第0个区块内不含交易，第1个区块内含有1个deploy交易。所以不发布
            if (block.getBlockHeight() <= INIT_BLOCK_HEIGHT) {
                return;
            }
            // send block
            JSONObject blockObj = (JSONObject) JSON.toJSON(block);
            blockObj.remove("insertTime");
            String jsonBlock = blockObj.toJSONString();
            log.info("推送数据block至activemq：{}", jsonBlock);
            mqServiceImpl.publishBlockMessage(jsonBlock);
            log.info("推送数据block成功");
            // send transaction
            for (TransactionPO tx : transactions) {
                JSONObject txObj = (JSONObject) JSON.toJSON(tx);
                txObj.remove("businessHash");
                txObj.put("txCategory",txObj.get("category"));
                txObj.remove("category");
                String jsonTransaction = txObj.toJSONString();
                log.info("推送数据transaction至activemq：{}", jsonTransaction);
                mqServiceImpl.publishTransactionMessage(jsonTransaction);
            }
            log.info("推送数据transaction至activemq成功");
        } catch (SystemException e) {
            logError.error("推送数据至activemq失败", e);
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 入库
     * 
     * @param block
     * @param transactions
     * @throws SystemException
     */
    private void insertMongo(BlockPO block, List<TransactionPO> transactions,
            List<AssetPO> assets) {
        log.info("block、transactions、asset入库");
        try {
            mongoDaoImpl.addChannel(new ChannelPO(block.getChannelId()));
            mongoDaoImpl.addTransactionList(transactions);

            for (AssetPO asset : assets) {
                // TODO 性能优化，合并插入
                // updateAsset 1、获取 2、合并 3、更新 accetPO。
                // 查找数据库中的asset数据
                AssetPO ast = mongoDaoImpl.findAssetByAssetUid(asset.getAssetUid());
                if (ast == null) {
                    // 无asset则插入
                    mongoDaoImpl.addAsset(asset);
                } else {
                    Stage s = asset.getStages().get(0);
                    List<Stage> stages = ast.getStages();
                    if (stages == null) {
                        // 无stage则插入 理论上stages不会为null
                        stages = new ArrayList<Stage>();
                        stages.add(s);
                    } else {
                        // 有stage则判断是否有category
                        boolean findCategory = false;
                        for (int i = 0; i < stages.size() && !findCategory; i++) {
                            Stage stage = stages.get(i);
                            if (s.getCategory().equals(stage.getCategory())) {
                                // 有category则直接更新lastestStage
                                stage.setLastestStage(s.getLastestStage());
                                stage.setStageType(s.getStageType());
                                findCategory = true;
                            }
                        }
                        if (!findCategory) {
                            // 无category则直接更新lastestStage则插入一条stage
                            stages.add(s);
                        }

                    }
                    mongoDaoImpl.updateAssetStagesByAssetUId(asset.getAssetUid(), stages);
                }
            }
            mongoDaoImpl.addBlock(block);
            log.info("block、transactions、asset入库成功");
        } catch (Exception e) {
            logError.error("block、transactions、asset入库失败", e);
            throw e;
        }
    }
}

