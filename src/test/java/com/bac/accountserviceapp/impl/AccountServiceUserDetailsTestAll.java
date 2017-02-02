
package com.bac.accountserviceapp.impl;

import static com.bac.accountservice.AccountServiceRole.ADMIN;
import static com.bac.accountservice.AccountServiceRole.GUEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.impl.AccountServiceUserDetails;
import com.bac.accountserviceapp.impl.DataConstants;
import com.bac.accountserviceapp.impl.UserDetailsAuthority;

/**
 *
 * @author Simon Baird
 */
public class AccountServiceUserDetailsTestAll {

	private AccountServiceUserDetails instance;
	private Collection<UserDetailsAuthority> userDetailsAuthorities;
	//
	private final AccountServiceRole ACCESS_LEVEL_ROLE = GUEST;

	// logger
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceUserDetailsTestAll.class);

	public AccountServiceUserDetailsTestAll() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		instance = new AccountServiceUserDetails(null, null);
		userDetailsAuthorities = new HashSet<>();
	}

	@After
	public void tearDown() {
	}

	/**
	 * Newly instantiated instance should have an empty set of authorities
	 */
	@Test
	public void empty_Service_Should_Have_No_Authorities() {

		logger.info("empty_Service_Should_Have_No_Authorities");

		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		int empty = 0;
		assertEquals(empty, result.size());
	}

	/**
	 * Newly instantiated instance should have a null password
	 */
	@Test
	public void empty_Service_Should_Have_Null_Password() {

		logger.info("empty_Service_Should_Have_Null_Password");

		String expResult = null;
		assertEquals(expResult, instance.getPassword());
	}

	/**
	 * Newly instantiated instance should have a null User name
	 */
	@Test
	public void empty_Service_Should_Have_Null_Username() {

		logger.info("empty_Service_Should_Have_Null_Username");

		String expResult = null;
		assertEquals(expResult, instance.getUsername());
	}

	/**
	 * Newly instantiated instance should not have an expired account
	 */
	@Test
	public void empty_Service_Should_Not_Have_Expired_Account() {

		logger.info("empty_Service_Should_Not_Have_Expired_Account");
		boolean expResult = true;
		assertEquals(expResult, instance.isAccountNonExpired());
	}

	/**
	 * Newly instantiated instance should not have a locked account
	 */
	@Test
	public void empty_Service_Should_Not_Have_Locked_Account() {

		logger.info("empty_Service_Should_Not_Have_Locked_Account");

		boolean expResult = true;
		assertEquals(expResult, instance.isAccountNonLocked());
	}

	/**
	 * Newly instantiated instance should not have expired credentials
	 */
	@Test
	public void empty_Service_Should_Not_Have_Expired_Credentials() {

		logger.info("empty_Service_Should_Not_Have_Expired_Credentials");
		boolean expResult = true;
		assertEquals(expResult, instance.isCredentialsNonExpired());
	}

	/**
	 * Newly instantiated instance should not be enabled
	 */
	@Test
	public void empty_Service_Should_Not_Be_Enabled() {

		logger.info("empty_Service_Should_Not_Be_Enabled");

		boolean expResult = false;
		assertEquals(expResult, instance.isEnabled());
	}

	/**
	 * Service should return correct User name
	 */
	@Test
	public void service_Should_Return_Correct_Username() {

		logger.info("testGetUsername");
		final String expUserKey = UUID.randomUUID().toString();
		final String expPassword = UUID.randomUUID().toString();
		User user = getMockUser(expUserKey, expPassword, true);
		//
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expUserKey, instance.getUsername());
	}

	/**
	 * Service should return correct User password
	 */
	@Test
	public void service_Should_Return_Correct_Password() {

		logger.info("testGetPassword");
		final String expUserKey = UUID.randomUUID().toString();
		final String expPassword = UUID.randomUUID().toString();
		User user = getMockUser(expUserKey, expPassword, true);
		//
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expPassword, instance.getPassword());
	}

	/**
	 * Service should return the correct Account status
	 */
	@Test
	public void service_Should_Return_Correct_Account_Status() {

		logger.info("service_Should_Return_Correct_Account_Status");
		boolean expIsAccountNonExpired = true;
		//
		final String expUserKey = UUID.randomUUID().toString();
		final String expPassword = UUID.randomUUID().toString();
		User user = getMockUser(expUserKey, expPassword, true);
		//
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsAccountNonExpired, instance.isAccountNonExpired());
		//
		user = getMockUser(expUserKey, expPassword, false);
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsAccountNonExpired, instance.isAccountNonExpired());
	}

	/**
	 * Service should return the correct Account lock status
	 */
	@Test
	public void service_Should_Return_Correct_Account_Lock_Status() {

		logger.info("service_Should_Return_Correct_Account_Lock_Status");
		boolean expIsAccountNonLocked = true;
		//
		String expUserKey = UUID.randomUUID().toString();
		String expPassword = UUID.randomUUID().toString();
		User user = getMockUser(expUserKey, expPassword, true);
		//
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsAccountNonLocked, instance.isAccountNonLocked());
		//
		user = getMockUser(expUserKey, expPassword, false);
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsAccountNonLocked, instance.isAccountNonLocked());
	}

	/**
	 * Service should return the correct Credential status
	 */
	@Test
	public void service_Should_Return_Correct_Credential_Status() {

		logger.info("service_Should_Return_Correct_Credential_Status");
		boolean expIsCredentialsNonExpired = true;
		//
		String expUserKey = UUID.randomUUID().toString();
		String expPassword = UUID.randomUUID().toString();
		User user = getMockUser(expUserKey, expPassword, true);
		//
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsCredentialsNonExpired, instance.isCredentialsNonExpired());
		//
		user = getMockUser(expUserKey, expPassword, false);
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsCredentialsNonExpired, instance.isCredentialsNonExpired());
	}

	/**
	 * Service should return the correct enabled status
	 */
	@Test
	public void service_Should_Return_Correct_Enabled_Status() {

		logger.info("testIsEnabled");
		boolean expIsEnabled = true;
		//
		String expUserKey = UUID.randomUUID().toString();
		String expPassword = UUID.randomUUID().toString();
		User user = getMockUser(expUserKey, expPassword, true);
		//
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsEnabled, instance.isEnabled());
		//
		expIsEnabled = false;
		user = getMockUser(expUserKey, expPassword, false);
		instance = new AccountServiceUserDetails(user, null);
		assertEquals(expIsEnabled, instance.isEnabled());
	}

	/**
	 * Service should return the correct Authorities
	 */
	@Test
	public void service_Should_Return_The_Correct_Authorities() {

		logger.info("service_Should_Return_The_Correct_Authorities");

		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		int empty = 0;
		assertEquals(empty, result.size());
	}

	/**
	 * Service should return an empty set when provided with a single invalid
	 * authority
	 */
	@Test
	public void service_Should_Return_Empty_Set_For_Single_Invalid_Authority() {

		logger.info("service_Should_Return_Empty_Set_For_Single_Invalid_Authority");
		//
		// Inactive application
		//
		String expApplicationName = UUID.randomUUID().toString();
		Integer expAccessLevelId = new Double(Math.random()).intValue();
		Application application = getMockApplication(expApplicationName, false);

		AccessLevel accessLevel = getMockAccessLevel(expAccessLevelId);
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		final int empty = 0;
		assertEquals(empty, result.size());
		//
		// Inactive account user
		//
		expApplicationName = UUID.randomUUID().toString();
		expAccessLevelId = new Double(Math.random()).intValue();
		application = getMockApplication(expApplicationName, false);
		accessLevel = getMockAccessLevel(expAccessLevelId);
		userDetailsAuthorities.clear();
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		assertEquals(empty, result.size());
	}

	/**
	 * Service should return an empty set when provided with multiple invalid
	 * authorities
	 */
	// @Test
	public void service_Should_Return_Empty_Set_For_Multiple_Invalid_Authority() {

		logger.info("service_Should_Return_Empty_Set_For_Multiple_Invalid_Authority");

		Application application = getMockApplication(UUID.randomUUID().toString(), false);
		AccessLevel accessLevel = getMockAccessLevel(new Double(Math.random()).intValue());
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		application = getMockApplication(UUID.randomUUID().toString(), false);
		accessLevel = getMockAccessLevel(new Double(Math.random()).intValue());
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		application = getMockApplication(UUID.randomUUID().toString(), false);
		accessLevel = getMockAccessLevel(new Double(Math.random()).intValue());
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		int empty = 0;
		assertEquals(empty, result.size());
	}

	/**
	 * Service should return a valid authority
	 */
	@Test
	public void service_Should_Return_Correct_Single_Valid_Authority() {

		logger.info("service_Should_Return_Correct_Single_Valid_Authority");
		//
		//
		//
		final String expApplicationName = UUID.randomUUID().toString();
		final Integer expAccessLevelId = new Double(Math.random()).intValue();
		final String expAuthority = expApplicationName + DataConstants.AUTHORITY_SEPARATOR + ACCESS_LEVEL_ROLE.name();
		Application application = getMockApplication(expApplicationName, true);
		AccessLevel accessLevel = getMockAccessLevel(expAccessLevelId);
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		int single = 1;
		assertEquals(single, result.size());
		//
		//
		//
		String resultAuthority = result.stream().findFirst().get().getAuthority();
		assertEquals(expAuthority, resultAuthority);
	}

	/**
	 * Service should return only the valid authority when supplied with one
	 * valid and one invalid Application
	 */
	@Test
	public void service_Should_Return_CorrectAuthority_When_Supplied_With_Both_Valid_And_Invalid() {

		logger.info("service_Should_Return_CorrectAuthority_When_Supplied_With_Both_Valid_And_Invalid");
		//
		// Valid authority
		//
		final String expApplicationName = UUID.randomUUID().toString();
		final Integer expAccessLevelId = new Double(Math.random()).intValue();
		final String expAuthority = expApplicationName + DataConstants.AUTHORITY_SEPARATOR + ACCESS_LEVEL_ROLE.name();
		Application application = getMockApplication(expApplicationName, true);
		AccessLevel accessLevel = getMockAccessLevel(expAccessLevelId);
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));
		//
		// Invalid authority
		//
		application = getMockApplication(UUID.randomUUID().toString(), false);
		accessLevel = getMockAccessLevel(expAccessLevelId);
		userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		int single = 1;
		assertEquals(single, result.size());
		//
		//
		//
		String resultAuthority = result.stream().findFirst().get().getAuthority();
		assertEquals(expAuthority, resultAuthority);
	}
	/**
	 * Service should return all the valid authorities when supplied with multiple valid Applications
	 */
	@Test
	public void service_Should_Return_Correct_Authorities_When_Supplied_With_Multiple_Valid() {

		logger.info("service_Should_Return_Correct_Authorities_When_Supplied_With_Multiple_Valid");
		//
		// Valid authority 1
		//
		final String expApplicationName1 = UUID.randomUUID().toString();
		final Integer expAccessLevelId1 = new Double(Math.random()).intValue();
		final String expAuthority1 = expApplicationName1 + DataConstants.AUTHORITY_SEPARATOR + ACCESS_LEVEL_ROLE.name();
		Application application1 = getMockApplication(expApplicationName1, true);
		AccessLevel accessLevel1 = getMockAccessLevel(expAccessLevelId1);
		userDetailsAuthorities.add(new UserDetailsAuthority(application1, accessLevel1));
		//
		// Valid authority 2
		//
		final String expApplicationName2 = UUID.randomUUID().toString();
		final Integer expAccessLevelId2 = new Double(Math.random()).intValue();
		final String expAuthority2 = expApplicationName2 + DataConstants.AUTHORITY_SEPARATOR + ADMIN.name();
		Application application2 = getMockApplication(expApplicationName2, true);
		AccessLevel accessLevel2 = getMockAccessLevel(expAccessLevelId2, ADMIN);
		userDetailsAuthorities.add(new UserDetailsAuthority(application2, accessLevel2));


		instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
		Collection<? extends GrantedAuthority> result = instance.getAuthorities();
		assertTrue(result instanceof Set<?>);
		int both = 2;
		assertEquals(both, result.size());
		//
		//
		//
		List<String> resultAuthorities = result.stream().map(a -> a.getAuthority()).collect(Collectors.toList());
		assertTrue(resultAuthorities.contains(expAuthority1));
		assertTrue(resultAuthorities.contains(expAuthority2));
	}

	//
	//
	//
	private User getMockUser(final String userKey, final String password, final boolean enabled) {

		User user = mock(User.class);
		when(user.getUserKey()).thenReturn(userKey);
		when(user.getUserPassword()).thenReturn(password.getBytes());
		when(user.isEnabled()).thenReturn(enabled);
		return user;
	}

	private Application getMockApplication(final String applicationName, final boolean enabled) {

		Application application = mock(Application.class);
		when(application.getName()).thenReturn(applicationName);
		when(application.isEnabled()).thenReturn(enabled);
		return application;
	}

	private AccessLevel getMockAccessLevel(final Integer accessLevelId) {

		return getMockAccessLevel(accessLevelId, ACCESS_LEVEL_ROLE);
	}
	
	private AccessLevel getMockAccessLevel(final Integer accessLevelId, AccountServiceRole role) {

		AccessLevel accessLevel = mock(AccessLevel.class);
		when(accessLevel.getId()).thenReturn(accessLevelId);
		when(accessLevel.getAccountServiceRole()).thenReturn(role);
		return accessLevel;
	}
}
