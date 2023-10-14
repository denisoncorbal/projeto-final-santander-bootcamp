package org.dgc.expensecontrol.controller;

import java.util.List;

import org.dgc.expensecontrol.model.RegisterClass;
import org.dgc.expensecontrol.service.ClassService;
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
@RequestMapping(path = "api/v1/class")
public class RegisterClassController {
    private ClassService classService;

    public RegisterClassController(ClassService classService){
        this.classService = classService;
    }

    //create
    @PostMapping("")
    public ResponseEntity<RegisterClass> createClass(@RequestBody RegisterClass newClass){
        return ResponseEntity.created(null).body(classService.createClass(newClass));
    }
    
    //read
    @GetMapping("")
    public ResponseEntity<List<RegisterClass>> readClasses(){
        return ResponseEntity.ok(classService.readClasses());
    }
    @GetMapping("/{userEmail}")
    public ResponseEntity<List<RegisterClass>> readClassesByUser(@PathVariable(name = "userEmail") String userEmail){
        return ResponseEntity.ok(classService.readClassesByUser(userEmail));
    }
    
    //update
    @PutMapping("/{id}")
    public ResponseEntity<RegisterClass> updateClass(@PathVariable("id") Long id, @RequestBody RegisterClass updatedClass){
        return ResponseEntity.ok(classService.updateClass(id, updatedClass));
    }
    
    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable("id") Long id){
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
