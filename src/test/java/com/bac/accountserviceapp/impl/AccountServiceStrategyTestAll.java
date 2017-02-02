
package com.bac.accountserviceapp.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.impl.AccountServiceStrategy;
import com.bac.accountserviceapp.impl.DataConstants;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

/**
 *
 * @author Simon Baird
 */
public class AccountServiceStrategyTestAll extends AbstractHibernateTestCase {

	@Resource(name = "accountServiceStrategy")
	private AccountServiceStrategy instance;
	// logger
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceStrategyTestAll.class);

	/**
	 * Newly instantiated instance should return the default AccountService role
	 */
	@Test
	public void new_Service_Should_Return_Default_AccountService_Role() {

		logger.info("new_Service_Should_Return_Default_AccountService_Role");

		AccountServiceRole expectedRole = DataConstants.DEFAULT_ACCOUNT_SERVICE_ROLE;
		AccountServiceRole resultRole = instance.getDefaultAccountServiceRole();
		assertEquals(expectedRole, resultRole);
	}

	/**
	 * Service should return null querying the account for an application when
	 * the database is empty
	 * 
	 */
	@Test
	public void service_Should_Return_Null_getAccountForApplication_When_Database_is_Empty() {

		logger.info("service_Should_Return_Null_getAccountForApplication_When_Database_is_Empty");
		assertNull(instance.getAccountForApplication("any", "any"));
	}

	/**
	 * Service should return null querying the account for an application when
	 * the User does not exist
	 * 
	 */
	@Test
	public void service_Should_Return_Null_getAccountForApplication_When_User_Does_Not_Exist() {

		logger.info("service_Should_Return_Null_getAccountForApplication_When_User_Does_Not_Exist");
		final String applicationName = UUID.randomUUID().toString();
		createApplication(applicationName);

		assertNull(instance.getAccountForApplication(applicationName, "any"));
	}

	/**
	 * Service should return null querying the account for an application when
	 * the Application does not exist
	 * 
	 */
	@Test
	public void service_Should_Return_Null_getAccountForApplication_When_Application_Does_Not_Exist() {

		logger.info("service_Should_Return_Null_getAccountForApplication_When_User_Does_Not_Exist");
		final String userKey = UUID.randomUUID().toString();
		createUser(userKey);

		assertNull(instance.getAccountForApplication("any", userKey));
	}
	
	/**
	 * Service should return null querying the account for an application when
	 * the User and Application exist but no Account/User relationship exists
	 * 
	 */
	@Test
	public void service_Should_Return_Null_getAccountForApplication_When_AccountUser_Does_Not_Exist() {

		logger.info("service_Should_Return_Null_getAccountForApplication_When_AccountUser_Does_Not_Exist");
		
		final String applicationName = UUID.randomUUID().toString();
		createApplication(applicationName);
		
		final String userKey = UUID.randomUUID().toString();
		createUser(userKey);

		assertNull(instance.getAccountForApplication(applicationName, userKey));
	}
	
	
	/**
	 * Service should return a valid account for an application when
	 * the User/Application relationship exists
	 * 
	 */
	@Test
	public void service_Should_Return_Valid_Account_When_AccountUser_Exists() {

		logger.info("service_Should_Return_Valid_Account_When_AccountUser_Exists");
		
		final String applicationName = UUID.randomUUID().toString();
		final Application application = createApplication(applicationName);
		
		final String userKey = UUID.randomUUID().toString();
		final User user = createUser(userKey);
		
		final Account account = createAccountFor(application);
		
		final AccessLevel accessLevel = createAccessLevelFor(DataConstants.DEFAULT_ACCOUNT_SERVICE_ROLE);
		
		createAccountUserFor(account, user, accessLevel);

		final Account resultAccount = instance.getAccountForApplication(applicationName, userKey);
		assertNotNull(resultAccount);
		assertEquals(account, resultAccount);
	}

	/**
	 * 
	 * Test to ensure that created user can be accessed through the data accessor
	 */
	@Test
	public void created_User_Should_Be_Available_Through_The_Data_Accessor() {
	
		final User user = SimpleComponentFactory.getUser();
		final String userKey = UUID.randomUUID().toString();
		user.setUserKey(userKey);
		final User resultUser = instance.newUser(user);
		assertNotNull(dao.getUser(resultUser.getId()));
	}
	
	//
	//
	//
	private Application createApplication(String applicationName) {

		Application application = SimpleComponentFactory.getApplication();
		application.setName(applicationName);
		return dao.createApplication(application);
	}

	private User createUser(String name) {

		User user = SimpleComponentFactory.getUser();
		user.setUserKey(name);
		return dao.createUser(user);
	}
	
	private Account createAccountFor(Application application) {
		
		Account account = SimpleComponentFactory.getAccount();
		account.setApplicationId(application.getId());
		return dao.createAccount(account);
	}

	private AccessLevel createAccessLevelFor(AccountServiceRole role) {

		AccessLevel accessLevel = SimpleComponentFactory.getAccessLevel();
		accessLevel.setAccountServiceRole(role);
		return dao.createAccessLevel(accessLevel);
	}
	
	private AccountUser createAccountUserFor(Account account, User user, AccessLevel accessLevel) {
		
		AccountUser accountUser = SimpleComponentFactory.getAccountUser();
		accountUser.setAccessLevelId(accessLevel.getId());
		accountUser.setAccountId(account.getId());
		accountUser.setUserId(user.getId());
		return dao.createAccountUser(accountUser);
	}
}
