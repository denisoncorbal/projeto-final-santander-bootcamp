package org.dgc.expensecontrol.repository;

import java.util.List;
import java.util.Optional;

import org.dgc.expensecontrol.model.RegisterClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<RegisterClass, Long> {
    public Optional<RegisterClass> findByName(String name);

    public List<RegisterClass> findAllByRegisterUser_Email(String userEmail);
}
