package org.yascode.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.banking.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
}
