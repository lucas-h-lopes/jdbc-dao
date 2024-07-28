package util;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ConsoleManagerException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleManager {
    private final Scanner scanner;
    private final SellerDao sellerDao;
    private final DepartmentDao departmentDao;
    private final String errorMsg = "Error: Invalid operation.";
    private char answer;
    String specificAnswer;

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
        do {
            printGeneralOperations();
            answer = getStringUserInput("Operation: ").trim().charAt(0);

            try {
                switch (answer) {
                    case '1' -> {
                        getSpecificOperation(1);
                        findByIdLogic();
                    }
                    case '2' -> {
                        getSpecificOperation(2);
                        findAllLogic();
                    }
                    case '3' -> findByDepartmentLogic();

                    case '4' -> {
                        getSpecificOperation(4);
                        insertLogic();
                    }
                    case '5' -> {
                        getSpecificOperation(5);
                        updateLogic();
                    }
                    case '6' -> {
                        getSpecificOperation(6);
                        deleteLogic();
                    }
                    case '0' -> print(TextColor.formatToRed("Exiting...\n"));

                    default -> throw new ConsoleManagerException(errorMsg);
                }
            } catch (NumberFormatException | InputMismatchException e) {
                throw new ConsoleManagerException(errorMsg);
            }
        } while (answer != '0');
    }

    private void getSpecificOperation(int i) throws ConsoleManagerException {
        switch (i) {
            case 1 -> printFindById();
            case 2 -> printFindAll();
            case 4 -> printInsert();
            case 5 -> printUpdate();
            case 6 -> printDeleteById();
            default -> throw new ConsoleManagerException(errorMsg);
        }
    }

    private void printUpdateSeller() {
        printEqualSymbol();
        print("Fields available to update ");
        print(TextColor.formatToYellow("(Your changes will be saved to the database when you exit this screen)\n"));
        System.out.println(TextColor.formatToYellow("1.") + " Update Name");
        System.out.println(TextColor.formatToYellow("2.") + " Update Email");
        System.out.println(TextColor.formatToYellow("3.") + " Update BirthDate");
        System.out.println(TextColor.formatToYellow("4.") + " Update BaseSalary");
        System.out.println(TextColor.formatToYellow("5.") + " Update Department");
        System.out.println(TextColor.formatToRed("0.") + " Exit");
        print("Operation: ");
    }

    private void printUpdateDepartment() {
        printEqualSymbol();
        print("Fields available to update ");
        print(TextColor.formatToYellow("(Your changes will be saved to the database when you exit this screen)\n"));
        System.out.println(TextColor.formatToYellow("1.") + " Update Name");
        System.out.println(TextColor.formatToRed("0.") + " Exit");
        print("Operation: ");
    }

    private void updateSeller(Seller seller) throws ConsoleManagerException {
        char answer;
        do {
            printUpdateSeller();
            String strAnswer = scanner.nextLine().trim();
            if (strAnswer.isBlank()) throw new ConsoleManagerException(errorMsg);
            answer = strAnswer.charAt(0);

            switch (answer) {
                case '1' -> {
                    String sellerName = getStringUserInput("New name: ");
                    seller.setName(sellerName);
                    print("Name updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '2' -> {
                    String sellerEmail = getStringUserInput("New email: ");
                    seller.setEmail(sellerEmail);
                    print("Email updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '3' -> {
                    String sellerBirthDate = getStringUserInput("New birthdate (dd/MM/yyyy): ");
                    seller.setBirthDate(sellerBirthDate);
                    print("Birthdate updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '4' -> {
                    String sellerSalary = getStringUserInput("New base salary: ");
                    seller.setBaseSalary(Double.parseDouble(sellerSalary));
                    print("Base salary updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '5' -> {
                    seller.setDepartment(departmentDao.findById(Integer.parseInt(getStringUserInput("New Department Id: "))));
                    print("Department updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '0' -> print(TextColor.formatToRed("Exiting...\n"));
                default -> throw new ConsoleManagerException(errorMsg);
            }
        } while (answer != '0');
    }

    private void updateDepartment(Department department) throws ConsoleManagerException {
        char answer;
        do {
            printUpdateDepartment();
            String strAnswer = scanner.nextLine().trim();
            if (strAnswer.isBlank()) throw new ConsoleManagerException(errorMsg);
            answer = strAnswer.charAt(0);

            switch (answer) {
                case '1' -> {
                    String depName = getStringUserInput("New name: ");
                    department.setName(depName);
                    print("Name updated " + TextColor.formatToGreen("successfully!\n"));
                }
                case '0' -> print(TextColor.formatToRed("Exiting...\n"));
                default -> throw new ConsoleManagerException(errorMsg);
            }
        } while (answer != '0');
    }

    public String getStringUserInput(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    private Seller createSeller() throws ConsoleManagerException {
        String name = getStringUserInput("Seller name: ");
        String email = getStringUserInput("Email: ");
        String textDate = getStringUserInput("Birth date (dd/MM/yyyy): ");
        LocalDate parsedDate = LocalDate.parse(textDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String baseSalary = getStringUserInput("Base salary: ");

        return new Seller(null, name, email, parsedDate, Double.parseDouble(baseSalary), departmentDao.findById(Integer.parseInt(getStringUserInput("Department Id: "))));
    }

    public Department createDepartment() throws ConsoleManagerException {
        String depName = getStringUserInput("Department name: ");
        return new Department(null, depName);
    }

    public void closeScanner() {
        scanner.close();
    }

    public char getAnswer() {
        return answer;
    }

    private void findByIdLogic() {
        specificAnswer = getStringUserInput("Operation: ").trim();
        if (specificAnswer.equals("1")) {
            specificAnswer = getStringUserInput("Id: ");
            Seller seller = sellerDao.findById(Integer.parseInt(specificAnswer));
            System.out.println(TextColor.formatToBlue("\nSeller found: ") + seller);
        } else if (specificAnswer.equals("2")) {
            specificAnswer = getStringUserInput("Id: ");
            Department department = departmentDao.findById(Integer.parseInt(specificAnswer));
            System.out.println(TextColor.formatToBlue("\nDepartment found: ") + department);
        } else {
            throw new ConsoleManagerException(errorMsg);
        }
    }

    private void deleteLogic() {
        specificAnswer = getStringUserInput("Operation: ");
        if (specificAnswer.equals("1")) {
            specificAnswer = getStringUserInput("Seller Id: ");
            sellerDao.deleteById(Integer.parseInt(specificAnswer));
        } else if (specificAnswer.equals("2")) {
            specificAnswer = getStringUserInput("Department Id: ");
            departmentDao.delete(Integer.parseInt(specificAnswer));
        } else {
            throw new ConsoleManagerException(errorMsg);
        }
    }

    private void updateLogic() {
        specificAnswer = getStringUserInput("Operation: ");

        if (specificAnswer.equals("1")) {
            specificAnswer = getStringUserInput("Seller Id: ");
            Seller seller = sellerDao.findById(Integer.parseInt(specificAnswer));
            updateSeller(seller);
            if (sellerDao.findById(Integer.parseInt(specificAnswer)).equals(seller)) {
                System.out.println(TextColor.formatToYellow("At least one field must be modified to perform the update."));
            } else {
                sellerDao.update(seller);
            }
        } else if (specificAnswer.equals("2")) {
            specificAnswer = getStringUserInput("Department Id: ");
            Department department = departmentDao.findById(Integer.parseInt(specificAnswer));
            updateDepartment(department);
            if (department.equals(departmentDao.findById(Integer.parseInt(specificAnswer)))) {
                System.out.println(TextColor.formatToYellow("At least one field must be modified to perform the update."));
            } else {
                departmentDao.update(department);
            }
        } else {
            throw new ConsoleManagerException(errorMsg);
        }
    }

    private void insertLogic() {
        specificAnswer = getStringUserInput("Operation: ");
        if (specificAnswer.equals("1")) {
            sellerDao.insert(createSeller());
        } else if (specificAnswer.equals("2")) {
            departmentDao.insert(createDepartment());
        } else {
            throw new ConsoleManagerException(errorMsg);
        }
    }

    private void findByDepartmentLogic() {
        Department department = departmentDao.findById(Integer.parseInt(getStringUserInput("Department Id: ")));
        System.out.println("\nAll Sellers in the " + TextColor.formatToBlue(department.getName()) + " department");
        sellerDao.findByDepartment(department).forEach(System.out::println);
    }

    private void findAllLogic() {
        specificAnswer = getStringUserInput("Operation: ");
        if (specificAnswer.equals("1")) {
            printEqualSymbol();
            print("Listing all Sellers\n");
            sellerDao.findAll().forEach(System.out::println);
        } else if (specificAnswer.equals("2")) {
            printEqualSymbol();
            print("Listing all Departments\n");
            departmentDao.findAll().forEach(System.out::println);
        } else {
            throw new ConsoleManagerException(errorMsg);
        }
    }
    public void printEqualSymbol() {
        System.out.println("\n================");
    }

    private void printGeneralOperations() {
        printEqualSymbol();
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
