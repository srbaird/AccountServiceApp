/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import static com.bac.accountservice.AccountServiceAuthenticationOutcome.APPLICATION_CLOSED;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.BAD_CREDENTIALS;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.NO_APPLICATION;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.PENDING_CREATION;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.PRINCIPAL_EXISTS;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.UNKNOWN_PRINCIPAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bac.accountservice.AccountServiceAuthentication;
import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import com.bac.accountservice.AccountServiceRole;
import com.bac.accountservice.SimpleAccountServiceAuthentication;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.impl.AccountServiceApp;
import com.bac.accountserviceapp.impl.DataConstants;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

/**
 *
 * @author user0001
 */
public class AccountServiceAppTestCreateLogin extends AbstractHibernateTestCase {

	@Resource(name = "accountServiceApp")
	AccountServiceApp instance;

	@Resource(name = "passwordEncoder")
	PasswordEncoder encoder;
	//
	private Application resultApplication;
	private final String applicationName = UUID.randomUUID().toString();
	//
	private final AccountServiceRole expRole = DataConstants.DEFAULT_ACCOUNT_SERVICE_ROLE;
	//
	private User resultUser;
	private final String userName = UUID.randomUUID().toString();
	private final String userEmail = UUID.randomUUID().toString();
	private final String userPassword = UUID.randomUUID().toString();

	//
	private Account resultAccount;
	private final String expResourceName = UUID.randomUUID().toString();
	//
	private AccessLevel resultAccessLevel;
	//
	//
	private final Date createDate = getDateWithoutMillis();

