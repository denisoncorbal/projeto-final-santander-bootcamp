package org.dgc.expensecontrol.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;

    private Double registerValue;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    private RegisterUser registerUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private RegisterClass registerClass;

    public Register() {
    }

    public Register(Long id, LocalDate date, Double value, String type, RegisterUser user,
            RegisterClass transactionClass) {
        this.id = id;
        this.date = date;
        this.registerValue = value;
        this.type = type;
        this.registerUser = user;
        this.registerClass = transactionClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getRegisterValue() {
        return registerValue;
    }

    public void setRegisterValue(Double value) {
        this.registerValue = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RegisterUser getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(RegisterUser user) {
        this.registerUser = user;
    }

    public RegisterClass getRegisterClass() {
        return registerClass;
    }

    public void setRegisterClass(RegisterClass transactionClass) {
        this.registerClass = transactionClass;
    }

}