package com.abs.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 该算法计算消息摘要 顺序有关
 */
public class SHAHashUtil {

    /**
     * @param strSrc 要加密的字符串
     * @return SHA-256加密算法的字符串
     */
    public static String encryptSHA256(String strSrc) {
        if (strSrc == null || strSrc.trim().length() == 0) {
            return null;
        }
        return encrypt(strSrc, "SHA-256");
    }

    public static String encryptSHA256(byte[] bt) {
        if (bt == null)
            return null;
        return encryptSHA256(bt, "SHA-256");
    }

    /**
     * 对字符串加密,加密算法使用MD5, SHA-1、SHA-224、SHA-256、SHA-384、SHA-512。
     * 
     * 主要适用于数字签名标准（DigitalSignature Standard DSS）里面定义的数字签名算法（Digital Signature Algorithm DSA）。
     * 
     * @param strSrc 要加密的字符串
     * @param encName 加密类型
     * @return
     */
    private static String encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bts.length; i++) {
            String hex = Integer.toHexString(bts[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static String encryptSHA256(byte[] bt, String name) {
        MessageDigest md = null;
        String strDes = null;
        try {
            md = MessageDigest.getInstance(name);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static void main(String args[]) {
        String s = SHAHashUtil.encryptSHA256("sha-256");
        System.out.println(s);
        System.out.println(s.length());
    }
}
