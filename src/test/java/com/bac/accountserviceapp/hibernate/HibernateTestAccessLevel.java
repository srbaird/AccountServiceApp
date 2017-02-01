package com.bac.accountserviceapp.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import  com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.*;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

public class HibernateTestAccessLevel extends AbstractHibernateTestCase {


	/*
	 * Creating a new AccessLevel should have either null or default values for each
	 * member apart from the id.
	 */
	@Test
	public void createdAccessLevelHasNullMembers() {

		//
		// Persist a simple accessLevel and test
		//
		AccessLevel result = instance.createAccessLevel(SimpleComponentFactory.getAccessLevel());
		assertNotNull(result);
		assertNotNull(result.getId());
		//
		// The remaining members should have null values
		//
		assertNull(result.getAccountServiceRole());
	}

	/*
	 * A AccessLevel that has been deleted should not be found
	 */
	@Test
	public void deletedAccessLevelShouldNotExist() {

		//
		// Create a simple accessLevel
		//
		AccessLevel result = instance.createAccessLevel(SimpleComponentFactory.getAccessLevel());
		assertNotNull(result);
		assertNotNull(result.getId());
		//
		// Delete the accessLevel
		//
		instance.deleteAccessLevel(result);
		//
		// Re-reading the AccessLevel should now result in a null value
		//
		Integer accessLevelId = result.getId();
		assertNull(instance.getAccessLevel(accessLevelId));
	}

	/*
	 * An empty database should contain no AccessLevels
	 */
	@Test
	public void accessLevelShouldNotExistInAnEmptyDatabase() {

		final Integer nonExistentAccessLevelId = 1;
		assertNull(instance.getAccessLevel(nonExistentAccessLevelId));
	}



	/*
	 * When a newly created AccessLevel is read back it should contain the appropriate
	 * member values
	 */
	@Test
	public void createAccessLevelAndReadBack() {

		Integer accessLevelId = null;
		final AccountServiceRole accountServiceRole = ADMIN;

		{
			//
			// Insert a AccessLevel with the above values
			//
			final AccessLevel accessLevel = SimpleComponentFactory.getAccessLevel();
			accessLevel.setAccountServiceRole(accountServiceRole);
			instance.createAccessLevel(accessLevel);
			accessLevelId = accessLevel.getId();
		}
		final AccessLevel createdAccessLevel = instance.getAccessLevel(accessLevelId);
		assertEquals(createdAccessLevel.getAccountServiceRole(), accountServiceRole);
	}

	/*
	 * When a newly created AccessLevel is subsequently updated, reading back by it's
	 * id should reflect the updated values
	 */
	@Test
	public void updatedAccessLevelReflectsChanges() {

		//
		// Apply test values and update
		//
		Integer accessLevelId = null;
		final AccountServiceRole accountServiceRole = ADMIN;
		//
		{
			final AccessLevel accessLevel = instance.createAccessLevel(SimpleComponentFactory.getAccessLevel());
			accessLevelId = accessLevel.getId();
			accessLevel.setAccountServiceRole(accountServiceRole);
			instance.updateAccessLevel(accessLevel);
		}
		//
		// Read back the AccessLevel and confirm changes were persisted
		//
		final AccessLevel updatedAccessLevel = instance.getAccessLevel(accessLevelId);
		assertEquals(updatedAccessLevel.getAccountServiceRole(), accountServiceRole);
	}
}
