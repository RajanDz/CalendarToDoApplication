package com.calendarapp.CalendarTaskAplication.controller;


import com.calendarapp.CalendarTaskAplication.model.Task;
import com.calendarapp.CalendarTaskAplication.model.User;
import com.calendarapp.CalendarTaskAplication.model.dto.RegistrationDto;
import com.calendarapp.CalendarTaskAplication.security.jwt.JwtUtills;
import com.calendarapp.CalendarTaskAplication.service.CustomUserDetails;
import com.calendarapp.CalendarTaskAplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtills jwtUtills;
    private final UserService userService;
    public AuthController(AuthenticationManager authenticationManager, JwtUtills jwtUtills, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtills = jwtUtills;
        this.userService = userService;
    }

    @GetMapping("/login/{username}/{password}")
    public ResponseEntity<?> loginRequest(@PathVariable(name = "username") String username, @PathVariable(name = "password") String password){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (AuthenticationException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        }
        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtills.generateJwtCookie(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(jwtCookie);
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        String logout = jwtUtills.logout(request,response);
        return  ResponseEntity.ok().body(logout);
    }
    @PostMapping("/registration")
    public ResponseEntity<User> userRegistration(@RequestParam("firstname") String firstname,
                                                 @RequestParam("lastname") String lastname,
                                                 @RequestParam("username") String username,
                                                 @RequestParam("email") String email,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {

        User registration = userService.registration(firstname,lastname,username,email,password,profilePicture);
        return ResponseEntity.ok().body(registration);
    }
}
