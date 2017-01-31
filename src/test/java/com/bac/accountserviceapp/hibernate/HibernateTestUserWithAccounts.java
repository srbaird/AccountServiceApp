package com.bac.accountserviceapp.hibernate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.data.SimpleComponentFactory;

public class HibernateTestUserWithAccounts extends AbstractHibernateTestCase {

	// logger
	private static final Logger logger = LoggerFactory.getLogger(HibernateTestUserWithAccounts.class);

	/*
	 * A newly created User should have no Accounts associated with it
	 */
	@Test
	public void createdUserHasNoAccounts() {

		//
		// Create a simple user and test
		//
		User result = instance.createUser(SimpleComponentFactory.getUser());
		assertNotNull(result);
		assertNotNull(result.getId());
		assertTrue(result.getAccounts().isEmpty());
	}

	/*
	 * Adding an AccountUser entity for the User should result in the associated
	 * Account being returned by the getAccounts() method
	 */
	@Test
	public void userShouldHaveAsscociatedAccount() {

		//
		// Create a simple user
		//
		User user = instance.createUser(SimpleComponentFactory.getUser());
		//
		// Add an association to an account
		//
		final AccountUser accountUser = createDefaultValidAccountUser(user);
		assertEquals(user.getId(), accountUser.getUserId());
		//
		// User retrieval should ensure it is correctly refreshed 
		//
		user = instance.getUser(user.getId());
		final Set<? extends Account> userAccounts = user.getAccounts();
		logger.info("Accounts: {}", userAccounts );
		assertFalse(userAccounts.isEmpty());
		assertTrue(userAccounts.size() == 1);
		//
		//
		//
		final Account expectedAccount = instance.getAccount(accountUser.getAccountId());
		assertTrue(userAccounts.contains(expectedAccount));
	}
	
	/*
	 * Adding an AccountUser entity for the User should result in the associated
	 * Account being returned by the getAccounts() method
	 */
	@Test
	public void user_Should_Have_Asscociated_Account_When_Retrieved_By_Secondary_Key() {

		final String secondaryKey = "user@email.address";
		//
		// Create a simple user
		//
		User user = SimpleComponentFactory.getUser();
		user.setUserKey(secondaryKey);
		user = instance.createUser(user);
		//
		// Add an association to an account
		//
		final AccountUser accountUser = createDefaultValidAccountUser(user);
		assertEquals(user.getId(), accountUser.getUserId());
		//
		// User retrieval should ensure it is correctly refreshed 
		//
		user = instance.getUserBySecondaryKey(user);
		final Set<? extends Account> userAccounts = user.getAccounts();
		logger.info("Accounts: {}", userAccounts );
		assertFalse(userAccounts.isEmpty());
		assertTrue(userAccounts.size() == 1);
		//
		//
		//
		final Account expectedAccount = instance.getAccount(accountUser.getAccountId());
		assertTrue(userAccounts.contains(expectedAccount));
	}

	/*
	 * Adding an account through the setAccounts method should be ignored when
	 * the User is created
	 */
	@Test
	public void createdUserShouldNotPersistAccountsThroughThe_SetAccounts_Method() {

		Set<Account> accounts = new HashSet<>();
		accounts.add(getValidAccount());

		final User user = SimpleComponentFactory.getUser();
		user.setAccounts(accounts);

		final User userWithAccount = instance.createUser(user);
		assertTrue(userWithAccount.getAccounts().isEmpty());

	}

