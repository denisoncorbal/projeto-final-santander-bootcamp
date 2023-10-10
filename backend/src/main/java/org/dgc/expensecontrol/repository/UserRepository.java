package org.dgc.expensecontrol.repository;

import org.dgc.expensecontrol.model.RegisterUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RegisterUser, Long> {
    
}
