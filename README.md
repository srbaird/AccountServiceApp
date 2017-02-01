# Account Service App


This is an implementation of [Spring Security](http://projects.spring.io/spring-security/) to provide a simple database backed authentication method that is intended to run as a remote service such as an EJB. It was developed to provide a common service for all my web applications to authenticate user access through database configuration. The interface exposes a login using the Spring [AbstractAuthenticationToken](http://docs.spring.io/autorepo/docs/spring-security/current/apidocs/org/springframework/security/authentication/AbstractAuthenticationToken.html) request as well as application specific methods to authenticate an application and create a login. A second login method is provided to use an application provided login request format.

Authentication is carried out using the Spring [ProviderManager](http://docs.spring.io/autorepo/docs/spring-security/current/apidocs/org/springframework/security/authentication/ProviderManager.html) which is configured in a similar fashion to the following example. A full test configuration is available [here](https://github.com/srbaird/AccountServiceApp/blob/master/src/test/resources/applicationContextHSQL.xml)

```
<bean id="authenticationManager" class="org.springframework.security.authentication.ProviderManager">
  <property name="providers">
    <list>
      <ref local="daoAuthenticationProvider"/>
      <ref local="anonymousAuthenticationProvider"/>
      <ref local="ldapAuthenticationProvider"/>
    </list>
  </property>
</bean>

<bean id="daoAuthenticationProvider" class="org.springframework.security.authentication.dao.DaoAuthenticationProvider">
  <property name="userDetailsService" ref="inMemoryDaoImpl"/>
  <property name="passwordEncoder" ref="passwordEncoder"/>
</bean>
```
Encryption uses [bcryprt]( https://en.wikipedia.org/wiki/Bcrypt) through the supplied [BCryptPasswordEncoder](http://docs.spring.io/autorepo/docs/spring-security/current/apidocs/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html). Other functions are available in the current Spring Security release.

___

For convenience the project incorporates both the definition of the data model requirements and an implementation using Hibernate for a relational database design. In normal circumstances these would be separate projects and this may be easily achieved by separating out the relevant Hibernate package if required.

The data model is quite simple and naturally open to enhancements but suffices as a starting point. The following diagram shows that it basically implements a many-to-many relationship between users and accounts. Access can be controlled at any of the AccountUser, Account or Application levels and the Application can be closed to new sign-ups if required.

<p align="center">
<img src="https://github.com/srbaird/AccountServiceApp/blob/master/documents/datamodel.jpg" alt="Data model"  >
</p>

This project does not provide an interface into maintaining the data model but does implement a simple DAO pattern which provides many of the methods for this purpose. These are used in the test files which carry out the basic CRUD actions. Testing uses the HSQLDB in-memory database which means that no RDBMS needs to be installed but if DDL is required then the following is the Hibernate output (constraint names slightly amended) may be used to create a MySQL database.


```
create table access_level (id integer not null auto_increment, description varchar(255), primary key (id));
create table account (id integer not null auto_increment, active char(1) not null, application_id integer not null, create_date datetime, resource_name varchar(255), primary key (id));
create table account_user (user_id integer not null, account_id integer not null, access_level_id integer not null, account_message varchar(255), create_date datetime, enabled bit not null, last_access_date datetime, primary key (user_id, account_id));
create table application (id integer not null auto_increment, enabled bit not null, name varchar(255), registrationOpen bit not null, primary key (id));
create table user (id integer not null auto_increment, create_date datetime, enabled bit not null, password_salt tinyblob, user_key varchar(255), user_name varchar(255), user_password tinyblob, primary key (id));
alter table application add constraint UK_lspnba25gpku3nx3oecprrx8c  unique (name);

alter table user add constraint UK_USER_USER_KEY  unique (user_key);

alter table account add constraint FK_ACCOUNT_APPLICATION_ID_TO_APPLICTION_ID foreign key (application_id) references application (id);
alter table account_user add constraint FK_ACCOUNT_USER_ACCOUNT_TO_ID_ACCOUNT_ID foreign key (account_id) references account (id);
alter table account_user add constraint FK_ACCOUNT_USER_USER_ID_TO_USER_ID foreign key (user_id) references user (id);
alter table account_user add constraint FK_ACCOUNT_USER_ACCESS_LEVEL_ID_TO_ACCESS_LEVEL_ID foreign key (access_level_id) references access_level (id);
```


