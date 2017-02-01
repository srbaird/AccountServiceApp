/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountAccess;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

/**
 *
 * @author user0001
 */
public class AccountServiceUserDetailsService implements UserDetailsService {

    private AccountAccess access;
    //
    private final String nullAccessorMsg = "No instance of AccountAccess supplied";
    private final String nameNotFoundMsgFormat = "No instance of '%s' found";
    // logger    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceUserDetailsService.class);

    public AccountAccess getAccess() {
        return access;
    }

    public void setAccess(AccountAccess access) {
        this.access = access;
    }

    @Override
    public UserDetails loadUserByUsername(String userKey) throws UsernameNotFoundException {
    	
    	logger.debug("Load user for '{}'", userKey);

        Objects.requireNonNull(access, nullAccessorMsg);
        //
        //  Read the User object by secondary key
        //
        User user = new SimpleUser();
        user.setUserKey(userKey);
        user = access.getUserBySecondaryKey(user);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(nameNotFoundMsgFormat, userKey));
        }
        //  Read User accounts and generate UserDetailsAuthority list
        Collection<UserDetailsAuthority> userDetailsAuthorities = null;
        Set<? extends Account> accounts = user.getAccounts();
        if (accounts == null) {
            return new AccountServiceUserDetails(user, userDetailsAuthorities);
        }
        //
        //  Generate UserDetailsAuthority list
        //
        userDetailsAuthorities = new HashSet<>();
        for (Account account : accounts) {
            //
            //  Inactive accounts should be skipped
            //
            if (!account.isEnabled()) {
                continue;
            }

            AccountUser accountUser = new SimpleAccountUser();
            accountUser.setAccountId(account.getId());
            accountUser.setUserId(user.getId());
            accountUser = access.getAccountUser(accountUser);
            //
            //  Inactive user accounts should be skipped
            //
            if (!accountUser.isEnabled()) {
                continue;
            }
            Application application = access.getApplication(account.getApplicationId());
            //
            //  Inactive application should be skipped
            //
            if (!application.isEnabled()) {
                continue;
            }
            //
            //  Populate
            //
            AccessLevel accessLevel = access.getAccessLevel(accountUser.getAccessLevelId());
            UserDetailsAuthority uda = new UserDetailsAuthority(application, accessLevel);
            userDetailsAuthorities.add(uda);
        }
        //  
        //  Create new AccountsServiceUserDetails 
        //
        return new AccountServiceUserDetails(user, userDetailsAuthorities);
    }
}
