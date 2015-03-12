/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

//import com.bac.accountserviceapp.PasswordAuthentication;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.SimpleAccount;
import com.bac.accountserviceapp.data.SimpleAccountUser;
import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import static org.hamcrest.Matchers.*;
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
public class MysqlAccountServiceAppTestAccountUser {

    //
    private ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    // dao
    private static MysqlAccountAccessor instance;
    private BCryptPasswordEncoder encoder;
    private final byte[] salt = "No longer required".getBytes();
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppTestAccountUser.class);
    //
    //  
    //
    private final Integer APPLICATION1 = 1;
    private final Character ACTIVE = 'Y';
    private final Character INACTIVE = 'N';
    private final Integer OWNER = 2;
    private final Integer GUEST = 3;

    public MysqlAccountServiceAppTestAccountUser() {

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
    public void testCRUDAccountUser() throws NoSuchAlgorithmException, InvalidKeySpecException {

        logger.info("testCRUDAccountUser: Sequence start");
        logger.info("testCRUDAccountUser: Create");

        Integer userId = null;
        Integer accountId = null;
        User userResult = null;
        Account accountResult = null;
        AccountUser accountUserResult;
        AccountUser accountUser = null;

        try {
            //
            //  Create a user
            //
            String userName = "Desperate Dan";
            String userEmail = "ddan@beano.com";
            String userPassword = "1403772743";
            Date createDate = getDateWithoutMillis();
            String msg = "What time is it?";
            //
            User user = new SimpleUser();
            user.setUserName(userName);
            user.setUserEmail(userEmail);
//            user.setPasswordSalt(PasswordAuthentication.generateSalt());
//            user.setUserPassword(PasswordAuthentication.getEncryptedPassword(userPassword, user.getPasswordSalt()));
            user.setPasswordSalt(salt);
            user.setUserPassword(encoder.encode(userPassword).getBytes());
//            user.setActive(ACTIVE);
            user.setEnabled(true);
            user.setCreateDate(createDate);
            //
            //  Persist it and test
            //
            userResult = instance.createUser(user);
            assertThat(userResult, is(notNullValue()));
            assertThat(userResult.getId(), is(notNullValue()));
            userId = userResult.getId();
            // New, so should not have any accounts
            assertThat(userResult.getAccounts(), is(nullValue()));
            //
            //  Create an Account
            //
            String resourceName = "1403696013.db";
            //
            Account account = new SimpleAccount();
            account.setResourceName(resourceName);
//            account.setActive(ACTIVE);
            user.setEnabled(true);
            account.setCreateDate(createDate);
            account.setApplicationId(APPLICATION1);
            //
            accountResult = instance.createAccount(account);
            //
            assertThat(accountResult, is(notNullValue()));
            assertThat(accountResult.getId(), is(notNullValue()));
            accountId = accountResult.getId();
            //
            //  Create the Account User
            //
            accountUser = new SimpleAccountUser();
            accountUser.setAccountId(accountId);
            accountUser.setUserId(userId);
//            accountUser.setActive(ACTIVE);
            accountUser.setEnabled(true);
            accountUser.setCreateDate(createDate);
            accountUser.setLastAccessDate(createDate);
            accountUser.setAccessLevelId(OWNER);
            accountUser.setAccountMessage(msg);
            //
            accountUserResult = instance.createAccountUser(accountUser);
            assertThat(accountUserResult, is(notNullValue()));
            //
            //
            //
            logger.info("testCRUDAccountUser: Read");
            accountUserResult = instance.getAccountUser(accountUser);
            assertEquals(userId, accountUserResult.getUserId());
            assertEquals(accountId, accountUserResult.getAccountId());
//            assertEquals(ACTIVE, accountUserResult.getActive());
            assertEquals(true, accountUserResult.isEnabled());
            assertEquals(createDate, accountUserResult.getCreateDate());
            assertEquals(createDate, accountUserResult.getLastAccessDate());
            assertEquals(OWNER, accountUserResult.getAccessLevelId());
            assertEquals(msg, accountUserResult.getAccountMessage());
            //
            //  Re-read the user, it should now have an account
            //
            userResult = instance.getUser(user.getId());
            assertThat(userResult, is(notNullValue()));
            Set<? extends Account> accounts = userResult.getAccounts();
            assertThat(accounts, is(notNullValue()));
            assertFalse(accounts.isEmpty());
            assertEquals(1, accounts.size());
            Account linkedAccount = accounts.toArray(new Account[0])[0];
            assertThat(linkedAccount, is(notNullValue()));
            assertEquals(linkedAccount.getId(), accountId);
            //
            //  Perform updates
            //
            msg = "It's cow pie time!";
            createDate = getDateWithoutMillis();
            accountUser.setLastAccessDate(createDate);
//            accountUser.setActive(INACTIVE);
            accountUser.setEnabled(false);
            accountUser.setAccessLevelId(GUEST);
            accountUser.setAccountMessage(msg);
            //
            logger.info("testCRUDAccountUser: Update");
            accountUserResult = instance.updateAccountUser(accountUser);
            assertEquals(userId, accountUserResult.getUserId());
            assertEquals(accountId, accountUserResult.getAccountId());
//            assertEquals(INACTIVE, accountUserResult.getActive());
            assertEquals(false, accountUserResult.isEnabled());
            assertEquals(createDate, accountUserResult.getLastAccessDate());
            assertEquals(GUEST, accountUserResult.getAccessLevelId());
            assertEquals(msg, accountUserResult.getAccountMessage());

        } finally {
            //
            //  Delete User and test
            //
            logger.info("testCRUDAccountUser: Delete");
            instance.deleteUser(userResult);
            userResult = instance.getUser(userId);
            assertThat(userResult, is(nullValue()));
            //
            //  Account User should be gone
            //
            accountUserResult = instance.getAccountUser(accountUser);
            assertThat(accountUserResult, is(nullValue()));
            //
            //  Delete Account and test
            //
            instance.deleteAccount(accountResult);
            accountResult = instance.getAccount(accountId);
            assertThat(accountResult, is(nullValue()));
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
