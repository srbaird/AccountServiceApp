package com.bac.accountserviceapp.hibernate;

import org.hibernate.Query;

/**
 * A Class to allow secondary key access to be set by the appropriate Hibenate
 * entity.
 * 
 * @author Simon Baird
 *
 */
public interface ProxyHasSecondaryKey {

	/**
	 * Get the Hibernate named query which may be used to provide alternative
	 * access to a unique entitiy.
	 * 
	 * @return the Hibernate query name for the secondary key access.
	 */
	String getSecondaryKeyQueryName();

	/**
	 * A method to populate the parameter values required by the secondary key
	 * value. It is the responsibility of the entity to correctly complete the
	 * named query with the appropriate parameter values
	 * 
	 * @param query
	 *            the supplied named query with any required parameter values
	 *            populated.
	 */
	void setSecondaryKeyQuery(Query query);

}
