/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.hibernate;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Query;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.ApplicationAccountEntity;
import com.bac.accountserviceapp.data.SimpleComponentFactory;

/**
 *
 * @author user0001
 */
@Entity
@Table(name = "application")
@NamedQueries({ 
		@NamedQuery(name = "Application.all", query = "SELECT a FROM HibernateApplication a"),
		@NamedQuery(name = "Application.byName", query = "SELECT a FROM HibernateApplication a WHERE a.name=:name")
})
public class HibernateApplication implements Application, ApplicationAccountEntityProxy, ProxyHasSecondaryKey {

	private static final long serialVersionUID = -7905200891506463431L;
	//
	private final String secondaryKeyQueryName = "Application.byName";
	private final String applicationNameKeyParamName = "name";

	private Application delegate;

	public HibernateApplication() {
	}

	public HibernateApplication(Application delegate) {

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
	@Column(unique = true)
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
			delegate = SimpleComponentFactory.getApplication();
		}
		return delegate;
	}

	@Override
	public void setEnabled(boolean isEnabled) {

		getProxyDelegate().setEnabled(isEnabled);
	}

	@Override
	public boolean isEnabled() {

		return delegate == null ? false : delegate.isEnabled();
	}

	@Override
	public void setRegistrationOpen(boolean RegistrationOpen) {

		getProxyDelegate().setRegistrationOpen(RegistrationOpen);
	}

	@Override
	public boolean isRegistrationOpen() {

		return delegate == null ? false : delegate.isRegistrationOpen();
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

	/*
	 * List of accounts. Not required by Application interface but may be useful
	 * in the future. Included here to enforce foreign key delete
	 * 
	 */
	@OneToMany(mappedBy = "applicationId", targetEntity = HibernateAccount.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<Account> getAccounts() {

		return Collections.emptySet();
	}

	public void setAccounts(Set<Account> accounts) {

		// Do nothing
	}
}
