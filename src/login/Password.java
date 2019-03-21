package login;

import java.math.*;
import java.security.*;
import java.util.Random;

public class Password {

    private final Random rand = new SecureRandom();

    // will be switched to boolean or void when connected to database
    public String createPassword(String password, byte[] salt){
        String hashedPassword = getHash(password, salt);
        return hashedPassword;
    }

    // will be remade when connected to database
    public boolean checkPassword(String password, byte[] salt, String hashedPassword){
        if(hashedPassword.equals(getHash(password, salt))){
            return true;
        }
        return false;
    }
    //GetSalt()
    public byte[] getSalt(){
        byte[] salt = new byte[16];
        rand.nextBytes(salt);
        return salt;
    }

    public String getHash(String password, byte[] salt){
        try{
            // uses hash method SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // crates salt and adds it to MessageDigest md
            md.reset();
            md.update(salt);
            // hash the password with salt
            byte[] messageDigest = md.digest(password.getBytes());
            // converts into integer
            BigInteger number = new BigInteger(1, messageDigest);
            // converts into hex
            String hashText = number.toString(16);

            while(hashText.length() < 32){
                hashText = "0" + hashText;
            }
            return hashText;
        }catch (NoSuchAlgorithmException e){
            System.out.println("feil: " + e);
        }
        return null;
    }

    public static void main(String[] args) {
        Password password = new Password();
        byte[] salt1 = password.getSalt();
        byte[] salt2 = password.getSalt();
        String input = "hei";

        String encrypted1 = password.createPassword(input, salt1);
        String encrypted2 = password.createPassword(input, salt2);

        System.out.println(encrypted1);
        System.out.println(encrypted2);

        if(password.checkPassword(input, salt1, encrypted1)){
            System.out.println("correct password and salt for this user");
        }
        if(!(password.checkPassword(input, salt2, encrypted1))){
            System.out.println("Wrong salt for this user");
        }
    }
}

