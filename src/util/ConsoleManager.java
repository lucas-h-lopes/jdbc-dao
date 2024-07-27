package util;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.exceptions.ConsoleManagerException;

import java.util.Scanner;

public class ConsoleManager {
    private final Scanner scanner;
    private SellerDao sellerDao;

    public ConsoleManager(Scanner scanner){
        this.scanner = scanner;
        sellerDao = DaoFactory.createSellerDao();
    }

    public void print(String text){
        System.out.print(text);
    }

    private void printGeneralOperations(){
        System.out.println("Operations");
        System.out.println(TextColor.formatToYellow("1.") + " Find by Id");
        System.out.println(TextColor.formatToYellow("2.") + " Find all");
        System.out.println(TextColor.formatToYellow("3.") + " Find Seller by Department");
        System.out.println(TextColor.formatToYellow("4.") + " Insert");
        System.out.println(TextColor.formatToYellow("5.") + " Update");
        System.out.println(TextColor.formatToYellow("6.") + " Delete by Id");
    }

    private void printFindById(){
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Find Seller by Id");
        System.out.println(TextColor.formatToYellow("2.") + " Find Department by Id");
    }

    private void printFindAll(){
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Find all Seller");
        System.out.println(TextColor.formatToYellow("2.") + " Find all Department");
    }

    private void printInsert(){
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Insert Seller");
        System.out.println(TextColor.formatToYellow("2.") + " Insert Department");
    }

    private void printUpdate(){
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Update Seller");
        System.out.println(TextColor.formatToYellow("2.") + " Update Department");
    }

    private void printDeleteById(){
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Delete Seller by Id");
        System.out.println(TextColor.formatToYellow("2.") + " Delete Department by Id");
    }

    public void getGeneralOperation() throws ConsoleManagerException{
        printGeneralOperations();
        char answer = getStringUserInput("Operation: ").trim().charAt(0);
        switch(answer){
            case '1' -> {
                getSpecificOperation(1);
                switch(getIntUserInput("Operation: ")){
                    case 1 -> System.out.println(sellerDao.findById(getIntUserInput("Id: ")));
                }
            }
            case '2' -> getSpecificOperation(2);
            case '3' -> System.out.println(sellerDao.findByDepartment(getDepartment()));
        }
    }

    private void getSpecificOperation(int i) throws ConsoleManagerException{
        switch(i){
            case 1 -> printFindById();
            case 2 -> printFindAll();
            case 4 -> printInsert();
            case 5 -> printUpdate();
            case 6 -> printDeleteById();
            default -> throw new ConsoleManagerException("Error: Invalid operation.");
        }
    }

    public String getStringUserInput(String prompt){
        print(prompt);
        return scanner.nextLine();
    }

    public int getIntUserInput(String prompt){
        print(prompt);
        int answer = scanner.nextInt();
        scanner.nextLine();
        return answer;
    }

    public double getDoubleUserInput(String prompt){
        print(prompt);
        double answer = scanner.nextDouble();
        scanner.nextLine();
        return answer;
    }

    public void closeScanner(){
        scanner.close();
    }
    public void printEqualSymbol(){
        System.out.println("\n================");
    }

    private Department getDepartment(){
        System.out.print("Department Id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Department Name: ");
        String name = scanner.nextLine();
        return new Department(id, name);
    }

}
