package encrypto.asymmetric;

import javafx.concurrent.Task;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public class AsymmetricEncryptionTask extends Task<byte[]> {

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
    protected byte[] call() throws Exception {
        return encryptHandler();
    }

    private byte[] encryptHandler() throws Exception {

        FileInputStream keyfis = new FileInputStream(publicKey);
        byte[] encKey = new byte[keyfis.available()];
        keyfis.read(encKey);
        keyfis.close();

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(plainText);
    }
}
