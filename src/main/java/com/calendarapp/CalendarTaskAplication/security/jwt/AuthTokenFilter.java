package com.calendarapp.CalendarTaskAplication.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtills jwtUtills;
    private final UserDetailsService userDetailsService;

    public AuthTokenFilter(JwtUtills jwtUtills, UserDetailsService userDetailsService) {
        this.jwtUtills = jwtUtills;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtills.getJwtFromCookie(request);
            if (jwt != null && jwtUtills.validateJwtToken(jwt)){
                String username = jwtUtills.getUsernameFromToken(jwt);

                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (user == null){
                    throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the user you are looking for(auth filter)");
                }
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
                logger.error("We have a problem while trying to procces user auth filter.");
        }
        filterChain.doFilter(request,response);
    }
}
