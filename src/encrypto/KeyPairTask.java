package encrypto;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Task for making the key pair
 */
public class KeyPairTask extends Task<File> {
    private File privateKey, publicKey;
    private String algorithm;

    KeyPairTask(String algorithm, File publicKey, File privateKey) {
        this.algorithm = algorithm;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    protected File call() {
        createKeyPair();
        return null;
    }

    /**
     * Create and write key pair to file
     */
    private void createKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            FileOutputStream fileOutputStream;
            if (keyPair != null) {
                try {
                    fileOutputStream = new FileOutputStream(privateKey);
                    fileOutputStream.write(keyPair.getPrivate().getEncoded());
                } catch (FileNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Don't have permission to create private key file.");
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Can't create private key file.");
                }

                try {
                    fileOutputStream = new FileOutputStream(publicKey);
                    fileOutputStream.write(keyPair.getPublic().getEncoded());
                } catch (FileNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Don't have permission to create public key file.");
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Can't create public key file.");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The algorithm type is invalid.");
            alert.showAndWait();
        }




    }
}
