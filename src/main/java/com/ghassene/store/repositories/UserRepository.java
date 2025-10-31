package com.ghassene.store.repositories;

import com.ghassene.store.Dtos.UserDto;
import com.ghassene.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
}
