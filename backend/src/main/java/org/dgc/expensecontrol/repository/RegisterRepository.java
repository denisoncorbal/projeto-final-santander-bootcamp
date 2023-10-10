package org.dgc.expensecontrol.repository;

import org.dgc.expensecontrol.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    
}
