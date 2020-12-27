package com.epam.esm.controller;

import com.epam.esm.config.JwtTokenProvider;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = AuthenticationController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    public static final String URL = "/auth";
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = LogManager.getLogger();

    @Autowired
    public AuthenticationController(UserService userService, JwtTokenProvider tokenProvider,
                                    AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationDto authenticationDto){
        try{
            String username = authenticationDto.getUsername();
            log.info("username=" + username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    authenticationDto.getPassword()));
            User user = userService.getByName(username);
            log.info("user = " + user);
            String token = tokenProvider.createToken(username, user.getRole());

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("userName", username);
            responseMap.put("token", token);
            return ResponseEntity.ok(responseMap);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
