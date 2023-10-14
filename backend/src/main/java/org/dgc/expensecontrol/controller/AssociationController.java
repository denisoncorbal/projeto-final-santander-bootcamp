package org.dgc.expensecontrol.controller;

import java.util.Optional;

import org.dgc.expensecontrol.model.Register;
import org.dgc.expensecontrol.model.RegisterClass;
import org.dgc.expensecontrol.service.AssociationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/association")
public class AssociationController {
    private AssociationService associationService;

    public AssociationController(AssociationService associationService){
        this.associationService = associationService;
    }

    @PostMapping("/register/{registerId}")
    public ResponseEntity<Register> associateRegister(@PathVariable("registerId") Long registerId, @RequestParam(name = "userEmail") Optional<String> userEmail, @RequestParam(name = "className") Optional<String> className){
        if(userEmail.isPresent() && className.isPresent())
            return ResponseEntity.ok(associationService.associateRegister(registerId, userEmail.get(), className.get()));
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/class/{classId}")
    public ResponseEntity<RegisterClass> associateRegisterClass(@PathVariable("classId") Long classId, @RequestParam(name = "userEmail") Optional<String> userEmail){
        if(userEmail.isPresent())
            return ResponseEntity.ok(associationService.associateRegisterClass(classId, userEmail.get()));
        return ResponseEntity.badRequest().build();
    }

}
