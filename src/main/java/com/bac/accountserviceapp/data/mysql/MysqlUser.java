/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import static com.bac.accountserviceapp.data.mysql.DataConstants.*;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "user")
@NamedQueries({
    @NamedQuery(
            name = "User.all",
            query = "SELECT u FROM MysqlUser u"),
    @NamedQuery(
            name = "User.byEmailAndPassword",
            query = "SELECT u FROM MysqlUser u WHERE u.userEmail=:userEmail")
})
public class MysqlUser implements User, ApplicationAccountEntityProxy {

    private static final long serialVersionUID = 1L;
    private final String secondaryKeyQueryName = "User.byEmailAndPassword";
    private final String userEmailKeyParamName = "userEmail";

    private User delegate;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlUser.class);

    public MysqlUser() {
    }

    public MysqlUser(User delegate) {

        this.delegate = delegate;
    }

    @Transient
    @Override
    public User getDelegate() {

        return delegate;
    }

    @Override
    public void setDelegate(ApplicationAccountEntity delegate) {
        this.delegate = (User) delegate;
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

    @Column(name = "user_name")
    @Override
    public String getUserName() {

        return delegate == null ? null : delegate.getUserName();
    }

    @Override
    public void setUserName(String userName) {

        getProxyDelegate().setUserName(userName);
    }

    @Column(name = "user_email")
    @Override
    public String getUserEmail() {

        return delegate == null ? null : delegate.getUserEmail();
    }

    @Override
    public void setUserEmail(String userEmail) {

        getProxyDelegate().setUserEmail(userEmail);
    }

    @Column(name = "user_password")
    @Override
    public byte[] getUserPassword() {

        return delegate == null ? null : delegate.getUserPassword();
    }

    @Override
    public void setUserPassword(byte[] userPassword) {

        getProxyDelegate().setUserPassword(userPassword);
    }

    @Column(name = "password_salt")
    @Override
    public byte[] getPasswordSalt() {
        return delegate == null ? null : delegate.getPasswordSalt();
    }

    @Override
    public void setPasswordSalt(byte[] pSalt) {

        getProxyDelegate().setPasswordSalt(pSalt);
    }

//    @Override
    public Character getActive() {

//        return delegate == null ? null : delegate.getActive();
        return delegate == null ? null : delegate.isEnabled() ? ACTIVE : INACTIVE;

    }

//    @Override
    public void setActive(Character active) {

//        getProxyDelegate().setActive(active);
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

        return secondaryKeyQueryName;
    }

    @Override
    public void setSecondaryKeyQuery(Query query) {

        query.setParameter(userEmailKeyParamName, getUserEmail());
    }

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = MysqlAccount.class)
    @JoinTable(name = "account_user",
            joinColumns = @JoinColumn(name = "user_id"), // application_user.user_id join to user.id
            inverseJoinColumns = @JoinColumn(name = "account_id"))  // application_user.account_id join to account.id
    @Override
    public Set<? extends Account> getAccounts() {

        return delegate == null ? null : delegate.getAccounts();
    }

    @Override
    public void setAccounts(Set<? extends Account> accounts) {

        getProxyDelegate().setAccounts(accounts);
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

    //
    //  Private methods
    //
    @Transient
    private User getProxyDelegate() {

        if (delegate == null) {
            delegate = new SimpleUser();
        }
        return delegate;
    }

}
