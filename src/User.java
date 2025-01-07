import java.io.Serializable;

public class User implements Serializable{
    private String username;
    private String password;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void saveUser() {
        System.out.println("User saved as " + this.username);
    }

    public void loginUser() {
    }

   
    public void deleteUser(){

    }

    public void updatePassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

   
    
}
