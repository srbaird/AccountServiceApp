
package com.bac.accountserviceapp.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountAccess;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

/**
 * Implementation of UserDetailsService to query the data source for active User
 * accounts.
 * 
 * @author Simon Baird
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountAccess access;
	//
	private final String NULL_ACCESSOR_MSG = "No instance of AccountAccess supplied";
	private final String NAME_NOT_FOUND_MSG_FORMAT = "No instance of '%s' found";
	// logger
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceUserDetailsService.class);

	public AccountAccess getAccess() {
		return access;
	}

	public void setAccess(AccountAccess access) {
		this.access = access;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String userKey) throws UsernameNotFoundException {

		logger.debug("Load user details for '{}'", userKey);

		Objects.requireNonNull(access, NULL_ACCESSOR_MSG);
		//
		// Read the User object by secondary key
		//
		User user = new SimpleUser();
		user.setUserKey(userKey);
		user = access.getUserBySecondaryKey(user);
		if (user == null) {
			throw new UsernameNotFoundException(String.format(NAME_NOT_FOUND_MSG_FORMAT, userKey));
		}

		// Read User accounts and generate UserDetailsAuthority list
		Collection<UserDetailsAuthority> userDetailsAuthorities = null;

		// Filter out disabled accounts
		Set<? extends Account> accounts = user.getAccounts().stream().filter(a -> a.isEnabled())
				.collect(Collectors.toSet());
		if (accounts == null) {

			logger.debug("No active accounts found for '{}'", userKey);
			return new AccountServiceUserDetails(user, userDetailsAuthorities);
		}
		//
		// Generate UserDetailsAuthority list
		//
		logger.debug("'{}' has {} accounts to process", userKey, accounts.size());
		userDetailsAuthorities = new HashSet<>();
		for (Account account : accounts) {

			AccountUser accountUser = new SimpleAccountUser();
			accountUser.setAccountId(account.getId());
			accountUser.setUserId(user.getId());
			accountUser = access.getAccountUser(accountUser);
			//
			// Inactive user accounts should be skipped
			//
			if (!accountUser.isEnabled()) {
				logger.debug("AccountUser is disabled");
				continue;
			}
			Application application = access.getApplication(account.getApplicationId());
			//
			// Inactive application should be skipped
			//
			if (!application.isEnabled()) {
				logger.debug("Application is disabled");
				continue;
			}
			//
			// Populate
			//
			AccessLevel accessLevel = access.getAccessLevel(accountUser.getAccessLevelId());
			UserDetailsAuthority uda = new UserDetailsAuthority(application, accessLevel);
			userDetailsAuthorities.add(uda);
		}
		//
		// Create new AccountsServiceUserDetails
		//
		return new AccountServiceUserDetails(user, userDetailsAuthorities);
	}
}
