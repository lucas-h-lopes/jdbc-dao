package model.entities;
import model.exceptions.ConsoleManagerException;
import java.util.Objects;

public class Department {
    private Integer id;
    private String name;

    public Department(Integer id, String name){
        if(name.isBlank()) throw new ConsoleManagerException("Error: Department name is required.");
        this.id = id;
        this.name = name;
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
        if (name.isBlank()) throw new ConsoleManagerException("Error: Department's name is required.");
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString(){
        return getId() + ", " + getName();
    }
}
