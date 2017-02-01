/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import com.bac.accountserviceapp.Application;

/**
 *
 * @author user0001
 */
public class SimpleApplication implements Application {

    private Integer id;
    private String name;
    private boolean isEnabled;
    private boolean isRegistrationOpen;

    public SimpleApplication() {
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

//    @Override
//    public Character getAcceptRegistration() {
//        return acceptRegistration;
//    }

//    @Override
//    public void setAcceptRegistration(Character acceptRegistration) {
//        this.acceptRegistration = acceptRegistration;
//    }

    @Override
    public void setEnabled(boolean isEnabled) {
        
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isEnabled() {
        
        return isEnabled;
    }

    @Override
    public boolean isRegistrationOpen() {
        return isRegistrationOpen;
    }

    @Override
    public void setRegistrationOpen(boolean isRegistrationOpen) {
        this.isRegistrationOpen = isRegistrationOpen;
    }
    
    
}
