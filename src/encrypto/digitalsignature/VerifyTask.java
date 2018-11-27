package encrypto.digitalsignature;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class VerifyTask extends Task<Boolean> {

    private File src, key, sign;
    private String algorithm;

    public VerifyTask(File src, File key, File sign, String algorithm) {
        this.src = src;
        this.key = key;
        this.sign = sign;
        this.algorithm = algorithm;
    }


    @Override
    protected Boolean call() throws Exception {
        return verifyFile();
    }

    private boolean verifyFile() throws Exception {
        FileInputStream fis = new FileInputStream(key);
        int byteRead = 0;
        byte[] keyBytes = new byte[fis.available()];
        fis.read(keyBytes);
        fis.close();
        KeyFactory keyFactory = null;

        if (algorithm.endsWith("DSA")) {
            keyFactory = KeyFactory.getInstance("DSA");
        } else if (algorithm.endsWith("RSA")) {
            keyFactory = KeyFactory.getInstance("RSA");
        } else {
            throw new NoSuchAlgorithmException();
        }

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
        Signature sign = Signature.getInstance(algorithm, "SUN");
        sign.initVerify(publicKey);
        fis = new FileInputStream(src);
        byte[] bytes = new byte[1024];
        while ((byteRead = fis.read(bytes)) != -1) {
            sign.update(bytes, 0, byteRead);
        }
        fis.close();
        fis = new FileInputStream(this.sign);
        byte[] signBytes = new byte[fis.available()];
        fis.read(signBytes);
        return sign.verify(signBytes);
    }
}
