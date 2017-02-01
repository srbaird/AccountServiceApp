/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.User;

/**
 *
 * @author user0001
 */
public class SimpleUser implements User {

	private Integer id;
	private String userName;
	private String userKey;
	private byte[] userPassword;
	private byte[] passwordSalt;
	private Date createDate;
	private Set<? extends Account> accounts;
	private boolean isEnabled;

    // logger    
    private static final Logger logger = LoggerFactory.getLogger(SimpleUser.class);
    
	public SimpleUser() {

	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getUserKey() {
		return userKey;
	}

	@Override
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	@Override
	public byte[] getUserPassword() {
		return userPassword;
	}

	@Override
	public void setUserPassword(byte[] userPassword) {
		this.userPassword = userPassword;
	}

	@Override
	public byte[] getPasswordSalt() {
		return passwordSalt;
	}

	@Override
	public void setPasswordSalt(byte[] pSalt) {
		this.passwordSalt = pSalt;
	}


	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public Set<? extends Account> getAccounts() {
		return accounts;
	}

	/*
	 * Restrict any set of Accounts to SimpleAccounts
	 */
	@Override
	public void setAccounts(Set<? extends Account> accounts) {

		logger.debug("Setting accounts to: {}", accounts);
		this.accounts = accounts == null ? null
				: accounts.stream().map(a -> SimpleComponentFactory.getAccount(a)).collect(Collectors.toSet());
	}

	@Override
	public void setEnabled(boolean isEnabled) {

		this.isEnabled = isEnabled;
	}

	@Override
	public boolean isEnabled() {

		return isEnabled;
	}
}
