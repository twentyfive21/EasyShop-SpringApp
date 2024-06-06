package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
@PreAuthorize("permitAll()")
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    //  create an Autowired controller to inject the profileDao
    @Autowired
    public ProfileController(UserDao userDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @GetMapping()
   public Profile getProfileById(Principal principal){
    String userName = principal.getName();
    User user = userDao.getByUserName(userName);
    return profileDao.getProfileById(user.getId());
   }

   @PutMapping()
    public void update(@RequestBody Profile profile, Principal principal){
       String userName = principal.getName();
       User user = userDao.getByUserName(userName);
       profileDao.update(user.getId(),profile);
    }


}
