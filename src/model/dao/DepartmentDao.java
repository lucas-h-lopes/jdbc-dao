package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {
    void insert(Department department);

    Department findById(int id);

    List<Department> findAll();

    void update(Department department);

    void delete(int id);
}
