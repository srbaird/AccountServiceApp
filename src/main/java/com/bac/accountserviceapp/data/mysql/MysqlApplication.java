/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleApplication;
import static com.bac.accountserviceapp.data.mysql.DataConstants.*;
import javax.persistence.Column;
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
@Table(name = "application")
@NamedQueries({
    @NamedQuery(
            name = "Application.all",
            query = "SELECT a FROM MysqlApplication a"),
    @NamedQuery(
            name = "Application.byName",
            query = "SELECT a FROM MysqlApplication a WHERE a.name=:name")
})
public class MysqlApplication implements Application, ApplicationAccountEntityProxy {

    private static final long serialVersionUID = 1L;
    //
    private final String secondaryKeyQueryName = "Application.byName";
    private final String applicationNameKeyParamName = "name";

    private Application delegate;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlApplication.class);

    public MysqlApplication() {
    }

    public MysqlApplication(Application delegate) {

        this.delegate = delegate;
    }

    @Transient
    @Override
    public Application getDelegate() {

        return delegate;
    }

    @Override
    public void setDelegate(ApplicationAccountEntity delegate) {
        this.delegate = (Application) delegate;
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

    @Override
    public String getName() {

        return delegate == null ? null : delegate.getName();
    }

    @Override
    public void setName(String name) {

        getProxyDelegate().setName(name);

    }

    @Transient
    private Application getProxyDelegate() {

        if (delegate == null) {
            delegate = new SimpleApplication();
        }
        return delegate;
    }

    @Column(nullable = false)
    public Character getActive() {

        return delegate == null ? null : delegate.isEnabled() ? ACTIVE : INACTIVE;
    }

    public void setActive(Character active) {

        getProxyDelegate().setEnabled(ACTIVE.equals(active));
    }

//    @Override
    @Column(name = "accept_registration")
    public Character getAcceptRegistration() {

        //       return delegate == null ? null : delegate.getAcceptRegistration();
        return delegate == null ? null : delegate.isRegistrationOpen() ? ACTIVE : INACTIVE;
    }

//    @Override
    public void setAcceptRegistration(Character acceptRegistration) {

//        getProxyDelegate().setAcceptRegistration(acceptRegistration);
        getProxyDelegate().setRegistrationOpen(ACTIVE.equals(acceptRegistration));
    }

    @Transient
    @Override
    public String getSecondaryKeyQueryName() {

        return secondaryKeyQueryName;
    }

    @Override
    public void setSecondaryKeyQuery(Query query) {

        query.setParameter(applicationNameKeyParamName, getName());
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

    @Override
    public void setRegistrationOpen(boolean RegistrationOpen) {

        getProxyDelegate().setRegistrationOpen(RegistrationOpen);
    }

    @Transient
    @Override
    public boolean isRegistrationOpen() {

        return delegate == null ? false : delegate.isRegistrationOpen();
    }
}
