/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountservice.AccountServiceAuthentication;
import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.*;
import com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.ADMIN;
import com.bac.accountservice.SimpleAccountServiceAuthentication;
import com.bac.accountserviceapp.*;
import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.Account;
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
import java.util.Calendar;
import java.util.Date;
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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceAppNativeAuthenticationTest {

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


    // logger    
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppNativeAuthenticationTest.class);

    public MysqlAccountServiceAppNativeAuthenticationTest() {

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
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Ensure the 
            //
            String expPassword = null;
            String resultPassword = resultAuthentication.getAccountPassword();
            assertEquals(expPassword, resultPassword);
            AccountServiceRole expRole = ADMIN;
            AccountServiceRole resultRole = resultAuthentication.getAccountRole();
            assertEquals(expRole, resultRole);
            String expResource = resourceName;
            String resultResource = (String) resultAuthentication.getAccountResource();
            assertEquals(expResource, resultResource);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication: Delete");
            deleteUserAccount();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAuthentication_NoApplication() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_NoApplication");

        try {
            createUserAccount();
            //
            //  Authenticate
            //
            AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
            authentication.setAccountKey(userEmail);
            authentication.setAccountPassword(userPassword);
            instance.login(authentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_NoApplication", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_NoApplication: Delete");
            deleteUserAccount();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAuthentication_NoUserKey() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_NoUserKey");

        try {
            createUserAccount();
            //
            //  Authenticate
            //
            AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
            authentication.setApplicationName(applicationName);
            authentication.setAccountPassword(userPassword);
            instance.login(authentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_NoUserKey", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_NoUserKey: Delete");
            deleteUserAccount();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAuthentication_NoPassword() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_NoPassword");

        try {
            createUserAccount();
            //
            //  Authenticate
            //
            AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
            authentication.setAccountPassword(userPassword);
            authentication.setAccountKey(userEmail);
            instance.login(authentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_NoPassword", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_NoPassword: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InvalidUserName() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InvalidUserName");

        try {
            createUserAccount();
            //
            //  Authenticate
            //
            AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
            authentication.setApplicationName(applicationName);
            authentication.setAccountKey(UUID.randomUUID().toString());
            authentication.setAccountPassword(userPassword);
            AccountServiceAuthentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, UNKNOWN_PRINCIPAL);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InvalidUserName", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InvalidUserName: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InvalidPassword() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InvalidPassword");

        try {
            createUserAccount();
            //
            //  Authenticate
            //
            AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
            authentication.setApplicationName(applicationName);
            authentication.setAccountKey(userEmail);
            authentication.setAccountPassword(UUID.randomUUID().toString());
            AccountServiceAuthentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, BAD_CREDENTIALS);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InvalidPassword", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InvalidPassword: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_InvalidApplication() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_InvalidApplication");

        try {
            createUserAccount();
            //
            //  Authenticate
            //
            AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
            authentication.setApplicationName(UUID.randomUUID().toString());
            authentication.setAccountKey(userEmail);
            authentication.setAccountPassword(userPassword);
            AccountServiceAuthentication resultAuthentication = instance.login(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_ROLE);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InvalidApplication", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InvalidApplication: Delete");
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
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_ROLE);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_NoUserAccount", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_NoUserAccount: Delete");
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
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, DISABLED_PRINCIPAL);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InactiveUser", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InactiveUser: Delete");
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
            account.setEnabled(false);
            resultAccount = accessor.updateAccount(account);
            assertThat(resultAccount, is(notNullValue()));
            assertEquals(accountId, resultAccount.getId());
            assertEquals(false, resultAccount.isEnabled());
            //
            //  Authenticate
            //
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_ROLE);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InactiveAccount", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InactiveAccount: Delete");
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
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_ROLE);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InactiveApplication", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InactiveApplication: Delete");
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
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_ROLE);
            //
            //  Ensure the 
            //
            authenticateEmptyDetails(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_InactiveAccountUser", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_InactiveAccountUser: Delete");
            deleteUserAccount();
        }
    }

    @Test
    public void testAuthentication_TwoApplications() throws NoSuchAlgorithmException {

        logger.info("testAuthentication_TwoApplications");

        Application application2;
        Application resultApplication2;
        final String applicationName2 = UUID.randomUUID().toString();
        Integer applicationId2;
        //
        Account account2;
        Account resultAccount2;
        final String resourceName2 = UUID.randomUUID().toString();
        Integer accountId2;
        //
        AccountUser accountUser2;
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
            AccountServiceAuthentication resultAuthentication = standardAuthentication();
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, AUTHENTICATED);
            //
            //  Ensure the 
            //
            String expPassword = null;
            String resultPassword = resultAuthentication.getAccountPassword();
            assertEquals(expPassword, resultPassword);
            AccountServiceRole expRole = ADMIN;
            AccountServiceRole resultRole = resultAuthentication.getAccountRole();
            assertEquals(expRole, resultRole);
            String expResource = resourceName;
            String resultResource = (String) resultAuthentication.getAccountResource();
            assertEquals(expResource, resultResource);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testAuthentication_TwoApplications", ex);
        } finally {
            // Clean up
            logger.info("testAuthentication_TwoApplications: Delete");
            deleteUserAccount();
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

    private AccountServiceAuthentication standardAuthentication() {

        AccountServiceAuthentication authentication = new SimpleAccountServiceAuthentication();
        authentication.setApplicationName(applicationName);
        authentication.setAccountKey(userEmail);
        authentication.setAccountPassword(userPassword);
        AccountServiceAuthentication resultAuthentication = instance.login(authentication);
        assertNotNull(resultAuthentication);
        return resultAuthentication;
    }



    private void authenticateOutcome(AccountServiceAuthentication resultAuthentication, AccountServiceAuthenticationOutcome expOutcome) {

        Object authenticationDetails = resultAuthentication.getAuthenticationOutcome();
        assertNotNull(authenticationDetails);
        Class<?> expDetailsClass = AccountServiceAuthenticationOutcome.class;
        Class<?> resultDetailsClass = authenticationDetails.getClass();
        assertEquals(expDetailsClass, resultDetailsClass);
        AccountServiceAuthenticationOutcome authenticationOutcome = (AccountServiceAuthenticationOutcome) authenticationDetails;
        assertEquals(expOutcome, authenticationOutcome);
    }

    private void authenticateEmptyDetails(AccountServiceAuthentication resultAuthentication) {

        String expPassword = null;
        String resultPassword = resultAuthentication.getAccountPassword();
        assertEquals(expPassword, resultPassword);
        AccountServiceRole expRole = null;
        AccountServiceRole resultRole = resultAuthentication.getAccountRole();
        assertEquals(expRole, resultRole);
        String expResource = null;
        String resultResource = (String) resultAuthentication.getAccountResource();
        assertEquals(expResource, resultResource);
    }
}
