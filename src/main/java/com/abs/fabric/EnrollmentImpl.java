package com.abs.fabric;

import java.security.PrivateKey;

import org.hyperledger.fabric.sdk.Enrollment;

public class EnrollmentImpl implements Enrollment {
	/** 私钥 */
	private PrivateKey key;
	/** 证书，Base64 PEM 格式 */
	private String cert;
	/** 公钥，Hex 编码格式 */
	private String publicKey;
	
	/**
	 * 初始化用户 enroll 信息
	 * @param key
	 * @param cert
	 * @param publicKey
	 */
	public EnrollmentImpl(PrivateKey key, String cert, String publicKey) {
		this.key = key;
		this.cert = cert;
		this.publicKey = publicKey;
	}

	public void setKey(PrivateKey key) {
		this.key = key;
	}
	
	@Override
	public PrivateKey getKey() {
		return key;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}
	
	@Override
	public String getCert() {
		return cert;
	}
	
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String getPublicKey() {
		return publicKey;
	}
}
