/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author user0001
 */
public class AccountServiceUserDetailsTest {

    private AccountServiceUserDetails instance;
    private Collection<UserDetailsAuthority> userDetailsAuthorities;
    //
    private final String authoritySeparator = "::";
    private final AccountServiceRole accessLevelRole = GUEST;

    // logger    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceUserDetailsTest.class);

    public AccountServiceUserDetailsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new AccountServiceUserDetails(null, null);
        userDetailsAuthorities = new HashSet<>();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAuthorities method, of class AccountServiceUserDetails.
     */
    @Test
    public void testGetAuthorities_NullDetails() {

        logger.info("testGetAuthorities_NullDetails");
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 0;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
    }

    /**
     * Test of getPassword method, of class AccountServiceUserDetails.
     */
    @Test
    public void testGetPassword_NullDetails() {

        logger.info("testGetPassword_NullDetails");
        String expResult = null;
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUsername method, of class AccountServiceUserDetails.
     */
    @Test
    public void testGetUsername_NullDetails() {

        logger.info("testGetUsername_NullDetails");
        String expResult = null;
        String result = instance.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of isAccountNonExpired method, of class AccountServiceUserDetails.
     */
    @Test
    public void testIsAccountNonExpired_NullDetails() {

        logger.info("testIsAccountNonExpired_NullDetails");
        boolean expResult = true;
        boolean result = instance.isAccountNonExpired();
        assertEquals(expResult, result);
    }

    /**
     * Test of isAccountNonLocked method, of class AccountServiceUserDetails.
     */
    @Test
    public void testIsAccountNonLocked_NullDetails() {

        logger.info("testIsAccountNonLocked_NullDetails");
        boolean expResult = true;
        boolean result = instance.isAccountNonLocked();
        assertEquals(expResult, result);
    }

    /**
     * Test of isCredentialsNonExpired method, of class
     * AccountServiceUserDetails.
     */
    @Test
    public void testIsCredentialsNonExpired_NullDetails() {

        logger.info("testIsCredentialsNonExpired_NullDetails");
        boolean expResult = true;
        boolean result = instance.isCredentialsNonExpired();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEnabled method, of class AccountServiceUserDetails.
     */
    @Test
    public void testIsEnabled_NullDetails() {

        logger.info("testIsEnabled_NullDetails");
        boolean expResult = false;
        boolean result = instance.isEnabled();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetUsername() {

        logger.info("testGetUsername");
        String expUserName = UUID.randomUUID().toString();
        String expPassword = UUID.randomUUID().toString();
        User user = getUser(expUserName, expPassword, true);
        //
        instance = new AccountServiceUserDetails(user, null);
        String result = instance.getUsername();
        assertEquals(expUserName, result);
    }

    @Test
    public void testGetPassword() {

        logger.info("testGetPassword");
        String expUserName = UUID.randomUUID().toString();
        String expPassword = UUID.randomUUID().toString();
        User user = getUser(expUserName, expPassword, true);
        //
        instance = new AccountServiceUserDetails(user, null);
        String result = instance.getPassword();
        assertEquals(expPassword, result);
    }

    @Test
    public void testIsAccountNonExpired() {

        logger.info("testIsAccountNonExpired");
        boolean expIsAccountNonExpired = true;
        //
        String expUserName = UUID.randomUUID().toString();
        String expPassword = UUID.randomUUID().toString();
        User user = getUser(expUserName, expPassword, true);
        //
        instance = new AccountServiceUserDetails(user, null);
        boolean result = instance.isAccountNonExpired();
        assertEquals(expIsAccountNonExpired, result);
        //
        user = getUser(expUserName, expPassword, false);
        instance = new AccountServiceUserDetails(user, null);
        result = instance.isAccountNonExpired();
        assertEquals(expIsAccountNonExpired, result);
    }

    @Test
    public void testIsAccountNonLocked() {

        logger.info("testIsAccountNonLocked");
        boolean expIsAccountNonLocked = true;
        //
        String expUserName = UUID.randomUUID().toString();
        String expPassword = UUID.randomUUID().toString();
        User user = getUser(expUserName, expPassword, true);
        //
        instance = new AccountServiceUserDetails(user, null);
        boolean result = instance.isAccountNonLocked();
        assertEquals(expIsAccountNonLocked, result);
        //
        user = getUser(expUserName, expPassword, false);
        instance = new AccountServiceUserDetails(user, null);
        result = instance.isAccountNonLocked();
        assertEquals(expIsAccountNonLocked, result);
    }

    @Test
    public void testIsCredentialsNonExpired() {

        logger.info("testIsCredentialsNonExpired");
        boolean expIsCredentialsNonExpired = true;
        //
        String expUserName = UUID.randomUUID().toString();
        String expPassword = UUID.randomUUID().toString();
        User user = getUser(expUserName, expPassword, true);
        //
        instance = new AccountServiceUserDetails(user, null);
        boolean result = instance.isCredentialsNonExpired();
        assertEquals(expIsCredentialsNonExpired, result);
        //
        user = getUser(expUserName, expPassword, false);
        instance = new AccountServiceUserDetails(user, null);
        result = instance.isCredentialsNonExpired();
        assertEquals(expIsCredentialsNonExpired, result);
    }

    @Test
    public void testIsEnabled() {

        logger.info("testIsEnabled");
        boolean expIsEnabled = true;
        //
        String expUserName = UUID.randomUUID().toString();
        String expPassword = UUID.randomUUID().toString();
        User user = getUser(expUserName, expPassword, true);
        //
        instance = new AccountServiceUserDetails(user, null);
        boolean result = instance.isEnabled();
        assertEquals(expIsEnabled, result);
        //
        expIsEnabled = false;
        user = getUser(expUserName, expPassword, false);
        instance = new AccountServiceUserDetails(user, null);
        result = instance.isEnabled();
        assertEquals(expIsEnabled, result);
    }

    @Test
    public void testGetAuthorities_NoAuthorities() {

        logger.info("testGetAuthorities_NoAuthorities");
        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 0;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
    }

    @Test
    public void testGetAuthorities_OneInvalidAuthority() {

        logger.info("testGetAuthorities_OneInvalidAuthority");
        //
        //  Inactive application
        //
        String expApplicationName = UUID.randomUUID().toString();
        Integer expAccessLevelId = new Double(Math.random()).intValue();
        Application application = getApplication(expApplicationName, false);
        AccessLevel accessLevel = getAccessLevel(expAccessLevelId);
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 0;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
        //
        //  Inactive account user
        //
        expApplicationName = UUID.randomUUID().toString();
        expAccessLevelId = new Double(Math.random()).intValue();
        application = getApplication(expApplicationName, false);
        accessLevel = getAccessLevel(expAccessLevelId);
        userDetailsAuthorities.clear();
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        expSize = 0;
        resultSize = result.size();
        assertEquals(expSize, resultSize);
    }

//    @Test
    public void testGetAuthorities_MultipleInvalidAuthories() {

        logger.info("testGetAuthorities_MultipleInvalidAuthories");

        Application application = getApplication(UUID.randomUUID().toString(), false);
//        AccountUser accountUser = getAccountUser(new Double(Math.random()).intValue(), true);
        AccessLevel accessLevel = getAccessLevel(new Double(Math.random()).intValue());
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        application = getApplication(UUID.randomUUID().toString(), true);
        accessLevel = getAccessLevel(new Double(Math.random()).intValue());
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        application = getApplication(UUID.randomUUID().toString(), false);
        accessLevel = getAccessLevel(new Double(Math.random()).intValue());
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 0;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
    }

    @Test
    public void testGetAuthorities_OneValidAuthority() {

        logger.info("testGetAuthorities_OneValidAuthority");
        //
        //  
        //
        String expApplicationName = UUID.randomUUID().toString();
        Integer expAccessLevelId = new Double(Math.random()).intValue();
        String expAuthority = expApplicationName + authoritySeparator + accessLevelRole.name();
        Application application = getApplication(expApplicationName, true);
        //       AccountUser accountUser = getAccountUser(expAccessLevelId, true);
        AccessLevel accessLevel = getAccessLevel(expAccessLevelId);
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 1;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
        //
        //
        //
        GrantedAuthority grantedAuthority0 = (GrantedAuthority) result.toArray()[0];
        String resultAuthority = grantedAuthority0.getAuthority();
        assertEquals(expAuthority, resultAuthority);
    }

    @Test
    public void testGetAuthorities_OneValidOneInvalidAuthority() {

        logger.info("testGetAuthorities_OneValidOneInvalidAuthority");
        //
        //  Valid authority
        //
        String expApplicationName = UUID.randomUUID().toString();
        Integer expAccessLevelId = new Double(Math.random()).intValue();
        String expAuthority = expApplicationName + authoritySeparator + accessLevelRole.name();
        Application application = getApplication(expApplicationName, true);
        //       AccountUser accountUser = getAccountUser(expAccessLevelId, true);
        AccessLevel accessLevel = getAccessLevel(expAccessLevelId);
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));
        //
        //  Invalid authority
        //
        application = getApplication(UUID.randomUUID().toString(), false);
        //       accountUser = getAccountUser(new Double(Math.random()).intValue(), false);
        accessLevel = getAccessLevel(expAccessLevelId);
        userDetailsAuthorities.add(new UserDetailsAuthority(application, accessLevel));

        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 1;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
        //
        //
        //
        GrantedAuthority grantedAuthority0 = (GrantedAuthority) result.toArray()[0];
        String resultAuthority = grantedAuthority0.getAuthority();
        assertEquals(expAuthority, resultAuthority);
    }

    @Test
    public void testGetAuthorities_TwoValidAuthorities() {

        logger.info("testGetAuthorities_TwoValidAuthorities");
        //
        //  Valid authority 1
        //
        String expApplicationName1 = UUID.randomUUID().toString();
        Integer expAccessLevelId1 = new Double(Math.random()).intValue();
        String expAuthority1 = expApplicationName1 + authoritySeparator + accessLevelRole.name();
        Application application1 = getApplication(expApplicationName1, true);
//        AccountUser accountUser1 = getAccountUser(expAccessLevelId1, true);
        AccessLevel accessLevel1 = getAccessLevel(expAccessLevelId1);
        userDetailsAuthorities.add(new UserDetailsAuthority(application1, accessLevel1));
        //
        //  Valid authority 2
        //
        String expApplicationName2 = UUID.randomUUID().toString();
        Integer expAccessLevelId2 = new Double(Math.random()).intValue();
        String expAuthority2 = expApplicationName2 + authoritySeparator +  ADMIN.name();
        Application application2 = getApplication(expApplicationName2, true);
        //       AccountUser accountUser2 = getAccountUser(expAccessLevelId2, true);
        AccessLevel accessLevel2 = getAccessLevel(expAccessLevelId2);
        when(accessLevel2.getAccountServiceRole()).thenReturn(ADMIN);
        userDetailsAuthorities.add(new UserDetailsAuthority(application2, accessLevel2));

        instance = new AccountServiceUserDetails(null, userDetailsAuthorities);
        Collection<? extends GrantedAuthority> result = instance.getAuthorities();
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
        int expSize = 2;
        int resultSize = result.size();
        assertEquals(expSize, resultSize);
        //
        //
        //
        String[] authorities = new String[expSize];
        int index = 0;
        for (GrantedAuthority authority : result) {
            authorities[index++] = authority.getAuthority();
        }
        List<String> authoritiesList = Arrays.asList(authorities);
        assertTrue(authoritiesList.contains(expAuthority1));
        assertTrue(authoritiesList.contains(expAuthority2));
    }

    //
    //
    //
    private User getUser(final String userName, final String password, final boolean active) {

        User user = mock(User.class);
        when(user.getUserEmail()).thenReturn(userName);
        when(user.getUserPassword()).thenReturn(password.getBytes());
        //       when(user.getActive()).thenReturn(active);
        when(user.isEnabled()).thenReturn(active);
        return user;
    }

    private Application getApplication(final String applicationName, final boolean active) {

        Application application = mock(Application.class);
        when(application.getName()).thenReturn(applicationName);
//        when(application.getActive()).thenReturn(active);
        when(application.isEnabled()).thenReturn(active);
        return application;
    }

    private AccountUser getAccountUser(final Integer accessLevelId, final boolean active) {

        AccountUser accountuser = mock(AccountUser.class);
        when(accountuser.getAccessLevelId()).thenReturn(accessLevelId);
//        when(accountuser.getActive()).thenReturn(active);
        when(accountuser.isEnabled()).thenReturn(active);
        return accountuser;
    }

    private AccessLevel getAccessLevel(final Integer accessLevelId) {

        AccessLevel accessLevel = mock(AccessLevel.class);
        when(accessLevel.getId()).thenReturn(accessLevelId);
        when(accessLevel.getAccountServiceRole()).thenReturn(accessLevelRole);
        return accessLevel;
    }
}
