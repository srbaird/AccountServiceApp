/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author user0001
 */
public class SimpleUser implements User {

    private Integer id;
    private String userName;
    private String userEmail;
    private byte[] userPassword;
    private byte[] passwordSalt;
    private Character active;
    private final Character DEFAULT_ACTIVE = 'N';
    private Date createDate;
    private Set<? extends Account> accounts;
    private boolean isEnabled;

    public SimpleUser() {

    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public byte[] getUserPassword() {
        return userPassword;
    }

    @Override
    public void setUserPassword(byte[] userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    @Override
    public void setPasswordSalt(byte[] pSalt) {
        this.passwordSalt = pSalt;
    }

//    @Override
//    public Character getActive() {
//        return active == null ? DEFAULT_ACTIVE : active;
//    }

//    @Override
//    public void setActive(Character active) {
//        this.active =  active == null ? DEFAULT_ACTIVE : active;
//    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Set<? extends Account> getAccounts() {
        return accounts;
    }

    @Override
    public void setAccounts(Set<? extends Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isEnabled() {
        
        return isEnabled;
    }
}
