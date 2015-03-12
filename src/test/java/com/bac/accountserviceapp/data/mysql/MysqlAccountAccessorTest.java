/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class MysqlAccountAccessorTest {

    MysqlAccountAccessor instance;
    // logger    
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountAccessorTest.class);

    public MysqlAccountAccessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        
        instance = new MysqlAccountAccessor();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class MysqlAccountAccessor.
     */
    @Test
    public void testInit() {
        
        logger.info("testInit");
        instance.init();
    }
}
