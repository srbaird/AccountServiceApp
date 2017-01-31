package com.bac.accountserviceapp.hibernate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		HibernateTestAccessLevel.class, 
		HibernateTestAccount.class, 
		HibernateTestAccountUser.class,
		HibernateTestApplication.class, 
		HibernateTestUser.class, 
		HibernateTestUserWithAccounts.class })
public class AllTests {

}
