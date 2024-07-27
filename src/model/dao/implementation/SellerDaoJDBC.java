package model.dao.implementation;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.time.LocalDate;
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
        String sql = "insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?,?,?,?,?)";
        PreparedStatement pStatement = null;
        try {
            pStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, seller.getName());
            pStatement.setString(2, seller.getEmail());
            pStatement.setObject(3, seller.getBirthDate());
            pStatement.setDouble(4, seller.getBaseSalary());
            pStatement.setInt(5, seller.getDepartment().getId());

            pStatement.executeUpdate();
            ResultSet id = pStatement.getGeneratedKeys();
            if (!id.next()) {
                throw new DbException("Error: It was not possible to insert Seller -> " + seller + ".");
            } else {
                seller.setId(id.getInt (1));
                System.out.println("Successfully inserted!\nSeller -> " + findById(id.getInt(1)) + ".");
            }
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        String sql = "update seller set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? where Id = ?";
        PreparedStatement pStatement = null;
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, seller.getName());
            pStatement.setString(2, seller.getEmail());
            pStatement.setObject(3, seller.getBirthDate());
            pStatement.setDouble(4, seller.getBaseSalary());
            pStatement.setInt(5, seller.getDepartment().getId());
            pStatement.setInt(6, seller.getId());

            Seller tempSeller = findById(seller.getId());
            if(tempSeller == null){
                throw new DbException("Error: There is no Seller with id " + seller.getId());
            }
            System.out.println("Successfully updated.\nOld Seller -> "  + tempSeller);
            int affected = pStatement.executeUpdate();

            if(affected == 0){
                throw new DbException("Error: It was not possible to update Seller -> " + seller);
            }

            System.out.println("Updated Seller -> " + findById(seller.getId()));

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE from seller where Id = ?";
        PreparedStatement pStatement = null;
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, id);

            Seller seller = findById(id);
            if (seller == null) {
                throw new DbException("Error: There is no Seller with id " + id);
            }
            System.out.println("Successfully deleted!\nSeller -> " + findById(id) + ".");
            pStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
        }
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
            throw new DbException("Error: There is no Seller with id " + id);

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(pStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findAll() {
        String sql = "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id ORDER BY Name";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while (resultSet.next()) {
                Department department = map.get(resultSet.getInt("DepartmentId"));
                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), department);
                }
                sellers.add(instantiateSeller(resultSet, department));
            }
            if (sellers.isEmpty()) {
                throw new DbException("Error: There is no Seller in the database");
            }
            return sellers;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }

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
                resultSet.getObject("BirthDate", LocalDate.class),
                resultSet.getDouble("BaseSalary"),
                department);
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        return new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
    }
}
