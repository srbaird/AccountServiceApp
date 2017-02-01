/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.impl;

import java.util.Date;
import java.util.Objects;

import com.bac.accountserviceapp.Account;

/**
 *
 * @author user0001
 */
public class SimpleAccount implements Account {

    private Integer id;
    private Integer applicationId;
    private String resourceName;
    private Date createDate;
    private boolean isEnabled;

    public SimpleAccount() {

    }
   
	//
	// Package only access to clone an Account
	//
	SimpleAccount(Account account) {

		Objects.requireNonNull(account, "Attempt to instantiate with a null account");
		id = account.getId();
		applicationId = account.getApplicationId();
		resourceName = account.getResourceName();
		isEnabled = account.isEnabled();
		createDate = account.getCreateDate();
	}

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getApplicationId() {
        return applicationId;
    }

    @Override
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isEnabled() {
        
        return isEnabled;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleAccount other = (SimpleAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleAccount [id=" + id + ", applicationId=" + applicationId + ", resourceName=" + resourceName
				+ ", enabled=" + isEnabled + ", createDate=" + createDate + "]";
	}

}
