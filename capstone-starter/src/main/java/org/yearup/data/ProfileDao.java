package org.yearup.data;


import org.springframework.security.access.method.P;
import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile create(Profile profile);
    Profile getProfileById(int id);
    void update(int id, Profile profile);

}
