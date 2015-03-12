/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.SimpleApplication;
import java.util.Calendar;
import java.util.Date;
import static org.hamcrest.Matchers.*;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceAppTestApplication {

    //
    private ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    // dao
    private static MysqlAccountAccessor instance;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppTestApplication.class);
    //
    //  
    //
    private final Integer APPLICATION1 = 1;
    private final Character ACTIVE = 'Y';
    private final Character INACTIVE = 'N';
    private final Character ACCEPT_REGISTRATION = 'Y';
    private final Character PREVENT_REGISTRATION = 'N';

    public MysqlAccountServiceAppTestApplication() {

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

        instance = appContext.getBean(MYSQL_ACCOUNT_ACCESSOR, MysqlAccountAccessor.class);
        instance.init();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCRUDApplication() {

        logger.info("testCRUDApplication: Sequence start");
        logger.info("testCRUDApplication: Create");
        Integer id;
        String name = "Frap-ray blaster";

        //
        //  Create a simple access level
        //
        Application application = new SimpleApplication();
        application.setName(name);
        application.setEnabled(true);
 //       application.setAcceptRegistration(PREVENT_REGISTRATION);
        application.setRegistrationOpen(false);
        //
        //  Persist it and test
        //
        Application result = instance.createApplication(application);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(notNullValue()));
        //
        //  Read it back and test
        //
        logger.info("testCRUDApplication: Read");
        id = result.getId();
        result = instance.getApplication(id);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(true, result.isEnabled());
//        assertEquals(PREVENT_REGISTRATION, result.getAcceptRegistration());
        assertEquals(false, result.isRegistrationOpen());
        //
        //  Update it and test
        //
        logger.info("testCRUDApplication: Update");
        name = "Atomic napalm neutralizer";
        //
        application = new SimpleApplication();
        application.setId(id);
        application.setName(name);
        application.setEnabled(false);
//        application.setAcceptRegistration(ACCEPT_REGISTRATION);
        application.setRegistrationOpen(true);
        //
        result = instance.updateApplication(application);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(false, result.isEnabled());
//        assertEquals(ACCEPT_REGISTRATION, result.getAcceptRegistration());
        assertEquals(true, result.isRegistrationOpen());
        //
        //  Delete it and test
        //
        logger.info("testCRUDApplication: Delete");
        application = instance.getApplication(id);
        instance.deleteApplication(application);
        result = instance.getApplication(id);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testGetUserBySecondaryKey() {

        logger.info("testGetUserBySecondaryKey");
        Integer id;
        String name = "Frap-ray blaster";

        //
        //  Create a simple application
        //
        Application application = new SimpleApplication();
        application.setName(name);
//        application.setActive(ACTIVE);
        application.setEnabled(true);

        //
        //  Persist it and test
        //
        Application result = instance.createApplication(application);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(notNullValue()));
        //
        //  Read it back and test
        //
        logger.info("testCRUDApplication: Read");
        id = result.getId();
        result = instance.getApplication(id);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
//        assertEquals(ACTIVE, result.getActive());
        assertEquals(true, result.isEnabled());

        //
        //  Create a second simple application and use it to query
        //
        Application secondaryApp = new SimpleApplication();
        secondaryApp.setName(name);
//        secondaryApp.setActive(ACTIVE);
        application.setEnabled(true);
        result = instance.getApplicationBySecondaryKey(secondaryApp);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
//        assertEquals(ACTIVE, result.getActive());
        assertEquals(true, result.isEnabled());
        //
        //  Delete it and test
        //
        instance.deleteApplication(result);
        result = instance.getApplication(id);
        assertThat(result, is(nullValue()));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidCreate_NullPassword() {
        logger.info("testInvalidCreate_NullPassword");
        //
        //  Create an empty application
        //
        Application application = new SimpleApplication();
        instance.createApplication(application);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidCreate_DuplicateName() {
        logger.info("testInvalidCreate_DuplicateName");

        Integer id;
        String name = "Frap-ray blaster";
        //
        //  Create a simple application
        //
        Application application = new SimpleApplication();
        application.setName(name);
//        application.setActive(ACTIVE);
        application.setEnabled(true);
        //
        //  Persist it and test
        //
        Application result = instance.createApplication(application);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(notNullValue()));
        id = result.getId();
        //
        //  Create second application with the same name
        //
        Application secondaryApp = new SimpleApplication();
        application.setName(name);
//        application.setActive(ACTIVE);
        application.setEnabled(true);
        //
        //  Persist it and test
        //
        try {
            instance.createApplication(secondaryApp);
        } finally {
            //
            //  Delete the original
            //
            instance.deleteApplication(result);
            result = instance.getApplication(id);
            assertThat(result, is(nullValue()));
        }
    }

    //
    //
    //
    private Date getDateWithoutMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }
}
