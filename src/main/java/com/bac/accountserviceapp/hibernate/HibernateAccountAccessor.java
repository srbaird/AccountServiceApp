/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.hibernate;

import java.util.Objects;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bac.accountserviceapp.AccessByPrimaryKey;
import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountAccess;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

/**
 *
 * @author Simon Baird
 */
@Transactional
public class HibernateAccountAccessor implements AccountAccess {

	private EntityProxyObjectFactory objectFactory;

	private SessionFactory sessionFactory;

	private Session session;
	// logger
	private static final Logger logger = LoggerFactory.getLogger(HibernateAccountAccessor.class);
	//
	private final String nullParameter = "Supplied parameter was null";
	//

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;

	}

	public EntityProxyObjectFactory getObjectFactory() {
		return objectFactory;
	}

	@Resource(name = "objectFactory")
	public void setObjectFactory(EntityProxyObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	//
	// Access level
	//
	@Override
	public AccessLevel getAccessLevel(Integer id) {

		ApplicationAccountEntityProxy proxy = getEntity(objectFactory.getAccessLevelClass(), id);
		if (proxy != null) {
			// Evict any residual entities...
			evictEntity(proxy);
			// ... and re-read it
			proxy = getEntity(objectFactory.getAccessLevelClass(), id);
		}
		return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public AccessLevel createAccessLevel(AccessLevel accessLevel) {

		ApplicationAccountEntityProxy proxy = createEntity(objectFactory.getObject(accessLevel));
		return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public AccessLevel updateAccessLevel(AccessLevel accessLevel) {

		ApplicationAccountEntityProxy proxy = updateEntity(objectFactory.getAccessLevelClass(), accessLevel);
		return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public void deleteAccessLevel(AccessLevel accessLevel) {

		deleteEntity(objectFactory.getAccessLevelClass(), accessLevel);
	}

	@Override
	public AccessLevel getAccessLevelBySecondaryKey(AccessLevel accessLevel) {

		ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(objectFactory.getSecondaryKeyObject(accessLevel));
		return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
	}

	//
	// Account
	//
	@Override
	public Account getAccount(Integer id) {

		ApplicationAccountEntityProxy proxy = getEntity(objectFactory.getAccountClass(), id);
		if (proxy != null) {
			// Evict any residual entities...
			evictEntity(proxy);
			// ... and re-read it
			proxy = getEntity(objectFactory.getAccountClass(), id);
		}
		return (Account) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public Account createAccount(Account account) {

		ApplicationAccountEntityProxy proxy = createEntity(objectFactory.getObject(account));
		return (Account) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public Account updateAccount(Account account) {

		ApplicationAccountEntityProxy proxy = updateEntity(objectFactory.getAccountClass(), account);
		return (Account) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public void deleteAccount(Account account) {

		deleteEntity(objectFactory.getAccountClass(), account);
	}

	//
	// Account User
	//
	public AccountUser getAccountUser(AccountUser accountUser) {

		ApplicationAccountEntityProxy proxy = getEntity(objectFactory.getObject(accountUser));
		if (proxy != null) {
			// Evict any residual entities...
			evictEntity(proxy);
			// ... and re-read it
			proxy = getEntity(objectFactory.getObject(accountUser));
		}
		return (AccountUser) (proxy == null ? null : proxy.getDelegate());
	}

	public AccountUser createAccountUser(AccountUser accountUser) {

		ApplicationAccountEntityProxy proxy = createEntity(objectFactory.getObject(accountUser));
		return (AccountUser) (proxy == null ? null : proxy.getDelegate());
	}

	public AccountUser updateAccountUser(AccountUser accountUser) {

		ApplicationAccountEntityProxy proxy = updateEntity(objectFactory.getObject(accountUser));
		return (AccountUser) (proxy == null ? null : proxy.getDelegate());
	}

	public void deleteAccountUser(AccountUser accountUser) {

		deleteEntity(objectFactory.getObject(accountUser));
	}

	@Override
	public AccountUser getAccountUserBySecondaryKey(AccountUser accountUser) {

		ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(objectFactory.getSecondaryKeyObject(accountUser));
		return (AccountUser) (proxy == null ? null : proxy.getDelegate());
	}

	//
	// Application
	//
	@Override
	public Application getApplication(Integer id) {

		ApplicationAccountEntityProxy proxy = getEntity(objectFactory.getApplicationClass(), id);
		if (proxy != null) {
			// Evict any residual entities...
			evictEntity(proxy);
			// ... and re-read it
			proxy = getEntity(objectFactory.getApplicationClass(), id);
		}
		return (Application) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public Application createApplication(Application application) {

		ApplicationAccountEntityProxy proxy = createEntity(objectFactory.getObject(application));
		return (Application) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public Application updateApplication(Application application) {

		ApplicationAccountEntityProxy proxy = updateEntity(objectFactory.getApplicationClass(), application);
		return (Application) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public void deleteApplication(Application application) {

		deleteEntity(objectFactory.getApplicationClass(), application);
	}

	@Override
	public Application getApplicationBySecondaryKey(Application application) {

		ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(objectFactory.getSecondaryKeyObject(application));
		return (Application) (proxy == null ? null : proxy.getDelegate());
	}

	//
	// User
	//
	@Override
	public User getUser(Integer id) {

		ApplicationAccountEntityProxy proxy = getEntity(objectFactory.getUserClass(), id);
		if (proxy != null) {
			// Evict any residual entities...
			evictEntity(proxy);
			// ... and re-read it
			proxy = getEntity(objectFactory.getUserClass(), id);
		}
		final User user = (User) (proxy == null ? null : proxy.getDelegate());
		// Evict to prevent session from re-applying empty values on secondary key read
		if (proxy != null) {
			evictEntity(proxy); 
		}
		return user;
	}

	@Override
	public User createUser(User user) {

		ApplicationAccountEntityProxy proxy = createEntity(objectFactory.getObject(user));
		return (User) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public User updateUser(User user) {

		ApplicationAccountEntityProxy proxy = updateEntity(objectFactory.getUserClass(), user);
		return (User) (proxy == null ? null : proxy.getDelegate());
	}

	@Override
	public void deleteUser(User user) {

		deleteEntity(objectFactory.getUserClass(), user);
	}

	@Override
	public User getUserBySecondaryKey(User user) {

		ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(objectFactory.getSecondaryKeyObject(user));
		return proxy == null ? null : getUser(((User) proxy).getId());
	}

	//
	// Persistence methods
	//
	private ApplicationAccountEntityProxy createEntity(ApplicationAccountEntityProxy proxy) throws HibernateException {

		Objects.requireNonNull(proxy, nullParameter);
		session = sessionFactory.getCurrentSession();
		try {
			session.saveOrUpdate(proxy);
			session.flush(); // required to apply the insert
		} catch (HibernateException e) {
			logger.error("Create entity", e.getMessage());
			throw (e);
		}
		return proxy;
	}

	private ApplicationAccountEntityProxy getEntity(Class<?> clazz, Integer id) throws HibernateException {

		Objects.requireNonNull(id, nullParameter);
		ApplicationAccountEntityProxy proxy;
		session = sessionFactory.getCurrentSession();
		try {
			proxy = (ApplicationAccountEntityProxy) session.get(clazz, id);
		} catch (HibernateException e) {
			logger.error("Read entity using id", e.getMessage());
			throw (e);
		}
		return proxy;
	}

	private void evictEntity(ApplicationAccountEntityProxy entity) {

		session = sessionFactory.getCurrentSession();
		try {
			session.evict(entity);
		} catch (HibernateException e) {
			logger.error("Evicting an entity", e.getMessage());
		}
	}

	private ApplicationAccountEntityProxy getEntity(ApplicationAccountEntityProxy entity) throws HibernateException {

		Objects.requireNonNull(entity, nullParameter);
		ApplicationAccountEntityProxy proxy;
		session = sessionFactory.getCurrentSession();
		try {
			session.evict(entity);
			proxy = (ApplicationAccountEntityProxy) session.get(entity.getClass(), entity);
		} catch (HibernateException e) {
			logger.error("Read entity by proxy", e.getMessage());
			throw (e);
		}
		return proxy;
	}

	private ApplicationAccountEntityProxy updateEntity(Class<?> clazz, AccessByPrimaryKey entity)
			throws HibernateException {

		Objects.requireNonNull(entity, nullParameter);
		ApplicationAccountEntityProxy proxy;
		session = sessionFactory.getCurrentSession();
		try {
			proxy = (ApplicationAccountEntityProxy) session.get(clazz, entity.getId());
			proxy.setDelegate(entity);
			proxy = (ApplicationAccountEntityProxy) session.merge(proxy);
			session.flush(); // required to apply the update
		} catch (HibernateException e) {
			logger.error("Update entity", e.getMessage());
			throw (e);
		}
		return proxy;
	}

	private ApplicationAccountEntityProxy updateEntity(ApplicationAccountEntityProxy entity) throws HibernateException {

		Objects.requireNonNull(entity, nullParameter);
		ApplicationAccountEntityProxy proxy;
		session = sessionFactory.getCurrentSession();
		try {
			proxy = (ApplicationAccountEntityProxy) session.load(entity.getClass(), entity);
			proxy = (ApplicationAccountEntityProxy) session.merge(proxy);
			session.flush(); // required to apply the update
		} catch (HibernateException e) {
			logger.error("Update entity by proxy", e.getMessage());
			throw (e);
		}
		return proxy;
	}

	private void deleteEntity(Class<?> clazz, AccessByPrimaryKey entity) throws HibernateException {

		Objects.requireNonNull(entity, nullParameter);
		session = sessionFactory.getCurrentSession();
		try {
			ApplicationAccountEntityProxy proxy = (ApplicationAccountEntityProxy) session.get(clazz, entity.getId());
			session.delete(proxy);
			session.flush();
		} catch (HibernateException e) {
			logger.error("Delete entity", e.getMessage());
			throw (e);
		}
	}

	private void deleteEntity(ApplicationAccountEntityProxy entity) throws HibernateException {

		Objects.requireNonNull(entity, nullParameter);

		session = sessionFactory.getCurrentSession();
		try {
			ApplicationAccountEntityProxy proxy = (ApplicationAccountEntityProxy) session.get(entity.getClass(),
					entity);
			session.delete(proxy);
			session.flush();
		} catch (HibernateException e) {
			logger.error("Delete entity by proxy", e.getMessage());
			throw (e);
		}
	}

	private ApplicationAccountEntityProxy getEntityBySecondaryKey(ProxyHasSecondaryKey entity)
			throws HibernateException {

		Objects.requireNonNull(entity, nullParameter);
		String queryName = entity.getSecondaryKeyQueryName();

		if (queryName == null || queryName.isEmpty()) {
			return null;
		}
		ApplicationAccountEntityProxy proxy = null;
		session = sessionFactory.getCurrentSession();
		try {
			Query query = session.getNamedQuery(queryName);
			entity.setSecondaryKeyQuery(query);
			proxy = (ApplicationAccountEntityProxy) query.uniqueResult();
		} catch (HibernateException e) {
			logger.error("Retrieve entity by secondary key", e.getMessage());
			throw (e);
		}
		return proxy;
	}

}
