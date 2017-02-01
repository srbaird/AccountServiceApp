/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.accountserviceapp.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bac.accountserviceapp.AccessLevel;
import com.bac.accountserviceapp.Application;
import com.bac.accountserviceapp.User;

/**
 *
 * @author user0001
 */
public class AccountServiceUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String password;
    private String userName;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled;

    private final Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    // logger    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceUserDetails.class);

    public AccountServiceUserDetails(User user, Collection<? extends UserDetailsAuthority> userDetailsAuthorities) {

        if (user != null) {

            userName = user.getUserKey();
            password = user.getUserPassword() == null ? null : new String(user.getUserPassword());
            enabled = user.isEnabled();
        } else {
        	logger.debug("Supplied User is null");
        }

        if (userDetailsAuthorities == null) {
        	logger.debug("No user details authorities supplied");
            return;
        }
        for (UserDetailsAuthority uda : userDetailsAuthorities) {
            Application application = uda.getApplication();
            AccessLevel accessLevel = uda.getAccessLevel();
            if (application != null && accessLevel != null) {
                if (application.isEnabled() ) {
                    String serviceRole = accessLevel.getAccountServiceRole() == null ? "No available role" : accessLevel.getAccountServiceRole().name();
                    String authority = String.format(DataConstants.AUTHORITY_FORMAT, application.getName(), DataConstants.AUTHORITY_SEPARATOR, serviceRole);
                    authorities.add(new SimpleGrantedAuthority(authority));
                }
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {

        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {

        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {

        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }
}
