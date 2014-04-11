package PassMe;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Database implements Serializable {
    private final byte[] passPhraseHash;
    final List<Item> items;

    Database(String passPhrase, List<Item> data) throws NoSuchAlgorithmException {
        this.passPhraseHash = digest(passPhrase);
        this.items = data;
    }

    public void auth(String passPhrase) throws NoSuchAlgorithmException {
        if (! Arrays.equals(digest(passPhrase), this.passPhraseHash)) {
            throw new AuthFailureException();
        }
    }

    private static byte[] digest(String passPhrase) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(passPhrase.getBytes());
        return md.digest();
    }

    // inner classes

    static class Item implements Serializable {
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

    static class AuthFailureException extends RuntimeException {
    }
}


