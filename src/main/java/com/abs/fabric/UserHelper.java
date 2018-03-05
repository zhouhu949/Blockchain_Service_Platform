package com.abs.fabric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import com.abs.config.ConfigHelper;
import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.exception.SystemException;

import io.netty.util.internal.StringUtil;

public class UserHelper {
	private static final Logger logger = Logger.getLogger(UserHelper.class);
	
	private static User user;
	
	/**
	 * 获取 User 单例，若为空则从文件中加载
	 * @return User 单例
	 * @throws SystemException 
	 * @throws BusinessException 
	 */
    public static User getUser() throws SystemException, BusinessException {
        if (user == null) {
            synchronized (UserHelper.class) {
                if (user == null) {
                	loadUser();
                }
            }
        }
        return user;
    }
    
    /**
     * 从配置文件中加载用户信息
     * @throws SystemException 
     * @throws BusinessException 
     */
    private static void loadUser() throws SystemException, BusinessException {
    	String name = ConfigHelper.getFabricConfig().getUserName();
    	String affilication = ConfigHelper.getFabricConfig().getUserAffiliation();
    	String mspid = ConfigHelper.getFabricConfig().getUserMSPID();
    	String keyPath = ConfigHelper.getFabricConfig().getUserKeyPath();
    	String certPath = ConfigHelper.getFabricConfig().getUserCertPath();
    	String certType = ConfigHelper.getFabricConfig().getUserCertType();
    	
    	if (StringUtil.isNullOrEmpty(name) || StringUtil.isNullOrEmpty(affilication) || StringUtil.isNullOrEmpty(mspid) 
    			|| StringUtil.isNullOrEmpty(keyPath) || StringUtil.isNullOrEmpty(certPath) || StringUtil.isNullOrEmpty(certType)) {
    		throw new SystemException(ExceptionEnum.CONFIG_BLOCKCHAIN_USER_INCOMPLETE);
    	}
    	
    	Enrollment enrollment = getEnrollmentFromFile(keyPath, certPath, certType);
    	user = new UserImpl(name, affilication, mspid, enrollment);
    	logger.info("从配置文件中加载用户信息完毕：" + name);
    }
    
    /**
     * 根据私钥、证书路径及证书类型读取私钥、证书，并包装为 Enrollment 类型
     * @param keyPath
     * @param certPath
     * @param certType
     * @return
     * @throws BusinessException 
     */
    private static Enrollment getEnrollmentFromFile(String keyPath, String certPath, String certType) throws BusinessException {
    	Security.addProvider(new BouncyCastleProvider());
    	// 读取私钥
    	File keyFile = new File(keyPath);
    	PEMParser pemParser = null;
    	KeyPair keyPair = null;
    	try {
    		pemParser = new PEMParser(new FileReader(keyFile));
			Object keyParsed = pemParser.readObject();
			if (keyParsed instanceof PEMKeyPair) {
				keyPair = new JcaPEMKeyConverter().getKeyPair((PEMKeyPair) keyParsed);
			} else {
				throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_KEY_PARSE_ERROR);
			}
		} catch (IOException e) {
			throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_KEY_PARSE_ERROR);
		} finally {
			try {
				if (pemParser != null) {
					pemParser.close();
				}
			} catch (IOException e) {
				throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_KEY_PARSE_ERROR);
			}
		}
    	
    	// 读取证书
    	File certFile = new File(certPath);
    	X509Certificate cert = null;
    	try {
    		pemParser = new PEMParser(new FileReader(certFile));
			Object certParsed = pemParser.readObject();
			if (certParsed instanceof X509CertificateHolder) {
				cert = new JcaX509CertificateConverter().getCertificate((X509CertificateHolder) certParsed);
			} else {
				throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_CERT_PARSE_ERROR);
			}
		} catch (IOException | CertificateException e) {
			throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_CERT_PARSE_ERROR);
		} finally {
			try {
				pemParser.close();
			} catch (IOException e) {
				throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_CERT_PARSE_ERROR);
			}
		}
    	
    	// 判断私钥和证书对应的公钥是否相等
    	if (!Hex.toHexString(keyPair.getPublic().getEncoded()).equals(Hex.toHexString(cert.getPublicKey().getEncoded()))) {
    		throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_KEY_CERT_MISMATCH);
    	}
    	
    	PrivateKey key = keyPair.getPrivate();
    	String publicKeyString = Hex.toHexString(keyPair.getPublic().getEncoded());
    	StringWriter certStringWriter = null;
    	try {
    	    certStringWriter = new StringWriter();
    	    JcaPEMWriter pemWriter = new JcaPEMWriter(certStringWriter);
    	    pemWriter.writeObject(cert);
    	    pemWriter.close();
		} catch (IOException e) {
			throw new BusinessException(ExceptionEnum.BLOCKCHAIN_USER_CERT_PARSE_ERROR);
		}
    	
    	logger.info("从文件中读取私钥、证书信息完毕");
    	return new EnrollmentImpl(key, certStringWriter.toString(), publicKeyString);
    }

}
