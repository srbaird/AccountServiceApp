/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.SimpleAccount;
import java.util.Calendar;
import java.util.Date;
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

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceAppTestAccount {

    //
    private ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    // dao
    private static MysqlAccountAccessor instance;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppTestAccount.class);
    //
    //  
    //
    private final Integer APPLICATION1 = 1;
    private final Character ACTIVE = 'Y';
    private final Character INACTIVE = 'N';

    public MysqlAccountServiceAppTestAccount() {

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

    /**
     * Test of getAccount method, of class MysqlApplicationAccount.
     */
    @Test
    public void testCRUDAccount() {

        logger.info("testCRUDAccount: Sequence start");
        logger.info("testCRUDAccount: Create");
        Integer id;
        String resourceName = "1403696013.db";
        Date createDate = getDateWithoutMillis();
        //
        Account account = new SimpleAccount();
        account.setResourceName(resourceName);
        account.setEnabled(true);
        account.setCreateDate(createDate);
        account.setApplicationId(APPLICATION1);
        //
        Account result = instance.createAccount(account);
        //
        assertThat(result, is(notNullValue()));
        assertEquals(resourceName, result.getResourceName());
        assertEquals(true, result.isEnabled());
        assertEquals(createDate, result.getCreateDate());
        assertEquals(APPLICATION1, result.getApplicationId());
        //
        logger.info("testCRUDAccount: Read");
        id = result.getId();
        result = instance.getAccount(id);
        //
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(resourceName, result.getResourceName());
        assertEquals(true, result.isEnabled());
        assertEquals(createDate, result.getCreateDate());
        assertEquals(APPLICATION1, result.getApplicationId());
        //
        logger.info("testCRUDAccount: Update");
        resourceName = "1403704855.db";
        createDate = getDateWithoutMillis();
        //
        account = instance.getAccount(id);
        account.setResourceName(resourceName);
         account.setEnabled(false);
        account.setCreateDate(createDate);
        //
        result = instance.updateAccount(account);
        //
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(resourceName, result.getResourceName());
        assertEquals(false, result.isEnabled());
        assertEquals(createDate, result.getCreateDate());
        assertEquals(APPLICATION1, result.getApplicationId());
        //
        logger.info("testCRUDAccount: Delete");
        account = instance.getAccount(id);
        instance.deleteAccount(account);
        result = instance.getAccount(id);
        assertThat(result, is(nullValue()));
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
