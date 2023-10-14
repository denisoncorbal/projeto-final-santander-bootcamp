package org.dgc.expensecontrol.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class RegisterUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(
        mappedBy = "registerUser",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private Set<Register> registers = new HashSet<Register>();

    @OneToMany(
        mappedBy = "registerUser",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private Set<RegisterClass> classes = new HashSet<RegisterClass>();

    public RegisterUser() {
    }

    public RegisterUser(Long id, String firstName, String lastName, String email, String password,
            Set<Register> transactions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registers = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<RegisterClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<RegisterClass> classes) {
        this.classes = classes;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(Set<Register> transactions) {
        this.registers = transactions;
    }

    public void addRegister(Register register) {
        registers.add(register);
        register.setRegisterUser(this);
    }

    public void removeRegister(Register register){
        registers.remove(register);
        register.setRegisterUser(null);
    }

    public void addClass(RegisterClass registerClass) {
        classes.add(registerClass);
        registerClass.setRegisterUser(this);
    }

    public void removeClass(RegisterClass registerClass){
        classes.remove(registerClass);
        registerClass.setRegisterUser(this);
    }
}
