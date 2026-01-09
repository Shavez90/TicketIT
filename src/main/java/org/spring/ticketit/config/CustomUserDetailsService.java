package org.spring.ticketit.config;





import lombok.RequiredArgsConstructor;
import org.spring.ticketit.model.User;
import org.spring.ticketit.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
        public class CustomUserDetailsService implements UserDetailsService {

            private final UserRepository userRepository;

        @Override
            public UserDetails loadUserByUsername(String email)
                    throws UsernameNotFoundException {

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found"));

                return org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().name())
                        .build();
            }
        }


