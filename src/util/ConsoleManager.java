package util;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ConsoleManagerException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleManager {
    private final Scanner scanner;
    private final SellerDao sellerDao;
    private final DepartmentDao departmentDao;

    public ConsoleManager(Scanner scanner) {
        this.scanner = scanner;
        sellerDao = DaoFactory.createSellerDao();
        departmentDao = DaoFactory.createDepartmentDao();
        Locale.setDefault(Locale.US);
    }

    public void print(String text) {
        System.out.print(text);
    }

    public void getGeneralOperation() throws ConsoleManagerException {
        printGeneralOperations();
        char answer = getStringUserInput("Operation: ").trim().charAt(0);
        int specificAnswer;
        switch (answer) {

            case '1' -> {
                getSpecificOperation(1);
                specificAnswer = getIntUserInput("Operation: ");
                if (specificAnswer == 1) {
                    specificAnswer = getIntUserInput("Id: ");
                    System.out.println(sellerDao.findById(specificAnswer));
                } else if (specificAnswer == 2) {
                    specificAnswer = getIntUserInput("Id: ");
                    System.out.println(departmentDao.findById(specificAnswer));
                } else {
                    throw new ConsoleManagerException("Error: Invalid operation.");
                }
            }
            case '2' -> {
                getSpecificOperation(2);
                specificAnswer = getIntUserInput("Operation: ");
                if (specificAnswer == 1) {
                    printEqualSymbol();
                    print("Listing all Sellers\n");
                    sellerDao.findAll().forEach(System.out::println);
                } else if (specificAnswer == 2) {
                    printEqualSymbol();
                    print("Listing all Departments\n");
                    departmentDao.findAll().forEach(System.out::println);
                }
            }
            case '3' ->
                    sellerDao.findByDepartment(departmentDao.findById(getIntUserInput("Department Id: "))).forEach(System.out::println);
            case '4' -> {
                getSpecificOperation(4);
                specificAnswer = getIntUserInput("Operation: ");
                if (specificAnswer == 1) {
                    sellerDao.insert(createSeller());
                } else if (specificAnswer == 2) {
                    departmentDao.insert(createDepartment());
                } else {
                    throw new ConsoleManagerException("Error: Invalid operation.");
                }
            }
            case '5' -> {
                getSpecificOperation(5);
                specificAnswer = getIntUserInput("Operation: ");

                if (specificAnswer == 1) {
                    specificAnswer = getIntUserInput("Seller Id: ");
                    Seller seller = sellerDao.findById(specificAnswer);
                    updateSeller(seller);
                    sellerDao.update(seller);
                } else if (specificAnswer == 2) {
                    specificAnswer = getIntUserInput("Department Id: ");
                    Department department = departmentDao.findById(specificAnswer);
                    updateDepartment(department);
                    departmentDao.update(department);
                } else {
                    throw new ConsoleManagerException("Error: Invalid operation.");
                }
            }
            case '6' -> {
                getSpecificOperation(6);
                specificAnswer = getIntUserInput("Operation: ");
                if(specificAnswer == 1){
                    specificAnswer = getIntUserInput("Seller Id: ");
                    sellerDao.deleteById(specificAnswer);
                }else if(specificAnswer == 2){
                    specificAnswer = getIntUserInput("Department Id: ");
                    departmentDao.delete(specificAnswer);
                }else{
                    throw new ConsoleManagerException("Error: Invalid operation");
                }
            }
        }
    }

    private void getSpecificOperation(int i) throws ConsoleManagerException {
        switch (i) {
            case 1 -> printFindById();
            case 2 -> printFindAll();
            case 4 -> printInsert();
            case 5 -> printUpdate();
            case 6 -> printDeleteById();
            default -> throw new ConsoleManagerException("Error: Invalid operation.");
        }
    }

    private Seller updateSeller(Seller seller) throws ConsoleManagerException {
        char answer;
        do {
            printEqualSymbol();
            print("Fields available to update\n");
            System.out.println(TextColor.formatToYellow("1.") + " Update Name");
            System.out.println(TextColor.formatToYellow("2.") + " Update Email");
            System.out.println(TextColor.formatToYellow("3.") + " Update BirthDate");
            System.out.println(TextColor.formatToYellow("4.") + " Update BaseSalary");
            System.out.println(TextColor.formatToYellow("5.") + " Update Department");
            System.out.println(TextColor.formatToRed("0.") + " Exit");
            print("Operation: ");
            answer = scanner.nextLine().trim().charAt(0);

            switch (answer) {
                case '1' -> {
                    seller.setName(getStringUserInput("New name: "));
                    print("Name updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '2' -> {
                    seller.setEmail(getStringUserInput("New email: "));
                    print("Email updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '3' -> {
                    seller.setBirthDate(LocalDate.parse(getStringUserInput("New birthdate (dd/MM/yyyy): "), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    print("Birthdate updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '4' -> {
                    seller.setBaseSalary(getDoubleUserInput("New base salary: "));
                    print("Base salary updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '5' -> {
                    seller.setDepartment(departmentDao.findById(getIntUserInput("New Department Id: ")));
                    print("Department updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '0' -> print(TextColor.formatToRed("Exiting...\n"));
                default -> throw new ConsoleManagerException("Error: Invalid operation.");
            }
        } while (answer != '0');
        return seller;
    }

    private Department updateDepartment(Department department) throws ConsoleManagerException{
        char answer;
        do {
            printEqualSymbol();
            print("Fields available to update\n");
            System.out.println(TextColor.formatToYellow("1.") + " Update Name");
            System.out.println(TextColor.formatToRed("0.") + " Exit");
            print("Operation: ");
            answer = scanner.nextLine().trim().charAt(0);

            switch (answer) {
                case '1' -> {
                    department.setName(getStringUserInput("New name: "));
                    print("Name updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '0' -> print(TextColor.formatToRed("Exiting...\n"));
                default -> throw new ConsoleManagerException("Error: Invalid operation.");
            }
        } while (answer != '0');
        return department;
    }

    public String getStringUserInput(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    public int getIntUserInput(String prompt) {
        print(prompt);
        int answer = scanner.nextInt();
        scanner.nextLine();
        return answer;
    }

    public Seller createSeller() {
        String name = getStringUserInput("Seller name: ");
        String email = getStringUserInput("Email: ");
        String textDate = getStringUserInput("Birth date (dd/MM/yyyy): ");
        double baseSalary = getDoubleUserInput("Base salary: ");
        return new Seller(null, name, email, LocalDate.parse(textDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")), baseSalary, departmentDao.findById(getIntUserInput("Department Id: ")));
    }

    public Department createDepartment() {
        return new Department(null, getStringUserInput("Department name: "));
    }

    public double getDoubleUserInput(String prompt) {
        print(prompt);
        double answer = scanner.nextDouble();
        scanner.nextLine();
        return answer;
    }

    public void closeScanner() {
        scanner.close();
    }

    public void printEqualSymbol() {
        System.out.println("\n================");
    }

    private void printGeneralOperations() {
        System.out.println("Operations");
        System.out.println(TextColor.formatToYellow("1.") + " Find by Id");
        System.out.println(TextColor.formatToYellow("2.") + " Find all");
        System.out.println(TextColor.formatToYellow("3.") + " Find Seller by Department");
        System.out.println(TextColor.formatToYellow("4.") + " Insert");
        System.out.println(TextColor.formatToYellow("5.") + " Update");
        System.out.println(TextColor.formatToYellow("6.") + " Delete by Id");
        System.out.println(TextColor.formatToYellow("0.") + TextColor.formatToRed(" Exit"));
    }

    private void printFindById() {
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Find Seller by Id");
        System.out.println(TextColor.formatToYellow("2.") + " Find Department by Id");
    }

    private void printFindAll() {
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Find all Seller");
        System.out.println(TextColor.formatToYellow("2.") + " Find all Department");
    }

    private void printInsert() {
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Insert Seller");
        System.out.println(TextColor.formatToYellow("2.") + " Insert Department");
    }

    private void printUpdate() {
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Update Seller");
        System.out.println(TextColor.formatToYellow("2.") + " Update Department");
    }

    private void printDeleteById() {
        printEqualSymbol();
        System.out.println("Specific operations");
        System.out.println(TextColor.formatToYellow("1.") + " Delete Seller by Id");
        System.out.println(TextColor.formatToYellow("2.") + " Delete Department by Id");
    }

}
