/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bac.accountserviceapp.data;

import com.bac.accountservice.AccountServiceRole;
import static com.bac.accountservice.AccountServiceRole.*;

/**
 *
 * @author user0001
 */
public class DataConstants {
    
    // Active flags
    public static final Character ACTIVE = 'Y';
    public static final Character INACTIVE = 'N';
    //
    public static final AccountServiceRole DEFAULT_ACCOUNT_SERVICE_ROLE = GUEST;
    //
    public static final String AUTHORITY_SEPARATOR = "::";
    public static final String AUTHORITY_FORMAT = "%s%s%s";
    public static final String AUTHORITY_STRING_PATTERN = "(.*)"+ AUTHORITY_SEPARATOR + "(.*)";
    
}
