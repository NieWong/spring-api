package com.shoponline.ecommerce.repositories;

import com.shoponline.ecommerce.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

