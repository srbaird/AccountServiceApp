/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import static com.bac.accountservice.AccountServiceRole.GUEST;
import static com.bac.accountservice.AccountServiceRole.OWNER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.impl.DataConstants;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

/**
 *
 * @author user0001
 */
public class UserDetailsServiceTest extends AbstractHibernateTestCase {

	@Resource(name = "userDetailsService")
	UserDetailsService instance;

	// logger
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceTest.class);

	public UserDetailsServiceTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	/**
	 * Loading a User by name from an empty data source should result in an
	 * exception
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void load_User_From_Empty_Data_Source() {

		logger.info("load_User_From_Empty_Data_Source");
		final String userName = UUID.randomUUID().toString();
		instance.loadUserByUsername(userName);
	}

	/**
	 * Loading a valid user which has no Accounts should return a UserDetails
	 * object with an empty set of authorities
	 */
	@Test
	public void user_With_No_accounts_Should_Return_Empty_Authorities_Set() {

		logger.info("user_With_No_accounts_Should_Return_Empty_Authorities_Set");
		final String expUserKey = UUID.randomUUID().toString();
		createUser(expUserKey);
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		assertNotNull(result);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int empty = 0;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(empty, resultAuthorities.size());
	}

	/**
	 * Loading a User with a single valid account should return a UserDetails
	 * object with a single authority matching the expected pattern
	 */
	@Test
	public void user_With_One_Valid_Account_Should_Return_Single_Authority() {

		logger.info("user_With_One_Valid_Account_Should_Return_Single_Authority");

		String expUserKey = UUID.randomUUID().toString();
		String expApplicationName = UUID.randomUUID().toString();
		AccountServiceRole expRole = OWNER;

		String expAuthority = expApplicationName + DataConstants.AUTHORITY_SEPARATOR + expRole.name();

		final User user = createUser(expUserKey);
		final Application application = createApplication(expApplicationName);
		final Account account = createAccount(application);
		final AccessLevel owner = createAccessLevel(expRole);
		createAccountUser(user, account, owner);
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int single = 1;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(single, resultAuthorities.size());
		String resultingAuthority = resultAuthorities.stream().findFirst().get().getAuthority();
		assertEquals(expAuthority, resultingAuthority);
	}

	/**
	 * Loading a User with a single invalid account should return a UserDetails
	 * object with an empty set of authorities
	 */
	@Test
	public void user_With_One_Invalid_Account_Should_Return_Empty_Authorities() {

		logger.info("user_With_One_Invalid_Account_Should_Return_Empty_Authorities");

		String expUserKey = UUID.randomUUID().toString();
		String expApplicationName = UUID.randomUUID().toString();
		AccountServiceRole expRole = OWNER;
		// Standard set up
		final User user = createUser(expUserKey);
		final Application application = createApplication(expApplicationName);
		final Account account = createAccount(application);
		final AccessLevel owner = createAccessLevel(expRole);
		createAccountUser(user, account, owner);
		//
		account.setEnabled(false);
		dao.updateAccount(account);
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int empty = 0;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(empty, resultAuthorities.size());
	}

	/**
	 * Loading a User with a single valid account but an invalid Application
	 * should return a UserDetails object with an empty set of authorities
	 */
	@Test
	public void user_With_Valid_Account_Invalid_Application_Should_Return_Empty_Authorities() {

		logger.info("user_With_Valid_Account_Invalid_Application_Should_Return_Empty_Authorities");

		String expUserKey = UUID.randomUUID().toString();
		String expApplicationName = UUID.randomUUID().toString();
		AccountServiceRole expRole = OWNER;
		// Standard set up
		final User user = createUser(expUserKey);
		final Application application = createApplication(expApplicationName);
		final Account account = createAccount(application);
		final AccessLevel owner = createAccessLevel(expRole);
		createAccountUser(user, account, owner);
		//
		application.setEnabled(false);
		dao.updateApplication(application);
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int empty = 0;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(empty, resultAuthorities.size());
	}

	/**
	 * Loading a User with a single valid account but an invalid AccountUser
	 * should return a UserDetails object with an empty set of authorities
	 */
	@Test
	public void user_With_Valid_Account_Invalid_AccountUser_Should_Return_Empty_Authorities() {

		logger.info("user_With_Valid_Account_Invalid_AccountUser_Should_Return_Empty_Authorities");

		String expUserKey = UUID.randomUUID().toString();
		String expApplicationName = UUID.randomUUID().toString();
		AccountServiceRole expRole = OWNER;
		// Standard set up
		final User user = createUser(expUserKey);
		final Application application = createApplication(expApplicationName);
		final Account account = createAccount(application);
		final AccessLevel owner = createAccessLevel(expRole);
		final AccountUser accountUser = createAccountUser(user, account, owner);
		//
		accountUser.setEnabled(false);
		dao.updateAccountUser(accountUser);
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int empty = 0;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(empty, resultAuthorities.size());
	}

	/**
	 * Loading a User with a multiple valid account should return a UserDetails
	 * object with a matching authorities for each account
	 */
	@Test
	public void user_With_Multiple_Valid_Accounts_Should_Return_All_Authorities() {

		logger.info("user_With_Multiple_Valid_Accounts_Should_Return_All_Authorities");

		String expUserKey = UUID.randomUUID().toString();
		String expApplicationName1 = UUID.randomUUID().toString();
		String expApplicationName2 = UUID.randomUUID().toString();
		AccountServiceRole expRole1 = OWNER;
		AccountServiceRole expRole2 = GUEST;

		String expAuthority1 = expApplicationName1 + DataConstants.AUTHORITY_SEPARATOR + expRole1.name();
		String expAuthority2 = expApplicationName2 + DataConstants.AUTHORITY_SEPARATOR + expRole2.name();

		final User user = createUser(expUserKey);
		final Application application1 = createApplication(expApplicationName1);
		final Account account1 = createAccount(application1);
		final AccessLevel owner = createAccessLevel(expRole1);
		createAccountUser(user, account1, owner);
		//
		final Application application2 = createApplication(expApplicationName2);
		final Account account2 = createAccount(application2);
		final AccessLevel guest = createAccessLevel(expRole2);
		createAccountUser(user, account2, guest);		
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int two = 2;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(two, resultAuthorities.size());
		//
		List<?>  resultAuthorityNames =  resultAuthorities.stream().map(a -> a.getAuthority()).collect(Collectors.toList());
		List<?> expectedAuthorites = Arrays.asList(new String[] {expAuthority1, expAuthority2});
		assertTrue(resultAuthorityNames.containsAll(expectedAuthorites));
	}
	
	/**
	 * Loading a User with a multiple accounts should return a UserDetails
	 * object with a matching authorities only for each valid account. 
	 */
	@Test
	public void user_With_Multiple_Accounts_Should_Only_Return_Enabled_Ones() {

		logger.info("user_With_Multiple_Accounts_Should_Only_Return_Enabled_Ones");

		String expUserKey = UUID.randomUUID().toString();
		String expApplicationName1 = UUID.randomUUID().toString();
		String expApplicationName2 = UUID.randomUUID().toString();
		AccountServiceRole expRole1 = OWNER;
		AccountServiceRole expRole2 = GUEST;

		String expAuthority1 = expApplicationName1 + DataConstants.AUTHORITY_SEPARATOR + expRole1.name();

		final User user = createUser(expUserKey);
		final Application application1 = createApplication(expApplicationName1);
		final Account account1 = createAccount(application1);
		final AccessLevel owner = createAccessLevel(expRole1);
		createAccountUser(user, account1, owner);
		//
		final Application application2 = createApplication(expApplicationName2);
		final Account account2 = createAccount(application2);
		final AccessLevel guest = createAccessLevel(expRole2);
		final AccountUser accountUser = createAccountUser(user, account2, guest);
		//
		accountUser.setEnabled(false);
		dao.updateAccountUser(accountUser);
		//
		UserDetails result = instance.loadUserByUsername(expUserKey);
		Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
		int single = 1;
		assertEquals(expUserKey, result.getUsername());
		assertEquals(single, resultAuthorities.size());
		//
		List<?>  resultAuthorityNames =  resultAuthorities.stream().map(a -> a.getAuthority()).collect(Collectors.toList());
		List<?> expectedAuthorites = Arrays.asList(new String[] {expAuthority1});
		assertTrue(resultAuthorityNames.containsAll(expectedAuthorites));
	}
	//
	// Convenience methods
	//

	private User createUser(String userKey) {

		final User user = SimpleComponentFactory.getUser();
		user.setUserKey(userKey);
		return dao.createUser(user);
	}

	private Application createApplication(String applicationName) {

		final Application application = SimpleComponentFactory.getApplication();
		application.setName(applicationName);
		application.setEnabled(true);
		return dao.createApplication(application);
	}

	private Account createAccount(Application application) {

		final Account account = SimpleComponentFactory.getAccount();
		account.setApplicationId(application.getId());
		account.setEnabled(true);
		return dao.createAccount(account);
	}

	private AccessLevel createAccessLevel(AccountServiceRole role) {

		final AccessLevel accessLevel = SimpleComponentFactory.getAccessLevel();
		accessLevel.setAccountServiceRole(role);
		return dao.createAccessLevel(accessLevel);
	}

	private AccountUser createAccountUser(User user, Account account, AccessLevel level) {

		final AccountUser accountUser = SimpleComponentFactory.getAccountUser();
		accountUser.setAccountId(account.getId());
		accountUser.setUserId(user.getId());
		accountUser.setAccessLevelId(level.getId());
		accountUser.setEnabled(true);
		return dao.createAccountUser(accountUser);
	}
}
