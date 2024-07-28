package application;

import db.DB;
import db.DbException;
import model.exceptions.ConsoleManagerException;
import util.ConsoleManager;
import util.TextColor;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {


        ConsoleManager console = new ConsoleManager(new Scanner(System.in));

        console.print("Welcome to my " + TextColor.formatToBlue("CRUD") + " project using " + TextColor.formatToGreen("JDBC") + "!");
        do {

            try {
                console.getGeneralOperation();
            } catch (DbException | ConsoleManagerException e) {
                System.out.println(e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println(TextColor.formatToRed("Error: Invalid date format, failed to parse. " + e.getMessage()));
            }
        } while (console.getAnswer() != '0');
        DB.closeConnection();
        console.closeScanner();
    }
}
