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
public interface Account extends AccessByPrimaryKey, Enableable{
    
    void setId(Integer id);
    
    Integer getApplicationId();
    
    void setApplicationId(Integer applicationId);
    
    String getResourceName();
    
    void setResourceName(String resourceName);
    
    Date getCreateDate();
    
    void setCreateDate(Date createDate);
    
}
