/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bac.accountserviceapp.data;

import java.util.Objects;

/**
 *
 * @author user0001
 */
public class UserDetailsAuthority {
    
    private Application application;
    private AccessLevel accessLevel;

    public UserDetailsAuthority(Application application, AccessLevel accessLevel) {
        this.application = application;
        this.accessLevel = accessLevel;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.application);
        hash = 97 * hash + Objects.hashCode(this.accessLevel);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDetailsAuthority other = (UserDetailsAuthority) obj;
        if (!Objects.equals(this.application, other.application)) {
            return false;
        }
        return Objects.equals(this.accessLevel, other.accessLevel);
    }       
}
