package com.abs.config;

public class MQConfig extends Config {
    public final static String FILE_PATH = "activemq.properties";

    private final static String BROKER_IP = "activemq.ip";
    private final static String BROKER_PASSWORD = "activemq.password";
    private final static String BROKER_USER_NAME = "activemq.userName";
    private final static String BROKER_USEASYNCSEND = "activemq.useAsyncSend";
    private final static String BROKER_TOPICNAME_BLOCK = "activemq.topicName.block";
    private final static String BROKER_SESSIONCACHESIZE = "activemq.sessionCacheSize";
    private final static String BROKER_TOPICNAME_TRANSACTION = "activemq.topicName.transaction";

    public String getBrokerIp() {
        return getProperty(BROKER_IP);
    }

    public String getUserName() {
        return getProperty(BROKER_USER_NAME);
    }

    public String getPassword() {
        return getProperty(BROKER_PASSWORD);
    }

    public String getTopicNameTransaction() {
        return getProperty(BROKER_TOPICNAME_TRANSACTION);
    }

    public String getTopicNameBlock() {
        return getProperty(BROKER_TOPICNAME_BLOCK);
    }

    public String getSessionCacheSize() {
        return getProperty(BROKER_SESSIONCACHESIZE);
    }

    public String getUseAsyncSend() {
        return getProperty(BROKER_USEASYNCSEND);
    }
}

// end
