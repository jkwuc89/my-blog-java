package com.kwedinger.blog.security;

import com.kwedinger.blog.model.User;
import com.kwedinger.blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(emailAddress.toLowerCase().strip())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailAddress));
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmailAddress())
            .password(user.getPasswordDigest())
            .authorities(Collections.emptyList())
            .build();
    }
}
