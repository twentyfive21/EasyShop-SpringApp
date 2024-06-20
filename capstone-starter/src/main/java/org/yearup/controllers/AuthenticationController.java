package org.yearup.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.authentication.LoginDto;
import org.yearup.models.authentication.LoginResponseDto;
import org.yearup.models.authentication.RegisterUserDto;
import org.yearup.models.User;
import org.yearup.security.jwt.JWTFilter;
import org.yearup.security.jwt.TokenProvider;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
public class AuthenticationController {

    // TokenProvider is responsible for creating JWT tokens
    private final TokenProvider tokenProvider;
    // AuthenticationManagerBuilder helps build authentication manager
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    // Data access object for users
    private UserDao userDao;
    // Data access object for profiles
    private ProfileDao profileDao;

    // Constructor to initialize the required dependencies
    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao, ProfileDao profileDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    // Endpoint to handle user login
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        // Create authentication token using username and password from login request
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // Authenticate the user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Set the authenticated user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate JWT token
        String jwt = tokenProvider.createToken(authentication, false);

        try {
            // Fetch the user details using the username from the login request
            User user = userDao.getByUserName(loginDto.getUsername());

            // If user is not found, throw a NOT_FOUND exception
            if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            // Create HTTP headers and add the JWT token to the headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            // Return the response with the JWT token and user details
            return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);
        } catch (Exception ex) {
            // If any error occurs, throw an INTERNAL_SERVER_ERROR exception
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // Endpoint to handle user registration
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto newUser) {
        try {
            // Check if the username already exists
            boolean exists = userDao.exists(newUser.getUsername());
            // If username exists, throw a BAD_REQUEST exception
            if (exists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already Exists.");
            }

            // Create a new user
            User user = userDao.create(new User(0, newUser.getUsername(), newUser.getPassword(), newUser.getRole()));

            // Create a new profile for the user
            Profile profile = new Profile();
            profile.setUserId(user.getId());
            profileDao.create(profile);

            // Return the response with the created user details
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            // If any error occurs, throw an INTERNAL_SERVER_ERROR exception
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
