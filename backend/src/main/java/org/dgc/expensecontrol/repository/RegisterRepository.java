package org.dgc.expensecontrol.repository;

import java.util.List;

import org.dgc.expensecontrol.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    public List<Register> findAllByRegisterUser_Email(String userEmail);
    public List<Register> findAllByRegisterUser_EmailAndType(String userEmail, String type);
}
