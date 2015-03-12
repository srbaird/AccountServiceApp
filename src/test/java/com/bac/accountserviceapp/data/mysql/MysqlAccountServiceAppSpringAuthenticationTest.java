/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.*;
import com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.ADMIN;
import com.bac.accountserviceapp.*;
import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountServiceUserDetails;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.SimpleAccessLevel;
import com.bac.accountserviceapp.data.SimpleAccount;
import com.bac.accountserviceapp.data.SimpleAccountUser;
import com.bac.accountserviceapp.data.SimpleApplication;
import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceAppSpringAuthenticationTest {

    //
    ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String APP_BEAN_NAME = "accountServiceApp";
    private AccountServiceApp instance;
    private BCryptPasswordEncoder encoder;
    // dao
    private static MysqlAccountAccessor accessor;
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    //
    private Application application;
    private Application resultApplication;
    private final String applicationName = UUID.randomUUID().toString();
    private Integer applicationId;
    //
    private AccessLevel accessLevel;
    private AccessLevel resultAccessLevel;
    private final String accessLevelDescription = UUID.randomUUID().toString();
    private final AccountServiceRole accessLevelRole = ADMIN;
    private Integer accessLevelId;
    //
    private User user;
    private User resultUser;
    private final String userName = UUID.randomUUID().toString();
    private final String userEmail = UUID.randomUUID().toString();
    private final String userPassword = UUID.randomUUID().toString();
    private Integer userId = null;
    //
    private Account account;
    private Account resultAccount;
    private final String resourceName = UUID.randomUUID().toString();
    private Integer accountId = null;
    //
    private AccountUser accountUser;
    private AccountUser resultAccountUser;
    //
    private final Date createDate = getDateWithoutMillis();
    private final String AUTHORITY_SEPARATOR = "::";

    // logger    
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppSpringAuthenticationTest.class);

    public MysqlAccountServiceAppSpringAuthenticationTest() {

        appContext = new ClassPathXmlApplicationContext(contextFile);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = appContext.getBean(APP_BEAN_NAME, AccountServiceApp.class);
        // The accessor should be initialized by the application instance
        accessor = appContext.getBean(MYSQL_ACCOUNT_ACCESSOR, MysqlAccountAccessor.class);
        //
        encoder = new BCryptPasswordEncoder();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAuthentication() throws NoSuchAlgorithmException {

        logger.info("testAuthentication");

        try {
            createUserAccount();
            //
            //  Apply any user account manipulation here
            //

            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Get the Authentication Principal: It should represent the UserDetails
            //
            authenticatePrincipal(resultAuthentication, AccountServiceUserDetails.class);
            authenticateUserDetails((UserDetails) resultAuthentication.getPrincipal(), userEmail);
            //
            //  Ensure that the Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 1;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);
            final String expAuthority = applicationName + AUTHORITY_SEPARATOR + accessLevelRole.name();
            List<String> authoritiesList = getAuthorityList(authorities);
            assertNotNull(authoritiesList);
            assertTrue(authoritiesList.contains(expAuthority));

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InvalidUserName() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InvalidUserName");

        try {
            createUserAccount();
            final String invalidUserEmail = UUID.randomUUID().toString();
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(invalidUserEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, UNKNOWN_PRINCIPAL);
            //
            //  Get the Authentication Principal: It should represent the invalid user name
            //
            authenticatePrincipal(resultAuthentication, invalidUserEmail.getClass());
            assertEquals(invalidUserEmail, resultAuthentication.getPrincipal());
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InvalidPassword() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InvalidPassword");

        try {
            createUserAccount();
            final String invalidUserPassword = UUID.randomUUID().toString();
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, invalidUserPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, BAD_CREDENTIALS);
            //
            //  Get the Authentication Principal: It should represent the user name
            //
            authenticatePrincipal(resultAuthentication, userEmail.getClass());
            assertEquals(userEmail, resultAuthentication.getPrincipal());
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_NoUserAccount() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_NoUserAccount");

        try {
            createUserAccount();
            //
            //  Delete the user account. This will not affect the clean up process
            //
            accountUser = accessor.getAccountUser(accountUser);
            accessor.deleteAccountUser(accountUser);
            resultAccountUser = accessor.getAccountUser(accountUser);
            assertThat(resultAccountUser, is(nullValue()));
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Get the Authentication Principal: It should represent the UserDetails
            //
            authenticatePrincipal(resultAuthentication, AccountServiceUserDetails.class);
            authenticateUserDetails((UserDetails) resultAuthentication.getPrincipal(), userEmail);
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InactiveUser() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InactiveUser");

        try {
            createUserAccount();
            //
            //  Set the User to Inactive
            //
            user.setEnabled(false);
            resultUser = accessor.updateUser(user);
            assertThat(resultUser, is(notNullValue()));
            assertEquals(userId, resultUser.getId());
            assertEquals(false, resultUser.isEnabled());
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, DISABLED_PRINCIPAL);
            //
            //  Get the Authentication Principal: It should represent the user name
            //
            authenticatePrincipal(resultAuthentication, userEmail.getClass());
            assertEquals(userEmail, resultAuthentication.getPrincipal());
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InactiveAccount() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InactiveAccount");

        try {
            createUserAccount();
            //
            //  Set the Account to Inactive
            //
            //           account.setActive(INACTIVE);
            account.setEnabled(false);
            resultAccount = accessor.updateAccount(account);
            assertThat(resultAccount, is(notNullValue()));
            assertEquals(accountId, resultAccount.getId());
//            assertEquals(INACTIVE, resultAccount.getActive());
            assertEquals(false, resultAccount.isEnabled());
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Get the Authentication Principal: It should represent the UserDetails
            //
            authenticatePrincipal(resultAuthentication, AccountServiceUserDetails.class);
            authenticateUserDetails((UserDetails) resultAuthentication.getPrincipal(), userEmail);
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InactiveApplication() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InactiveApplication");

        try {
            createUserAccount();
            //
            //  Set the Application to Inactive
            //
            application.setEnabled(false);
            resultApplication = accessor.updateApplication(application);
            assertThat(resultApplication, is(notNullValue()));
            assertEquals(applicationId, resultApplication.getId());
            assertEquals(false, resultApplication.isEnabled());
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Get the Authentication Principal: It should represent the UserDetails
            //
            authenticatePrincipal(resultAuthentication, AccountServiceUserDetails.class);
            authenticateUserDetails((UserDetails) resultAuthentication.getPrincipal(), userEmail);
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InactiveAccountUser() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InactiveAccountUser");

        try {
            createUserAccount();
            //
            //  Set the Account User to Inactive
            //
            accountUser.setEnabled(false);
            resultAccountUser = accessor.updateAccountUser(accountUser);
            assertThat(resultAccountUser, is(notNullValue()));
            assertEquals(false, resultAccountUser.isEnabled());
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Get the Authentication Principal: It should represent the UserDetails
            //
            authenticatePrincipal(resultAuthentication, AccountServiceUserDetails.class);
            authenticateUserDetails((UserDetails) resultAuthentication.getPrincipal(), userEmail);
            //
            //  Ensure that no Authority has been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 0;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_TwoApplications() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InactiveAccountUser");

        Application application2;
        Application resultApplication2;
        final String applicationName2 = UUID.randomUUID().toString();
        Integer applicationId2 = null;
        //
        Account account2;
        Account resultAccount2;
        final String resourceName2 = UUID.randomUUID().toString();
        Integer accountId2 = null;
        //
        AccountUser accountUser2 = null;
        AccountUser resultAccountUser2;

        try {
            createUserAccount();
            //
            //  Create an additonal Application, Account and AccountUser
            //
            //
            //  Create a simple application
            //
            application2 = new SimpleApplication();
            application2.setName(applicationName2);
            application2.setEnabled(true);
            //
            //  Persist it and test
            //
            resultApplication2 = accessor.createApplication(application2);
            assertThat(resultApplication2, is(notNullValue()));
            assertThat(resultApplication2.getId(), is(notNullValue()));
            applicationId2 = resultApplication2.getId();

            //
            //  Create a simple account
            //
            account2 = new SimpleAccount();
            account2.setResourceName(resourceName2);
            account2.setEnabled(true);
            account2.setCreateDate(createDate);
            account2.setApplicationId(applicationId2);
            //
            //  Persist it and test
            //
            resultAccount2 = accessor.createAccount(account2);
            assertThat(resultAccount2, is(notNullValue()));
            assertThat(resultAccount2.getId(), is(notNullValue()));
            accountId2 = resultAccount2.getId();
            //
            //  Create the Account User
            //
            accountUser2 = new SimpleAccountUser();
            accountUser2.setAccountId(accountId2);
            accountUser2.setUserId(userId);
            accountUser2.setEnabled(true);
            accountUser2.setCreateDate(createDate);
            accountUser2.setLastAccessDate(createDate);
            accountUser2.setAccessLevelId(accessLevelId);
            //
            //  Persist it and test
            //
            //
            resultAccountUser2 = accessor.createAccountUser(accountUser2);
            assertThat(resultAccountUser2, is(notNullValue()));
            //
            //  Authenticate
            //
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Get the Authentication Principal: It should represent the UserDetails
            //
            authenticatePrincipal(resultAuthentication, AccountServiceUserDetails.class);
            authenticateUserDetails((UserDetails) resultAuthentication.getPrincipal(), userEmail);
            //
            //  Ensure that both Authorities have been granted
            //
            Collection<? extends GrantedAuthority> authorities = resultAuthentication.getAuthorities();
            assertNotNull(authorities);
            int expAuthoritySize = 2;
            int resultAuthoritySize = authorities.size();
            assertEquals(expAuthoritySize, resultAuthoritySize);
            List<String> authoritiesList = getAuthorityList(authorities);
            assertNotNull(authoritiesList);
            final String expAuthority1 = applicationName + AUTHORITY_SEPARATOR + accessLevelRole.name();
            assertTrue(authoritiesList.contains(expAuthority1));
            final String expAuthority2 = applicationName2 + AUTHORITY_SEPARATOR + accessLevelRole.name();
            assertTrue(authoritiesList.contains(expAuthority2));

        } catch (InvalidKeySpecException ex) {
            logger.error("Password encryption error");
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
            //
            //  Clean up the second application account
            //
            //  Remove Account 
            //
            account2 = accessor.getAccount(accountId2);
            accessor.deleteAccount(account2);
            resultAccount2 = accessor.getAccount(accountId2);
            assertThat(resultAccount2, is(nullValue()));
            //
            //  Remove Account User
            //
//            accountUser2 = accessor.getAccountUser(accountUser2);
//            accessor.deleteAccountUser(accountUser2);
            resultAccountUser2 = accessor.getAccountUser(accountUser2);
            assertThat(resultAccountUser2, is(nullValue()));
            //
            //  Remove Application
            //
            application2 = accessor.getApplication(applicationId2);
            accessor.deleteApplication(application2);
            resultApplication2 = accessor.getApplication(applicationId2);
            assertThat(resultApplication2, is(nullValue()));
        }
    }

    //
    //  Private methods
    //
    private Date getDateWithoutMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    private void createUserAccount() throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] salt = "No longer reqired".getBytes();
        //
        //  Create a simple application
        //
        application = new SimpleApplication();
        application.setName(applicationName);
        application.setEnabled(true);
        //
        //  Persist it and test
        //
        resultApplication = accessor.createApplication(application);
        assertThat(resultApplication, is(notNullValue()));
        assertThat(resultApplication.getId(), is(notNullValue()));
        applicationId = resultApplication.getId();
        //
        //  Create a simple account
        //
        account = new SimpleAccount();
        account.setResourceName(resourceName);
        account.setEnabled(true);
        account.setCreateDate(createDate);
        account.setApplicationId(applicationId);
        //
        //  Persist it and test
        //
        resultAccount = accessor.createAccount(account);
        assertThat(resultAccount, is(notNullValue()));
        assertThat(resultAccount.getId(), is(notNullValue()));
        accountId = resultAccount.getId();
        //
        //
        //  Create a simple access level
        //
        accessLevel = new SimpleAccessLevel();
        accessLevel.setAccountServiceRole(accessLevelRole);
        //
        //  Persist it and test
        //
        resultAccessLevel = accessor.createAccessLevel(accessLevel);
        assertThat(resultAccessLevel, is(notNullValue()));
        assertThat(resultAccessLevel.getId(), is(notNullValue()));
        accessLevelId = resultAccessLevel.getId();
        //
        //  Create a simple user
        //
        user = new SimpleUser();
        user.setUserName(userName);
        user.setUserEmail(userEmail);
        user.setEnabled(true);
        user.setCreateDate(createDate);
        // Authentication
        user.setPasswordSalt(salt);
//        user.setUserPassword(PasswordAuthentication.getEncryptedPassword(userPassword, salt));
        user.setUserPassword(encoder.encode(userPassword).getBytes());
        //
        //  Persist it and test
        //
        resultUser = accessor.createUser(user);
        assertThat(resultUser, is(notNullValue()));
        assertThat(resultUser.getId(), is(notNullValue()));
        Assert.assertArrayEquals(salt, resultUser.getPasswordSalt());
        userId = resultUser.getId();
        //
        //  Create the Account User
        //
        accountUser = new SimpleAccountUser();
        accountUser.setAccountId(accountId);
        accountUser.setUserId(userId);
        accountUser.setEnabled(true);
        accountUser.setCreateDate(createDate);
        accountUser.setLastAccessDate(createDate);
        accountUser.setAccessLevelId(accessLevelId);
        //
        //  Persist it and test
        //
        //
        resultAccountUser = accessor.createAccountUser(accountUser);
        assertThat(resultAccountUser, is(notNullValue()));
    }

    private void deleteUserAccount() {

        //
        // Remove User
        //
        user = accessor.getUser(userId);
        accessor.deleteUser(user);
        resultUser = accessor.getUser(userId);
        assertThat(resultUser, is(nullValue()));
        //
        //  Account User should be gone
        //
        resultAccountUser = accessor.getAccountUser(accountUser);
        assertThat(resultAccountUser, is(nullValue()));
        //
        //  Remove Access Level
        //
        accessLevel = accessor.getAccessLevel(accessLevelId);
        accessor.deleteAccessLevel(accessLevel);
        resultAccessLevel = accessor.getAccessLevel(accessLevelId);
        assertThat(resultAccessLevel, is(nullValue()));
        //
        //  Remove Account 
        //
        account = accessor.getAccount(accountId);
        accessor.deleteAccount(account);
        resultAccount = accessor.getAccount(accountId);
        assertThat(resultAccount, is(nullValue()));
        //
        //  Remove Application
        //
        application = accessor.getApplication(applicationId);
        accessor.deleteApplication(application);
        resultApplication = accessor.getApplication(applicationId);
        assertThat(resultApplication, is(nullValue()));
    }

    private String[] getAuthorityArray(Collection<? extends GrantedAuthority> authorities) {

        String[] authoritiesArray = new String[authorities.size()];
        int index = 0;
        for (GrantedAuthority authority : authorities) {
            authoritiesArray[index++] = authority.getAuthority();
        }
        return authoritiesArray;
    }

    private List<String> getAuthorityList(Collection<? extends GrantedAuthority> authorities) {

        String[] authoritiesArray = getAuthorityArray(authorities);
        return Arrays.asList(authoritiesArray);
    }

    private void authenticateOutcome(Authentication resultAuthentication, AccountServiceAuthenticationOutcome expOutcome) {

        Object authenticationDetails = resultAuthentication.getDetails();
        assertNotNull(authenticationDetails);
        Class<?> expDetailsClass = AccountServiceAuthenticationOutcome.class;
        Class<?> resultDetailsClass = authenticationDetails.getClass();
        assertEquals(expDetailsClass, resultDetailsClass);
        AccountServiceAuthenticationOutcome authenticationOutcome = (AccountServiceAuthenticationOutcome) authenticationDetails;
        assertEquals(expOutcome, authenticationOutcome);
    }

    private void authenticatePrincipal(Authentication resultAuthentication, Class<?> expClass) {

        Object authenticationPrincipal = resultAuthentication.getPrincipal();
        assertNotNull(authenticationPrincipal);
        Class<?> resultClass = authenticationPrincipal.getClass();
        assertEquals(expClass, resultClass);
    }

    private void authenticateUserDetails(UserDetails userDetails, String expUserName) {

        boolean expEnabled = true;
        boolean resultEnabled = userDetails.isEnabled();
        assertEquals(expEnabled, resultEnabled);
        String resultUsername = userDetails.getUsername();
        assertEquals(expUserName, resultUsername);
    }
}
