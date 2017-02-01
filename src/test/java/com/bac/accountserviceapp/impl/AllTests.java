package com.bac.accountserviceapp.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		AccountServiceAppTestAuthenticate.class, 
		AccountServiceAppTestCreateLogin.class,
		AccountServiceAppTestNativeAuthentication.class, 
		AccountServiceAppTestSpringAuthentication.class,
		AccountServiceStrategyTest.class, 
		AccountServiceUserDetailsTest.class, 
		UserDetailsServiceTest.class })
public class AllTests {

}
