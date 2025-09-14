package com.ghassenebenslimene.store.repositories;

import com.ghassenebenslimene.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}