package PassMe;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class Cli {
    private Manager manager;


    public Cli(String passPhrase) {
        try {
            this.manager = new Manager(passPhrase);
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void help() {
        System.out.println("Available commands:");
        System.out.println("help   show this help");
        System.out.println("all    show all items");
        System.out.println("new    <host> <user> [password]");
        System.out.println("search <host>");
        System.out.println("dump dump the commands which can regenerate the data");
        System.out.println("Enter exit to exit");
        System.out.println("For commands not recognized, it will be treated as a host and search it");
    }

    public void waitForCommand() throws Exception {
        help();
        try {
            ConsoleReader cr = new ConsoleReader();
            cr.setExpandEvents(false);
            cr.addCompleter(new StringsCompleter("help", "all", "new", "search", "dump", "exit"));
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
                } else if ( command.equals("dump") ) {
                    manager.dump();
                } else if ( command.equals("exit") ) {
                    break;
                } else {
                    manager.searchItem(command);
                }
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
