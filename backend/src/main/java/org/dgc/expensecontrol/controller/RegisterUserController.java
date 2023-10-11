package org.dgc.expensecontrol.controller;

import java.util.List;

import org.dgc.expensecontrol.model.RegisterUser;
import org.dgc.expensecontrol.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/user")
public class RegisterUserController {

    private UserService userService;

    public RegisterUserController(UserService userService){
        this.userService = userService;
    }

    //create
    @PostMapping("")
    public ResponseEntity<RegisterUser> createUser(@RequestBody RegisterUser newUser){
        return ResponseEntity.created(null).body(userService.createUser(newUser));
    }
    
    //read
    @GetMapping("")
    public ResponseEntity<List<RegisterUser>> readUsers(){
        return ResponseEntity.ok(userService.readUser());
    }
    
    //update
    @PutMapping("/{id}")
    public ResponseEntity<RegisterUser> updateUser(@PathVariable("id") Long id, @RequestBody RegisterUser updatedUser){
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }
    
    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
