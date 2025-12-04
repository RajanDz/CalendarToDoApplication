package com.calendarapp.CalendarTaskAplication.service;

import com.calendarapp.CalendarTaskAplication.model.User;
import com.calendarapp.CalendarTaskAplication.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"We can not find user"));
        CustomUserDetails userDetails = CustomUserDetails.build(user);
        System.out.println("Inside loadUserByUsername, UserDetails: " + userDetails.getUsername());
        return userDetails;
    }
}
