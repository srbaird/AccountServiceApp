/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.Query;

import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleComponentFactory;

/**
 *
 * @author user0001
 */

@Entity
@Table(name = "account_user", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "account_id" }))
@NamedQueries({ 
		@NamedQuery(name = "AccountUser.all", query = "SELECT a FROM HibernateAccountUser a"),
		@NamedQuery(name = "AccountUser.byAccountAndUser", query = "SELECT a FROM HibernateAccountUser a WHERE a.accountId=:accountId AND a.userId=:userId") 
})
public class HibernateAccountUser implements AccountUser, ApplicationAccountEntityProxy, ProxyHasSecondaryKey {

	private static final long serialVersionUID = 4229098127758294327L;
	private final String secondaryKeyQueryName = "AccountUser.byAccountAndUser";
	private final String applicationNameKeyParamName1 = "accountId";
	private final String applicationNameKeyParamName2 = "userId";

	private AccountUser delegate;
	// logger
	// private static final Logger logger =
	// LoggerFactory.getLogger(HibernateAccountUser.class);
	// foreign key entities

	public HibernateAccountUser() {
	}

	public HibernateAccountUser(AccountUser delegate) {

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
	@Column(name = "account_id", nullable = false)
	public Integer getAccountId() {

		return getProxyDelegate().getAccountId();
	}

	@Override
	public void setAccountId(Integer id) {

		getProxyDelegate().setAccountId(id);
	}

	@Id
	@Override
	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {

		return getProxyDelegate().getUserId();
	}

	@Override
	public void setUserId(Integer id) {

		getProxyDelegate().setUserId(id);
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	public Date getCreateDate() {

		return getProxyDelegate().getCreateDate();
	}

	@Override
	public void setCreateDate(Date createDate) {

		getProxyDelegate().setCreateDate(createDate);
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_access_date")
	public Date getLastAccessDate() {

		return getProxyDelegate().getLastAccessDate();
	}

	@Override
	public void setLastAccessDate(Date date) {

		getProxyDelegate().setLastAccessDate(date);
	}

	@Override
	@Column(name = "access_level_id", nullable = false)
	public Integer getAccessLevelId() {

		return getProxyDelegate().getAccessLevelId();
	}

	@Override
	public void setAccessLevelId(Integer id) {

		getProxyDelegate().setAccessLevelId(id);
	}

	@Column(name = "account_message")
	public String getAccountMessage() {

		return getProxyDelegate().getAccountMessage();
	}

	public void setAccountMessage(String msg) {
		getProxyDelegate().setAccountMessage(msg);
	}

	@Override
	public void setEnabled(boolean isEnabled) {

		getProxyDelegate().setEnabled(isEnabled);
	}

	@Override
	public boolean isEnabled() {

		return delegate == null ? false : delegate.isEnabled();
	}
	/*
	 * Named query processing **********************
	 */

	@Transient
	@Override
	public String getSecondaryKeyQueryName() {

		return secondaryKeyQueryName;
	}

	@Override
	public void setSecondaryKeyQuery(Query query) {

		query.setParameter(applicationNameKeyParamName1, getAccountId());
		query.setParameter(applicationNameKeyParamName2, getUserId());
	}
	/*
	 * HashCode and Equals
	 *
	 */

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
		final HibernateAccountUser other = (HibernateAccountUser) obj;
		if (this.delegate != other.delegate && (this.delegate == null || !this.delegate.equals(other.delegate))) {
			return false;
		}
		return true;
	}

	@Transient
	private AccountUser getProxyDelegate() {

		if (delegate == null) {
			delegate = SimpleComponentFactory.getAccountUser();
		}
		return delegate;
	}
}