	/*
	 * Adding multiple AccountUser entities for the User should result in all
	 * and only the associated Accounts being returned by the getAccounts()
	 * method
	 */
	@Test
	public void userShouldHaveAllAssociatedAccounts() {

		//
		// Create a simple user
		//
		User user = instance.createUser(SimpleComponentFactory.getUser());
		//
		// Add a number of Accounts to the User
		//
		final AccountUser accountUser1 = createDefaultValidAccountUser(user);
		final AccountUser accountUser2 = createDefaultValidAccountUser(user);
		final AccountUser accountUser3 = createDefaultValidAccountUser(user);
		//
		//
		// User retrieval should ensure it is correctly refreshed 
		//
		user = instance.getUser(user.getId());
		final Set<? extends Account> userAccounts = user.getAccounts();
		assertTrue(userAccounts.size() == 3);
		//
		//
		//
		List<Account> expectedAccounts = Arrays.asList(new Account[] { instance.getAccount(accountUser1.getAccountId()),
				instance.getAccount(accountUser2.getAccountId()), instance.getAccount(accountUser3.getAccountId()) });
		assertTrue(userAccounts.containsAll(expectedAccounts));
	}

	/*
	 * Adding an AccountUser entity for the User and subsequently deleting it
	 * should result in the getAccounts() method returning an empty set
	 */
	@Test
	public void userShouldNotHaveDeletedAccount() {

		//
		// Create a simple user
		//
		User user = instance.createUser(SimpleComponentFactory.getUser());
		//
		// Add an association to an account
		//
		final AccountUser accountUser = createDefaultValidAccountUser(user);
		assertEquals(user.getId(), accountUser.getUserId());
		//
		// Delete the association
		//
		instance.deleteAccountUser(accountUser);
		//
		// User retrieval should ensure it is correctly refreshed 
		//
		user = instance.getUser(user.getId());
		assertTrue(user.getAccounts().isEmpty());
	}

	/*
	 * Deleting a User with an Account should delete any AccountUser
	 * associations but not delete the Account
	 */
	@Test
	public void deletedUserShouldCascadeDeleteAccountUsers() {

		//
		// Create a simple user
		//
		User user = instance.createUser(SimpleComponentFactory.getUser());
		//
		// Add an association to an account
		//
		AccountUser accountUser = createDefaultValidAccountUser(user);
		//
		// Delete the user
		//
		instance.deleteUser(user);
		//
		// Ensure the AccountUser has been deleted
		//
		assertNull(instance.getAccountUser(accountUser));
		//
		// Ensure the Account still exists
		//
		assertNotNull(instance.getAccount(accountUser.getAccountId()));
		
	}
	/*
	 * Deleting a Account should cascade delete any AccountUser
	 * associations but not delete the User
	 */
	@Test
	public void deletedAccountShouldCascadeDeleteAccountUsers() {

		//
		// Create a simple user
		//
		User user = instance.createUser(SimpleComponentFactory.getUser());
		//
		// Add an association to an account
		//
		final AccountUser accountUser = createDefaultValidAccountUser(user);
		//
		// Get the Account
		//
		final Account account = instance.getAccount(accountUser.getAccountId());
		//
		// Delete the account
		//
		instance.deleteAccount(account);
		//
		// Ensure the AccountUser has been deleted
		//
		assertNull(instance.getAccountUser(accountUser));
		//
		// User retrieval should ensure it is correctly refreshed 
		//
		assertFalse(instance.getUser(user.getId()) == null);
	}

	/*
	 * Convenience methods for setting up test environment
	 * ***************************************************
	 */

	private AccountUser createDefaultValidAccountUser(User user) {

		AccountUser accountUser = SimpleComponentFactory.getAccountUser();

		accountUser.setAccountId(getValidAccountId());
		accountUser.setUserId(user.getId());
		accountUser.setAccessLevelId(getValidAccessLevelId());

		AccountUser result = instance.createAccountUser(accountUser);
		return result;
	}

	private Account getValidAccount() {

		Account account = SimpleComponentFactory.getAccount();
		account.setApplicationId(getValidApplicationId());
		return instance.createAccount(account);
	}

	private Integer getValidAccountId() {

		return getValidAccount().getId();
	}

	private Integer getValidApplicationId() {

		Application valid = instance.createApplication(SimpleComponentFactory.getApplication());
		return valid.getId();
	}

	private Integer getValidAccessLevelId() {

		AccessLevel valid = instance.createAccessLevel(SimpleComponentFactory.getAccessLevel());
		return valid.getId();
	}
}
