/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp;

import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.User;

/**
 *
 * @author user0001
 */
public interface AccountAccess {

    Account getAccount(Integer id);

    Account createAccount(Account account);

    Account updateAccount(Account account);

    void deleteAccount(Account account);

    AccountUser getAccountUser(AccountUser accountUser);

    AccountUser createAccountUser(AccountUser accountUser);

    AccountUser updateAccountUser(AccountUser accountUser);

    void deleteAccountUser(AccountUser accountUser);

    User getUser(Integer id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User getUserBySecondaryKey(User user);

    AccessLevel getAccessLevel(Integer id);

    AccessLevel createAccessLevel(AccessLevel accessLevel);

    AccessLevel updateAccessLevel(AccessLevel accessLevel);

    void deleteAccessLevel(AccessLevel accessLevel);

    AccessLevel getAccessLevelBySecondaryKey(AccessLevel accessLevel);

    Application getApplication(Integer id);

    Application createApplication(Application application);

    Application updateApplication(Application application);

    void deleteApplication(Application application);

    Application getApplicationBySecondaryKey(Application application);

}
