package encrypto;

import javafx.concurrent.Task;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class AsymmetricEncryptionTask extends Task<String> {

    private String algorithm, mode, padding;
    private byte[] plainText;
    private File publicKey;

    AsymmetricEncryptionTask(String algorithm, String mode, String padding, File publicKey, byte[] plainText) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
        this.publicKey = publicKey;
        this.plainText = plainText;
    }

    @Override
    protected String call() {
        return new String(Objects.requireNonNull(encryptHandler()));
    }

    private byte[] encryptHandler() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("AsymmetricRSA.encrypt: " + e.getMessage());
        }
        try {
            if (cipher != null) {
                cipher.init(Cipher.ENCRYPT_MODE, (Key) publicKey);
            }
        } catch (InvalidKeyException e) {
            System.out.println("AsymmetricRSA.encrypt: " + e.getMessage());
        }

        try {
            if (cipher != null) {
                return cipher.doFinal(plainText);
            }
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("AsymmetricRSA.encrypt: " + e.getMessage());
        }
        return null;
    }
}
