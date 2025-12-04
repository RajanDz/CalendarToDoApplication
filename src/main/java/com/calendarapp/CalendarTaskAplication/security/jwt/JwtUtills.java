package com.calendarapp.CalendarTaskAplication.security.jwt;

import com.calendarapp.CalendarTaskAplication.model.User;
import com.calendarapp.CalendarTaskAplication.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtUtills {
    private static final Logger logger =  LoggerFactory.getLogger(JwtUtills.class);

    private final UserRepository userRepository;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${app.jwtCookieName}")
    private String cookieName;

    public JwtUtills(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public ResponseCookie generateJwtCookie(UserDetails userDetails){
        String jwt = generateToken(userDetails.getUsername());
        return ResponseCookie.from(cookieName,jwt)
                .path("/")
                .maxAge(30 * 60)
                .httpOnly(true)
                .build();
    }
    public String generateToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"We can not found user"));
        List<String> rolesName = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        return Jwts.builder().setSubject(user.getUsername())
                .claim("roles", rolesName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String logout(HttpServletRequest request, HttpServletResponse response){
        Cookie jwtCookie = WebUtils.getCookie(request,cookieName);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        return "Logout successfull";
    }

    public String getUsernameFromJwtToken(HttpServletRequest request){
        Cookie jwtCookie = WebUtils.getCookie(request,cookieName);
        if (jwtCookie == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"We can not find token of user");
        }
        return Jwts.parserBuilder().setSigningKey(key())
                .build()
                .parseClaimsJws(jwtCookie.getValue())
                .getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parserBuilder().setSigningKey((SecretKey) key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getJwtFromCookie(HttpServletRequest request){
        Cookie jwtCookie = WebUtils.getCookie(request,cookieName);
        if (jwtCookie.getValue() != null && !jwtCookie.getValue().isEmpty()){
            return jwtCookie.getValue();
        }
        return "Cookie not found!";
    }

    public String getUsernameFromToken(String jwt){
        return Jwts.parserBuilder().setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody().getSubject();
    }
}
