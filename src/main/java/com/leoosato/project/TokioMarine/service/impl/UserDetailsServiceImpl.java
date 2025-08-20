package com.leoosato.project.TokioMarine.service.impl;

import com.leoosato.project.TokioMarine.model.UserModel;
import com.leoosato.project.TokioMarine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // mapeia "USER" -> "ROLE_USER"
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + u.getRole()))
        );
    }
}