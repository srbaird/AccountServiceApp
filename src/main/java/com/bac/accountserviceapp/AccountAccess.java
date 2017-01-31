/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp;

/**
 *
 * @author user0001
 */
public interface AccountAccess {

	AccessLevel getAccessLevel(Integer id);

	AccessLevel createAccessLevel(AccessLevel accessLevel);

	AccessLevel updateAccessLevel(AccessLevel accessLevel);

	void deleteAccessLevel(AccessLevel accessLevel);

	AccessLevel getAccessLevelBySecondaryKey(AccessLevel accessLevel);

	// *************************************************

	Account getAccount(Integer id);

	Account createAccount(Account account);

	Account updateAccount(Account account);

	void deleteAccount(Account account);

	// *************************************************

	AccountUser getAccountUser(AccountUser accountUser);

	AccountUser createAccountUser(AccountUser accountUser);

	AccountUser updateAccountUser(AccountUser accountUser);

	void deleteAccountUser(AccountUser accountUser);
	
	AccountUser getAccountUserBySecondaryKey(AccountUser accountUser);

	// *************************************************

	Application getApplication(Integer id);

	Application createApplication(Application application);

	Application updateApplication(Application application);

	void deleteApplication(Application application);

	Application getApplicationBySecondaryKey(Application application);

	// *************************************************

	User getUser(Integer id);

	User createUser(User user);

	User updateUser(User user);

	void deleteUser(User user);

	User getUserBySecondaryKey(User user);

}
