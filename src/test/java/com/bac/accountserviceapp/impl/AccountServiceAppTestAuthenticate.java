
package com.bac.accountserviceapp.impl;

import static com.bac.accountservice.AccountServiceAuthenticationOutcome.AUTHENTICATED;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.NO_APPLICATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bac.accountservice.AccountServiceApplication;
import com.bac.accountservice.SimpleAccountServiceApplication;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.impl.AccountServiceApp;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

/**
 *
 * @author Simon Baird
 */
public class AccountServiceAppTestAuthenticate extends AbstractHibernateTestCase {

	//
	@Resource(name = "accountServiceApp")
	private AccountServiceApp instance;
	// logger
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceAppTestAuthenticate.class);

	// ***********************************************************
	// Successful authentication instances
	// ***********************************************************
	@Test
	public void authenticating_An_Existing_Application_Should_Return_Correct_Attributes() {

		logger.info("authenticating_An_Existing_Application_Should_Return_Correct_Attributes");

		final String applicationName = UUID.randomUUID().toString();
		final boolean isEnabled = true;
		final boolean isOpen = true;
		{
			final Application application = SimpleComponentFactory.getApplication();
			application.setName(applicationName);
			application.setEnabled(isEnabled);
			application.setRegistrationOpen(isOpen);
			dao.createApplication(application);
		}
		//
		// Authenticate
		//
		AccountServiceApplication authentication = new SimpleAccountServiceApplication();
		authentication.setName(applicationName);
		AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
		//
		// Test that the application is authentic
		//
		assertNotNull(resultAuthentication);
		assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
		assertEquals(isEnabled, resultAuthentication.isEnabled());
		assertEquals(isOpen, resultAuthentication.isRegistrationOpen());

	}

	@Test
	public void authenticating_A_Disabled_Application_Should_Return_Correct_Attributes() {

		logger.info("authenticating_An_Disable_Application_Should_Return_Correct_Attributes");

		final String applicationName = UUID.randomUUID().toString();
		final boolean isEnabled = false;
		final boolean isOpen = true;
		{
			final Application application = SimpleComponentFactory.getApplication();
			application.setName(applicationName);
			application.setEnabled(isEnabled);
			application.setRegistrationOpen(isOpen);
			dao.createApplication(application);
		}
		//
		// Authenticate
		//
		AccountServiceApplication authentication = new SimpleAccountServiceApplication();
		authentication.setName(applicationName);
		AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
		//
		// Test that the application is authentic
		//
		assertNotNull(resultAuthentication);
		assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
		assertEquals(isEnabled, resultAuthentication.isEnabled());
		assertEquals(isOpen, resultAuthentication.isRegistrationOpen());

	}

	@Test
	public void authenticating_A_Closed_Application_Should_Return_Correct_Attributes() {

		logger.info("testAuthenticateApplication_RegistrationClosed");

		final String applicationName = UUID.randomUUID().toString();
		final boolean isEnabled = true;
		final boolean isOpen = false;
		{
			final Application application = SimpleComponentFactory.getApplication();
			application.setName(applicationName);
			application.setEnabled(isEnabled);
			application.setRegistrationOpen(isOpen);
			dao.createApplication(application);
		}
		//
		// Authenticate
		//
		AccountServiceApplication authentication = new SimpleAccountServiceApplication();
		authentication.setName(applicationName);
		AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
		//
		// Test that the application is authentic
		//
		assertNotNull(resultAuthentication);
		assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
		assertEquals(isEnabled, resultAuthentication.isEnabled());
		assertEquals(isOpen, resultAuthentication.isRegistrationOpen());
	}

	// ***********************************************************
	// Unsuccessful authentication instances
	// ***********************************************************
	@Test
	public void authenticating_An_Unknown_Application_Should_Return_Appropriate_Status() {

		logger.info("authenticating_An_Unknown_Application_Should_Return_Appropriate_Status");

		final String applicationName = UUID.randomUUID().toString();
		final boolean defaultEnabled = false;
		final boolean defaultOpen = false;
		//
		// Authenticate
		//
		AccountServiceApplication authentication = new SimpleAccountServiceApplication();
		authentication.setName(applicationName);
		AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
		//
		// Authenticate the result
		//
		assertEquals(NO_APPLICATION, resultAuthentication.getAuthenticationOutcome());
		assertEquals(defaultEnabled, resultAuthentication.isEnabled());
		assertEquals(defaultOpen, resultAuthentication.isRegistrationOpen());
	}



}
