package PassMe;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

class Password {
    public static String createRandomPassword(int length) {
        if (length < 8  ) length = 8;
        if (length > 100  ) length = 100;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while ( i < length ) {
            int a = random.nextInt(127);
            if ( a >= 33 ) {
                sb.append((char)a);
                i++;
            }
        }
        return sb.toString();
    }

    public static String createRandomPassword(){
        return createRandomPassword(10);
    }

    public static byte[] encrypt(String passPhrase, String plaintext) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Key key = getKey(passPhrase);
        Cipher c = Cipher.getInstance("AES");
        c.init( Cipher.ENCRYPT_MODE, key );
        return c.doFinal(plaintext.getBytes());
    }

    public static String decrypt( String passPhrase, byte[] cipherText ) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Key key = getKey(passPhrase);
        Cipher c = Cipher.getInstance("AES");
        c.init( Cipher.DECRYPT_MODE, key );
        return new String( c.doFinal(cipherText) );
    }

    private static Key getKey(String passPhrase) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(passPhrase.getBytes());
        return new SecretKeySpec(md.digest(), 0, 16, "AES");
    }

}
