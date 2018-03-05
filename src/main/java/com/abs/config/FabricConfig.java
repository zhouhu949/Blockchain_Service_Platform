package com.abs.config;

import com.abs.fabric.UserHelper;

public class FabricConfig extends Config{
	public final static String FILE_PATH = "fabric.properties";
	
    private final static String USER_NAME = "fabric.user.name";
    private final static String USER_AFFILIATION = "fabric.user.affiliation";
    private final static String USER_MSPID = "fabric.user.mspid";
    private final static String USER_KEY_PATH = "fabric.user.key.path";
    private final static String USER_CERT_PATH = "fabric.user.cert.path";
    private final static String USER_CERT_TYPE = "fabric.user.cert.type";
    
    private final static String ENDORSER_COUNT = "fabric.endorser.count";
    private final static String ENDORSER_NAME_BASE = "fabric.endorser.name";
    private final static String ENDORSER_URL_BASE = "fabric.endorser.url";
    
    private final static String EVENTHUB_COUNT = "fabric.eventhub.count";
    private final static String EVENTHUB_NAME_BASE = "fabric.eventhub.name";
    private final static String EVENTHUB_URL_BASE = "fabric.eventhub.url";
    
    private final static String ORDERER_COUNT = "fabric.orderer.count";
    private final static String ORDERER_NAME_BASE = "fabric.orderer.name";
    private final static String ORDERER_URL_BASE = "fabric.orderer.url";
    
    private final static String CHAIN_NAME = "fabric.chain.name";
    private final static String CHAINCODE_NAME = "fabric.chaincode.name";
    private final static String CHAINCODE_VERSION = "fabric.chaincode.version";
    private final static String CHAINCODE_PATH = "fabric.chaincode.path";
    private final static String CHAINCODE_SOURCE = "fabric.chaincode.source";
    private final static String CHAINCODE_MIN_ENDORSERS = "fabric.chaincode.minendorsers";
    
    private final static String TX_RETRY_COUNT = "fabric.transation.retry.count";
    private final static String TX_RETRY_INTERVAL = "fabric.transation.retry.interval";
    
    private final static String CONFIGTX_PATH = "fabric.configtx.path";
    private final static String ENDORSEMENT_POLICY_PATH = "fabric.endorsementpolicy.path";
    
    public String getUserName() {
    	return getProperty(USER_NAME);
    }
    
    public String getUserAffiliation() {
    	return getProperty(USER_AFFILIATION);
    }
    
    public String getUserMSPID() {
    	return getProperty(USER_MSPID);
    }
    
    public String getUserKeyPath() {
        String webRootPath = UserHelper.class.getResource("/").getPath();
    	return getProperty(USER_KEY_PATH).replace("classPath", webRootPath);
    }
    
    public String getUserCertPath() {
        String webRootPath = UserHelper.class.getResource("/").getPath();
    	return getProperty(USER_CERT_PATH).replace("classPath", webRootPath);
    }
    
    public String getUserCertType() {
    	return getProperty(USER_CERT_TYPE);
    }
    
    public String getEndorserCount() {
    	return getProperty(ENDORSER_COUNT);
    }
    
    public String getEndorserName(int index) {
    	return getProperty(ENDORSER_NAME_BASE + "." + index);
    }
    
    public String getEndorserURL(int index) {
    	return getProperty(ENDORSER_URL_BASE + "." + index);
    }
    
    public String getEventhubCount() {
    	return getProperty(EVENTHUB_COUNT);
    }
    
    public String getEventhubName(int index) {
    	return getProperty(EVENTHUB_NAME_BASE + "." + index);
    }
    
    public String getEventhubURL(int index) {
    	return getProperty(EVENTHUB_URL_BASE + "." + index);
    }
    
    public String getOrdererCount() {
    	return getProperty(ORDERER_COUNT);
    }
    
    public String getOrdererName(int index) {
    	return getProperty(ORDERER_NAME_BASE + "." + index);
    }
    
    public String getOrdererURL(int index) {
    	return getProperty(ORDERER_URL_BASE + "." + index);
    }
    
    public String getChainName() {
    	return getProperty(CHAIN_NAME);
    }
    
    public String getChaincodeName() {
    	return getProperty(CHAINCODE_NAME);
    }
    
    public String getChaincodeVersion() {
    	return getProperty(CHAINCODE_VERSION);
    }
    
    public String getChaincodePath() {
    	return getProperty(CHAINCODE_PATH);
    }
    
    public String getChaincodeSource() {
    	return getProperty(CHAINCODE_SOURCE);
    }
    
    public String getChaincodeMinEndorsers() {
    	return getProperty(CHAINCODE_MIN_ENDORSERS);
    }
    
    public String getTxRetryCount() {
    	return getProperty(TX_RETRY_COUNT);
    }
    
    public String getTxRetryInterval() {
    	return getProperty(TX_RETRY_INTERVAL);
    }
    
    public String getConfigtxPath() {
        String webRootPath = UserHelper.class.getResource("/").getPath();
    	return getProperty(CONFIGTX_PATH).replace("classPath", webRootPath);
    }
    
    public String getEndorsementPolicyPath() {
        String webRootPath = UserHelper.class.getResource("/").getPath();
    	return getProperty(ENDORSEMENT_POLICY_PATH).replace("classPath", webRootPath);
    }
}

