/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountservice.AccountServiceApplication;
import com.bac.accountservice.AccountServiceAuthentication;
import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.*;
import com.bac.accountservice.SimpleAccountServiceApplication;
import com.bac.accountserviceapp.*;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.SimpleApplication;
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
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceAppAuthenticateAppTest {

    //
    ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String APP_BEAN_NAME = "accountServiceApp";
    private AccountServiceApp instance;
    // dao
    private static MysqlAccountAccessor accessor;
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    //
    private Application application;
    private Application resultApplication;
    private final String applicationName = UUID.randomUUID().toString();
    private final boolean expEnabled = true;
    private final boolean expRegistrationOpen = true;
    private Integer applicationId;
    //
    //
    private final Date createDate = getDateWithoutMillis();
    // logger    
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppAuthenticateAppTest.class);

    public MysqlAccountServiceAppAuthenticateAppTest() {

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
    }

    @After
    public void tearDown() {
    }

    //  ***********************************************************
    //  Successful authentication instances
    //  ***********************************************************
    @Test
    public void testAuthenticateApplication() {

        logger.info("testAuthenticateApplication");

        try {
            createApplication();
            //
            //  Authenticate
            //
            AccountServiceApplication authentication = standardAuthentication();
            AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Authenticate the result
            //
            assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
            assertEquals(expEnabled, resultAuthentication.isEnabled());
            assertEquals(expRegistrationOpen, resultAuthentication.isRegistrationOpen());
        } finally {
            // Clean up
            logger.info("testAuthenticateApplication: Delete");
            deleteApplication();
        }
    }

    @Test
    public void testAuthenticateApplication_Disabled() {

        logger.info("testAuthenticateApplication_Disabled");

        try {
            createApplication();
            //
            //  Set enabled false
            //
            Application updateApplication = accessor.getApplication(applicationId);
            assertNotNull(updateApplication);
            boolean testEnabled = false;
            updateApplication.setEnabled(testEnabled);
            accessor.updateApplication(updateApplication);
            //
            //  Authenticate
            //
            AccountServiceApplication authentication = standardAuthentication();
            AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Authenticate the result
            //
            assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
            assertEquals(testEnabled, resultAuthentication.isEnabled());
            assertEquals(expRegistrationOpen, resultAuthentication.isRegistrationOpen());
        } finally {
            // Clean up
            logger.info("testAuthenticateApplication_Disabled: Delete");
            deleteApplication();
        }
    }

    @Test
    public void testAuthenticateApplication_RegistrationClosed() {

        logger.info("testAuthenticateApplication_RegistrationClosed");

        try {
            createApplication();
            //
            //  Set enabled false
            //
            Application updateApplication = accessor.getApplication(applicationId);
            assertNotNull(updateApplication);
            boolean testRegistrationOpen = false;
            updateApplication.setRegistrationOpen(testRegistrationOpen);
            accessor.updateApplication(updateApplication);
            //
            //  Authenticate
            //
            AccountServiceApplication authentication = standardAuthentication();
            AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Authenticate the result
            //
            assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
            assertEquals(expEnabled, resultAuthentication.isEnabled());
            assertEquals(testRegistrationOpen, resultAuthentication.isRegistrationOpen());
        } finally {
            // Clean up
            logger.info("testAuthenticateApplication_RegistrationClosed: Delete");
            deleteApplication();
        }
    }

    @Test
    public void testAuthenticateApplication_DisabledAndRegistrationClosed() {

        logger.info("testAuthenticateApplication_DisabledAndRegistrationClosed");

        try {
            createApplication();
            //
            //  Set enabled false
            //
            Application updateApplication = accessor.getApplication(applicationId);
            assertNotNull(updateApplication);
            boolean testEnabled = false;
            boolean testRegistrationOpen = false;
            updateApplication.setEnabled(testEnabled);
            updateApplication.setRegistrationOpen(testRegistrationOpen);
            accessor.updateApplication(updateApplication);
            //
            //  Authenticate
            //
            AccountServiceApplication authentication = standardAuthentication();
            AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Authenticate the result
            //
            assertEquals(AUTHENTICATED, resultAuthentication.getAuthenticationOutcome());
            assertEquals(testEnabled, resultAuthentication.isEnabled());
            assertEquals(testRegistrationOpen, resultAuthentication.isRegistrationOpen());
        } finally {
            // Clean up
            logger.info("testAuthenticateApplication_DisabledAndRegistrationClosed: Delete");
            deleteApplication();
        }
    }

    //  ***********************************************************
    //  Unsuccessful authentication instances
    //  ***********************************************************
    @Test
    public void testAuthenticateApplication_NoApp() {

        logger.info("testAuthenticateApplication_NoApp");

        try {
            createApplication();
            //
            //  Authenticate
            //
            AccountServiceApplication authentication = standardAuthentication();
            authentication.setName(UUID.randomUUID().toString());
            AccountServiceApplication resultAuthentication = instance.authenticateApplication(authentication);
            assertNotNull(resultAuthentication);
            //
            //  Authenticate the result
            //
            boolean testEnabled = false;
            boolean testRegistrationOpen = false;
            assertEquals(NO_APPLICATION, resultAuthentication.getAuthenticationOutcome());
            assertEquals(testEnabled, resultAuthentication.isEnabled());
            assertEquals(testRegistrationOpen, resultAuthentication.isRegistrationOpen());
        } finally {
            // Clean up
            logger.info("testAuthenticateApplication_NoApp: Delete");
            deleteApplication();
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

    private void createApplication() {

        //
        //  Create a simple application
        //
        application = new SimpleApplication();
        application.setName(applicationName);
        application.setEnabled(expEnabled);
        application.setRegistrationOpen(expRegistrationOpen);
        //
        //  Persist it and test
        //
        resultApplication = accessor.createApplication(application);
        assertThat(resultApplication, is(notNullValue()));
        assertThat(resultApplication.getId(), is(notNullValue()));
        assertEquals(expEnabled, resultApplication.isEnabled());
        assertEquals(expRegistrationOpen, resultApplication.isRegistrationOpen());
        applicationId = resultApplication.getId();
    }

    private void deleteApplication() {
        //
        //  Remove Application
        //
        application = accessor.getApplication(applicationId);
        accessor.deleteApplication(application);
        resultApplication = accessor.getApplication(applicationId);
        assertThat(resultApplication, is(nullValue()));

    }

    private AccountServiceApplication standardAuthentication() {

        AccountServiceApplication authentication = new SimpleAccountServiceApplication();
        authentication.setName(applicationName);
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

}
