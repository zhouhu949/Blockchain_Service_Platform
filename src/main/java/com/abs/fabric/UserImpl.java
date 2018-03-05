package com.abs.fabric;

import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class UserImpl implements User{
	private String name;
	private Set<String> roles;
	private String account;
	private String affiliation;
	private String organization;
	Enrollment enrollment;
	private String MSPID;
	
	/**
	 * 初始化用户，此四个参数为 Fabric Java SDK UserContext 校验所必须
	 * @param name  用户名
	 * @param affiliation 用户组织
	 * @param mspid 用户所属 MSP ID
	 * @param enrollment 用户 enroll 信息，包括私钥、公钥、E-Cert 等
	 */
	public UserImpl(String name, String affiliation, String mspid, Enrollment enrollment) {
		this.name = name;
		this.affiliation = affiliation;
		this.MSPID = mspid;
		this.enrollment = enrollment;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	@Override
	public String getMSPID() {
		return this.MSPID;
	}

	public void setMSPID(String mspid) {
		this.MSPID = mspid;
	}
}
