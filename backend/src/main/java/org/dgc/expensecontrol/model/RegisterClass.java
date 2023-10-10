package org.dgc.expensecontrol.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class RegisterClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "registerClass",
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private Set<Register> registers = new HashSet<Register>();

    public RegisterClass() {
    }

    public RegisterClass(Long id, String name, Set<Register> transactions) {
        this.id = id;
        this.name = name;
        this.registers = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(Set<Register> transactions) {
        this.registers = transactions;
    }

    public void addTransaction(Register transaction){
        registers.add(transaction);
        transaction.setRegisterClass(this);
    }

    public void removeTransaction(Register transaction){
        registers.remove(transaction);
        transaction.setRegisterClass(null);
    }
}
