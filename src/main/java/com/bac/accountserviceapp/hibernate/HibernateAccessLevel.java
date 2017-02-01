/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.hibernate;

import java.util.Collections;
import java.util.Set;

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

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.ApplicationAccountEntity;
import com.bac.accountserviceapp.impl.SimpleComponentFactory;

/**
 *
 * @author user0001
 */
@Entity
@Table(name = "access_level")
@NamedQueries({ 
	@NamedQuery(name = "AccessLevel.all", query = "SELECT l FROM HibernateAccessLevel l"), 
    @NamedQuery(
            name = "AccessLevel.byDescription",
            query = "SELECT a FROM HibernateAccessLevel a WHERE a.description=:description")
})
public class HibernateAccessLevel implements AccessLevel, ApplicationAccountEntityProxy, ProxyHasSecondaryKey {


	private static final long serialVersionUID = 7070661218280343199L;
	private final String secondaryKeyQueryName = "AccessLevel.byDescription";
    private final String userEmailKeyParamName = "description";
	private AccessLevel delegate;
	// logger
	// private static final Logger logger =
	// LoggerFactory.getLogger(HibernateAccessLevel.class);

	public HibernateAccessLevel() {
	}

	public HibernateAccessLevel(AccessLevel delegate) {

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
		return getProxyDelegate().getId();
	}

	@Override
	public void setId(Integer id) {

		checkId(id);
		getProxyDelegate().setId(id);
	}

	private void checkId(Integer id) {

		final Integer localId = getProxyDelegate().getId();
		if (localId != null && localId != id) {
			throw new IllegalArgumentException("Id is already set");
		}
	}
	

    public String getDescription() {

        return getAccountServiceRole() == null ? "null" : getAccountServiceRole().name();
    }

    public void setDescription(String description) {

        try {
            getProxyDelegate().setAccountServiceRole(AccountServiceRole.valueOf(description));
        } catch (IllegalArgumentException ex) {
            getProxyDelegate().setAccountServiceRole(null);
        }
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

	@Transient
	private AccessLevel getProxyDelegate() {

		if (delegate == null) {
			delegate = SimpleComponentFactory.getAccessLevel();
		}
		return delegate;
	}
	//
	// Mapping for referential integrity purposes only
	//
	@OneToMany(mappedBy = "accessLevelId", targetEntity = HibernateAccountUser.class)
	public Set<AccountUser> getAccountUsers() {
		
		return Collections.emptySet();
	}

	public void setAccountUsers(Set<AccountUser> accountUsers) {

		// Do nothing
	}
}
