/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.*;
import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.SimpleAccessLevel;
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
public class MysqlAccountServiceAppTestAccessLevel {

    //
    private ApplicationContext appContext;
    private static final String contextFile = "applicationContext.xml";
    private final String MYSQL_ACCOUNT_ACCESSOR = "accountAccessor";
    // dao
    private static MysqlAccountAccessor instance;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceAppTestAccessLevel.class);
    //
    //  
    //
    private final Integer APPLICATION1 = 1;
    private final Character ACTIVE = 'Y';
    private final Character INACTIVE = 'N';

    public MysqlAccountServiceAppTestAccessLevel() {

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
    public void testCRUDAccessLevel() {

        logger.info("testCRUDAccessLevel: Sequence start");
        logger.info("testCRUDAccessLevel: Create");
        Integer id;
        AccountServiceRole role = ADMIN;

        //
        //  Create a simple access level
        //
        AccessLevel accessLevel = new SimpleAccessLevel();
        accessLevel.setAccountServiceRole(role);
        //
        //  Persist it and test
        //
        AccessLevel result = instance.createAccessLevel(accessLevel);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(notNullValue()));
        //
        //  Read it back and test
        //
        logger.info("testCRUDAccessLevel: Read");
        id = result.getId();
        result = instance.getAccessLevel(id);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(role, result.getAccountServiceRole());
        //
        //  Update it and test
        //
        logger.info("testCRUDAccessLevel: Update");
        role = GUEST;
        //
        accessLevel = new SimpleAccessLevel();
        accessLevel.setId(id);
        accessLevel.setAccountServiceRole(role);
        //
        result = instance.updateAccessLevel(accessLevel);
        assertThat(result, is(notNullValue()));
        assertEquals(id, result.getId());
        assertEquals(role, result.getAccountServiceRole());
        //
        //  Delete it and test
        //
        logger.info("testCRUDAccessLevel: Delete");
        accessLevel = instance.getAccessLevel(id);
        instance.deleteAccessLevel(accessLevel);
        result = instance.getAccessLevel(id);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testGetAccessLevelBySecondaryKey() {

        logger.info("testCRUDAccessLevel: Sequence start");
        AccountServiceRole expRole = OWNER;
        //
        //  Create a simple access level
        //
        AccessLevel accessLevel = new SimpleAccessLevel();
        accessLevel.setAccountServiceRole(expRole);
        //
        //
        //
        AccessLevel resultAccessLevel = instance.getAccessLevelBySecondaryKey(accessLevel);
        assertNotNull(resultAccessLevel);
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
