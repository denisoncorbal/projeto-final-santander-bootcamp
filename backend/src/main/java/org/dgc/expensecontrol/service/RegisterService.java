package org.dgc.expensecontrol.service;

import java.util.List;

import org.dgc.expensecontrol.model.Register;
import org.dgc.expensecontrol.repository.RegisterRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private RegisterRepository registerRepository;

    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public Register createRegister(Register newRegister) {
        return registerRepository.save(newRegister);
    }

    public List<Register> readRegisters() {
        return registerRepository.findAll();
    }

    public Register updateRegister(Long id, Register updatedRegister) {
        Register actualRegister = registerRepository.findById(id).get();
        actualRegister.setDate(updatedRegister.getDate());
        actualRegister.setRegisterValue(updatedRegister.getRegisterValue());
        actualRegister.setType(updatedRegister.getType());
        return registerRepository.save(actualRegister);
    }

    public void deleteRegister(Long id) {
        registerRepository.deleteById(id);
    }

    public List<Register> readRegistersByUser(String userEmail) {
        return registerRepository.findAllByRegisterUser_Email(userEmail);
    }

    public List<Register> readRegistersByUserAndType(String userEmail, String type) {
        return registerRepository.findAllByRegisterUser_EmailAndType(userEmail, type);
    }

}
