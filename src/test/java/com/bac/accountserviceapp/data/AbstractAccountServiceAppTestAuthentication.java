package com.bac.accountserviceapp.data;

import static com.bac.accountservice.AccountServiceAuthenticationOutcome.AUTHENTICATED;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.BAD_CREDENTIALS;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.DISABLED_PRINCIPAL;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.UNKNOWN_PRINCIPAL;
import static com.bac.accountservice.AccountServiceRole.ADMIN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

public abstract class AbstractAccountServiceAppTestAuthentication<T> extends AbstractHibernateTestCase {

	@Resource(name = "accountServiceApp")
	AccountServiceApp instance;

	@Resource(name = "passwordEncoder")
	PasswordEncoder encoder;
	//
	Application resultApplication;
	final String applicationName = UUID.randomUUID().toString();
	Integer applicationId;
	//
	AccessLevel resultAccessLevel;
	final AccountServiceRole accessLevelRole = ADMIN;
	Integer accessLevelId;
	//
	User resultUser;
	final String userName = UUID.randomUUID().toString();
	final String userKey = UUID.randomUUID().toString();
	final String userPassword = UUID.randomUUID().toString();
	final String invaliduserKey = UUID.randomUUID().toString();
	final String invalidUserPassword = UUID.randomUUID().toString();
	Integer userId = null;
	//
	Account resultAccount;
	final String resourceName = UUID.randomUUID().toString();
	Integer accountId = null;
	//
	final Date createDate = getDateWithoutMillis();
	//
	//
	//
	AccountServiceAuthenticationOutcome authenticatedOutcome = AUTHENTICATED;
	AccountServiceAuthenticationOutcome unknownUserOutcome = UNKNOWN_PRINCIPAL;
	AccountServiceAuthenticationOutcome badPasswordOutcome = BAD_CREDENTIALS;
	AccountServiceAuthenticationOutcome missingAccountRelationshipOutcome = AUTHENTICATED;
	AccountServiceAuthenticationOutcome inactiveAccountOutcome = AUTHENTICATED;
	AccountServiceAuthenticationOutcome inactiveUserOutcome = DISABLED_PRINCIPAL;
	AccountServiceAuthenticationOutcome inactiveApplicationrOutcome = AUTHENTICATED;
	AccountServiceAuthenticationOutcome inactiveAccountRelationshipOutcome = AUTHENTICATED;


	private static final Logger logger = LoggerFactory.getLogger(AccountServiceAppTestSpringAuthentication.class);

	public AbstractAccountServiceAppTestAuthentication() {
		super();
	}

	/**
	 * A valid Application/User relationship should result in a authenticated
	 * login.
	 */
	@Test
	public void valid_Account_Should_Result_in_Authenticated_Login()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("valid_Account_Should_Result_in_Authenticated_Login");

