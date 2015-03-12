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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author user0001
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    MysqlAccountServiceAppCreateLoginTest.class, 
    MysqlAccountServiceAppNativeAuthenticationTest.class, 
    MysqlAccountServiceAppSpringAuthenticationTest.class, 
    MysqlAccountServiceAppAuthenticateAppTest.class,
    MysqlAccountAccessorTest.class, 
    MysqlAccountServiceAppTestAccessLevel.class, 
    MysqlAccountServiceAppTestAccountUser.class, 
    MysqlUserDetailsServiceTest.class, 
    MysqlAccountServiceAppTestUser.class, 
    MysqlAccountServiceAppTestApplication.class, 
    MysqlAccountServiceAppTestUserAuthentication.class, 
    MysqlAccountServiceAppTestAccount.class})

public class AllMysqlAccountServiceAppTests {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
