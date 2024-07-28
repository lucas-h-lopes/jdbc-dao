package model.dao.implementation;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import util.TextColor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private final Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        String sql = "insert into department (Name) Values (?)";
        PreparedStatement pStatement = null;
        ResultSet id = null;
        try {
            pStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, department.getName());
            pStatement.executeUpdate();
            id = pStatement.getGeneratedKeys();

            if (id.next()) {
                System.out.println(TextColor.formatToGreen("\nSuccessfully") + " inserted!" + TextColor.formatToBlue("\nDepartment: ") + findById(id.getInt(1)) + ".");
                department.setId(id.getInt(1));
            } else {
                throw new DbException("Error: It was not possible to insert Department -> " + department);
            }

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
            DB.closeResultSet(id);
        }
    }

    @Override
    public Department findById(int id) {
        String sql = "select * from department where Id = ?";
        PreparedStatement pStatement = null;
        ResultSet resultSet;
        try {
            pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, id);

            resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                return new Department(resultSet.getInt("Id"), resultSet.getString("Name"));
            }

            throw new DbException("Error: There is no Department with id " + id);
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
        }
    }

    @Override
    public List<Department> findAll() {
        String sql = "select * from department ORDER BY Id";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            List<Department> departments = new ArrayList<>();
            while (resultSet.next()) {
                departments.add(new Department(resultSet.getInt("Id"), resultSet.getString("Name")));
            }
            if (departments.isEmpty()) {
                throw new DbException("Error: There is no Department in the database");
            }
            return departments;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public void update(Department department) {
        String sql = "update department set Name = ? where Id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());

            Department oldDepartment = findById(department.getId());

            System.out.println(TextColor.formatToGreen("\nSuccessfully") + " updated!\n" + TextColor.formatToYellow("Old Department: ") + oldDepartment);
            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new DbException("Error: It was not possible to update Department -> " + oldDepartment);
            }

            System.out.println(TextColor.formatToBlue("Updated Department: ") + findById(department.getId()));

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE from department where Id = ?";
        PreparedStatement pStatement = null;
        try {
            pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, id);

            Department department = findById(id);
            System.out.println(TextColor.formatToGreen("\nSuccessfully") + " deleted!\n" + TextColor.formatToBlue("Department: ") + department + ".");
            pStatement.executeUpdate();


        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
        }
    }

}
