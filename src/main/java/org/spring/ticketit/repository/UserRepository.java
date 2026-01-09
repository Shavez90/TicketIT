package org.spring.ticketit.repository;

import org.spring.ticketit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Override
    Optional<User> findByEmail(String s);
}
