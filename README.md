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

This project does not provide an interface into maintaining the data model but does implement a simple DAO pattern which provides many of the methods for this purpose. These are used in the test files which carry out the basic CRUD actions. Testing uses the HSQLDB in-memory database which means that no RDBMS needs to be installed but if DDL is required then the following is the Hibernate output may suffice to create a permanent database


```
TODO: Add example DDL here... 
```


