/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceAppTestUser {

    private ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    // dao
    private static MysqlAccountAccessor instance;
    private BCryptPasswordEncoder encoder;
    private final byte[] salt = "No longer required".getBytes();
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppTestUser.class);
    //
    //  
    //

    public MysqlAccountServiceAppTestUser() {

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
        encoder = new BCryptPasswordEncoder();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCRUDUser() throws NoSuchAlgorithmException, InvalidKeySpecException {

        logger.info("testCRUDUser: Sequence start");
        logger.info("testCRUDUser: Create");
        Integer id = null;
        String userName = "Desperate Dan";
        String userEmail = "ddan@beano.com";
        String userPassword = "1403772743";
        Date createDate = getDateWithoutMillis();
        //
        User user = null;
        User result = null;

        try {
            //
            //  Create a simple user
            //
            user = new SimpleUser();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setPasswordSalt(salt);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
            user.setEnabled(true);
            user.setCreateDate(createDate);
            //
            //  Persist it and test
            //
            result = instance.createUser(user);
            assertThat(result, is(notNullValue()));
            assertThat(result.getId(), is(notNullValue()));
            //
            //  Read it back and test
            //
            logger.info("testCRUDUser: Read");
            id = result.getId();
            result = instance.getUser(id);
            assertThat(result, is(notNullValue()));
            assertEquals(id, result.getId());
            assertEquals(userName, result.getUserName());
            assertEquals(userEmail, result.getUserEmail());
            assertEquals(true, result.isEnabled());
            assertEquals(createDate, result.getCreateDate());
            // Authenticate
            assertTrue(encoder.matches(userPassword, result.getUserPassword().toString()));
            //
            //  Update it and test
            //
            logger.info("testCRUDUser: Update");
            userName = "Minnie the Minx";
            userEmail = "mminx@beano.com";
            userPassword = "1403773566";
            createDate = getDateWithoutMillis();
            //
            user = new SimpleUser();
            user.setId(id);
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.setPasswordSalt(salt);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
            user.setEnabled(false);
            user.setCreateDate(createDate);
            //
            result = instance.updateUser(user);
            assertThat(result, is(notNullValue()));
            assertEquals(id, result.getId());
            assertEquals(userName, result.getUserName());
            assertEquals(userEmail, result.getUserEmail());
            assertEquals(false, result.isEnabled());
            assertEquals(createDate, result.getCreateDate());
            // Authenticate//
            assertTrue(encoder.matches(userPassword, result.getUserPassword().toString()));

        } catch (Exception e) {

            logger.error("testCRUDUser", e);

        } finally {
            //
            //  Delete it and test
            //
            logger.info("testCRUDUser: Delete");
            user = instance.getUser(id);
            instance.deleteUser(user);
            result = instance.getUser(id);
            assertThat(result, is(nullValue()));
        }
    }

    @Test
    public void testGetUserBySecondaryKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        logger.info("testGetUserBySecondaryKey");
        Integer id = null;
        String userEmail = "ddan@beano.com";
        String userPassword = "1403772743";
        //
        User user = null;
        User result = null;

        try {
            //
            //  Create a simple user
            //
            user = new SimpleUser();
            user.setUserEmail(userEmail);
            user.setPasswordSalt(salt);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
            //
            //  Persist it and test
            //
            user = instance.createUser(user);
            assertThat(user, is(notNullValue()));
            assertThat(user.getId(), is(notNullValue()));
            //
            id = user.getId();
            //
            //  Create a second simple user and use it to query
            //
            User secondaryUser = new SimpleUser();
            secondaryUser.setUserEmail(userEmail);
            result = instance.getUserBySecondaryKey(secondaryUser);
            assertThat(result, is(notNullValue()));
            assertEquals(id, result.getId());
            assertEquals(userEmail, result.getUserEmail());

        } finally {
            //
            //  Delete it and test
            //
            instance.deleteUser(user);
            result = instance.getUser(id);
            assertThat(result, is(nullValue()));
        }
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidCreate_NullEmail() throws NoSuchAlgorithmException, InvalidKeySpecException {
        logger.info("testInvalidCreate_NullEmail");

        String userName = "Roger the Dodger";
        String userPassword = "1403794400";
        Date createDate = getDateWithoutMillis();
        //
        //  Create a simple user
        //
        User user = new SimpleUser();
        user.setUserName(userName);
        user.setPasswordSalt(salt);
        user.setUserPassword(encoder.encode(userPassword).getBytes());
        user.setEnabled(true);
        user.setCreateDate(createDate);
        //
        //  Persist it
        //
        instance.createUser(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidCreate_DuplicateEmail() throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        logger.info("testInvalidCreate_NullPassword");

        Integer id;
        String userEmail = "rdodger@beano.com";
        String userPassword = "1403795210";
        Date createDate = getDateWithoutMillis();
        //
        //  Create a simple user
        //
        User user = new SimpleUser();
        user.setUserEmail(userEmail);
        user.setPasswordSalt(salt);
        user.setUserPassword(encoder.encode(userPassword).getBytes());
        user.setEnabled(true);
        user.setCreateDate(createDate);
        //
        //  Persist it and test
        //
        logger.info("testInvalidCreate_NullPassword: First user");
        User result = instance.createUser(user);
        id = result.getId();
        result = instance.getUser(id);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(userEmail, result.getUserEmail());
        assertEquals(true, result.isEnabled());
        assertEquals(createDate, result.getCreateDate());
        // Authenticate
        assertTrue(encoder.matches(userPassword, new String(result.getUserPassword())));
        //
        //  Create second user with the same email
        //
        userPassword = "1403795396";
        createDate = getDateWithoutMillis();
        //
        user = new SimpleUser();
        user.setUserEmail(userEmail);

        user.setPasswordSalt(salt);
        user.setUserPassword(encoder.encode(userPassword).getBytes());
        user.setEnabled(true);
        user.setCreateDate(createDate);
        //
        //  Persist it and test
        //
        try {
            logger.info("testInvalidCreate_NullPassword: Second user");
            instance.createUser(user);
        } finally {
            //
            //  Delete the original
            //
            instance.deleteUser(result);
            result = instance.getUser(id);
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
