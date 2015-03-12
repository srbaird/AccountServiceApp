/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data.mysql;

import com.bac.accountserviceapp.AccountAccessor;
import com.bac.accountserviceapp.data.AccessByPrimaryKey;
import com.bac.accountserviceapp.data.AccessLevel;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.User;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class MysqlAccountAccessor extends AccountAccessor {

    private SessionFactory diSessionFactory;
    private SessionFactory sessionFactory;
    private Session session;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(MysqlAccountAccessor.class);
    //
    private final String nullParameter = "Supplied parameter was null";
    private final String uninitialized = "Account access has not been initialized";

    public MysqlAccountAccessor() {

    }

    @Override
    protected void init() {
        //
        //  Set the object session factory to the injected value
        //
        sessionFactory = diSessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return diSessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.diSessionFactory = sessionFactory;
    }

    //
    //  Account
    //    
    @Override
    public Account getAccount(Integer id) {

        ApplicationAccountEntityProxy proxy = getEntity(MysqlAccount.class, id);
        return (Account) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public Account createAccount(Account account) {

        ApplicationAccountEntityProxy proxy = createEntity(new MysqlAccount(account));
        return (Account) (proxy == null ? null : proxy.getDelegate());

    }

    @Override
    public Account updateAccount(Account account) {

        ApplicationAccountEntityProxy proxy = updateEntity(MysqlAccount.class, account);
        return (Account) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public void deleteAccount(Account account) {

        deleteEntity(MysqlAccount.class, account);
    }

    //
    //  Account User
    //
    @Override
    public AccountUser getAccountUser(AccountUser accountUser) {

        ApplicationAccountEntityProxy proxy = getEntity(new MysqlAccountUser(accountUser));
        return (AccountUser) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public AccountUser createAccountUser(AccountUser accountUser) {

        ApplicationAccountEntityProxy proxy = createEntity(new MysqlAccountUser(accountUser));
        return (AccountUser) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public AccountUser updateAccountUser(AccountUser accountUser) {

        ApplicationAccountEntityProxy proxy = updateEntity(new MysqlAccountUser(accountUser));
        return (AccountUser) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public void deleteAccountUser(AccountUser accountUser) {

        deleteEntity(new MysqlAccountUser(accountUser));
    }

    //
    //  User
    //    
    @Override
    public User getUser(Integer id) {

        ApplicationAccountEntityProxy proxy = getEntity(MysqlUser.class, id);
        return (User) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public User createUser(User user) {

        ApplicationAccountEntityProxy proxy = createEntity(new MysqlUser(user));
        return (User) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public User updateUser(User user) {

        ApplicationAccountEntityProxy proxy = updateEntity(MysqlUser.class, user);
        return (User) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public void deleteUser(User user) {

        deleteEntity(MysqlUser.class, user);
    }

    @Override
    public User getUserBySecondaryKey(User user) {

        ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(new MysqlUser(user));
        return (User) (proxy == null ? null : proxy.getDelegate());
    }

    //
    //  Access level
    //    
    @Override
    public AccessLevel getAccessLevel(Integer id) {

        ApplicationAccountEntityProxy proxy = getEntity(MysqlAccessLevel.class, id);
        return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public AccessLevel getAccessLevelBySecondaryKey(AccessLevel accessLevel) {
 
        ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(new MysqlAccessLevel(accessLevel));
        return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public AccessLevel createAccessLevel(AccessLevel accessLevel) {

        ApplicationAccountEntityProxy proxy = createEntity(new MysqlAccessLevel(accessLevel));
        return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public AccessLevel updateAccessLevel(AccessLevel accessLevel) {

        ApplicationAccountEntityProxy proxy = updateEntity(MysqlAccessLevel.class, accessLevel);
        return (AccessLevel) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public void deleteAccessLevel(AccessLevel accessLevel) {

        deleteEntity(MysqlAccessLevel.class, accessLevel);
    }

    //
    //  Application
    //    
    @Override
    public Application getApplication(Integer id) {

        ApplicationAccountEntityProxy proxy = getEntity(MysqlApplication.class, id);
        return (Application) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public Application createApplication(Application application) {

        ApplicationAccountEntityProxy proxy = createEntity(new MysqlApplication(application));
        return (Application) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public Application updateApplication(Application application) {

        ApplicationAccountEntityProxy proxy = updateEntity(MysqlApplication.class, application);
        return (Application) (proxy == null ? null : proxy.getDelegate());
    }

    @Override
    public void deleteApplication(Application application) {

        deleteEntity(MysqlApplication.class, application);
    }

    @Override
    public Application getApplicationBySecondaryKey(Application application) {

        ApplicationAccountEntityProxy proxy = getEntityBySecondaryKey(new MysqlApplication(application));
        return (Application) (proxy == null ? null : proxy.getDelegate());
    }

    //
    //  Persistence methods. Require the object to have been initialized    
    //
    private ApplicationAccountEntityProxy createEntity(ApplicationAccountEntityProxy proxy) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(proxy, nullParameter);
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(proxy);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Create entity", e);
            throw (e);
        }
        return proxy;
    }

    private ApplicationAccountEntityProxy getEntity(Class<?> clazz, Integer id) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(id, nullParameter);
        ApplicationAccountEntityProxy proxy;
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            proxy = (ApplicationAccountEntityProxy) session.get(clazz, id);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Read entity using id", e);
            throw (e);
        }
        return proxy;
    }

    private ApplicationAccountEntityProxy getEntity(ApplicationAccountEntityProxy entity) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(entity, nullParameter);
        ApplicationAccountEntityProxy proxy;
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            proxy = (ApplicationAccountEntityProxy) session.get(entity.getClass(), entity);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Read entity by proxy", e);
            throw (e);
        }
        return proxy;
    }

    private ApplicationAccountEntityProxy updateEntity(Class<?> clazz, AccessByPrimaryKey entity) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(entity, nullParameter);
        ApplicationAccountEntityProxy proxy;
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            proxy = (ApplicationAccountEntityProxy) session.get(clazz, entity.getId());
            proxy.setDelegate(entity);
            session.merge(proxy);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Update entity", e);
            throw (e);
        }
        return proxy;
    }

    private ApplicationAccountEntityProxy updateEntity(ApplicationAccountEntityProxy entity) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(entity, nullParameter);
        ApplicationAccountEntityProxy proxy;
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
//            proxy = (ApplicationAccountEntityProxy) session.get(entity.getClass(), entity);
//            ApplicationAccountEntity newDelegate = entity.getDelegate();
//            proxy.setDelegate(newDelegate);
            session.update(entity);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Update entity by proxy", e);
            throw (e);
        }
        return entity;
    }

    private void deleteEntity(Class<?> clazz, AccessByPrimaryKey entity) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(entity, nullParameter);
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            ApplicationAccountEntityProxy proxy = (ApplicationAccountEntityProxy) session.get(clazz, entity.getId());
            session.delete(proxy);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Delete entity", e);
            throw (e);
        }
    }

    private void deleteEntity(ApplicationAccountEntityProxy entity) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(entity, nullParameter);

        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            ApplicationAccountEntityProxy proxy = (ApplicationAccountEntityProxy) session.get(entity.getClass(), entity);
            session.delete(proxy);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Read entity by proxy", e);
            throw (e);
        }

    }

    private ApplicationAccountEntityProxy getEntityBySecondaryKey(ApplicationAccountEntityProxy entity) throws HibernateException {

        Objects.requireNonNull(sessionFactory, uninitialized);
        Objects.requireNonNull(entity, nullParameter);
        String queryName = entity.getSecondaryKeyQueryName();
        if (queryName == null || queryName.isEmpty()) {
            return null;
        }
        ApplicationAccountEntityProxy proxy = null;
        session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.getNamedQuery(queryName);
            entity.setSecondaryKeyQuery(query);
            proxy = (ApplicationAccountEntityProxy) query.uniqueResult();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.error("Retrieve entity by secondary key", e);
            throw (e);
        }
        return proxy;
    }

}
