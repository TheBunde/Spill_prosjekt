package user;

import java.math.*;
import java.security.*;
import java.util.Random;

/**
 * The methods in this class are used to encrypt passwords
 * and to check if a password corresponds to a encrypted one.
 * SHA256 is the hash function used in this class.
 *
 * @author magnubau
 */
public class Password {

    private final Random rand = new SecureRandom();

    /**
     *used to hash a password with a salt.
     *
     * @param password      a password.
     * @param salt          a salt.
     * @return              a haashed password.
     */
    public String createPassword(String password, byte[] salt){
        String hashedPassword = getHash(password, salt);
        return hashedPassword;
    }

    /**
     * hashes a password and checks if it is equal to
     * another hased password.
     *
     * @param password          a password
     * @param salt              a salt
     * @param hashedPassword    a hashed password
     * @return
     */
    public boolean checkPassword(String password, byte[] salt, String hashedPassword){
        if(hashedPassword.equals(getHash(password, salt))){
            return true;
        }
        return false;
    }

    /**
     * Creates a list of random bytes.
     *
     * @return      list of random bytes.
     */
    public byte[] getSalt(){
        byte[] salt = new byte[16];
        rand.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password with a salt.
     *
     * @param password      a password.
     * @param salt          a salt.
     * @return              a hased passwoord.
     */
    public String getHash(String password, byte[] salt){
        try{
            /* uses SHA-256 function to hash password */
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            /* crates salt and adds it to MessageDigest md */
            md.reset();
            md.update(salt);
            /* hash the password */
            byte[] messageDigest = md.digest(password.getBytes());
            /* converts into integer */
            BigInteger number = new BigInteger(1, messageDigest);
            /* converts into hex */
            String hashText = number.toString(16);

            while(hashText.length() < 32){
                hashText = "0" + hashText;
            }
            return hashText;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
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