		createUserAccount();

		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, authenticatedOutcome);
		//
		//
		// Ensure that the Authority has been granted
		//
		assertTrue(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));
	}

	@Test
	public void invalid_User_Should_Result_in_Failed_Login() throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("invalid_User_Should_Result_in_Failed_Login");

		createUserAccount();

		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, unknownUserOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));

	}

	@Test
	public void invalid_Password_Should_Result_in_Failed_Login()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("invalid_Password_Should_Result_in_Failed_Login");

		createUserAccount();
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		;
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, badPasswordOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));

	}

	@Test
	public void missing_AccountUSer_Should_Result_in_Failed_Login()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("missing_AccountUSer_Should_Result_in_Failed_Login");

		createUserAccount();
		//
		// Delete the user account.
		//
		final AccountUser accountUser = SimpleComponentFactory.getAccountUser();
		accountUser.setAccountId(accountId);
		accountUser.setUserId(userId);
		dao.deleteAccountUser(accountUser);
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, missingAccountRelationshipOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));

	}

	@Test
	public void inactive_User_Should_Result_in_Failed_Login() throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("inactive_User_Should_Result_in_Failed_Login");

		createUserAccount();
		//
		// Set the User to Inactive
		//
		final User user = dao.getUser(userId);
		user.setEnabled(false);
		resultUser = dao.updateUser(user);
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, inactiveUserOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));

	}

	@Test
	public void inactive_Account_Should_Result_in_No_Authority()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("inactive_Account_Should_Result_in_No_Authority");

		createUserAccount();
		//
		// Set the Account to Inactive
		//
		final Account account = dao.getAccount(accountId);
		account.setEnabled(false);
		resultAccount = dao.updateAccount(account);
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, inactiveAccountOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));
	}

	@Test
	public void inactive_Application_Should_Result_in_No_Authority()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("inactive_Application_Should_Result_in_No_Authority");

		createUserAccount();
		//
		// Set the Application to Inactive
		//
		final Application application = dao.getApplication(applicationId);
		application.setEnabled(false);
		resultApplication = dao.updateApplication(application);
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, inactiveApplicationrOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));

	}

	@Test
	public void inactive_AccountUser_Should_Result_in_No_Authority()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("inactive_AccountUser_Should_Result_in_No_Authority");

		createUserAccount();
		//
		// Set the Account User to Inactive
		//
		AccountUser accountUser = SimpleComponentFactory.getAccountUser();
		accountUser.setAccountId(accountId);
		accountUser.setUserId(userId);
		accountUser = dao.getAccountUser(accountUser);
		accountUser.setEnabled(false);
		dao.updateAccountUser(accountUser);
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, inactiveAccountRelationshipOutcome);
		//
		// Ensure that no Authority has been granted
		//
		assertFalse(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));
	}

	@Test
	public void multiple_AccountUsers_Should_Result_In_Multiple_Authorities()
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		logger.info("multiple_AccountUsers_Should_Result_In_Multiple_Authorities");

		createUserAccount();
		//
		// Create an additonal Application, Account and AccountUser
		//
		//
		// Create a simple application
		//
		final String applicationName2 = UUID.randomUUID().toString();
		Application application2 = SimpleComponentFactory.getApplication();
		application2.setName(applicationName2);
		application2.setEnabled(true);
		application2 = dao.createApplication(application2);
		//
		// Create a simple account
		//
		Account account2 = SimpleComponentFactory.getAccount();
		account2.setEnabled(true);
		account2.setApplicationId(application2.getId());
		account2 = dao.createAccount(account2);
		//
		// Create the Account User
		//
		final AccountUser accountUser2 = SimpleComponentFactory.getAccountUser();
		accountUser2.setAccountId(account2.getId());
		accountUser2.setUserId(userId);
		accountUser2.setEnabled(true);
		accountUser2.setAccessLevelId(accessLevelId);
		dao.createAccountUser(accountUser2);
		//
		// Authenticate
		//
		T resultAuthentication = getAuthentication();
		assertNotNull(resultAuthentication);
		//
		// Check the outcome
		//
		authenticateOutcome(resultAuthentication, authenticatedOutcome);
		//
		// Ensure that application authority has been granted
		//
		assertTrue(authenticateApplicationAccess(resultAuthentication, resultApplication, accessLevelRole));

	}

	abstract T getAuthentication();

	abstract void authenticateOutcome(T resultAuthentication, AccountServiceAuthenticationOutcome expOutcome);
	
	abstract boolean authenticateApplicationAccess(T resultAuthentication, Application application, AccountServiceRole role );

	void createUserAccount() throws NoSuchAlgorithmException, InvalidKeySpecException {

		//
		// Create a simple application
		//
		final Application createApplication = SimpleComponentFactory.getApplication();
		createApplication.setName(applicationName);
		createApplication.setEnabled(true);
		//
		// Persist it and test
		//
		resultApplication = dao.createApplication(createApplication);
		applicationId = resultApplication.getId();
		//
		// Create a simple account
		//
		final Account createAccount = SimpleComponentFactory.getAccount();
		createAccount.setResourceName(resourceName);
		createAccount.setEnabled(true);
		createAccount.setCreateDate(createDate);
		createAccount.setApplicationId(applicationId);
		//
		// Persist it
		//
		resultAccount = dao.createAccount(createAccount);
		accountId = resultAccount.getId();
		//
		//
		// Create a simple access level
		//
		final AccessLevel createAccessLevel = SimpleComponentFactory.getAccessLevel();
		createAccessLevel.setAccountServiceRole(accessLevelRole);
		//
		// Persist it
		//
		resultAccessLevel = dao.createAccessLevel(createAccessLevel);
		accessLevelId = resultAccessLevel.getId();
		//
		// Create a simple user
		//
		final User createUser = SimpleComponentFactory.getUser();
		createUser.setUserName(userName);
		createUser.setUserKey(userKey);
		createUser.setEnabled(true);
		createUser.setCreateDate(createDate);
		// Authentication
		// user.setPasswordSalt(salt);
		createUser.setUserPassword(encoder.encode(userPassword).getBytes());
		//
		// Persist it
		//
		resultUser = dao.createUser(createUser);
		userId = resultUser.getId();
		//
		// Create the Account User
		//
		final AccountUser createAccountUser = SimpleComponentFactory.getAccountUser();
		createAccountUser.setAccountId(accountId);
		createAccountUser.setUserId(userId);
		createAccountUser.setEnabled(true);
		createAccountUser.setCreateDate(createDate);
		createAccountUser.setLastAccessDate(createDate);
		createAccountUser.setAccessLevelId(accessLevelId);
		//
		// Persist it
		//
		//
		dao.createAccountUser(createAccountUser);
	}
}