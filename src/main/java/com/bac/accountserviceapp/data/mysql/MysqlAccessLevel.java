/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleAccessLevel;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
@Entity
@Table(name = "access_level")
@NamedQueries({
    @NamedQuery(
            name = "AccessLevel.all",
            query = "SELECT a FROM MysqlAccessLevel a"),
    @NamedQuery(
            name = "AccessLevel.byDescription",
            query = "SELECT a FROM MysqlAccessLevel a WHERE a.description=:description")
})
public class MysqlAccessLevel implements AccessLevel, ApplicationAccountEntityProxy {

    private static final long serialVersionUID = 1L;
    private final String secondaryKeyQueryName = "AccessLevel.byDescription";
    private final String userEmailKeyParamName = "description";

    private AccessLevel delegate;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccessLevel.class);

    public MysqlAccessLevel() {
    }

    public MysqlAccessLevel(AccessLevel delegate) {

        this.delegate = delegate;
    }

    @Transient
    @Override
    public AccessLevel getDelegate() {

        return delegate;
    }

    @Override
    public void setDelegate(ApplicationAccountEntity delegate) {
        this.delegate = (AccessLevel) delegate;
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

    public String getDescription() {

        return getAccountServiceRole() == null ? "null" : getAccountServiceRole().name();
    }

    public void setDescription(String description) {

        try {
            getProxyDelegate().setAccountServiceRole(AccountServiceRole.valueOf(description));
        } catch (IllegalArgumentException ex) {
            logger.warn("Unable to set description:", ex.getMessage());
            getProxyDelegate().setAccountServiceRole(null);
        }
    }


    @Transient
    private AccessLevel getProxyDelegate() {

        if (delegate == null) {
            delegate = new SimpleAccessLevel();
        }
        return delegate;
    }

    @Transient
    @Override
    public AccountServiceRole getAccountServiceRole() {

        return delegate == null ? null : delegate.getAccountServiceRole();
    }

    @Override
    public void setAccountServiceRole(AccountServiceRole role) {

        getProxyDelegate().setAccountServiceRole(role);
    }

    @Transient
    @Override
    public String getSecondaryKeyQueryName() {

        return secondaryKeyQueryName;
    }

    @Override
    public void setSecondaryKeyQuery(Query query) {

        query.setParameter(userEmailKeyParamName, getDescription());
    }
}
