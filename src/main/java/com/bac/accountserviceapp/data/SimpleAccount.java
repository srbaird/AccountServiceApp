/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import java.util.Date;

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

}
