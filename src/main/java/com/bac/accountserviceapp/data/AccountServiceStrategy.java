/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.User;

/**
 *
 * @author user0001
 */
public abstract class AccountServiceStrategy {

    protected abstract void init();

    protected abstract AccountServiceRole getDefaultAccountServiceRole();

    protected abstract Account getAccountForApplication(String applicationKey, String userKey);

    protected abstract Application getApplication(String applicationKey);

    protected abstract User getUser(String userKey);

    protected abstract User newUser(User user);

    protected abstract AccountUser newAccountUser(Account account, User user);
}
