/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author user0001
 */
public interface User extends AccessByPrimaryKey, Enableable {

    void setId(Integer id);

    String getUserName();

    void setUserName(String userName);

    String getUserKey();

    void setUserKey(String userKey);

    byte[] getUserPassword();

    void setUserPassword(byte[] userPassword);

    byte[] getPasswordSalt();

    void setPasswordSalt(byte[] pSalt);

    Date getCreateDate();

    void setCreateDate(Date createDate);

    Set<? extends Account> getAccounts();

    void setAccounts(Set<? extends Account> accounts);
}
