/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import java.util.Objects;

import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountAccess;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

/**
 *
 * @author Simon Baird
 */
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AccountServiceStrategyImpl extends AccountServiceStrategy {

	@Autowired
    private AccountAccess accountAccessor;
	
    private final String INVALID_USER_MSG = "An invalid user was supplied";
    // logger
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceStrategyImpl.class);

    public AccountAccess getAccountAccessor() {
        return accountAccessor;
    }

    public void setAccountAccessor(AccountAccess accountAccessor) {
        this.accountAccessor = accountAccessor;
    }

    @Override
    protected AccountServiceRole getDefaultAccountServiceRole() {
        
        return DataConstants.DEFAULT_ACCOUNT_SERVICE_ROLE;
    }

    @Override
    protected Account getAccountForApplication(String applicationKey, String userKey) {

        User user = getUser(userKey);       
        Application application = getApplication( applicationKey);

        if (user != null && application != null) {
            //
            //  Find the account for this User/Application combination
            //
            for (Account account : user.getAccounts()) {
                if (application.getId().equals(account.getApplicationId())) {
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    protected Application getApplication(String applicationKey) {

        Application application = SimpleComponentFactory.getApplication();
        application.setName(applicationKey);
        return accountAccessor.getApplicationBySecondaryKey(application);
    }

    @Override
    protected User getUser(String userKey) {

        User user = SimpleComponentFactory.getUser();
        user.setUserKey(userKey);
        return accountAccessor.getUserBySecondaryKey(user);
    }

    @Override
    protected User newUser(User user) {

        User accessedUser;
        try {
            accessedUser = accountAccessor.createUser(user);
        } catch (Exception e) {
            logger.error("Cannot create user", e);
            accessedUser = null;
        }
        return accessedUser;
    }

    @Override
    protected AccountUser newAccountUser(Account account, User user) {

        // Assume a valid user
        Objects.requireNonNull(user.getId(), INVALID_USER_MSG);

        // Create the account
        try {
            account = accountAccessor.createAccount(account);
        } catch (Exception e) {
            logger.error("Cannot create account", e);
            return null;
        }
        //
        //  Read the default access level id
        AccessLevel ownerAccess = SimpleComponentFactory.getAccessLevel();
        ownerAccess.setAccountServiceRole(DataConstants.DEFAULT_ACCOUNT_SERVICE_ROLE);
        try {
            ownerAccess = accountAccessor.getAccessLevelBySecondaryKey(ownerAccess);
            if (ownerAccess == null) {
            	throw new NullPointerException();
            }
        } catch (Exception e) {
            logger.error("Unable to locate the default access level for new login", e);
            return null;
        }

        AccountUser accountUser = SimpleComponentFactory.getAccountUser();
        accountUser.setAccountId(account.getId());
        accountUser.setUserId(user.getId());
        accountUser.setCreateDate(account.getCreateDate());
        accountUser.setAccessLevelId(ownerAccess.getId());
        try {
            accountUser = accountAccessor.createAccountUser(accountUser);
        } catch (Exception e) {
            logger.error("Cannot locate access level", e);
            return null;
        }
        return accountUser;
    }

}
