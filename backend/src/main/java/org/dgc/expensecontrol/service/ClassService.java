package org.dgc.expensecontrol.service;

import java.util.List;

import org.dgc.expensecontrol.model.RegisterClass;
import org.dgc.expensecontrol.repository.ClassRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassService {

    private ClassRepository classRepository;

    public ClassService(ClassRepository classRepository){
        this.classRepository = classRepository;
    }

    public RegisterClass createClass(RegisterClass newClass) {
        return classRepository.save(newClass);
    }

    public List<RegisterClass> readClasses() {
        return classRepository.findAll();
    }

    public RegisterClass updateClass(Long id, RegisterClass updatedClass) {
        RegisterClass actualClass = classRepository.findById(id).get();
        actualClass.setName(updatedClass.getName());
        return classRepository.save(actualClass);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
    
}
