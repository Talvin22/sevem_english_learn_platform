package com.dzhaparov.config;


import com.dzhaparov.entity.user.User;
import com.dzhaparov.repository.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));


        String authority = "ROLE_" + user.getRole().name();

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(authority)
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getHashed_password(),
                authorities
        );
    }
}
