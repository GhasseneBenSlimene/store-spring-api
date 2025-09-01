package com.ghassenebenslimene.store.repositories;

import com.ghassenebenslimene.store.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
