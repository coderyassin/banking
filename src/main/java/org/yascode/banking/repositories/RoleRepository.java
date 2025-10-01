package org.yascode.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.banking.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByName(String roleName);
}
