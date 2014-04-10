package PassMe;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testPassword()
    {
       System.out.println( Password.createRandomPassword());
       System.out.println( Password.createRandomPassword(50));
       System.out.println( Password.createRandomPassword(30));
       System.out.println( Password.createRandomPassword(1));
       System.out.println( Password.createRandomPassword(120));
    }

    public void testEncryptionDecryption() throws Exception {
        String passPhrase = "passme";

        for( int i = 0; i < 1000; i++ ) {
            String text = Password.createRandomPassword();
            byte[] encrypted = Password.encrypt(passPhrase, text);
            String decrypted = Password.decrypt(passPhrase, encrypted);
            assertEquals( text, decrypted );
        }

    }

    public void testManagerNew() throws Exception {
        Manager m = new Manager("1");
        m.newItem("google", "google");
        m.newItem("baidu", "baidu");
        m.newItem("distrowatch", "distrowatch", "distrowatch");
        m.showAllItem();
    }

    public void testManagerSearch() throws Exception {
        Manager m = new Manager("1");
        m.searchItem("baidu");
        m.searchItem("google");
        m.searchItem("notFound");
    }

}
