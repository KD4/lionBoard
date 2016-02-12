package com.github.lionboard.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Service
public class SecurityUtil {
    private static final Logger logger =
            LoggerFactory.getLogger(SecurityUtil.class);
    public static void logInUser(com.github.lionboard.model.User user) {
        String password="";
        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(user.getRoles()));
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(user.getIdentity(), password, roles);

        logger.debug(user.getIdentity()+" sign in..");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}