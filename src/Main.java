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
    private static final String HELP_MESSAGE = "Enter 1 to register, 2 to login, 3 to delete, 4 to update user details, 5 to exit";
    private static Logger log;


    public Main() {
        users = loadUsers();
        log = new Logger();
        printUsernames();
            }   
     
   public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Main main = new Main();
        System.out.println("Welcome to the system ");
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
                    log.logInfo("Users have been loaded! "+users.toString());
                    return users;
                } catch (Exception e) {
                    return new HashMap<String, User>();
                }
            
        }

        private void printUsernames(){
            if (users.isEmpty()) {
        System.out.println("No users present");
        log.logInfo("While loading! No users present initially");
            }else{
                log.logInfo("Printing to standard output the users present! ");
                for (Entry<String, User> userName: users.entrySet()) {
                    System.out.println(userName);
                    log.logInfo(userName.toString());
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
         System.out.println("At your service now! ");
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
            updateUser(scanner);
        case "help":
            System.out.println(HELP_MESSAGE);
            log.logInfo("The user asked for help!");
            break;
        case "exit":
        case "5":
            System.out.println("Goodbye");
            log.logInfo("The user exited the program");
            return;
        default:
            System.out.println(INVALID_INPUT_MESSAGE);
            log.logWarning("The user input an invalid input");
            break;
    }
} catch (Exception e) {
    System.out.println("Invalid input. Please enter a number ");
    log.logError(e.getMessage()+ " Invalid input. Please enter a number ");
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
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        users.put(user.getUsername(), user);
        log.logInfo("The system has registered a new user! "+user.getUsername());
        saveUsers();

            }
        
    // private void saveUser(User user) {
    //     // Save a user
    //     users.put(user.getUsername(), user);
    //     try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
    //         oos.writeObject(users);
    //         saveUsers();
    //         System.out.println("User saved successfully");
    //         log.logInfo("The system has saved a user! ");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         log.logInfo("The system has failed to save a user! ");

    //     }
    //  }
     
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
            log.logInfo("Object Output Stream has saved the list of the System users!!");
        } catch (IOException e) {
            e.printStackTrace();
            log.logError("An error occurred while saving the list of users! ");

        }
    }
        
    /**
     * Login a user
     * 
     * @param scanner
     */
    public void loginUser(Scanner scanner) {
    int attempts = 0;
    System.out.println("Enter your username: ");
    String username = scanner.nextLine();
    System.out.println("Enter your password: ");
    String password = scanner.nextLine();
    
    while (attempts < 3) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("You have successfully logged in");
            log.logInfo("A user "+user.getUsername()+ " has logged in after "+attempts+" attempts");
            return;
        } else {
            attempts++;
            if (attempts < 3) {
                System.out.println("Invalid username or password. " + (3 - attempts) + " attempts remaining.");
                System.out.println("Enter your username: ");
                username = scanner.nextLine();
                System.out.println("Enter your password: ");
                password = scanner.nextLine();
            } else {
                System.out.println("Invalid username or password. No attempts remaining.");
                log.logInfo("A user's attempts are done. Account locked! "+user.getUsername());
            }
        }
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
                log.logWarning("A user has been removed! "+user.getUsername());
                users.remove(user.getUsername());
                saveUsers();
                System.out.println("User removed! Users list is updated.");
            } else if (confirmation.equalsIgnoreCase("No")) {
                System.out.println("Operation stopped!!");
                log.logWarning("A user has stopped operation to delete an account "+user.getUsername());
            } else {
                System.out.println(INVALID_INPUT_MESSAGE);
                log.logError("A user tried to delete a non existent account! ");

            }
        } else {
            System.out.println("User not found.");
            log.logError("No user was found for account");
        }
    }
    
    public void updateUser(Scanner scanner){

    }

}