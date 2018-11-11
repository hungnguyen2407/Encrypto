package encrypto;

import javafx.concurrent.Task;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

public class AsymmetricDecryptionTask extends Task<String> {

    private String algorithm, mode, padding;
    private byte[] cipherText;
    private File privateKey;

    AsymmetricDecryptionTask(String algorithm, String mode, String padding, File privateKey, byte[] cipherText) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
        this.privateKey = privateKey;
        this.cipherText = cipherText;
    }

    @Override
    protected String call() throws Exception {
        return new String(Objects.requireNonNull(decryptHandler()), StandardCharsets.UTF_8);
    }

    private byte[] decryptHandler() throws Exception {
        FileInputStream keyfis = new FileInputStream(privateKey);
        byte[] decKey = new byte[keyfis.available()];
        keyfis.read(decKey);
        keyfis.close();

        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(decKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);

        Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return cipher.doFinal(cipherText);
    }
}
