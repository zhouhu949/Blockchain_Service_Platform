package com.deploy.fabric;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.ChainCodeID;
import org.hyperledger.fabric.sdk.ChainConfiguration;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.UpgradeProposalRequest;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
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

import io.netty.util.internal.StringUtil;
public class FabricAdmin {
    private static final Logger logger = LoggerFactory.getLogger(FabricAdmin.class);
    private static final Logger logError = LoggerFactory.getLogger("operation");
    
    private HFClient client;
    private Chain chain;
    private ChainCodeID chaincode;
    private AdminInitType initType;
    private static FabricAdmin fabricAdmin;
    
    public FabricAdmin(AdminInitType initType) throws SystemException, BusinessException {
    	init(initType);
    }

    /**
     * 初始化Fabirc admin
     * @throws SystemException
     * @throws BusinessException
     */
    private void init(AdminInitType initType) throws SystemException, BusinessException {
    	logger.info("初始化Fabric admin，初始化类型 为：" + initType.toString());
    	
        //初始化HFClient
        initClient();
        
        //初始化chain
        logger.info("初始化chain");
        Collection<Orderer> orderers = getOrdererInfo();
        Collection<Peer> endorsers = getEndorserInfo();
        Collection<EventHub> eventhubs = getEventhubInfo();
        
		String chainName = ConfigHelper.getFabricConfig().getChainName();
		switch (initType) {
		case INIT_ONLY:
		case INIT_WITH_JOIN_PEER:
			try {
				chain = client.newChain(chainName);
			} catch (InvalidArgumentException e) {
				logError.error("Fabric chain初始化错误：" + e.getMessage());
				throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAIN_INIT_ERROR);
			}
			break;
		case INIT_WITH_CREATE_CHANNEL_JOIN_PEER:
			String configtxPath = ConfigHelper.getFabricConfig().getConfigtxPath();
			ChainConfiguration chainConfiguration;
			try {
				chainConfiguration = new ChainConfiguration(new File(configtxPath));
				Orderer orderer0 = orderers.iterator().next();
				orderers.remove(orderer0);
				chain = client.newChain(chainName, orderer0, chainConfiguration);
			} catch (IOException | InvalidArgumentException | TransactionException e) {
				logError.error("Fabric chain初始化错误：" + e.getMessage());
				throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAIN_INIT_ERROR);
			}
			break;
		default:
			logError.error("Admin初始化类型参数错误");
			throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
		}
        
		// 加入 orderer
		for (Orderer orderer : orderers) {
			try {
				chain.addOrderer(orderer);
			} catch (InvalidArgumentException e) {
                logError.error("设置Fabric节点信息不完整：" + e.getMessage());
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
			}
		}
		
		// 加入 endorser
		switch (initType) {
		case INIT_ONLY:
			// 加入 endorser
			for (Peer endorser : endorsers) {
				try {
					chain.addPeer(endorser);
				} catch (InvalidArgumentException e) {
	                logError.error("设置Fabric节点信息不完整：" + e.getMessage());
	                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
				}
			}
			break;
		case INIT_WITH_JOIN_PEER:
		case INIT_WITH_CREATE_CHANNEL_JOIN_PEER:
			// 加入 endorser至channel, 必须属于同一 org，即 MSPID 需一致
			for (Peer endorser : endorsers) {
				if (endorser.getName().startsWith(client.getUserContext().getAffiliation())) {
					try {
						chain.joinPeer(endorser);
					} catch (ProposalException e) {
			            logError.error("Fabric chain初始化错误：" + e.getMessage());
			            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAIN_INIT_ERROR);
					}
				}
			}
			break;
		default:
			logError.error("Admin初始化类型参数错误");
			throw new BusinessException(ExceptionEnum.REQUEST_PARAMETER_ERROR);
		}
		
		// 加入 eventhub
		for (EventHub eventhub : eventhubs) {
			if (eventhub.getName().startsWith(client.getUserContext().getAffiliation())) {
				try {
					chain.addEventHub(eventhub);
				} catch (InvalidArgumentException e) {
	                logError.error("设置Fabric节点信息不完整：" + e.getMessage());
	                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
				}
			}
		}
		
		try {
			chain.initialize();
		} catch (InvalidArgumentException | TransactionException e) {
			logError.error("Fabric chain初始化错误：" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CLIENT_INIT_ERROR);
		}
		
		initChaincode();
    }
   
    private void initClient() throws SystemException, BusinessException {
        //初始化HFClient
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
    }
    
    private void initChaincode() throws SystemException {
        //初始化Chaincode
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
    
    private Collection<Orderer> getOrdererInfo() throws SystemException {
        String ordererCount = ConfigHelper.getFabricConfig().getOrdererCount();
        if (StringUtil.isNullOrEmpty(ordererCount)) {
            logError.error("设置Fabric节点信息不完整：");
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
        }
        Collection<Orderer> orderers = new LinkedList<>();
        
        for (int i = 0; i < Integer.parseInt(ordererCount); i++) {
        	String ordererName = ConfigHelper.getFabricConfig().getOrdererName(i);
        	String ordererURL = ConfigHelper.getFabricConfig().getOrdererURL(i);
            if (StringUtil.isNullOrEmpty(ordererName) || StringUtil.isNullOrEmpty(ordererURL)) {
                logError.error("设置Fabric节点信息不完整：");
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
            }
            try {
				orderers.add(client.newOrderer(ordererName, ordererURL));
			} catch (InvalidArgumentException e) {
                logError.error("设置Fabric节点信息不完整：" + e.getMessage());
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
			}
        }
        
        return orderers;
    }
    
    private Collection<Peer> getEndorserInfo() throws SystemException {
        String endorserCount = ConfigHelper.getFabricConfig().getEndorserCount();
        if (StringUtil.isNullOrEmpty(endorserCount)) {
            logError.error("设置Fabric节点信息不完整：");
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
        }
        Collection<Peer> endorsers = new LinkedList<>();
        
        for (int i = 0; i < Integer.parseInt(endorserCount); i++) {
        	String endorserName = ConfigHelper.getFabricConfig().getEndorserName(i);
        	String endorserURL = ConfigHelper.getFabricConfig().getEndorserURL(i);
            if (StringUtil.isNullOrEmpty(endorserName) || StringUtil.isNullOrEmpty(endorserURL)) {
                logError.error("设置Fabric节点信息不完整：");
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
            }
            try {
				endorsers.add(client.newPeer(endorserName, endorserURL));
			} catch (InvalidArgumentException e) {
                logError.error("设置Fabric节点信息不完整：" + e.getMessage());
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
			}
        }
        
        return endorsers;
    }
    
    private Collection<EventHub> getEventhubInfo() throws SystemException {
    	String eventhubCount = ConfigHelper.getFabricConfig().getEventhubCount(); 
        if (StringUtil.isNullOrEmpty(eventhubCount)) {
            logError.error("设置Fabric节点信息不完整：");
            throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
        }
    	
    	Collection<EventHub> eventhubs = new LinkedList<>();
        
        for (int i = 0; i < Integer.parseInt(eventhubCount); i++) {
        	String eventhubName = ConfigHelper.getFabricConfig().getEventhubName(i);
        	String eventhubURL = ConfigHelper.getFabricConfig().getEventhubURL(i);
            if (StringUtil.isNullOrEmpty(eventhubName) || StringUtil.isNullOrEmpty(eventhubURL)) {
                logError.error("设置Fabric节点信息不完整：");
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
            }
            try {
				eventhubs.add(client.newEventHub(eventhubName, eventhubURL));
			} catch (InvalidArgumentException e) {
                logError.error("设置Fabric节点信息不完整：" + e.getMessage());
                throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_NODE_INCOMPLETE);
			}
        }
        
        return eventhubs;
    }
    
    /**
     * 获取Fabric Admin
     * 
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public static FabricAdmin getFabricAdmin(AdminInitType initType) throws SystemException, BusinessException {
        if (fabricAdmin == null || fabricAdmin.initType != initType) {
            synchronized (FabricAdmin.class) {
                if (fabricAdmin == null || fabricAdmin.initType != initType) {
                    fabricAdmin = new FabricAdmin(initType);
                }
            }
        }
        return fabricAdmin;
    }

    /**
     * 安装chaincode到endorser，注意endorse的MSPID需与user一致
     * 
     * @return chaincode名称
     * @throws SystemException 
     */
    public String installChaincode() throws SystemException {
    	logger.info("安装chaincode");
        InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(chaincode);
        installProposalRequest.setChaincodeSourceLocation(new File(ConfigHelper.getFabricConfig().getChaincodeSource()));
        installProposalRequest.setChaincodeVersion(chaincode.getVersion());

        Collection<Peer> endorsersInSameOrg = new LinkedList<>();
		for (Peer endorser : chain.getPeers()) {
			if (endorser.getName().startsWith(client.getUserContext().getAffiliation())) {
				endorsersInSameOrg.add(endorser);
			}
		}
		
		try {
			Collection<ProposalResponse> responses = chain.sendInstallProposal(installProposalRequest, endorsersInSameOrg);
			for (ProposalResponse resp : responses) {
				logger.info("安装chaincode至 " + resp.getPeer().getName() + " ，安装结果：" + resp.getMessage());
			}
		} catch (ProposalException | InvalidArgumentException e) {
            logError.error("安装chaincode错误" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAINCODE_INSTALLATION_ERROR);
		}

        return chaincode.getName();
    }
    
    /**
     * 实例化chaincode
     * 
     * @return
     * @throws SystemException 
     */
    public String instantiateChaincode() throws SystemException {
    	logger.info("实例化chaincode");
        InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chaincode);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[] {});

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        try {
			chaincodeEndorsementPolicy.fromYamlFile(new File(ConfigHelper.getFabricConfig().getEndorsementPolicyPath()));
		} catch (ChaincodeEndorsementPolicyParseException | IOException e) {
            logError.error("实例化chaincode错误" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAINCODE_INSTANTIATE_ERROR);
		}
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Collection<ProposalResponse> responses = null;
		try {
			responses = chain.sendInstantiationProposal(instantiateProposalRequest, chain.getPeers());
			for (ProposalResponse resp : responses) {
				logger.info("实例化chaincode至 " + resp.getPeer().getName() + " ，结果：" + resp.getMessage());
				if (resp.getStatus() != ProposalResponse.Status.SUCCESS) {
		            logError.error("实例化chaincode错误," + resp.getMessage());
		            throw new SystemException(ExceptionEnum.BLOCKCHAIN_TRANSACTION_SEND_ERROR);
				}
			}
			chain.sendTransaction(responses, chain.getOrderers());
		} catch (ProposalException | InvalidArgumentException e) {
            logError.error("实例化chaincode错误" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAINCODE_INSTANTIATE_ERROR);
		}
		
		ProposalResponse response = responses.iterator().next();
		String txID = response.getTransactionID();
		logger.info("txID: " + txID);
		return txID;
    }
    
    /**
     * 升级chaincode
     * 
     * @return
     * @throws SystemException 
     */
    public String upgradeChaincode() throws SystemException {
    	logger.info("升级chaincode");
    	UpgradeProposalRequest upgradeProposalRequest = client.newUpgradeProposalRequest();
        upgradeProposalRequest.setChaincodeID(chaincode);
        upgradeProposalRequest.setFcn("init");
        upgradeProposalRequest.setArgs(new String[] {});

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        try {
			chaincodeEndorsementPolicy.fromYamlFile(new File(ConfigHelper.getFabricConfig().getEndorsementPolicyPath()));
		} catch (ChaincodeEndorsementPolicyParseException | IOException e) {
            logError.error("升级chaincode错误" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAINCODE_UPGRADE_ERROR);
		}
        upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Collection<ProposalResponse> responses = null;
		try {
			responses = chain.sendUpgradeProposal(upgradeProposalRequest);
			for (ProposalResponse resp : responses) {
				logger.info("升级chaincode至 " + resp.getPeer().getName() + " ，结果：" + resp.getMessage());
				if (resp.getStatus() != ProposalResponse.Status.SUCCESS) {
		            logError.error("升级chaincode错误" + resp.getMessage());
		            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAINCODE_UPGRADE_ERROR);
				}
			}
			chain.sendTransaction(responses, chain.getOrderers());
		} catch (ProposalException | InvalidArgumentException e) {
            logError.error("升级chaincode错误" + e.getMessage());
            throw new SystemException(ExceptionEnum.BLOCKCHAIN_CHAINCODE_UPGRADE_ERROR);
		}
		
		ProposalResponse response = responses.iterator().next();
		String txID = response.getTransactionID();
		logger.info("txID: " + txID);
		return txID;
    }
}
