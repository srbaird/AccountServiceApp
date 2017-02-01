/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import com.bac.accountservice.AccountServiceRole;
import com.bac.accountserviceapp.AccessLevel;

/**
 *
 * @author user0001
 */
public class SimpleAccessLevel implements AccessLevel {

    private Integer id;
    private AccountServiceRole role;

    public SimpleAccessLevel() {

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
    public AccountServiceRole getAccountServiceRole() {
        return role;    }

    @Override
    public void setAccountServiceRole(AccountServiceRole role) {
         this.role = role;
    }
}
