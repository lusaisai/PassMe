package PassMe;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;


public class Cli {
    private Manager manager;


    public Cli(String passPhrase) {
        try {
            this.manager = new Manager(passPhrase);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void help() {
        System.out.println("Available commands:");
        System.out.println("help   show this help");
        System.out.println("all    show all items");
        System.out.println("new    <host> <user> [password]");
        System.out.println("search <host>");
        System.out.println("Enter exit to exit");
        System.out.println("For commands not recognized, it will treat it as a host and search it");
    }

    public void waitForCommand() {
        help();
        try {
            ConsoleReader cr = new ConsoleReader();
            cr.addCompleter(new StringsCompleter("help", "all", "new", "search", "exit"));
            while (true) {
                String command = cr.readLine("> ").trim();
                if ( command.equals("") ) continue;

                if ( command.equals("help") ) {
                    help();
                } else if ( command.equals("all") ) {
                    manager.showAllItem();
                } else if ( command.startsWith("new") ) {
                    String[] fields = command.split("\\s+");
                    if ( fields.length == 3 ) {
                        manager.newItem(fields[1], fields[2]);
                    } else if ( fields.length == 4 ) {
                        manager.newItem(fields[1], fields[2], fields[3]);
                    } else {
                        System.out.println("Missing arguments");
                        System.out.println("new    <host> <user> [password]");
                    }
                } else if ( command.startsWith("search") ) {
                    String[] fields = command.split("\\s+");
                    if ( fields.length == 2 ) {
                        manager.searchItem(fields[1]);
                    } else {
                        System.out.println("Missing arguments");
                        System.out.println("search <host>");
                    }
                } else if ( command.equals("exit") ) {
                    break;
                } else {
                    manager.searchItem(command);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exit");
        }
    }
}
