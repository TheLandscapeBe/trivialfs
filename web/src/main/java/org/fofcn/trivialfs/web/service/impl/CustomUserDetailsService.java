package org.fofcn.trivialfs.web.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.web.entity.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: <{}>", username);

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername("user");
        userDetails.setPassword(passwordEncoder.encode("password"));

        if (username.equals(userDetails.getUsername())) {
            return userDetails;
        }

        return null;
    }
}
