package com.ghassenebenslimene.store.repositories;

import com.ghassenebenslimene.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}