package org.dgc.expensecontrol.service;

import java.util.List;

import org.dgc.expensecontrol.model.RegisterUser;
import org.dgc.expensecontrol.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public RegisterUser createUser(RegisterUser newUser) {
        return userRepository.save(newUser);
    }

    public List<RegisterUser> readUser() {
        return userRepository.findAll();
    }

    public RegisterUser updateUser(Long id, RegisterUser updatedUser) {
        RegisterUser actualUser = userRepository.findById(id).get();
        actualUser.setFirstName(updatedUser.getFirstName());
        actualUser.setLastName(updatedUser.getLastName());
        return userRepository.save(actualUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public RegisterUser readUser(String userEmail) {
        return userRepository.findByEmail(userEmail).get();
    }
    
}
