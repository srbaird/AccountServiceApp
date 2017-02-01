/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import static com.bac.accountservice.AccountServiceAuthenticationOutcome.NO_ROLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Before;

import com.bac.accountservice.AccountServiceAuthentication;
import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import com.bac.accountservice.AccountServiceRole;
import com.bac.accountservice.SimpleAccountServiceAuthentication;
import com.bac.accountserviceapp.Application;

/**
 *
 * @author user0001
 */
public class AccountServiceAppTestNativeAuthentication
		extends AbstractAccountServiceAppTestAuthentication<AccountServiceAuthentication> {

	private AccountServiceAuthentication authentication;

	@Before
	public void setAuthentication() {

		authentication = new SimpleAccountServiceAuthentication();
		authentication.setApplicationName(applicationName);
		authentication.setAccountKey(userKey);
		authentication.setAccountPassword(userPassword);

		missingAccountRelationshipOutcome = NO_ROLE;
		inactiveApplicationrOutcome = NO_ROLE;
		inactiveAccountOutcome = NO_ROLE;
		inactiveAccountRelationshipOutcome = NO_ROLE;
	}

	@Override
	public void invalid_User_Should_Result_in_Failed_Login() throws NoSuchAlgorithmException, InvalidKeySpecException {

		authentication.setAccountKey(invaliduserKey);
		super.invalid_User_Should_Result_in_Failed_Login();
	}

	@Override
	public void invalid_Password_Should_Result_in_Failed_Login()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		authentication.setAccountPassword(invalidUserPassword);
		super.invalid_Password_Should_Result_in_Failed_Login();
	}

	@Override
	AccountServiceAuthentication getAuthentication() {

		AccountServiceAuthentication localAuthentication = new SimpleAccountServiceAuthentication();
		localAuthentication.setApplicationName(authentication.getApplicationName());
		localAuthentication.setAccountKey(authentication.getAccountKey());
		localAuthentication.setAccountPassword(authentication.getAccountPassword());
		AccountServiceAuthentication resultAuthentication = instance.login(localAuthentication);
		assertNotNull(resultAuthentication);
		return resultAuthentication;
	}

	@Override
	boolean authenticateApplicationAccess(AccountServiceAuthentication resultAuthentication, Application application,
			AccountServiceRole role) {

		return resultAuthentication.getApplicationName().equals(application.getName())
				&& resultAuthentication.getAccountRole() == role;
	}

	@Override
	void authenticateOutcome(AccountServiceAuthentication resultAuthentication,
			AccountServiceAuthenticationOutcome expOutcome) {
		assertEquals(expOutcome, resultAuthentication.getAuthenticationOutcome());

	}

}
