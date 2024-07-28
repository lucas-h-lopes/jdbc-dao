package model.entities;

import model.exceptions.ConsoleManagerException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Seller {
    private Integer id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private Double baseSalary;
    private Department department;

    public Seller(Integer id, String name, String email, LocalDate birthDate, Double baseSalary, Department department) throws ConsoleManagerException{
        if (name.isBlank()) throw new ConsoleManagerException("Error: Seller name is required.");
        if (email.isBlank()) throw new ConsoleManagerException("Error: Seller email is required.");
        if (String.valueOf(birthDate).isBlank()) throw new ConsoleManagerException("Error: Seller birthdate is required.");
        if (birthDate.isAfter(LocalDate.now())) throw new ConsoleManagerException("Error: Birthdate cannot be later than the current date.");
        if (baseSalary < 0) throw new ConsoleManagerException("Error: Seller's base salary must be greater than zero.");

        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ConsoleManagerException{
        if (name.isBlank()) throw new ConsoleManagerException("Error: Seller's name is required.");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ConsoleManagerException{
        if (email.isBlank()) throw new ConsoleManagerException("Error: Seller's email is required.");
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthDate(String textDate) throws ConsoleManagerException{
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (textDate.isEmpty()){
            throw new ConsoleManagerException("Error: Seller's birthdate is required.");
        }
        if(LocalDate.parse(textDate,fmt).isAfter(LocalDate.now())){
            throw new ConsoleManagerException("Error: Birthdate cannot be later than the current date.");
        }
        this.birthDate = LocalDate.parse(textDate, fmt);
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) throws ConsoleManagerException{
        if(baseSalary < 0){
            throw new ConsoleManagerException("Error: Seller's base salary must be greater than zero.");
        }
        this.baseSalary = baseSalary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(id, seller.id) && Objects.equals(name, seller.name) && Objects.equals(email, seller.email) && Objects.equals(birthDate, seller.birthDate) && Objects.equals(baseSalary, seller.baseSalary) && Objects.equals(department, seller.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, birthDate, baseSalary, department);
    }

    @Override
    public String toString(){
        return getId() + ", " + getName() + ", " + getEmail() + ", " + getBirthDate() + ", " + String.format("%.2f", getBaseSalary()) + ", Department = [" + getDepartment() + "]";
    }
}
