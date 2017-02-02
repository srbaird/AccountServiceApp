package com.bac.accountserviceapp.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		AccountServiceAppTestAuthenticate.class, 
		AccountServiceAppTestCreateLogin.class,
		AccountServiceAppTestNativeAuthentication.class, 
		AccountServiceAppTestSpringAuthentication.class,
		AccountServiceStrategyTestAll.class, 
		AccountServiceUserDetailsTestAll.class, 
		UserDetailsServiceTestAll.class })
public class AllTests {

}
