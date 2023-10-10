package org.dgc.expensecontrol.repository;

import org.dgc.expensecontrol.model.RegisterClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<RegisterClass, Long> {
    
}
