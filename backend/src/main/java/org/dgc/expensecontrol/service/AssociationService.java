package org.dgc.expensecontrol.service;

import org.dgc.expensecontrol.model.Register;
import org.dgc.expensecontrol.model.RegisterClass;
import org.dgc.expensecontrol.model.RegisterUser;
import org.dgc.expensecontrol.repository.ClassRepository;
import org.dgc.expensecontrol.repository.RegisterRepository;
import org.dgc.expensecontrol.repository.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AssociationService {

    private UserRepository userRepository;
    private ClassRepository classRepository;
    private RegisterRepository registerRepository;

    public AssociationService(UserRepository userRepository, ClassRepository classRepository, RegisterRepository registerRepository){
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.registerRepository = registerRepository;
    }

    @Transactional    
    public Register associateRegister(Long registerId, String userEmail, String className) {
        Register register = registerRepository.findById(registerId).get();
        RegisterUser user = userRepository.findByEmail(userEmail).get();
        RegisterClass registerClass = classRepository.findByName(className).get();
        user.addRegister(register);
        registerClass.addRegister(register);
        return registerRepository.save(register);
    }

    @Transactional
    public RegisterClass associateRegisterClass(Long classId, String userEmail) {
        RegisterClass registerClass = classRepository.findById(classId).get();
        RegisterUser user = userRepository.findByEmail(userEmail).get();
        user.addClass(registerClass);
        return classRepository.save(registerClass);
    }
    
}
