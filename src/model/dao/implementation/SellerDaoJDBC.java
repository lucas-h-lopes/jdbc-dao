package model.dao.implementation;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void delete(Seller seller) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement pStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?";
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, id);
            resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                return instantiateSeller(resultSet, new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName")));
            }
            throw new DbException("Error: There is no Seller with Id = " + id);

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        String sql = "SELECT seller.*,department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE DepartmentId = ? " +
                "ORDER BY Name";
        ResultSet resultSet = null;
        PreparedStatement pStatement = null;
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, department.getId());
            resultSet = pStatement.executeQuery();
            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while (resultSet.next()) {
                Department depart = map.get(resultSet.getInt("DepartmentId"));
                if (depart == null) {
                    depart = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), depart);
                }
                sellers.add(instantiateSeller(resultSet, depart));
            }
            return sellers;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
            DB.closeResultSet(resultSet);
        }
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        return new Seller(resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getObject("BirthDate", LocalDateTime.class),
                resultSet.getDouble("BaseSalary"),
                department);
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        return new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
    }
}
