package me.missionary.blueberry.profile;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
public interface IProfileManager<T> {

    void createProfile(UUID uuid);

    Optional<T> getProfile(UUID uuid);
}
