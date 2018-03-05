package com.test.fabric;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.protos.common.Common.BlockData;
import org.hyperledger.fabric.protos.common.Common.ChannelHeader;
import org.hyperledger.fabric.protos.common.Common.Envelope;
import org.hyperledger.fabric.protos.common.Common.Header;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.ChainCodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abs.config.ConfigHelper;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;
import com.abs.fabric.UserHelper;
import com.abs.fabric.listener.ASN1BlockHeader;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.netty.util.internal.StringUtil;
public class FabricPublisherSingleton {
    private static final Logger logger = LoggerFactory.getLogger(FabricPublisherSingleton.class);
    private static final Logger logError = LoggerFactory.getLogger("operation");
    
    private HFClient client;
    private Chain chain;
    private ChainCodeID chaincode;
    private static FabricPublisherSingleton fabricPublisher;
    public FabricPublisherSingleton() {
        try {
            init();
        } catch (SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

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
                    || StringUtil.isNullOrEmpty(eventhubCount) || StringUtil.isNullOrEmpty(ordererCount)) {
                logError.error("设置Fabric chain信息不完整：");
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
            }
            
            chain = client.newChain(chainName);
            
            // 初始化 Endorser 信息
            for (int i = 0; i < Integer.parseInt(endorserCount); i++) {
            	String endorserName = ConfigHelper.getFabricConfig().getEndorserName(i);
            	String endorserURL = ConfigHelper.getFabricConfig().getEndorserURL(i);
                if (StringUtil.isNullOrEmpty(endorserName) || StringUtil.isNullOrEmpty(endorserURL)) {
                    logError.error("设置Fabric chain信息不完整：");
                    throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
                }
                chain.addPeer(client.newPeer(endorserName, endorserURL));
            }
            
            // 初始化 Eventhub 信息
            for (int i = 0; i < Integer.parseInt(eventhubCount); i++) {
            	String eventhubName = ConfigHelper.getFabricConfig().getEventhubName(i);
            	String eventhubURL = ConfigHelper.getFabricConfig().getEventhubURL(i);
                if (StringUtil.isNullOrEmpty(eventhubName) || StringUtil.isNullOrEmpty(eventhubURL)) {
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
        chaincode = ChainCodeID.newBuilder().setName(chaincodeName)
                .setVersion(chaincodeVersion).setPath(chaincodePath).build();
    }

    /**
     * 获取 Fabric Publisher
     * 
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public static FabricPublisherSingleton getFabricPublisher() throws SystemException, BusinessException {
        if (fabricPublisher == null) {
            synchronized (FabricPublisherSingleton.class) {
                if (fabricPublisher == null) {
                    fabricPublisher = new FabricPublisherSingleton();
                }
            }
        }
        return fabricPublisher;
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
	public String sendInvokeTransaction(String txType, String orgCode, String assetUID, String outTradeNo, String category,
			String previousTxID, String businessHash, String bizContent) throws SystemException, BusinessException {
		if (StringUtil.isNullOrEmpty(txType) || StringUtil.isNullOrEmpty(orgCode) || StringUtil.isNullOrEmpty(assetUID)
				|| StringUtil.isNullOrEmpty(outTradeNo) || StringUtil.isNullOrEmpty(category)
				|| StringUtil.isNullOrEmpty(businessHash) || StringUtil.isNullOrEmpty(bizContent)) {
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
			throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_RETRY_INCOMPLETE);
		}

		if (StringUtil.isNullOrEmpty(minEndorsers)) {
			logError.error("设置Fabric chaincode配置信息不完整");
			throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_CHAINCODE_INCOMPLETE);
		}

		TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
		transactionProposalRequest.setChaincodeID(chaincode);
		transactionProposalRequest.setFcn(txType);
		transactionProposalRequest.setArgs(
				new String[] { orgCode, assetUID, outTradeNo, category, previousTxID, businessHash, bizContent });

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
						logger.info(
								"第" + (i + 1) + "次获取背书：节点 " + response.getPeer().getName() + " 返回成功 " + response.getMessage());
						successResp.add(response);
						endorsersToSendProposal
								.removeIf((Peer endorser) -> endorser.getName().equals(response.getPeer().getName()));
					} else {
						if (i == Integer.parseInt(retryCount) - 1) {
							logError.error(
									"第" + (i + 1) + "次获取背书：节点 " + response.getPeer().getName() + " 返回异常 " + response.getMessage());
							throw parseBusinessExceptionFromMessage(response.getMessage());
						}
						logger.info(
								"第" + (i + 1) + "次获取背书：节点 " + response.getPeer().getName() + " 返回异常 " + response.getMessage());
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
		if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_PARAMETERS_NUMBER_INCORRECT.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_PARAMETERS_NUMBER_INCORRECT);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_PUT_STATE_ERROR.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_PUT_STATE_ERROR);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_GET_STATE_ERROR.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_GET_STATE_ERROR);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_NO_STAGE_DEFINITION.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_NO_STAGE_DEFINITION);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_CATEGORY_INCORRECT.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_CATEGORY_INCORRECT);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_NO_WRITE_ACCESS.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_NO_WRITE_ACCESS);
		} else if (errorMessage.contains(ExceptionEnum.CHAINCODE_EXEC_INCORRECT_SEQUENCE.getErrorCode())) {
			return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_INCORRECT_SEQUENCE);
		}
		
		return new BusinessException(ExceptionEnum.CHAINCODE_EXEC_OPERATION_ERROR);
	}

	/**
	 * QUERY draft
	 * @param queryKey
	 * @return
	 */
	public String sendQueryTransaction(String queryKey) {
		TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
		transactionProposalRequest.setChaincodeID(chaincode);
		transactionProposalRequest.setFcn("QUERY");
		transactionProposalRequest.setArgs(
				new String[] { queryKey });

		Collection<Peer> endorsersToSendProposal = new LinkedList<>(chain.getPeers());
		Collection<ProposalResponse> transactionPropResp;

		try {
			transactionPropResp = chain.sendTransactionProposal(transactionProposalRequest,
					endorsersToSendProposal);
			for (ProposalResponse response : transactionPropResp) {
				return response.getMessage();
			}
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}
	
    /**
     * 查询当前区块链最新区块哈希
     * 
     * @return
     * @throws BusinessException
     */
    public String getCurrentBlockHash() throws BusinessException {
        BlockchainInfo blockchainInfo = null;
        try {
            blockchainInfo = chain.queryBlockchainInfo();
        } catch (InvalidArgumentException | ProposalException e) {
            logError.error("查询区块哈希错误：" + e.getMessage());
            // throw new BusinessException(ExceptionEnum.FABRIC_BLOCK_HASH_QUERY_ERROR);
        }
        String currentBlockHash = Hex.toHexString(blockchainInfo.getCurrentBlockHash());
        String previousBlockHash = Hex.toHexString(blockchainInfo.getPreviousBlockHash());
        long currentBlockHeight = blockchainInfo.getHeight();
        logger.info("当前区块个数为: " + currentBlockHeight);
        logger.info("当前区块哈希为: " + currentBlockHash);
        logger.info("前序区块哈希为: " + previousBlockHash);
        return currentBlockHash;
    }

    public String getBlockHashByHeight(int blockHeight) throws BusinessException, InvalidProtocolBufferException {
        BlockInfo blockInfo = null;
        try {
            blockInfo = chain.queryBlockByNumber(blockHeight);
        } catch (InvalidArgumentException | ProposalException e) {
            logError.error("查询区块错误：" + e.getMessage());
            // throw new BusinessException(ExceptionEnum.FABRIC_BLOCK_HASH_QUERY_ERROR);
        }

        long number = blockInfo.getBlock().getHeader().getNumber();
        ByteString previousHash = blockInfo.getBlock().getHeader().getPreviousHash();
        ByteString dataHash = blockInfo.getBlock().getHeader().getDataHash();
        logger.info("查询区块高度为: " + number);
        
        BlockData blockData = blockInfo.getBlock().getData();
        int blockIndex = -1;
        for (ByteString txData : blockData.getDataList()) {
            blockIndex++;
            logger.info("txIndex: " + blockIndex);
            Envelope txEnvelope = Envelope.parseFrom(txData);
            Payload payload = Payload.parseFrom(txEnvelope.getPayload());
            Header plh = payload.getHeader();
            ChannelHeader channelHeader = ChannelHeader.parseFrom(plh.getChannelHeader());
            String txID = channelHeader.getTxId();
            logger.info("txID: " + txID);
        }
        
        ASN1BlockHeader blockHeader = new ASN1BlockHeader(number, previousHash, dataHash);
        String blockHash = blockHeader.getSHA256Hash();
        logger.info("查询区块哈希为: " + blockHash);
        return blockHash;
    }

    public String sendTransactionTest() {
        TransactionProposalRequest transactionProposalRequest =
                client.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincode);
        transactionProposalRequest.setFcn("invoke");
        transactionProposalRequest.setArgs(new String[] {"a", "b", "1"});

        Collection<ProposalResponse> successResp = new LinkedList<>();
        Collection<ProposalResponse> failResp = new LinkedList<>();
        Collection<ProposalResponse> transactionPropResp;
        try {
            transactionPropResp =
                    chain.sendTransactionProposal(transactionProposalRequest, chain.getPeers());
            for (ProposalResponse response : transactionPropResp) {
                if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                    System.out.println("Successful transaction proposal response Txid: "
                            + response.getTransactionID() + " From "
                            + response.getPeer().getName());
                    successResp.add(response);
                } else {
                    failResp.add(response);
                    System.out.println("Failed transaction proposal response Txid: "
                            + response.getTransactionID() + " From "
                            + response.getPeer().getName());
                }
            }

            chain.sendTransaction(successResp);
        } catch (ProposalException | InvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public String queryTransactionTest() throws InvalidArgumentException, ProposalException {
        final ChainCodeID chainCodeID;
        final String CHAIN_CODE_NAME = "mycc";
        final String CHAIN_CODE_PATH =
                "github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02";
        final String CHAIN_CODE_VERSION = "1.0";

        chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION).setPath(CHAIN_CODE_PATH).build();

        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(new String[] {"b"});
        queryByChaincodeRequest.setFcn("query");
        queryByChaincodeRequest.setChaincodeID(chainCodeID);

        Collection<ProposalResponse> queryProposals =
                chain.queryByChaincode(queryByChaincodeRequest, chain.getPeers());
        for (ProposalResponse proposalResponse : queryProposals) {
            if (!proposalResponse.isVerified()
                    || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                logError.error(
                        "Failed query proposal from peer " + proposalResponse.getPeer().getName()
                                + " status: " + proposalResponse.getStatus() + ". Messages: "
                                + proposalResponse.getMessage() + ". Was verified : "
                                + proposalResponse.isVerified());
            } else {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload()
                        .toStringUtf8();
                logger.info("Query payload of a from peer "
                        + proposalResponse.getPeer().getName() + " gets " + payload);
            }
        }
        return null;
    }
}
