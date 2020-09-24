package no.ntnu.backend.pentbrukt.Security;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.backend.pentbrukt.Entity.LoginStructure;
import no.ntnu.backend.pentbrukt.Service.UserPrincipal;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static no.ntnu.backend.pentbrukt.Security.JwtProperties.SECRETCODE;
import static org.springframework.security.config.Elements.JWT;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // When a POST request to /login is made, this will get triggered.
    // Need to pass in {"username": "kjetilhammerseth@gmail.com", "password":"password123"} in request body
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Get credentials and map to login structure entity
        LoginStructure credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginStructure.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Login token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>());

        // Authenticate
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        return auth;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //Get Principal
        UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();

        // Build token
        String token = com.auth0.jwt.JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_time))
                .sign(HMAC512(SECRETCODE.getBytes()));
        // Add token to the response (success)
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }

}
