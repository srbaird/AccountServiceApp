/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleAccount;
import static com.bac.accountserviceapp.data.mysql.DataConstants.*;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "account")
public class MysqlAccount implements Account, ApplicationAccountEntityProxy {

    private static final long serialVersionUID = 1L;

    private Account delegate;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccount.class);

    public MysqlAccount() {
    }

    public MysqlAccount(Account delegate) {

        this.delegate = delegate;
    }

    @Transient
    @Override
    public Account getDelegate() {

        return delegate;
    }

    @Override
    public void setDelegate(ApplicationAccountEntity delegate) {
        this.delegate = (Account) delegate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Integer getId() {
        return delegate == null ? null : delegate.getId();
    }

    @Override
    public void setId(Integer id) {

        getProxyDelegate().setId(id);
    }

    @Column(name = "application_id")
    @Override
    public Integer getApplicationId() {

        return delegate == null ? null : delegate.getApplicationId();
    }

    @Override
    public void setApplicationId(Integer applicationId) {

        getProxyDelegate().setApplicationId(applicationId);

    }

    @Column(name = "resource_name")
    @Override
    public String getResourceName() {

        return delegate == null ? null : delegate.getResourceName();
    }

    @Override
    public void setResourceName(String resourceName) {

        getProxyDelegate().setResourceName(resourceName);

    }

//    @Override 
    @Column(nullable = false)
    public Character getActive() {
        
        return delegate == null ? null : delegate.isEnabled()? ACTIVE : INACTIVE;
    }

//    @Override
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

    @Transient
    @Override
    public String getSecondaryKeyQueryName() {

        return "";
    }

    @Override
    public void setSecondaryKeyQuery(Query query) {

    }

    @Transient
    private Account getProxyDelegate() {

        if (delegate == null) {
            delegate = new SimpleAccount();
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
