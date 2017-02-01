/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.impl.DataConstants;

/**
 *
 * @author user0001
 */
public class AccountServiceAppTestSpringAuthentication
		extends AbstractAccountServiceAppTestAuthentication<Authentication> {

	private UsernamePasswordAuthenticationToken authentication;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceAppTestSpringAuthentication.class);

	@Before
	public void setAuthentication() {

		authentication = new UsernamePasswordAuthenticationToken(userKey, userPassword);
	}

	@Override
	public void invalid_User_Should_Result_in_Failed_Login() throws NoSuchAlgorithmException, InvalidKeySpecException {

		authentication = new UsernamePasswordAuthenticationToken(invaliduserKey, userPassword);
		super.invalid_User_Should_Result_in_Failed_Login();
	}

	@Override
	public void invalid_Password_Should_Result_in_Failed_Login()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		authentication = new UsernamePasswordAuthenticationToken(userKey, invalidUserPassword);
		super.invalid_Password_Should_Result_in_Failed_Login();
	}

	@Override
	Authentication getAuthentication() {

		Authentication result =  instance.login(authentication);
		logger.info("Result: {}", result);
		return instance.login(authentication);
	}

	@Override
	void authenticateOutcome(Authentication resultAuthentication, AccountServiceAuthenticationOutcome expOutcome) {

		Object authenticationDetails = resultAuthentication.getDetails();
		assertNotNull(authenticationDetails);
		Class<?> expDetailsClass = AccountServiceAuthenticationOutcome.class;
		Class<?> resultDetailsClass = authenticationDetails.getClass();
		assertEquals(expDetailsClass, resultDetailsClass);
		AccountServiceAuthenticationOutcome authenticationOutcome = (AccountServiceAuthenticationOutcome) authenticationDetails;
		assertEquals(expOutcome, authenticationOutcome);
	}

	boolean authenticateApplicationAccess(Authentication resultAuthentication, Application application,
			AccountServiceRole role) {

		final String expAuthority = String.format(DataConstants.AUTHORITY_FORMAT, applicationName,
				DataConstants.AUTHORITY_SEPARATOR, role.name());
		return getAuthorities(resultAuthentication).stream().map(a -> a.getAuthority()).collect(Collectors.toList())
				.contains(expAuthority);

	}

	Collection<? extends GrantedAuthority> getAuthorities(Authentication resultAuthentication) {

		return resultAuthentication.getAuthorities();
	}
}
