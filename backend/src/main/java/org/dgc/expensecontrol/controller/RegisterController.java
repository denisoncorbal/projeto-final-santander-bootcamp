package org.dgc.expensecontrol.controller;

import java.util.List;

import org.dgc.expensecontrol.model.Register;
import org.dgc.expensecontrol.service.RegisterService;
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
@RequestMapping(path = "api/v1/register")
public class RegisterController {
    private RegisterService registerService;

    public RegisterController(RegisterService registerService){
        this.registerService = registerService;
    }

    //create
    @PostMapping("")
    public ResponseEntity<Register> createRegister(@RequestBody Register newRegister){
        return ResponseEntity.created(null).body(registerService.createRegister(newRegister));
    }

    //read
    @GetMapping("")
    public ResponseEntity<List<Register>> readRegisters(){
        return ResponseEntity.ok(registerService.readRegisters());
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<Register> updateRegister(@PathVariable("id") Long id, @RequestBody Register updatedRegister){
        return ResponseEntity.ok(registerService.updateRegister(id, updatedRegister));
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegister(@PathVariable("id") Long id){
        registerService.deleteRegister(id);
        return ResponseEntity.noContent().build();
    }
}
