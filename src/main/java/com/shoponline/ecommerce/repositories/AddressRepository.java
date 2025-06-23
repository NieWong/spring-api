package com.shoponline.ecommerce.repositories;

import com.shoponline.ecommerce.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}