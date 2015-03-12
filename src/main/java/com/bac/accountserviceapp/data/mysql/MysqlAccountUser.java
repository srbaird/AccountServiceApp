/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleAccountUser;
import static com.bac.accountserviceapp.data.mysql.DataConstants.*;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
@Entity
@Table(name = "account_user")
public class MysqlAccountUser implements AccountUser, ApplicationAccountEntityProxy {

    private static final long serialVersionUID = 1L;

    private AccountUser delegate;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountUser.class);

    public MysqlAccountUser() {
    }

    public MysqlAccountUser(AccountUser delegate) {

        this.delegate = delegate;
    }

    @Transient
    public AccountUser getDelegate() {

        return delegate;
    }

    public void setDelegate(ApplicationAccountEntity delegate) {
        this.delegate = (AccountUser) delegate;
    }

    @Id
    @Override
    @Column(name = "account_id")
    public Integer getAccountId() {

        return delegate == null ? null : delegate.getAccountId();
    }

    @Override
    public void setAccountId(Integer id) {

        getProxyDelegate().setAccountId(id);
    }

    @Id
    @Override
    @Column(name = "user_id")
    public Integer getUserId() {

        return delegate == null ? null : delegate.getUserId();
    }

    @Override
    public void setUserId(Integer id) {

        getProxyDelegate().setUserId(id);
    }


    @Column(nullable = false)
    public Character getActive() {

        return delegate == null ? null : delegate.isEnabled()? ACTIVE : INACTIVE;
    }


    public void setActive(Character active) {

        getProxyDelegate().setEnabled(ACTIVE.equals(active));
    }

    @Override
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    public Date getCreateDate() {

        return delegate == null ? null : delegate.getCreateDate();
    }

    @Override
    public void setCreateDate(Date createDate) {

        getProxyDelegate().setCreateDate(createDate);
    }

    @Override
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_access_date")
    public Date getLastAccessDate() {

        return delegate == null ? null : delegate.getLastAccessDate();
    }

    @Override
    public void setLastAccessDate(Date date) {

        getProxyDelegate().setLastAccessDate(date);
    }

    @Override
    @Column(name = "access_level_id")
    public Integer getAccessLevelId() {

        return delegate == null ? null : delegate.getAccessLevelId();
    }

    @Override
    public void setAccessLevelId(Integer id) {

        getProxyDelegate().setAccessLevelId(id);
    }

    @Column(name = "account_message")
    public String getAccountMessage() {
        return delegate == null ? null : delegate.getAccountMessage();
    }

    public void setAccountMessage(String msg) {
        getProxyDelegate().setAccountMessage(msg);
    }

    @Transient
    public String getSecondaryKeyQueryName() {
        return "";
    }

    public void setSecondaryKeyQuery(Query query) {
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final MysqlAccountUser other = (MysqlAccountUser) obj;
        if (this.delegate != other.delegate && (this.delegate == null || !this.delegate.equals(other.delegate))) {
            return false;
        }
        return true;
    }

    @Transient
    private AccountUser getProxyDelegate() {

        if (delegate == null) {
            delegate = new SimpleAccountUser();
        }
        return delegate;
    }

    @Override
    public void setEnabled(boolean isEnabled) {

        getProxyDelegate().setEnabled(isEnabled);
    }

    @Transient
    @Override
    public boolean isEnabled() {

        return delegate == null ? false : delegate.isEnabled();
    }
}
