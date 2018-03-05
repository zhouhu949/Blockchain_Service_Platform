package com.deploy.fabric;

/**
 * Fabric client初始化类型
 *
 */
public enum AdminInitType {
	/** 只初始化client */
	INIT_ONLY,
	/** 初始化client，并在初始化过程中将同一组织的endorser加入channel */
	INIT_WITH_JOIN_PEER,
	/** 初始化client，并在初始化过程中创建channel，并将同一组织的endorser加入channel */
	INIT_WITH_CREATE_CHANNEL_JOIN_PEER
}
