/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.hibernate;

import java.util.Collections;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.Query;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.ApplicationAccountEntity;
import com.bac.accountserviceapp.User;
import com.bac.accountserviceapp.data.SimpleComponentFactory;

/**
 *
 * @author user0001
 */
@Entity
@Table(name = "user")
@NamedQueries({ 
		@NamedQuery(name = "User.all", query = "SELECT u FROM HibernateUser u"),
		@NamedQuery(name = "User.byUserKey", query = "SELECT u FROM HibernateUser u WHERE u.userKey=:userKey") 
})
public class HibernateUser implements User, ApplicationAccountEntityProxy, ProxyHasSecondaryKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4921536491464997292L;
	private final String secondaryKeyQueryName = "User.byUserKey";
	private final String userKeyParamName = "userKey";


	private User delegate;

	//private static final Logger logger = LoggerFactory.getLogger(HibernateUser.class);

	public HibernateUser() {
	}

	public HibernateUser(User delegate) {

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

	@Column(name = "user_key", unique=true)
	@Override
	public String getUserKey() {

		return delegate == null ? null : delegate.getUserKey();
	}

	@Override
	public void setUserKey(String userKey) {

		getProxyDelegate().setUserKey(userKey);
	}
	
	@Override
	public void setEnabled(boolean isEnabled) {

		getProxyDelegate().setEnabled(isEnabled);
	}

//	@Transient
	@Override
	public boolean isEnabled() {

		return delegate == null ? false : delegate.isEnabled();
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

		query.setParameter(userKeyParamName, getUserKey());
	}

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = HibernateAccount.class)
	@JoinTable(name = "account_user",
			// application_user.user_id join to user.id
			joinColumns = @JoinColumn(name = "user_id", insertable = false, updatable = false),
			// application_user.account_id join to account.id
			inverseJoinColumns = @JoinColumn(name = "account_id", insertable = false, updatable = false))	
	@Override
	public Set<Account> getAccounts() {

		return Collections.emptySet();
	}

	@Override
	public void setAccounts(Set<? extends Account> accounts) {

		getProxyDelegate().setAccounts(accounts);
	}

	//
	// Mapping for referential integrity purposes only
	//
	@OneToMany(mappedBy = "userId", targetEntity = HibernateAccountUser.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<AccountUser> getAccountUsers() {
		
		return Collections.emptySet();
	}

	public void setAccountUsers(Set<AccountUser> accountUsers) {

		// Do nothing
	}

	//
	// Private methods
	//
	@Transient
	private User getProxyDelegate() {

		if (delegate == null) {
			delegate = SimpleComponentFactory.getUser();
		}
		return delegate;
	}
}
