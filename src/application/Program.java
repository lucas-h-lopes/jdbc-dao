package application;

import db.DbException;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ConsoleManagerException;
import util.ConsoleManager;
import util.TextColor;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SellerDao sellerDao = DaoFactory.createSellerDao();
        ConsoleManager console = new ConsoleManager(new Scanner(System.in));

        console.print("Welcome to my " + TextColor.formatToBlue("CRUD") + " project using "+ TextColor.formatToGreen("JDBC") + "!");
        console.printEqualSymbol();
        try {
            console.getGeneralOperation();
        }catch(DbException | ConsoleManagerException e){
            System.out.println(e.getMessage());
        }

    }
}
