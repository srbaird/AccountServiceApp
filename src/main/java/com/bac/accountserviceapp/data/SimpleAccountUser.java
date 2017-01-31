/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import java.util.Date;

import com.bac.accountserviceapp.AccountUser;

/**
 *
 * @author user0001
 */
public class SimpleAccountUser implements AccountUser {

    private Integer accountId;
    private Integer userId;
    private Date createDate;
    private Date lastAccessDate;
    private Integer accessLevelId;
    private String msg;
    private boolean isEnabled;
    
    public SimpleAccountUser() {

    }

    @Override
    public Integer getAccountId() {
        return accountId;
    }

    @Override
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    @Override
    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    @Override
    public Integer getAccessLevelId() {
        return accessLevelId;
    }

    @Override
    public void setAccessLevelId(Integer accessLevelId) {
        this.accessLevelId = accessLevelId;
    }

    @Override
    public String getAccountMessage() {
        return msg;
    }

    @Override
    public void setAccountMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.accountId != null ? this.accountId.hashCode() : 0);
        hash = 73 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 73 * hash + (this.createDate != null ? this.createDate.hashCode() : 0);
        hash = 73 * hash + (this.lastAccessDate != null ? this.lastAccessDate.hashCode() : 0);
        hash = 73 * hash + (this.accessLevelId != null ? this.accessLevelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleAccountUser other = (SimpleAccountUser) obj;
        if (this.accountId != other.accountId && (this.accountId == null || !this.accountId.equals(other.accountId))) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
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