	// logger
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceAppTestCreateLogin.class);

	// ***********************************************************
	// Successful login instances
	// ***********************************************************
	@Test
	public void valid_Login_Creation_Should_Return_PENDING_CREATION()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("valid_Login_Creation_Should_Return_PENDING_CREATION");

		createApplication();
		AccountServiceAuthentication authentication = standardAuthentication();
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, PENDING_CREATION);
		//
		// Authenticate the result
		//
		successfulAuthentication(resultAuthentication);
	}

	@Test
	public void creating_An_Existing_Login_Should_Return_PENDING_CREATION()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_An_Existing_Login_Should_Return_PENDING_CREATION");

		createApplication();
		AccountServiceAuthentication authentication = this.standardAuthentication();
		//
		// Create a simple user
		//
		createUser();
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, PENDING_CREATION);
		//
		// Authenticate the result
		//
		successfulAuthentication(resultAuthentication);
	}

	// ***********************************************************
	// Unsuccessful login instances
	// ***********************************************************
	@Test
	public void creating_A_Login_With_Null_Application_Name_Should_Return_NO_APPLICATION()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_A_Login_With_Null_Application_Name_Should_Return_NO_APPLICATION");

		createApplication();
		AccountServiceAuthentication authentication = standardAuthentication();
		//
		authentication.setApplicationName(null);
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, NO_APPLICATION);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	public void creating_A_Login_With_Invalid_Application_Name_Should_Return_NO_APPLICATION()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_A_Login_With_Invalid_Application_Name_Should_Return_NO_APPLICATION");

		createApplication();
		AccountServiceAuthentication authentication = standardAuthentication();
		//
		authentication.setApplicationName(UUID.randomUUID().toString());
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, NO_APPLICATION);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	public void creating_A_Login_With_For_An_Inactive_Application_Should_Return_NO_APPLICATION()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_A_Login_With_For_An_Inactive_Application_Should_Return_NO_APPLICATION");

		createApplication();
		resultApplication.setEnabled(false);
		resultApplication = dao.updateApplication(resultApplication);

		AccountServiceAuthentication authentication = standardAuthentication();
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, NO_APPLICATION);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	public void creating_A_Login_With_For_A_Closed_Application_Should_Return_APPLICATION_CLOSED()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_A_Login_With_For_A_Closed_Application_Should_Return_APPLICATION_CLOSED");

		createApplication();
		resultApplication.setRegistrationOpen(false);
		resultApplication = dao.updateApplication(resultApplication);

		AccountServiceAuthentication authentication = standardAuthentication();
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, APPLICATION_CLOSED);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	@Test
	public void creating_A_Login_With_Null_User_Key_Should_Return_UNKNOWN_PRINCIPAL()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_A_Login_With_Null_User_Key_Should_Return_UNKNOWN_PRINCIPAL");

		createApplication();
		AccountServiceAuthentication authentication = standardAuthentication();
		//
		authentication.setAccountKey(null);
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, UNKNOWN_PRINCIPAL);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	@Test
	public void creating_A_Login_With_Null_User_Password_Should_Return_BAD_CREDENTIALS()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_A_Login_With_Null_User_Password_Should_Return_BAD_CREDENTIALS");

		createApplication();
		AccountServiceAuthentication authentication = standardAuthentication();
		//
		authentication.setAccountPassword(null);
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, BAD_CREDENTIALS);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	@Test
	public void creating_Login_With_Existing_User_But_Incorrect_Password_Should_Return_PRINCIPAL_EXISTS()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_Login_With_Existing_User_But_Incorrect_Password_Should_Return_PRINCIPAL_EXISTS");

		createApplication();
		createUser();

		AccountServiceAuthentication authentication = standardAuthentication();
		authentication.setAccountPassword(UUID.randomUUID().toString());

		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, PRINCIPAL_EXISTS);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	@Test
	public void creating_Login_With_Existing_User_And_Account_PRINCIPAL_EXISTS()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("creating_Login_With_Existing_User_And_Account_PRINCIPAL_EXISTS");

		createApplication();
		createUser();
		createAccountFor(resultApplication);
		createAccountUserFor(resultAccount, resultUser, resultAccessLevel);

		AccountServiceAuthentication authentication = standardAuthentication();
		//
		// Create Login
		//
		AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, PRINCIPAL_EXISTS);
		//
		// Authenticate the result
		//
		failedAuthentication(resultAuthentication);
	}

	//
	// Private methods
	//

	private void createApplication() throws NoSuchAlgorithmException, InvalidKeySpecException {

		//
		// Create a simple application
		//
		final Application application = SimpleComponentFactory.getApplication();
		application.setName(applicationName);
		application.setEnabled(true);
		application.setRegistrationOpen(true);
		//
		// Persist it
		//
		resultApplication = dao.createApplication(application);
		//
		// Create the default Access Level
		//
		final AccessLevel accessLevel = SimpleComponentFactory.getAccessLevel();
		accessLevel.setAccountServiceRole(DataConstants.DEFAULT_ACCOUNT_SERVICE_ROLE);
		resultAccessLevel = dao.createAccessLevel(accessLevel);
	}

	private void createUser() {

		final User user = SimpleComponentFactory.getUser();
		user.setUserName(userName);
		user.setUserKey(userEmail);
		user.setEnabled(true);
		user.setCreateDate(createDate);
		user.setUserPassword(encoder.encode(userPassword).getBytes());
		//
		// Persist it
		//
		resultUser = dao.createUser(user);
	}

	private void createAccountFor(Application application) {

		final Account account = SimpleComponentFactory.getAccount();
		account.setResourceName(expResourceName);
		account.setEnabled(true);
		account.setCreateDate(createDate);
		account.setApplicationId(application.getId());
		//
		// Persist it
		//
		resultAccount = dao.createAccount(account);
	}

	private void createAccountUserFor(Account account, User user, AccessLevel accessLevel) {

		final AccountUser accountUser = SimpleComponentFactory.getAccountUser();
		accountUser.setAccountId(account.getId());
		accountUser.setUserId(user.getId());
		accountUser.setEnabled(true);
		accountUser.setCreateDate(createDate);
		accountUser.setLastAccessDate(createDate);
		accountUser.setAccessLevelId(accessLevel.getId());
		//
		// Persist it
		//
		dao.createAccountUser(accountUser);
	}

	private AccountServiceAuthentication standardAuthentication() {

		AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
		authentication.setApplicationName(applicationName);
		authentication.setAccountKey(userEmail);
		authentication.setAccountPassword(userPassword);
		authentication.setAccountResource(expResourceName);
		return authentication;
	}

	private void authenticateOutcome(AccountServiceAuthentication resultAuthentication,
			AccountServiceAuthenticationOutcome expOutcome) {

		Object authenticationDetails = resultAuthentication.getAuthenticationOutcome();
		assertNotNull(authenticationDetails);
		Class<?> expDetailsClass = AccountServiceAuthenticationOutcome.class;
		Class<?> resultDetailsClass = authenticationDetails.getClass();
		assertEquals(expDetailsClass, resultDetailsClass);
		AccountServiceAuthenticationOutcome authenticationOutcome = (AccountServiceAuthenticationOutcome) authenticationDetails;
		assertEquals(expOutcome, authenticationOutcome);
	}

	private void successfulAuthentication(AccountServiceAuthentication resultAuthentication) {

		String expPassword = null;
		String resultPassword = resultAuthentication.getAccountPassword();
		assertEquals(expPassword, resultPassword);
		AccountServiceRole resultRole = resultAuthentication.getAccountRole();
		assertEquals(expRole, resultRole);
		String resultResource = (String) resultAuthentication.getAccountResource();
		assertEquals(expResourceName, resultResource);
	}

	private void failedAuthentication(AccountServiceAuthentication resultAuthentication) {

		String expPassword = null;
		String resultPassword = resultAuthentication.getAccountPassword();
		assertEquals(expPassword, resultPassword);
		AccountServiceRole resultRole = resultAuthentication.getAccountRole();
		assertEquals(null, resultRole);
		String expResource = null;
		String resultResource = (String) resultAuthentication.getAccountResource();
		assertEquals(expResource, resultResource);
	}
}
