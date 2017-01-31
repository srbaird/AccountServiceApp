package com.bac.accountserviceapp.hibernate;

import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Account;
import com.bac.accountserviceapp.AccountUser;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

public interface EntityProxyObjectFactory {

	ApplicationAccountEntityProxy getObject(AccessLevel delegate);

	ApplicationAccountEntityProxy getObject(Account delegate);

	ApplicationAccountEntityProxy getObject(AccountUser delegate);

	ApplicationAccountEntityProxy getObject(Application delegate);

	ApplicationAccountEntityProxy getObject(User delegate);

	/*
	 * Non-populated object instances
	 */
	ApplicationAccountEntityProxy getAccessLevelObject();

	ApplicationAccountEntityProxy getAccountObject();

	ApplicationAccountEntityProxy getAccountUserObject();

	ApplicationAccountEntityProxy getApplicationObject();

	ApplicationAccountEntityProxy getUserObject();

	/*
	 * Object class instances
	 * 
	 */
	Class<? extends AccessLevel> getAccessLevelClass();

	Class<? extends Account> getAccountClass();

	Class<? extends AccountUser> getAccountUserClass();

	Class<? extends Application> getApplicationClass();

	Class<? extends User> getUserClass();

	/*
	 * Secondary Key object instances
	 * 
	 */
	ProxyHasSecondaryKey getSecondaryKeyObject(AccessLevel delegate);

	ProxyHasSecondaryKey getSecondaryKeyObject(Application delegate);

	ProxyHasSecondaryKey getSecondaryKeyObject(AccountUser delegate);

	ProxyHasSecondaryKey getSecondaryKeyObject(User delegate);

}
