package PassMe;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Manager {
    private final String dataFile;
    private final String passPhrase;
    private Database db;

    public Manager(String passPhrase) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        this(passPhrase, System.getProperty("user.home") + "/.passme");
    }

    public Manager(String passPhrase, String dataDir) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        this.passPhrase = passPhrase;
        this.dataFile = dataDir + "/data.pm";
        File f = new File( this.dataFile );
        if ( f.exists() ) {
            System.out.println("Reading data from " + f.getPath());
            readData();
            this.db.auth(passPhrase);
        } else {
            try {
                File dir = new File(dataDir);
                if ( !dir.exists() ) {
                    if ( ! dir.mkdirs() ) throw new IOException();
                }
                if ( ! f.createNewFile() ) throw new IOException();
            } catch (IOException e) {
                System.err.println("Cannot create data file " + f.getPath());
                e.printStackTrace();
                System.exit(1);
            }
            this.db = new Database(passPhrase, new ArrayList<Database.Item>());
            System.out.println("Saving data into " + f.getPath());
            saveData();
        }
    }

    public void newItem(String host, String user, String password) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IOException {
        Database.Item newItem = new Database.Item( host, user, Password.encrypt(this.passPhrase, password) );
        for ( Database.Item item: this.db.items ) {
            if ( item.equals(newItem) && item.exp == null ) item.exp = new Date();
        }
        this.db.items.add(newItem);
        saveData();
        printHead();
        showItem(newItem);
    }

    public void newItem(String host, String user) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        String password = Password.createRandomPassword();
        newItem(host, user, password);
    }

    public void searchItem(String host) throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        printHead();
        for ( Database.Item item: this.db.items ) {
            if ( item.host.contains(host) && item.exp == null ) showItem(item);
        }
    }

    private static String dateFormat(Date d) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if ( d != null ) {
            return sd.format(d);
        } else {
            return "";
        }
    }


    public void showItem(Database.Item item) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException {
        System.out.println(String.format("%-20s\t%-20s\t%-20s\t%-20s\t%-20s", item.host, item.user, Password.decrypt(this.passPhrase, item.password), dateFormat(item.cre), dateFormat(item.exp)));
    }

    public void showAllItem() throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        printHead();
        for(Database.Item item: this.db.items) showItem(item);
    }

    private void printHead(){
        System.out.println(String.format("%-20s\t%-20s\t%-20s\t%-20s\t%-20s", "Host", "User", "Password", "Creation Date", "Expiration Date"));
        System.out.println("=====================================================================================================================");
    }

    public void dump() throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException {
        for ( Database.Item item: this.db.items ) {
            if ( item.exp == null ) {
                System.out.println("new " + item.host + " " + item.user + " " + Password.decrypt(this.passPhrase, item.password));
            }
        }
    }


    private void readData() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream( this.dataFile ));
        this.db = (Database) ois.readObject();
        ois.close();
    }

    private void saveData() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.dataFile ));
        oos.writeObject(this.db);
        oos.close();
    }

}
