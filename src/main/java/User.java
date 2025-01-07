package main.java;

import java.io.Serializable;

public class User implements Serializable{
    private String username;
    private String password;
    private boolean isLocked;
    private int attempts;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
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

    
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public int getAttempts() {
        return attempts;
    }

    public void resetAttempts(int attempts) {
        this.attempts = 0;
    }

    public void incrementAttempts(){
        this.attempts++;
        if (this.attempts >= 3){
            isLocked = true;
        }
    }

   
}
