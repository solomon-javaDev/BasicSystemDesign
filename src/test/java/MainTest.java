package test.java;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.Main;

public class MainTest {
    @SuppressWarnings("unused")
    private Main main;

    @BeforeEach
    void setUp(){
        main = new Main();
    }

    @Test
    void testRegister(){
        Scanner scanner = new Scanner("Bakka\nSolomon\n");
        main.registerUser(scanner);

        assertTrue(main.users.containsKey("Bakka"),"User should be registered");

        assertNotEquals("Solomon", main.users.get("Bakka").getPassword(),"Password should be hashed");
    }

    
}
