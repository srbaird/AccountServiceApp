/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.hibernate;


import java.io.Serializable;

import com.bac.accountserviceapp.ApplicationAccountEntity;

/**
 *
 * @author user0001
 */
public interface ApplicationAccountEntityProxy extends Serializable {

    ApplicationAccountEntity getDelegate();

    void setDelegate(ApplicationAccountEntity delegate);


}
