package main.java.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * Encryption: To encrypt a file, you need to generate a key
 * create a Cipher object, and use a CipherOutputStream to write the encrypted data to a file. 
 * The key is used to initialize the cipher, 
 * and the CipherOutputStream encrypts the data before writing it to the file.
 */
public class FileEncryptor {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_FILE = "secret.key";

    //Generating a secret key
    public static void generateKey() throws Exception {
        KeyGenerator KeyGenerator = javax.crypto.KeyGenerator.getInstance(ALGORITHM);
        KeyGenerator.init(128); // key length 128 bits
        SecretKey key = KeyGenerator.generateKey();

        try(FileOutputStream fos = new FileOutputStream(SECRET_KEY_FILE)){
            fos.write(key.getEncoded());
        }
    }

    private static SecretKeySpec getSecretKey() throws Exception{
        byte[] keyBytes = new byte[16];
        try(FileInputStream fis = new FileInputStream(SECRET_KEY_FILE)){
            fis.read(keyBytes);
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static void encryptFile(String inputFile, String outputFile) throws Exception {
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try(
            FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
        ){
            byte[] inputBytes = fis.readAllBytes();
            byte[] outputBytes = cipher.doFinal(inputBytes);
            fos.write(outputBytes);
        }
    }

    /*
     * Decryption: Decryption involves reading the encrypted data from a file,
     * using a CipherInputStream to decrypt the data,  
     * and then writing the decrypted content to a new file.
     * The same key used for encryption is required for decryption.
     */
    public static void decrypt(String inputFile, String outputFile) throws Exception {
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try(
            FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
        ){
            byte[] inputBytes = fis.readAllBytes();
            byte[] outputBytes = cipher.doFinal(inputBytes);
            fos.write(outputBytes);
        }
        
        
    }
}


