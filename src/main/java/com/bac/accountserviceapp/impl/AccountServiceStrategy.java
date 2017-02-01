/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

/**
 *
 * @author user0001
 */
public abstract class AccountServiceStrategy {

    protected abstract AccountServiceRole getDefaultAccountServiceRole();

    protected abstract Account getAccountForApplication(String applicationKey, String userKey);

    protected abstract Application getApplication(String applicationKey);

    protected abstract User getUser(String userKey);

    protected abstract User newUser(User user);

    protected abstract AccountUser newAccountUser(Account account, User user);
}
