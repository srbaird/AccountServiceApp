/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

//import com.bac.accountserviceapp.PasswordAuthentication;
import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
public class MysqlAccountServiceAppTestUserAuthentication {

    private ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    // dao
    private static MysqlAccountAccessor instance;
    private BCryptPasswordEncoder encoder;
    private final byte[] salt = "No longer required".getBytes();
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppTestUserAuthentication.class);
    //
    //  
    //
    private final Integer APPLICATION1 = 1;
    private final Character ACTIVE = 'Y';
    private final Character INACTIVE = 'N';

    public MysqlAccountServiceAppTestUserAuthentication() {

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
    public void testAuthentication() throws NoSuchAlgorithmException {

        logger.info("testAuthentication");
        User user;
        User result;
        Integer id = null;
        String userName = "Desperate Dan";
        String userEmail = "ddan@beano.com";
        String userPassword = "1403772743";
        Date createDate = getDateWithoutMillis();
//        byte[] salt = PasswordAuthentication.generateSalt();

        try {
            //
            //  Create a simple user
            //
            user = new SimpleUser();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            //           user.setActive(ACTIVE);
            user.setEnabled(true);
            user.setCreateDate(createDate);
            // Authentication
//            user.setPasswordSalt(salt);
//            user.setUserPassword(PasswordAuthentication.getEncryptedPassword(userPassword, salt));
            user.setPasswordSalt(salt);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
            //
            //  Persist it and test
            //
            result = instance.createUser(user);
            assertThat(result, is(notNullValue()));
            assertThat(result.getId(), is(notNullValue()));
            Assert.assertArrayEquals(salt, user.getPasswordSalt());
            id = result.getId();
            //
            //  Read it back and test
            //
            result = instance.getUser(id);
            assertThat(result, is(notNullValue()));
            assertEquals(id, result.getId());
            Assert.assertArrayEquals(salt, result.getPasswordSalt());
            byte[] resultSalt = result.getPasswordSalt();
            byte[] resultPassword = result.getUserPassword();
            //           assertTrue(PasswordAuthentication.authenticate(userPassword, resultPassword, resultSalt));
            assertTrue(encoder.matches(userPassword, result.getUserPassword().toString()));
            //
            //  Update it and test
            //
            userPassword = "1403773566";
            user = new SimpleUser();
            user.setId(id);
            user.setUserName(userName);
            user.setUserEmail(userEmail);
//            user.setUserPassword(PasswordAuthentication.getEncryptedPassword(userPassword, resultSalt));
//            user.setPasswordSalt(resultSalt);
            user.setPasswordSalt(salt);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
//            user.setActive(ACTIVE);
            user.setEnabled(true);
            user.setCreateDate(createDate);
            //
            instance.updateUser(user);
            result = instance.getUser(id);
            assertThat(result, is(notNullValue()));
            assertEquals(id, result.getId());
            Assert.assertArrayEquals(resultSalt, result.getPasswordSalt());
//            assertTrue(PasswordAuthentication.authenticate(userPassword, result.getUserPassword(), result.getPasswordSalt()));
            assertTrue(encoder.matches(userPassword, result.getUserPassword().toString()));

        } catch (Exception ex) {
            logger.error("Test authentication", ex);
        } finally {
            // Clean up
            logger.info("testCRUDUser: Delete");
            user = instance.getUser(id);
            instance.deleteUser(user);
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
