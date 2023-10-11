package org.dgc.expensecontrol.repository;

import java.util.Optional;

import org.dgc.expensecontrol.model.RegisterUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RegisterUser, Long> {
    public Optional<RegisterUser> findByEmail(String email);
}
