/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.*;
import com.bac.accountserviceapp.AccountServiceStrategy;
import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.SimpleAccessLevel;
import com.bac.accountserviceapp.data.SimpleAccountUser;
import com.bac.accountserviceapp.data.SimpleApplication;
import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class MysqlAccountServiceStrategy extends AccountServiceStrategy {

    private MysqlAccountAccessor accountAccessor;
    private final String noAccessorMsg = "No account accessor supplied";
    private final String invalidUserSupplied = "An invalid user was supplied";
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountServiceStrategy.class);

    public MysqlAccountAccessor getAccountAccessor() {
        return accountAccessor;
    }

    public void setAccountAccessor(MysqlAccountAccessor accountAccessor) {
        this.accountAccessor = accountAccessor;
    }

    @Override
    protected void init() {
        //
        //  
        //
        Objects.requireNonNull(accountAccessor, noAccessorMsg);
        accountAccessor.init();
    }

    @Override
    protected AccountServiceRole getDefaultAccountServiceRole() {
        
        return DataConstants.defaultAccountServiceRole;
    }

    @Override
    protected Account getAccountForApplication(String applicationKey, String userKey) {

        SimpleUser user = new SimpleUser();
        user.setUserEmail(userKey);
        User accessedUser = accountAccessor.getUserBySecondaryKey(user);

        SimpleApplication application = new SimpleApplication();
        application.setName(applicationKey);
        Application accessedApplication = accountAccessor.getApplicationBySecondaryKey(application);

        if (accessedUser != null && accessedApplication != null) {
            //
            //  Find the account for this User/Application combination
            //
            for (Account account : accessedUser.getAccounts()) {
                if (accessedApplication.getId().equals(account.getApplicationId())) {
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    protected Application getApplication(String applicationKey) {

        SimpleApplication application = new SimpleApplication();
        application.setName(applicationKey);
        Application accessedApplication = accountAccessor.getApplicationBySecondaryKey(application);
        return accessedApplication;
    }

    @Override
    protected User getUser(String userKey) {

        SimpleUser user = new SimpleUser();
        user.setUserEmail(userKey);
        User accessedUser = accountAccessor.getUserBySecondaryKey(user);
        return accessedUser;
    }

    @Override
    protected User newUser(User user) {

        User accessedUser;
        try {
            accessedUser = accountAccessor.createUser(user);
        } catch (HibernateException e) {
            logger.error("Cannot create user", e);
            accessedUser = null;
        }
        return accessedUser;
    }

    @Override
    protected AccountUser newAccountUser(Account account, User user) {

        // Assume a valid user
        Objects.requireNonNull(user, invalidUserSupplied);
        Objects.requireNonNull(user.getId(), invalidUserSupplied);

        // Create the account
        try {
            account = accountAccessor.createAccount(account);
        } catch (HibernateException e) {
            logger.error("Cannot create account", e);
            return null;
        }
        //
        //  Read the default access level id
        AccessLevel ownerAccess = new SimpleAccessLevel();
        ownerAccess.setAccountServiceRole(DataConstants.defaultAccountServiceRole);
        try {
            ownerAccess = accountAccessor.getAccessLevelBySecondaryKey(ownerAccess);
        } catch (HibernateException e) {
            logger.error("Cannot locate access level", e);
            return null;
        }

        AccountUser accountUser = new SimpleAccountUser();
        accountUser.setAccountId(account.getId());
        accountUser.setUserId(user.getId());
        accountUser.setCreateDate(account.getCreateDate());
        accountUser.setAccessLevelId(ownerAccess.getId());
        try {
            accountUser = accountAccessor.createAccountUser(accountUser);
        } catch (HibernateException e) {
            logger.error("Cannot locate access level", e);
            return null;
        }
        return accountUser;
    }

}
