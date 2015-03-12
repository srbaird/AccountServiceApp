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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
public class MysqlAccountServiceAppCreateLoginTest {

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
    private final AccountServiceRole expRole = DataConstants.defaultAccountServiceRole;
    //
    private User user;
    private User resultUser;
    private final String userName = UUID.randomUUID().toString();
    private final String userEmail = UUID.randomUUID().toString();
    private final String userPassword = UUID.randomUUID().toString();
    private Integer userId = null;
    //

    private Account resultAccount;
    private final String expResourceName = UUID.randomUUID().toString();
    private Integer accountId = null;
    //
    private AccountUser accountUser;
    private AccountUser resultAccountUser;
    //
    private final Date createDate = getDateWithoutMillis();

    // logger    
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppCreateLoginTest.class);

    public MysqlAccountServiceAppCreateLoginTest() {

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

    //  ***********************************************************
    //  Successfull login instances
    //  ***********************************************************
    @Test
    public void testCreateLogin() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin");

        try {
            createApplication();
            AccountServiceAuthentication authentication = standardAuthentication();
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, PENDING_CREATION);
            //
            //  Authenticate the result
            //
            successfulAuthentication(resultAuthentication);


        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin: Delete");
            deleteLoginDetails();
        }
    }

    @Test
    public void testCreateLogin_UserExists() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_UserExists");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            //  Create a simple user
            //
            user = new SimpleUser();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setEnabled(true);
            user.setCreateDate(createDate);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
            //
            //  Persist it and test
            //
            resultUser = accessor.createUser(user);
            assertThat(resultUser, is(notNullValue()));
            assertThat(resultUser.getId(), is(notNullValue()));
            userId = resultUser.getId();
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, PENDING_CREATION);
            //
            //  Authenticate the result
            //
            successfulAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_UserExists", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_UserExists: Delete");
            deleteLoginDetails();
        }
    }

    //  ***********************************************************
    //  Unsuccessfull login instances
    //  ***********************************************************
    @Test
    public void testCreateLogin_NoApplication() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_NoApplication");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            authentication.setApplicationName(null);
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_APPLICATION);
            //
            //  Authenticate the result
            //
            failedAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_NoApplication", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_NoApplication: Delete");
            deleteLoginDetails();
        }
    }

    @Test
    public void testCreateLogin_NoUserKey() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_NoUserKey");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            authentication.setAccountKey(null);
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, UNKNOWN_PRINCIPAL);
            //
            //  Authenticate the result
            //
            failedAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_NoUserKey", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_NoUserKey: Delete");
            deleteLoginDetails();
        }
    }

    @Test
    public void testCreateLogin_NoUserPassword() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_NoUserPassword");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            authentication.setAccountPassword(null);
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, BAD_CREDENTIALS);
            //
            //  Authenticate the result
            //
            failedAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_NoUserPassword", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_NoUserPassword: Delete");
            deleteLoginDetails();
        }
    }

    @Test
    public void testCreateLogin_InvalidApplication() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_NoApplication");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            authentication.setApplicationName(UUID.randomUUID().toString());
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, NO_APPLICATION);
            //
            //  Authenticate the result
            //
            failedAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_NoApplication", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_NoApplication: Delete");
            deleteLoginDetails();
        }
    }

    @Test
    public void testCreateLogin_UserExistsWithInvalidPassword() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_UserExistsWithInvalidPassword");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            //  Create a simple user
            //
            user = new SimpleUser();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setEnabled(true);
            user.setCreateDate(createDate);
            String invalidPassword = UUID.randomUUID().toString();
            user.setUserPassword(encoder.encode(invalidPassword).getBytes());
            //
            //  Persist it and test
            //
            resultUser = accessor.createUser(user);
            assertThat(resultUser, is(notNullValue()));
            assertThat(resultUser.getId(), is(notNullValue()));
            userId = resultUser.getId();
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, PRINCIPAL_EXISTS);
            //
            //  Authenticate the result
            //
           failedAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_UserExistsWithInvalidPassword", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_UserExistsWithInvalidPassword: Delete");
            deleteLoginDetails();
        }
    }

    @Test
    public void testCreateLogin_UserAlreadyAssigned() throws NoSuchAlgorithmException {

        logger.info("testCreateLogin_UserAlreadyAssigned");

        try {
            createApplication();
            AccountServiceAuthentication authentication = this.standardAuthentication();
            //
            //  Get access level
            //
            AccessLevel accessLevel = new SimpleAccessLevel();
            accessLevel.setAccountServiceRole(expRole);
            accessLevel = accessor.getAccessLevelBySecondaryKey(accessLevel);
            assertNotNull(accessLevel);
            assertNotNull(accessLevel.getId());
            //
            //  Create a simple account
            //
            resultAccount = new SimpleAccount();
            resultAccount.setResourceName(expResourceName);
            resultAccount.setEnabled(true);
            resultAccount.setCreateDate(createDate);
            resultAccount.setApplicationId(applicationId);
            //
            //  Persist it and test
            //
            resultAccount = accessor.createAccount(resultAccount);
            assertThat(resultAccount, is(notNullValue()));
            assertThat(resultAccount.getId(), is(notNullValue()));
            accountId = resultAccount.getId();
            //
            //  Create a simple user
            //
            user = new SimpleUser();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setEnabled(true);
            user.setCreateDate(createDate);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
            //
            //  Persist it and test
            //
            resultUser = accessor.createUser(user);
            assertThat(resultUser, is(notNullValue()));
            assertThat(resultUser.getId(), is(notNullValue()));
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
            accountUser.setAccessLevelId(accessLevel.getId());
            //
            //  Persist it and test
            //
            //
            accountUser = accessor.createAccountUser(accountUser);
            assertThat(accountUser, is(notNullValue()));
            //
            //  Create Login
            //
            AccountServiceAuthentication resultAuthentication = instance.createLogin(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Check the outcome
            //
            authenticateOutcome(resultAuthentication, PRINCIPAL_EXISTS);
            //
            //  Authenticate the result
            //
            failedAuthentication(resultAuthentication);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("testCreateLogin_UserAlreadyAssigned", ex);
        } finally {
            // Clean up
            logger.info("testCreateLogin_UserAlreadyAssigned: Delete");
            deleteLoginDetails();
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

    private void createApplication() throws NoSuchAlgorithmException, InvalidKeySpecException {

        //
        //  Create a simple application
        //
        application = new SimpleApplication();
        application.setName(applicationName);
        application.setEnabled(true);
        application.setRegistrationOpen(true);
        //
        //  Persist it and test
        //
        resultApplication = accessor.createApplication(application);
        assertThat(resultApplication, is(notNullValue()));
        assertThat(resultApplication.getId(), is(notNullValue()));
        applicationId = resultApplication.getId();
    }

    private void deleteLoginDetails() {

        //
        //  Get user by secondary key. If not null then get all the accounts and delete them then delete the user
        //
        user = new SimpleUser();
        user.setUserEmail(userEmail);
        user = accessor.getUserBySecondaryKey(user);

        if (user != null) {

            userId = user.getId();
            accessor.deleteUser(user);
            resultUser = accessor.getUser(userId);
            assertNull(resultUser);

            for (Account childAccount : user.getAccounts()) {
                accountId = childAccount.getId();
                accessor.deleteAccount(childAccount);
                resultAccount = accessor.getAccount(accountId);
                assertNull(resultAccount);
            }

        }
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
        authentication.setAccountResource(expResourceName);
        return authentication;
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

    private void successfulAuthentication(AccountServiceAuthentication resultAuthentication) {

        String expPassword = null;
        String resultPassword = resultAuthentication.getAccountPassword();
        assertEquals(expPassword, resultPassword);
        AccountServiceRole resultRole = resultAuthentication.getAccountRole();
        assertEquals(expRole, resultRole);
        String resultResource = (String) resultAuthentication.getAccountResource();
        assertEquals(expResourceName, resultResource);
    }

    private void failedAuthentication(AccountServiceAuthentication resultAuthentication) {

        String expPassword = null;
        String resultPassword = resultAuthentication.getAccountPassword();
        assertEquals(expPassword, resultPassword);
        AccountServiceRole resultRole = resultAuthentication.getAccountRole();
        assertEquals(null, resultRole);
        String expResource = null;
        String resultResource = (String) resultAuthentication.getAccountResource();
        assertEquals(expResource, resultResource);
    }
}
