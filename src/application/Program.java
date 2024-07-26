package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = new Seller(null, "LucasTeste", "lucas@gmail.com", LocalDateTime.parse("2024-07-26T20:21"), 50.0, new Department(3, null));
        sellerDao.insert(seller);

    }
}
