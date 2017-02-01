package com.bac.accountserviceapp.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;

import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

public class HibernateTestApplication extends AbstractHibernateTestCase {

	/*
	 * Creating a new Application should have either null or default values for
	 * each member apart from the id.
	 */
	@Test
	public void createdApplicationHasNullMembers() {

		//
		// Persist a simple Application and test
		//
		Application result = instance.createApplication(SimpleComponentFactory.getApplication());
		assertNotNull(result);
		assertNotNull(result.getId());
		//
		// Enabled defaults to false
		//
		final boolean DEFAULT_ENABLED = false;
		assertEquals(result.isEnabled(), DEFAULT_ENABLED);
		//
		// Accept registration defaults to false
		//
		final boolean DEFAULT_REGISTRATION_OPEN = false;
		assertEquals(result.isRegistrationOpen(), DEFAULT_REGISTRATION_OPEN);
		//
		// The remaining members should have null values
		//
		assertNull(result.getName());
	}

	/*
	 * A Application that has been deleted should not be found
	 */
	@Test
	public void deletedApplicationShouldNotExist() {

		//
		// Create a simple Application
		//
		Application result = instance.createApplication(SimpleComponentFactory.getApplication());
		assertNotNull(result);
		assertNotNull(result.getId());
		//
		// Delete the Application
		//
		instance.deleteApplication(result);
		//
		// Re-reading the Application should now result in a null value
		//
		Integer ApplicationId = result.getId();
		assertNull(instance.getApplication(ApplicationId));
	}

	/*
	 * An empty database should contain no Applications
	 */
	@Test
	public void ApplicationShouldNotExistInAnEmptyDatabase() {

		final Integer nonExistentApplicationId = 1;
		assertNull(instance.getApplication(nonExistentApplicationId));
	}



	/*
	 * When a newly created Application is read back it should contain the
	 * appropriate member values
	 */
	@Test
	public void createApplicationAndReadBack() {

		Integer applicationId = null;
		final String applicationName = "Application Account";
		final boolean enabled = true;
		final boolean acceptRegistration = true;
		{
			//
			// Insert a Application with the above values
			//
			final Application application = SimpleComponentFactory.getApplication();
			application.setEnabled(enabled);
			application.setRegistrationOpen(acceptRegistration);
			application.setName(applicationName);
			instance.createApplication(application);
			applicationId = application.getId();
		}
		final Application createdApplication = instance.getApplication(applicationId);
		assertEquals(createdApplication.isEnabled(), enabled);
		assertEquals(createdApplication.isRegistrationOpen(), acceptRegistration);
		assertEquals(createdApplication.getName(), applicationName);
	}

	/*
	 * When a newly created Application is subsequently updated, reading back by
	 * it's id should reflect the updated values
	 */
	@Test
	public void updatedApplicationReflectsChanges() {

		//
		// Apply test values and update
		//
		Integer applicationId = null;
		final String applicationName = "Application Account";
		final boolean enabled = true;
		final boolean acceptRegistration = true;
		{
			final Application application = instance.createApplication(SimpleComponentFactory.getApplication());
			applicationId = application.getId();
			application.setEnabled(enabled);
			application.setRegistrationOpen(acceptRegistration);
			application.setName(applicationName);
			instance.updateApplication(application);
		}
		//
		// Read back the Application and confirm changes were persisted
		//
		final Application updatedApplication = instance.getApplication(applicationId);
		assertEquals(updatedApplication.isEnabled(), enabled);
		assertEquals(updatedApplication.isRegistrationOpen(), acceptRegistration);
		assertEquals(updatedApplication.getName(), applicationName);
	}

	/*
	 * Application secondary keys should be unique. Creating more than one
	 * Application with the same secondary key (name) should throw an exception
	 */
	@Test(expected=ConstraintViolationException.class)
	public void duplicate_Secondary_Keys_Should_Cause_An_Exception() {
		
		final String secondaryKey = "ApplicationAccount";
		
		Application  application1 = SimpleComponentFactory.getApplication();
		Application  application2 = SimpleComponentFactory.getApplication();
		
		application1.setName(secondaryKey);
		application2.setName(secondaryKey);
		
		instance.createApplication(application1);
		instance.createApplication(application2);	
	}
	
	/*
	 * A newly created Application should be available through a secondary key
	 * search which in this case is the Application name.
	 */
	@Test
	public void testGetApplicationBySecondaryKey() {

		Integer applicationId = null;
		final String applicationName = "Application Account";
		{
			//
			// Insert a Application with the above value
			//
			final Application application = SimpleComponentFactory.getApplication();
			application.setName(applicationName);
			instance.createApplication(application);
			applicationId = application.getId();
		}
		//
		// The secondary key is the name. Searching via this should return the
		// same Application id
		//
		Application scondaryKeyApplication = SimpleComponentFactory.getApplication();
		scondaryKeyApplication.setName(applicationName);
		scondaryKeyApplication = instance.getApplicationBySecondaryKey(scondaryKeyApplication);
		assertEquals(applicationId, scondaryKeyApplication.getId());
	}
}
