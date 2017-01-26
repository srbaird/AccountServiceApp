/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

/**
 *
 * @author user0001
 */
public interface Application extends AccessByPrimaryKey, Enableable {

    void setId(Integer id);

    String getName();

    void setName(String name);
    
    boolean isRegistrationOpen();
    
    void setRegistrationOpen(boolean isRegistrationOpen);
}
