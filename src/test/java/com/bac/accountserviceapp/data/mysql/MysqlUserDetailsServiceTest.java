/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.AccountAccess;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author user0001
 */
public class MysqlUserDetailsServiceTest {

    private MysqlUserDetailsService instance;
    //
    private final String authoritySeparator = "::";
    // logger    
    private static final Logger logger = LoggerFactory.getLogger(MysqlUserDetailsServiceTest.class);

    public MysqlUserDetailsServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

        instance = new MysqlUserDetailsService();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetAccess() {

        logger.info("testGetAccess");
        AccountAccess resultAccess = instance.getAccess();
        assertNull(resultAccess);
    }

    @Test
    public void testSetAccess() {

        logger.info("testSetAccess");
        AccountAccess access = getAccountAccess();
        instance.setAccess(access);
        AccountAccess resultAccess = instance.getAccess();
        assertEquals(access, resultAccess);
    }

    /**
     * Test of loadUserByUsername method, of class MysqlUserDetailsService.
     */
    @Test(expected = NullPointerException.class)
    public void testLoadUserByUsername_NullAccountAccess() {

        logger.info("testLoadUserByUsername_NullAccountAccess");
        instance.setAccess(null);
        String string = "";
        instance.loadUserByUsername(string);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_NullUser() {

        logger.info("testLoadUserByUsername_NullUser");
        AccountAccess access = getAccountAccess();
        when(access.getUserBySecondaryKey((User) anyObject())).thenReturn(null);
        instance.setAccess(access);
        String userName = UUID.randomUUID().toString();
        UserDetails result = instance.loadUserByUsername(userName);
    }

    @Test
    public void testLoadUserByUsername_NoAccounts() {

        logger.info("testLoadUserByUsername_NoAccounts");
        String expUserName = UUID.randomUUID().toString();
        String expUserPassword = UUID.randomUUID().toString();
//        Set<? extends Account> userAccounts = new HashSet<>();
        //
        //  Set access to return the User object
        //
        AccountAccess access = getAccountAccess();
        User user = getUser();
        when(user.getUserEmail()).thenReturn(expUserName);
        when(user.getUserPassword()).thenReturn(expUserPassword.getBytes());
        when(user.getAccounts()).thenReturn(null);
        when(access.getUserBySecondaryKey((User) anyObject())).thenReturn(user);
        instance.setAccess(access);
        //
        UserDetails result = instance.loadUserByUsername(expUserName);
        assertNotNull(result);
        String resultUserName = result.getUsername();
        String resultPassword = result.getPassword();
        Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
        int expAuthoritiesSize = 0;
        int resultAuthoritiesSize = resultAuthorities.size();
        assertEquals(expUserName, resultUserName);
        assertEquals(expUserPassword, resultPassword);
        assertEquals(expAuthoritiesSize, resultAuthoritiesSize);
    }

//    @Test
    public void testLoadUserByUsername_OneAccount() {

        logger.info("testLoadUserByUsername_OneAccount");
        String expUserName = UUID.randomUUID().toString();
        String expUserPassword = UUID.randomUUID().toString();
        String expApplicationName = UUID.randomUUID().toString();
        int expAccessLevelId = 66;
//      
        //
        //  Set access to return the User object
        //
        AccountAccess access = getAccountAccess();
        //
        Set<Account> userAccounts = new HashSet<>();
        Account account = this.getAccount();
        userAccounts.add(account);
        // User

        User user = getUser(userAccounts);
        when(user.getUserEmail()).thenReturn(expUserName);
        when(user.getUserPassword()).thenReturn(expUserPassword.getBytes());
        when(access.getUserBySecondaryKey((User) anyObject())).thenReturn(user);
        //  Application
        Application application = getApplication();
        when(application.getName()).thenReturn(expApplicationName);
        when(application.isEnabled()).thenReturn(true);
        when(access.getApplication((Integer) anyObject())).thenReturn(application);
        // AccountUser
        AccountUser accountUser = getAccountUser();
        when(accountUser.getAccessLevelId()).thenReturn(expAccessLevelId);
        when(accountUser.isEnabled()).thenReturn(true);
        when(access.getAccountUser((AccountUser) anyObject())).thenReturn(accountUser);
        instance.setAccess(access);
        //
        UserDetails result = instance.loadUserByUsername(expUserName);
        assertNotNull(result);
        String resultUserName = result.getUsername();
        String resultPassword = result.getPassword();

        assertEquals(expUserName, resultUserName);
        assertEquals(expUserPassword, resultPassword);
        //
        Collection<? extends GrantedAuthority> resultAuthorities = result.getAuthorities();
        int expAuthoritiesSize = 1;
        int resultAuthoritiesSize = resultAuthorities.size();
        assertEquals(expAuthoritiesSize, resultAuthoritiesSize);
        List<?> authoritiesList = Arrays.asList(resultAuthorities.toArray());
        GrantedAuthority grantedAuthority = (GrantedAuthority) authoritiesList.get(0);
        String expAuthority = expApplicationName + authoritySeparator + expAccessLevelId;
        String resultAuthority = grantedAuthority.getAuthority();
        assertEquals(expAuthority, resultAuthority);
    }

    //
    //
    //
    private AccountAccess getAccountAccess() {

        AccountAccess accountAccess = mock(AccountAccess.class);
        return accountAccess;
    }

    private User getUser() {

        User user = mock(User.class);
        return user;
    }

    private User getUser(final Set<? extends Account> userAccounts) {

        User user = mock(User.class);
//        when(user.getAccounts()).thenAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) {
//
//                return userAccounts;
//            }
//        });
        Mockito.<Set<? extends Account>>when(user.getAccounts()).thenReturn(userAccounts);
        return user;
    }

    private Account getAccount() {

        Account account = mock(Account.class);
        return account;
    }

    private Application getApplication() {

        Application application = mock(Application.class);
        return application;
    }

    private AccountUser getAccountUser() {

        AccountUser accountUser = mock(AccountUser.class);
        return accountUser;
    }

}
