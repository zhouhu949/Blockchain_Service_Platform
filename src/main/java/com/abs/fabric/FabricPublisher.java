package com.abs.fabric;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.ChainCodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.abs.config.ConfigHelper;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.fabric.listener.FabricBlockListener;
import com.abs.loan.service.QueryLoanService;
import com.google.protobuf.InvalidProtocolBufferException;

import io.netty.util.internal.StringUtil;

@SuppressWarnings("rawtypes")
@Component(value = "fabricPublisher")
public class FabricPublisher implements ApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(FabricPublisher.class);
    private static final Logger logError = LoggerFactory.getLogger("operation");

    private HFClient client;
    private Chain chain;
    private ChainCodeID chaincode;
    @Autowired
    private FabricBlockListener fabricBlockListener;
    @Autowired
    private QueryLoanService queryLoanServiceImpl;

    public FabricPublisher() {}

    /**
     * 初始化 HFClient、Chain、Chaincode 并注册 Block listener
     * 
     * @throws Exception
     */
    private void init() throws SystemException, BusinessException {

        // 初始化 HFClient
        logger.info("初始化Fabric client");
        client = HFClient.createNewInstance();
        try {
            client.setUserContext(UserHelper.getUser());
        } catch (SystemException | BusinessException e) {
            throw e;
        } catch (InvalidArgumentException e) {
            logError.error("设置Fabric client用户信息不完整：" + e.getMessage());
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_USER_INCOMPLETE);
        }

        try {
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        } catch (CryptoException | InvalidArgumentException e) {
            logError.error("设置Fabric client加密套件错误：" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CLIENT_INIT_ERROR);
        }

        // 初始化 Chain
        logger.info("初始化chain");
        try {
            String chainName = ConfigHelper.getFabricConfig().getChainName();

            String endorserCount = ConfigHelper.getFabricConfig().getEndorserCount();
            String eventhubCount = ConfigHelper.getFabricConfig().getEventhubCount();
            String ordererCount = ConfigHelper.getFabricConfig().getOrdererCount();

            if (StringUtil.isNullOrEmpty(chainName) || StringUtil.isNullOrEmpty(endorserCount)
                    || StringUtil.isNullOrEmpty(eventhubCount)
                    || StringUtil.isNullOrEmpty(ordererCount)) {
                logError.error("设置Fabric chain信息不完整：");
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
            }

            chain = client.newChain(chainName);

            // 初始化 Endorser 信息
            for (int i = 0; i < Integer.parseInt(endorserCount); i++) {
                String endorserName = ConfigHelper.getFabricConfig().getEndorserName(i);
                String endorserURL = ConfigHelper.getFabricConfig().getEndorserURL(i);
                if (StringUtil.isNullOrEmpty(endorserName)
                        || StringUtil.isNullOrEmpty(endorserURL)) {
                    logError.error("设置Fabric chain信息不完整：");
                    throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
                }
                chain.addPeer(client.newPeer(endorserName, endorserURL));
            }

            // 初始化 Eventhub 信息
            for (int i = 0; i < Integer.parseInt(eventhubCount); i++) {
                String eventhubName = ConfigHelper.getFabricConfig().getEventhubName(i);
                String eventhubURL = ConfigHelper.getFabricConfig().getEventhubURL(i);
                if (StringUtil.isNullOrEmpty(eventhubName)
                        || StringUtil.isNullOrEmpty(eventhubURL)) {
                    logError.error("设置Fabric chain信息不完整：");
                    throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
                }
                chain.addEventHub(client.newEventHub(eventhubName, eventhubURL));
            }

            // 初始化 Orderer 信息
            for (int i = 0; i < Integer.parseInt(ordererCount); i++) {
                String ordererName = ConfigHelper.getFabricConfig().getOrdererName(i);
                String ordererURL = ConfigHelper.getFabricConfig().getOrdererURL(i);
                if (StringUtil.isNullOrEmpty(ordererName) || StringUtil.isNullOrEmpty(ordererURL)) {
                    logError.error("设置Fabric chain信息不完整：");
                    throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
                }
                chain.addOrderer(client.newOrderer(ordererName, ordererURL));
            }

            chain.initialize();
        } catch (InvalidArgumentException e) {
            logError.error("设置Fabric chain信息不完整：" + e.getMessage());
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
        } catch (TransactionException e) {
            logError.error("Fabric chain初始化错误：" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CLIENT_INIT_ERROR);
        }

        // 初始化 Chaincode
        logger.info("初始化chaincode");
        String chaincodeName = ConfigHelper.getFabricConfig().getChaincodeName();
        String chaincodeVersion = ConfigHelper.getFabricConfig().getChaincodeVersion();
        String chaincodePath = ConfigHelper.getFabricConfig().getChaincodePath();
        if (StringUtil.isNullOrEmpty(chaincodeName) || StringUtil.isNullOrEmpty(chaincodeVersion)
                || StringUtil.isNullOrEmpty(chaincodePath)) {
            logError.error("设置Fabric chaincode信息不完整");
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_CHAINCODE_INCOMPLETE);
        }
        chaincode = ChainCodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion)
                .setPath(chaincodePath).build();


        // 同步区块
        syncBlocks();

        // 注册 Block listener
        logger.info("注册 Block listener");
        try {
            chain.registerBlockListener(fabricBlockListener);
        } catch (InvalidArgumentException e) {
            logError.error("Fabric block listener初始化错误：" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_LISTENER_INIT_ERROR);
        }
    }

    /**
     * 同步区块，区块高度编号从0开始
     * 
     * @throws SystemException
     */
    public void syncBlocks() throws SystemException {
        Long maxBlockHeightDB = queryLoanServiceImpl.findMaxBlockHeight();
        BlockchainInfo blockchainInfo = null;
        try {
            blockchainInfo = chain.queryBlockchainInfo();
        } catch (InvalidArgumentException | ProposalException e) {
            logError.error("查询区块链信息错误：" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_BLOCKCHAIN_QUERY_ERROR);
        }
        long maxBlockHeightFabric = blockchainInfo.getHeight() - 1; // 默认从1开始，所以需要减去1

        while (maxBlockHeightDB < maxBlockHeightFabric) {
            for (long i = maxBlockHeightDB + 1; i <= maxBlockHeightFabric; i++) {
                logger.info("同步区块，高度为：{}", i);
                BlockInfo blockInfo = null;
                try {
                    blockInfo = chain.queryBlockByNumber(i);
                } catch (InvalidArgumentException | ProposalException e) {
                    logError.error("查询区块错误：" + e.getMessage());
                    throw new SystemException(ExceptionEnum.BLOCKCHAIN_BLOCKCHAIN_QUERY_ERROR);
                }
                BlockEvent blockEvent = null;
                try {
                    blockEvent = new BlockEvent(blockInfo.getBlock());
                } catch (InvalidProtocolBufferException e) {
                    logError.error("查询区块错误：" + e.getMessage());
                    throw new SystemException(ExceptionEnum.BLOCKCHAIN_BLOCKCHAIN_QUERY_ERROR);
                }
                fabricBlockListener.received(blockEvent);
            }

            // 更新本地区块高度和区块链区块高度
            maxBlockHeightDB = queryLoanServiceImpl.findMaxBlockHeight();
            try {
                blockchainInfo = chain.queryBlockchainInfo();
            } catch (InvalidArgumentException | ProposalException e) {
                logError.error("查询区块链信息错误：" + e.getMessage());
                throw new SystemException(ExceptionEnum.BLOCKCHAIN_BLOCKCHAIN_QUERY_ERROR);
            }
            maxBlockHeightFabric = blockchainInfo.getHeight() - 1; // 默认从1开始，所以需要减去1
        }
    }

    /**
     * 发送交易
     * 
     * @param txType
     * @param orgCode
     * @param assetUID
     * @param outTradeNo
     * @param category
     * @param previousTxID
     * @param businessHash
     * @param bizContent
     * @return txID 交易ID
     * @throws SystemException
     * @throws BusinessException
     */
    public String sendTransaction(String txType, String orgCode, String assetUID, String outTradeNo,
            String category, String previousTxID, String businessHash, String bizContent)
            throws SystemException, BusinessException {
        if (StringUtil.isNullOrEmpty(txType) || StringUtil.isNullOrEmpty(orgCode)
                || StringUtil.isNullOrEmpty(assetUID) || StringUtil.isNullOrEmpty(outTradeNo)
                || StringUtil.isNullOrEmpty(category) || StringUtil.isNullOrEmpty(businessHash)
                || StringUtil.isNullOrEmpty(bizContent)) {
            logError.error("交易参数不完整");
            throw new BusinessException(ExceptionEnum.TX_PARAMETER_INCOMPLETE);
        }

        if (previousTxID == null) {
            logError.error("交易参数不完整");
            throw new BusinessException(ExceptionEnum.TX_PARAMETER_INCOMPLETE);
        }

        String retryCount = ConfigHelper.getFabricConfig().getTxRetryCount();
        String retryInterval = ConfigHelper.getFabricConfig().getTxRetryInterval();
        String minEndorsers = ConfigHelper.getFabricConfig().getChaincodeMinEndorsers();

        if (StringUtil.isNullOrEmpty(retryCount) || StringUtil.isNullOrEmpty(retryInterval)) {
            logError.error("设置Fabric发送交易重试信息不完整");
            throw new SystemException(
                    ExceptionEnum.CONFIG_BLOCKCHAIN_RETRY_INCOMPLETE);
        }

        if (StringUtil.isNullOrEmpty(minEndorsers)) {
            logError.error("设置Fabric chaincode配置信息不完整");
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_CHAINCODE_INCOMPLETE);
        }

        TransactionProposalRequest transactionProposalRequest =
                client.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincode);
        transactionProposalRequest.setFcn(txType);
        transactionProposalRequest.setArgs(new String[] {orgCode, assetUID, outTradeNo, category,
                previousTxID, businessHash, bizContent});

        Collection<ProposalResponse> successResp = new LinkedList<>();
        Collection<Peer> endorsersToSendProposal = new LinkedList<>(chain.getPeers());
        Collection<ProposalResponse> transactionPropResp;

        for (int i = 0; i < Integer.parseInt(retryCount); i++) {
            logger.info("尝试第" + (i + 1) + "次获取背书");
            try {
                transactionPropResp = chain.sendTransactionProposal(transactionProposalRequest,
                        endorsersToSendProposal);
                for (ProposalResponse response : transactionPropResp) {
                    if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                        logger.info("第" + (i + 1) + "次获取背书：节点 " + response.getPeer().getName()
                                + " 返回成功 " + response.getMessage());
                        successResp.add(response);
                        endorsersToSendProposal.removeIf((Peer endorser) -> endorser.getName()
                                .equals(response.getPeer().getName()));
                    } else {
                        BusinessException err =
                                parseBusinessExceptionFromMessage(response.getMessage());
                        if (err.getErrorCode() != ExceptionEnum.CHAINCODE_EXEC_UNKNOW_ERROR
                                .getErrorCode() || i == Integer.parseInt(retryCount) - 1) {
                            logError.error(
                                    "第" + (i + 1) + "次获取背书：节点 " + response.getPeer().getName()
                                            + " 返回异常 " + response.getMessage());
                            throw parseBusinessExceptionFromMessage(response.getMessage());
                        }
                        logger.info("第" + (i + 1) + "次获取背书：节点 " + response.getPeer().getName()
                                + " 返回异常 " + response.getMessage());
                    }
                }
            } catch (ProposalException | InvalidArgumentException e) {
                if (i == Integer.parseInt(retryCount) - 1) {
                    logError.error("第" + (i + 1) + "次获取背书失败：" + e.getMessage());
                    throw new BusinessException(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR);
                }
                logger.info("第" + (i + 1) + "次获取背书失败：" + e.getMessage());
            }

            if (successResp.size() >= Integer.parseInt(minEndorsers)) {
                logger.info("第" + (i + 1) + "次获取背书成功：" + "共获得背书数量 " + successResp.size());
                break;
            } else {
                try {
                    TimeUnit.SECONDS.sleep(Integer.parseInt(retryInterval));
                } catch (NumberFormatException | InterruptedException e) {
                    if (i == Integer.parseInt(retryCount) - 1) {
                        logError.error("第" + (i + 1) + "次获取背书失败：" + e.getMessage());
                        throw new BusinessException(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR);
                    }
                    logger.info("第" + (i + 1) + "次获取背书失败：" + e.getMessage());
                }
            }
        }

        // 理论上讲永远不会执行，因为已经提前抛出异常
        if (successResp.size() < Integer.parseInt(minEndorsers)) {
            logError.error("获取背书失败");
            throw new BusinessException(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR);
        }

        for (int i = 0; i < Integer.parseInt(retryCount); i++) {
            logger.info("第" + (i + 1) + "次发送交易");
            CompletableFuture<TransactionEvent> future = chain.sendTransaction(successResp);
            if (future.isCompletedExceptionally()) {
                if (i == Integer.parseInt(retryCount) - 1) {
                    logError.error("发送交易失败");
                    throw new SystemException(ExceptionEnum.BLOCKCHAIN_TRANSACTION_SEND_ERROR);
                }
                try {
                    logger.info("第" + (i + 1) + "次发送交易失败");
                    TimeUnit.SECONDS.sleep(Integer.parseInt(retryInterval));
                } catch (NumberFormatException | InterruptedException e) {
                    logger.info("第" + (i + 1) + "次发送交易失败：" + e.getMessage());
                }
            } else {
                logger.info("第" + (i + 1) + "次发送交易成功");
                break;
            }
        }

        // 获取 txID
        ProposalResponse response = successResp.iterator().next();
        String txID = response.getTransactionID();
        logger.info("txID: " + txID);
        return txID;
    }

    private BusinessException parseBusinessExceptionFromMessage(String errorMessage) {
        if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_PARAMETERS_NUMBER_INCORRECT.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_PARAMETERS_NUMBER_INCORRECT);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_PUT_STATE_ERROR.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_PUT_STATE_ERROR);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_GET_STATE_ERROR.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_GET_STATE_ERROR);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_NO_STAGE_DEFINITION.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_NO_STAGE_DEFINITION);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_CATEGORY_INCORRECT.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_CATEGORY_INCORRECT);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_NO_WRITE_ACCESS.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_NO_WRITE_ACCESS);
        } else if (errorMessage
                .contains(ExceptionEnum.CHAINCODE_EXEC_INCORRECT_SEQUENCE.getErrorCode())) {
            return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_INCORRECT_SEQUENCE);
        }

        return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_UNKNOW_ERROR);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent arg0) {
        try {
            if (client == null) {
                init();
            }
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
