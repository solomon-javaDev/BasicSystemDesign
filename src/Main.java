/*
 * This is a simple Java program.
 * The program will mimmick a simple system
 * A console-based system that will allow a user to login and logout
 * Users can create accounts and delete accounts with a username and password
 * Users can also update their password
 * Passwords will be hashed before saving
 * The system will store the user's information in a file first then we shall move to a database
 * Users can login and logout by entering their credentials
 * Login attempts shall be recorded and after three unsuccessive attempts the account will be locked
 * The credentials shall be verified by the system against the stored credentials
 * The system will also have a log file that will store all the activities of the system
 * The system will also have a help file that will guide the user on how to use the system
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. ";
    private static final String FILE_PATH = "users.ser";
    private HashMap<String, User> users;

    public Main() {
        users = loadUsers();
        System.out.println("Users loaded successfully ");
        printUsernames();
            }   
     
   public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Main main = new Main();
        System.out.println("Welcome to the system 😒");
        main.Input(scanner);
    }

    /**
     * Load users from file
     * 
     * @return
     */
    private HashMap<String, User> loadUsers() {
        // Load users from file 
        //Deserialize the file
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FILE_PATH)))) {
                @SuppressWarnings("unchecked")
                HashMap<String, User> users = (HashMap<String, User>) ois.readObject();
                return users;
            } catch (Exception e) {
                return new HashMap<String, User>();
            }
        
    }

    private void printUsernames(){
        if (users.isEmpty()) {
    System.out.println("No users present");
        }else{
            for (Entry<String, User> userName: users.entrySet()) {
                System.out.println(userName);
            }
        }
    }
    /**
     * Input
     * 
     * @param scanner
     * @throws Exception
     */
    public void Input(Scanner scanner) throws Exception{

        while (true) {
         System.out.println("Enter 1 to register, 2 to login, 3 to delete, 4 to update password, 5 to exit");
       String input = scanner.nextLine();
    try {
    switch (input.toLowerCase()) {
        case "1":
            registerUser(scanner);
            break;
        case "2":
            loginUser(scanner);
            break;
        case "3":
            deleteUser(scanner);
            break;
        case "4":
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your new password: ");
            String password = scanner.nextLine();
            User user = new User(username, password);
            
            user.updatePassword(user.getPassword());
            break;
        case "help":
            System.out.println("Enter 1 to register, 2 to login, 3 to delete, 4 to update password, 5 to exit");
            break;
        case "exit":
        case "5":
            System.out.println("Goodbye");
            return;
        default:
            System.out.println(INVALID_INPUT_MESSAGE);
            break;
    }
} catch (Exception e) {
    System.out.println("Invalid input. Please enter a number ");
}
      
}
    }

    /**
     * Register a user
     * 
     * @param scanner
     */
    public void registerUser(Scanner scanner) {
        // Register a user
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        User user = new User(username, password);
        saveUser(user);
            }
        
    private void saveUser(User user) {
        // Save a user
        users.put(user.getUsername(), user);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(users);
            System.out.println("User saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
        
    /**
     * Login a user
     * 
     * @param scanner
     */
    public void loginUser(Scanner scanner) {
        // Login a user
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        User user = new User(username, password);
       if(users.values().contains(user)) {
           System.out.println("You have successfully logged in");
       } else {
              System.out.println("Invalid username or password");
       }
    }
    /**
     * Delete a user
     * 
     * @param scanner
     */
    public void deleteUser(Scanner scanner) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        User user = new User(username, password);
        boolean isPresent = users.containsKey(user.getUsername());
    
        if (isPresent) {
            System.out.println("Are you sure you want to delete " + user.getUsername() + "'s account? This process is undoable. (Yes/No)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("Yes")) {
                users.remove(user.getUsername());
                saveUsers();
                System.out.println("User removed! Users list is updated.");
            } else if (confirmation.equalsIgnoreCase("No")) {
                System.out.println("Operation stopped!!");
            } else {
                System.out.println(INVALID_INPUT_MESSAGE);
            }
        } else {
            System.out.println("User not found.");
        }
    }
    
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}