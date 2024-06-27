package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

// This tells Spring that this class is a REST controller
@RestController
// This sets the base URL for all methods in this controller
@RequestMapping("profile")
// This allows cross-origin requests (requests from different domains)
@CrossOrigin
// This allows all users to access the methods in this class
@PreAuthorize("permitAll()")
public class ProfileController {

    // These are references to the data access objects for profiles and users
    private ProfileDao profileDao;
    private UserDao userDao;

    // This constructor uses dependency injection to initialize profileDao and userDao
    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    // This method handles GET requests to "/profile"
    @GetMapping
    public Profile getProfileById(Principal principal) {
        try {
            // Get the username of the currently logged-in user
            String userName = principal.getName();
            // Find the user by their username
            User user = userDao.getByUserName(userName);
            // Get the profile of the user by their ID
            var profile = profileDao.getProfileById(user.getId());
            // If the profile is not found, throw a 404 Not Found error
            if (profile == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            // Return the profile
            return profile;
        } catch (Exception e) {
            // If something goes wrong, throw a 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Loading Profile");
        }
    }

    // This method handles PUT requests to "/profile"
    @PutMapping
    public void update(@RequestBody Profile profile, Principal principal) {
        try {
            // Get the username of the currently logged-in user
            String userName = principal.getName();
            // Find the user by their username
            User user = userDao.getByUserName(userName);
            // Update the profile of the user
            profileDao.update(user.getId(), profile);
        } catch (Exception e) {
            // If something goes wrong, throw a 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Profile not able to update");
        }
    }
}

