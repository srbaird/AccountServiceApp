/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp;

import com.bac.accountservice.AccountService;
import com.bac.accountservice.AccountServiceApplication;
import com.bac.accountservice.AccountServiceAuthentication;
import com.bac.accountservice.AccountServiceAuthenticationOutcome;
import static com.bac.accountservice.AccountServiceAuthenticationOutcome.*;
import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.data.Account;
import com.bac.accountserviceapp.data.AccountUser;
import com.bac.accountserviceapp.data.Application;
import com.bac.accountserviceapp.data.SimpleAccount;
import com.bac.accountserviceapp.data.SimpleUser;
import com.bac.accountserviceapp.data.User;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author user0001
 */
@Remote(AccountService.class)
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AccountServiceApp implements AccountService {

    @Autowired
    private AccountServiceStrategy strategy;
    @Autowired
    private ProviderManager authenticationManager;
    private BCryptPasswordEncoder encoder;
    //
    private final String noApplicationName = "No application name supplied";
    private final String incompleteLogin = "Login has not supplied correct credentials";
    private final String noAuthManagerMsg = "No authentication manager has been provided";
    private final String noAuthenticationMsg = "No authentication token was supplied";

    private Pattern pattern;
    private Matcher matcher;
    private final String AUTHORITY_STRING_PATTERN = "(.*)::(.*)";
    private final int AUTHORITY_PATTERN_COUNT = 2;
    private final int AUTHORITY_PATTERN_APPLICATION_ITEM = 1;
    private final int AUTHORITY_PATTERN_ROLE_ITEM = 2;
    // logger
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceApp.class);

    @PostConstruct
    public void init() {
        //
        //  Initialize the supplied accessor
        //
        strategy.init();
        //
        //  Compile the Authority string regexp
        //
        pattern = Pattern.compile(AUTHORITY_STRING_PATTERN);
        //
        //  Initialize the encoder
        //
        encoder = new BCryptPasswordEncoder();
    }

    public AccountServiceStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(AccountServiceStrategy strategy) {
        logger.info("Set strategy");
        this.strategy = strategy;
    }

    public ProviderManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(ProviderManager authenticationManager) {
        logger.info("Set authentication manager");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AccountServiceAuthentication login(AccountServiceAuthentication authentication) {
        //
        //  Validate authentication content
        //
        Objects.requireNonNull(authentication, noAuthenticationMsg);
        Objects.requireNonNull(authentication.getApplicationName(), noApplicationName);
        Objects.requireNonNull(authentication.getAccountKey(), incompleteLogin);
        Objects.requireNonNull(authentication.getAccountPassword(), incompleteLogin);
        //
        //  Clear out any pre-set values
        //
        authentication.setAccountResource(null);
        authentication.setAccountRole(null);
        authentication.setAuthenticationOutcome(null);
        //
        //  
        //
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authentication.getAccountKey(), authentication.getAccountPassword());
        Authentication loginToken = login(authenticationToken);
        authentication.setAccountPassword(null);
        //
        //  If the token is not authenticated then return
        //
        AccountServiceAuthenticationOutcome loginOutcome = (AccountServiceAuthenticationOutcome) loginToken.getDetails();
        if (loginOutcome != AUTHENTICATED) {
            authentication.setAuthenticationOutcome(loginOutcome);
            return authentication;
        }
        //
        //  Temporarily set outcome to no role and then verify that
        //
        authentication.setAuthenticationOutcome(NO_ROLE);
        final String expectedApplicationName = authentication.getApplicationName();
        if (loginToken.getAuthorities() != null) {

            for (GrantedAuthority authority : loginToken.getAuthorities()) {

                String authorityString = authority.getAuthority();
                matcher = pattern.matcher(authorityString);
                if (!matcher.matches() || matcher.groupCount() != AUTHORITY_PATTERN_COUNT) {
                    continue;
                }
                String authorityApplicationName = matcher.group(AUTHORITY_PATTERN_APPLICATION_ITEM);
                String authorityRole = matcher.group(AUTHORITY_PATTERN_ROLE_ITEM);

                if (!expectedApplicationName.equals(authorityApplicationName)) {
                    continue;
                }
                //
                //  Look up the AccountRole and add it to the outgoing authentication
                //
                AccountServiceRole accountRole;
                try {
                    accountRole = AccountServiceRole.valueOf(authorityRole);
                } catch (IllegalArgumentException e) {
                    logger.warn("Unable to find a valid Account Servie Role for '{}'", authorityRole);
                    accountRole = null;
                }
                authentication.setAccountRole(accountRole);
                authentication.setAuthenticationOutcome(AUTHENTICATED);
                break;
            }
        }
        //
        //  If NO_ROLE is overidden then populate the outgoing authentication with the Account resource
        //
        if (authentication.getAuthenticationOutcome() == AUTHENTICATED) {

            Account account = strategy.getAccountForApplication(expectedApplicationName, authentication.getAccountKey());
            if (account == null) {
                authentication.setAuthenticationOutcome(NO_RESOURCE);
            } else {
                authentication.setAccountResource(account.getResourceName());
            }
        }
        //
        //  Complete so return
        //
        return authentication;
    }

    @Override
    public Authentication login(AbstractAuthenticationToken authentication) {

        Objects.requireNonNull(authenticationManager, noAuthManagerMsg);
        Objects.requireNonNull(authentication, noAuthenticationMsg);

        try {
            authentication = (AbstractAuthenticationToken) authenticationManager.authenticate(authentication);
        } catch (ProviderNotFoundException e) {
            logger.warn("No authentication provider available for principal: '{}'", authentication.getPrincipal());
            authentication.setDetails(NO_PROVIDER);
            return authentication;
        } catch (BadCredentialsException | IllegalArgumentException e) {
            logger.warn("Unable to authenticate for principal: '{}'", authentication.getPrincipal());
            authentication.setDetails(BAD_CREDENTIALS);
            return authentication;
        } catch (UsernameNotFoundException e) {
            logger.warn("Unable to authenticate for principal: '{}'", e.getMessage());
            authentication.setDetails(UNKNOWN_PRINCIPAL);
            return authentication;
        } catch (DisabledException e) {
            logger.warn("Principal is disabled: '{}'", authentication.getPrincipal());
            authentication.setDetails(DISABLED_PRINCIPAL);
            return authentication;
        }
        authentication.setDetails(AUTHENTICATED);
        return authentication;
    }

    @Override
    public AccountServiceAuthentication createLogin(AccountServiceAuthentication authentication) {

        Objects.requireNonNull(authentication, noAuthenticationMsg);
        //
        //  Is the application active...
        Application application = strategy.getApplication(authentication.getApplicationName());
        if (application == null || !application.isEnabled()) {
            return setFailedCreateLoginAuthentication(authentication, NO_APPLICATION);
        }
        //  ...and accepting registrations?
        if (!application.isRegistrationOpen()) {
            return setFailedCreateLoginAuthentication(authentication, APPLICATION_CLOSED);
        }
        //
        //  Does the Principal already exist?... 
        User user = strategy.getUser(authentication.getAccountKey());
        if (user != null) {
            // validate it to see if we can proceed
            UsernamePasswordAuthenticationToken validation = new UsernamePasswordAuthenticationToken(authentication.getAccountKey(), authentication.getAccountPassword());
            Authentication loginAuthentication = login(validation);
            if (loginAuthentication == null || !Objects.equals(AUTHENTICATED, loginAuthentication.getDetails())) {
                //
                //  Can't create this account
                //
                return setFailedCreateLoginAuthentication(authentication, PRINCIPAL_EXISTS);
            }
        }
        //
        //  ...does it have an application account?
        Account account = strategy.getAccountForApplication(authentication.getApplicationName(), authentication.getAccountKey());
        if (account != null) {
            //
            //  Can't create this account
            //
            return setFailedCreateLoginAuthentication(authentication, PRINCIPAL_EXISTS);
        }
        //  Create Account/User as appropriate
        //
        Date createDate = new Date(System.currentTimeMillis());

        if (user == null) {
            user = new SimpleUser();
            user.setUserEmail(authentication.getAccountKey());
            try {
                logger.info("Set the password to '{}'", authentication.getAccountPassword());
                user.setUserPassword(authentication.getAccountPassword().getBytes());
            } catch (NullPointerException e) {
                return setFailedCreateLoginAuthentication(authentication, BAD_CREDENTIALS);
            }
            user.setUserPassword(encoder.encode(authentication.getAccountPassword()).getBytes());
            user.setUserName(authentication.getAccountName());
            user.setEnabled(false);
            user.setCreateDate(createDate);
            user = strategy.newUser(user);
            if (user == null) {
                return setFailedCreateLoginAuthentication(authentication, UNKNOWN_PRINCIPAL);
            }
        }

        // create User account
        account = new SimpleAccount();
        String accountResource = authentication.getAccountResource() instanceof String ? (String) authentication.getAccountResource() : "";
        account.setApplicationId(application.getId());
        account.setResourceName(accountResource);
        account.setEnabled(false);
        account.setCreateDate(createDate);

        AccountUser accountUser = strategy.newAccountUser(account, user);
        if (accountUser == null) {
            return setFailedCreateLoginAuthentication(authentication, NO_ROLE);
        }

        authentication.setAccountRole(strategy.getDefaultAccountServiceRole());
        authentication.setAuthenticationOutcome(PENDING_CREATION);
        // Clear down the password
        authentication.setAccountPassword(null);
        return authentication;
    }

    @Override
    public AccountServiceApplication authenticateApplication(AccountServiceApplication authentication) {

        Objects.requireNonNull(authentication, noAuthenticationMsg);
        //
        //  Get the application
        //  
        Application application = strategy.getApplication(authentication.getName());
        //
        //  No application 
        //
        if (application == null) {
            authentication.setEnabled(false);
            authentication.setRegistrationOpen(false);
            authentication.setAuthenticationOutcome(NO_APPLICATION);
            return authentication;
        }
        authentication.setEnabled(application.isEnabled());
        authentication.setRegistrationOpen(application.isRegistrationOpen());
        authentication.setAuthenticationOutcome(AUTHENTICATED);
        return authentication;
    }

    private AccountServiceAuthentication setFailedCreateLoginAuthentication(
            AccountServiceAuthentication authentication, AccountServiceAuthenticationOutcome outcome) {

        authentication.setAuthenticationOutcome(outcome);
        authentication.setAccountPassword(null);
        authentication.setAccountRole(null);
        authentication.setAccountResource(null);
        authentication.setAccountName(null);
        return authentication;
    }

}
