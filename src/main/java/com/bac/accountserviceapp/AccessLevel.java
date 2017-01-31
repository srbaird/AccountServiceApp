/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bac.accountserviceapp;

import com.bac.accountservice.AccountServiceRole;

/**
 *
 * @author user0001
 */
public interface AccessLevel extends AccessByPrimaryKey {
    
    void setId(Integer id);
        
    AccountServiceRole getAccountServiceRole();
    
    void setAccountServiceRole(AccountServiceRole role);

}
