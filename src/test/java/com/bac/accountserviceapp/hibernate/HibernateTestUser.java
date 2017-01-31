package com.bac.accountserviceapp.hibernate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;

import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.data.SimpleComponentFactory;

public class HibernateTestUser extends AbstractHibernateTestCase {

	/*
	 * Simple test to ensure that the test instance is correctly generated
	 */
	@Test
	public void instanceIsSetUpCorrectly() {

		assertNotNull(instance);
	}

	/*
	 * Creating a new User should have either null or default values for each
	 * member apart from the id.
	 */
	@Test
	public void createdUserHasNullMembers() {

		//
		// Persist a simple user and test
		//
		User result = instance.createUser(SimpleComponentFactory.getUser());
		assertNotNull(result);
		assertNotNull(result.getId());
		//
		// Enabled defaults to false
		//
		final boolean DEFAULT_ENABLED = false;
		assertEquals(result.isEnabled(), DEFAULT_ENABLED);
		//
		// The remaining members should have null values
		//
		assertNull(result.getCreateDate());
		assertNull(result.getPasswordSalt());
		assertNull(result.getUserKey());
		assertNull(result.getUserName());
		assertNull(result.getUserPassword());
		//
		// Accounts should be an empty Set
		//
		assertTrue(result.getAccounts().isEmpty());
	}

	/*
	 * A User that has been deleted should not be found
	 */
	@Test
	public void deletedUserShouldNotExist() {

		//
		// Create a simple user
		//
		User result = instance.createUser(SimpleComponentFactory.getUser());
		assertNotNull(result);
		assertNotNull(result.getId());
		//
		// Delete the user
		//
		instance.deleteUser(result);
		//
		// Re-reading the User should now result in a null value
		//
		Integer userId = result.getId();
		assertNull(instance.getUser(userId));
	}

	/*
	 * An empty database should contain no Users
	 */
	@Test
	public void userShouldNotExistInAnEmptyDatabase() {

		final Integer nonExistentUserId = 1;
		assertNull(instance.getUser(nonExistentUserId));
	}


	/*
	 * When a newly created User is read back it should contain the appropriate
	 * member values
	 */
	@Test
	public void createUserAndReadBack() {

		Integer userId = null;
		final String userName = "User name";
		final String userEmail = "user@email.address";
		final boolean enabled = true;
		final Date createDate = getDateWithoutMillis();
		final byte[] userPassword = "user password".getBytes();
		final byte[] passwordSalt = "password salt".getBytes();
		{
			//
			// Insert a User with the above values
			//
			final User user = SimpleComponentFactory.getUser();
			user.setEnabled(enabled);
			user.setCreateDate(createDate);
			user.setPasswordSalt(passwordSalt);
			user.setUserKey(userEmail);
			user.setUserName(userName);
			user.setUserPassword(userPassword);
			instance.createUser(user);
			userId = user.getId();
		}
		final User createdUser = instance.getUser(userId);
		assertEquals(createdUser.isEnabled(), enabled);
		assertTrue(createDate.compareTo(createdUser.getCreateDate()) == 0);
		assertArrayEquals( passwordSalt, createdUser.getPasswordSalt() );
		assertEquals(createdUser.getUserKey(), userEmail);
		assertEquals(createdUser.getUserName(), userName);
		assertArrayEquals( userPassword, createdUser.getUserPassword() );
	}

	/*
	 * When a newly created User is subsequently updated, reading back by it's
	 * id should reflect the updated values
	 */
	@Test
	public void updatedUserReflectsChanges() {

		//
		// Apply test values and update
		//
		Integer userId = null;
		final String userName = "User name";
		final String userEmail = "user@email.address";
		final boolean enabled = true;
		final Date createDate = getDateWithoutMillis();
		final byte[] userPassword = "user password".getBytes();
		final byte[] passwordSalt = "password salt".getBytes();
		//
		{
			final User user = instance.createUser(SimpleComponentFactory.getUser());
			userId = user.getId();
			user.setEnabled(enabled);
			user.setCreateDate(createDate);
			user.setPasswordSalt(passwordSalt);
			user.setUserKey(userEmail);
			user.setUserName(userName);
			user.setUserPassword(userPassword);
			instance.updateUser(user);
		}
		//
		// Read back the User and confirm changes were persisted
		//
		final User updatedUser = instance.getUser(userId);
		assertEquals(updatedUser.isEnabled(), enabled);
		assertTrue(createDate.compareTo(updatedUser.getCreateDate()) == 0);
		assertArrayEquals( passwordSalt, updatedUser.getPasswordSalt() );
		assertEquals(updatedUser.getUserKey(), userEmail);
		assertEquals(updatedUser.getUserName(), userName);
		assertArrayEquals( userPassword, updatedUser.getUserPassword() );
	}

	/*
	 * Secondary keys must be unique for Users. Creating more than one User with
	 * the same secondary key (email) should result in an exception
	 */
	@Test(expected = ConstraintViolationException.class)
	public void duplicate_Secondary_Keys_Should_Cause_An_Exception() {

		final String secondaryKey = "user@email.address";

		User user1 = SimpleComponentFactory.getUser();
		User user2 = SimpleComponentFactory.getUser();

		user1.setUserKey(secondaryKey);
		user2.setUserKey(secondaryKey);

		instance.createUser(user1);
		instance.createUser(user2);
	}

	/*
	 * A newly created user should be available through a secondary key search
	 * which in this case is the user email.
	 */
	@Test
	public void testGetUserBySecondaryKey() {

		Integer userId = null;
		final String userEmail = "user@email.address";
		{
			//
			// Insert a User with the above value
			//
			final User user = SimpleComponentFactory.getUser();
			user.setUserKey(userEmail);
			instance.createUser(user);
			userId = user.getId();
		}
		//
		// The secondary key is the email. Searching via this should return the
		// same user id
		//
		User scondaryKeyUser = SimpleComponentFactory.getUser();
		scondaryKeyUser.setUserKey(userEmail);
		scondaryKeyUser = instance.getUserBySecondaryKey(scondaryKeyUser);
		assertEquals(userId, scondaryKeyUser.getId());
	}
}
