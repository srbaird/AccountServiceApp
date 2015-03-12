/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.entity;

/**
 *
 * @author user0001
 */
public interface UserAccount {

    String getUserName();

    void setUserName(String userName);

    String getUserEmail();

    void setUserEmail(String userEmail);
    
    String getUserPassword();
    
    void setUserPassword(String userPassword);
}
