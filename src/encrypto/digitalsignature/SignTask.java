package encrypto.digitalsignature;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignTask extends Task<File> {

    private File src, key, des;
    private String algorithm;

    public SignTask(File src, File key, File des, String algorithm) {
        this.src = src;
        this.key = key;
        this.des = des;
        this.algorithm = algorithm;
    }

    @Override
    protected File call() throws Exception {

        return signFile();
    }

    private File signFile() throws Exception {
        Signature sign = Signature.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(key);
        byte[] keyBytes = new byte[fis.available()];
        fis.read(keyBytes);
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = null;
        if (algorithm.endsWith("DSA")) {
            keyFactory = KeyFactory.getInstance("DSA");
        } else if (algorithm.endsWith("RSA")) {
            keyFactory = KeyFactory.getInstance("RSA");
        } else {
            throw new NoSuchAlgorithmException();
        }
        PrivateKey privateKey = keyFactory.generatePrivate(priKeySpec);
        sign.initSign(privateKey);

        fis = new FileInputStream(src);
        int byteRead = 0;
        byte[] bytes = new byte[1024];
        while ((byteRead = fis.read(bytes)) != -1) {
            sign.update(bytes, 0, byteRead);
        }
        fis.close();
        byte[] signBytes = sign.sign();
        FileOutputStream fos = new FileOutputStream(des);
        fos.write(signBytes);
        fos.flush();
        fos.close();

        return des;
    }
}
