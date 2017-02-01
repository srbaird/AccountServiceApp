package com.bac.accountserviceapp.hibernate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

public class HibernateTestAccount extends AbstractHibernateTestCase {

	// logger
	private static final Logger logger = LoggerFactory.getLogger(HibernateTestAccount.class);

	/*
	 * Creating an Account with default values should fail as a null application
	 * id must fail the integrity constraint
	 */

	@Test(expected = ConstraintViolationException.class)
	public void defaultAccountShouldFail() {

		instance.createAccountUser(SimpleComponentFactory.getAccountUser());
	}

	/*
	 * Creating an Account with an unknown application id will fail the
	 * integrity constraint
	 */

	@Test(expected = ConstraintViolationException.class)
	public void createdAccountWithInvalidApplicationIdShouldFail() {

		//
		// Persist a simple Account and test
		//
		Account account = SimpleComponentFactory.getAccount();
		account.setApplicationId(0);
		instance.createAccount(account);
	}

	/*
	 * Updating the application id to a non-existent value should result in an
	 * integrity violation
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void updatingAccountWithInvalidApplicationIdShouldFail() {

		//
		// Persist a simple Account and test
		//
		Account account = createDefaultValidAccount();
		logger.info(account + " created. Application id is " + account.getApplicationId());
		account.setApplicationId(-99);
		account = instance.updateAccount(account);
		logger.info(account + " updated. Application id is now " + account.getApplicationId());
	}

	/**
	 * Once the Account is created then any attempt to change the id should
	 * result in an exceptions
	 */
	// TODO: Move this logic to the simple implementation ?
	// @Test(expected = IllegalArgumentException.class)
	public void updatingAccountIdShouldFail() {

		Account account = createDefaultValidAccount();
		Integer invalidId = account.getId() + 1;
		account.setId(invalidId);
		instance.updateAccount(account);
	}

	/*
	 * A newly created account should have null or default values and a valid
	 * application id
	 */
	@Test
	public void createdAccountHasNullMembers() {

		//
		// Persist a simple Account and test
		//
		Account result = createDefaultValidAccount();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getApplicationId());
		//
		// Enabled defaults to false
		//
		final boolean DEFAULT_ENABLED = false;
		assertEquals(result.isEnabled(), DEFAULT_ENABLED);
		//
		// The remaining members should have null values
		//
		assertNull(result.getCreateDate());
		assertNull(result.getResourceName());
	}

	/*
	 * An Account that has been deleted should not be found
	 */
	@Test
	public void deletedAccountShouldNotExist() {

		//
		// Create a simple account
		//
		Account result = createDefaultValidAccount();
		//
		// Delete the account
		//
		instance.deleteAccount(result);
		//
		// Re-reading the Account should now result in a null value
		//
		Integer accountId = result.getId();
		assertNull(instance.getAccount(accountId));
	}

	/*
	 * An empty database should contain no Accounts
	 */
	@Test
	public void accountShouldNotExistInAnEmptyDatabase() {

		final Integer nonExistentAccountId = 1;
		assertNull(instance.getAccount(nonExistentAccountId));
	}


	/*
	 * When a newly created Account is read back it should contain the
	 * appropriate member values
	 */
	@Test
	public void createAccountAndReadBack() {

		Integer accountId = null;
		final String resourceName = "Application Account";
		final boolean active = true;
		final Date createDate = getDateWithoutMillis();
		{
			//
			// Insert an Account with the above values
			//
			Account account = SimpleComponentFactory.getAccount();
			account.setEnabled(active);
			account.setCreateDate(createDate);
			account.setResourceName(resourceName);
			account.setApplicationId(getValidApplicationId());
			account = instance.createAccount(account);
			accountId = account.getId();
		}
		final Account createdAccount = instance.getAccount(accountId);
		assertEquals(createdAccount.isEnabled(), active);
		assertTrue(createDate.compareTo(createdAccount.getCreateDate()) == 0);
		assertEquals(createdAccount.getResourceName(), resourceName);
	}

	/*
	 * When a newly created Account is subsequently updated, reading back by
	 * it's id should reflect the updated values
	 */
	@Test
	public void updatedAccountReflectsChanges() {

		//
		// Apply test values and update
		//
		Integer accountId = null;
		final String resourceName = "Application Account";
		final boolean active = true;
		final Date createDate = getDateWithoutMillis();
		//
		{
			final Account account = createDefaultValidAccount();
			account.setEnabled(active);
			account.setCreateDate(createDate);
			account.setResourceName(resourceName);
			accountId = account.getId();
			instance.updateAccount(account);
		}
		//
		// Read back the Account and confirm changes were persisted
		//
		final Account updatedAccount = instance.getAccount(accountId);
		assertEquals(updatedAccount.isEnabled(), active);
		assertTrue(createDate.compareTo(updatedAccount.getCreateDate()) == 0);
		assertEquals(updatedAccount.getResourceName(), resourceName);
	}
	/*
	 * When an application is deleted any associated accounts should also be
	 * deleted
	 */
	@Test
	public void accountShouldBeDeletedWhenApplicationIsDeleted() {
		
		final Account account = createDefaultValidAccount();
		//
		//	Delete the application
		//
		instance.deleteApplication(instance.getApplication(account.getApplicationId()));
		//
		//	Ensure the Account does not exist any longer
		//
		assertNull(instance.getAccount(account.getId()));
	}
	

	private Account createDefaultValidAccount() {

		Integer validApplicationId = getValidApplicationId();
		Account account = SimpleComponentFactory.getAccount();

		account.setApplicationId(validApplicationId);
		Account result = instance.createAccount(account);
		return result;
	}

	private Integer getValidApplicationId() {

		Application valid = instance.createApplication(SimpleComponentFactory.getApplication());
		return valid.getId();
	}
}
