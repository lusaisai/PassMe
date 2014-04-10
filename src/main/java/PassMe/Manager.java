package PassMe;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Manager {
    private final String dataDir;
    private final String dataFile;
    private final String passPhrase;
    private List<Item> data;

    private static class Item implements Serializable {
        final String host;
        final String user;
        final Date cre;
        final byte[] password;
        Date exp;

        Item(String host, String user, byte[] password) {
            this.host = host;
            this.user = user;
            this.password = password;
            this.cre = new Date();
        }

        @Override
        public boolean equals(Object obj) {
            if (!( obj instanceof Item )) return false;
            Item item = (Item)obj;
            return item.host.equals(host) && item.user.equals(user);
        }

        @Override
        public int hashCode() {
            return user.hashCode() + host.hashCode();
        }
    }

    public Manager(String passPhrase) throws IOException, ClassNotFoundException {
        this.passPhrase = passPhrase;
        this.dataDir = System.getProperty("user.home") + "/.passme";
        this.dataFile = this.dataDir + "/data.pm";
        File f = new File( this.dataFile );
        if ( f.exists() ) {
            readData();
        } else {
            try {
                File dir = new File(this.dataDir);
                if ( !dir.exists() ) {
                    if ( ! dir.mkdirs() ) throw new IOException();
                }
                if ( ! f.createNewFile() ) throw new IOException();
            } catch (IOException e) {
                System.err.println("Cannot create data file " + f.getPath());
                e.printStackTrace();
                System.exit(1);
            }
            this.data = new ArrayList<Item>();
            saveData();
        }
    }

    public void newItem(String host, String user, String password) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IOException {
        Item newItem = new Item( host, user, Password.encrypt(this.passPhrase, password) );
        for ( Item item: this.data ) {
            if ( item.equals(newItem) && item.exp == null ) item.exp = new Date();
        }
        this.data.add(newItem);
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
        for ( Item item: this.data ) {
            if ( item.host.contains(host) && item.exp == null ) showItem(item);
        }
    }

    public void showItem(Item item) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException {
        System.out.println(String.format("%-20s\t%-20s\t%-30s\t%-30s\t%-30s", item.host, item.user, Password.decrypt(this.passPhrase, item.password), item.cre, item.exp));
    }

    public void showAllItem() throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        printHead();
        for(Item item: this.data) showItem(item);
    }

    private void printHead(){
        System.out.println(String.format("%-20s\t%-20s\t%-30s\t%-30s\t%-30s", "Host", "User", "Password", "Cre", "exp"));
        System.out.println("==================================================================================================================================");
    }


    private void readData() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream( this.dataFile ));
        this.data = (List<Item>) ois.readObject();
        ois.close();
    }

    private void saveData() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.dataFile ));
        oos.writeObject(this.data);
        oos.close();
    }

}
