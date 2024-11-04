package com.read.read_sphere.repository;

import com.read.read_sphere.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}