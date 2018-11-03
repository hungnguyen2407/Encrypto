package encrypto;

import javafx.concurrent.Task;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SymmetricEncryptionTask extends Task<File> {

    private String algorithm, mode, keySize, padding;
    private File fileSrc, fileDes, keyDes;

    private byte[] iv = new byte[8];
    private IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    public SymmetricEncryptionTask(String algorithm, String mode, String keySize, String padding, File fileSrc, File fileDes, File keyDes) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.keySize = keySize;
        this.padding = padding;
        this.fileSrc = fileSrc;
        this.fileDes = fileDes;
        this.keyDes = keyDes;
    }

    @Override
    protected File call() throws Exception {
        encryptHandler();
        return fileDes;
    }

    private void encryptHandler() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        SecretKey key;
        FileInputStream fis;
        FileOutputStream fos;
        if(keyDes.exists())
        {
            fis = new FileInputStream(keyDes);
            byte[] container = new byte[1024];
            int byteRead;
            byteRead = fis.read(container);
            key = new SecretKeySpec(container, 0, byteRead, algorithm);

        } else {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(Integer.parseInt(keySize));
            key = keyGenerator.generateKey();

            //Save key file
            fos = new FileOutputStream(keyDes);
            fos.write(key.getEncoded());
            fos.flush();
            fos.close();
        }

        Cipher cipher;

        if ("AES".equals(algorithm))
            ivParameterSpec = new IvParameterSpec(new byte[16]);
        if (mode == null | "".equals(mode)) {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else if ("ECB".equals(mode) | "GCM".equals(mode)) {
            cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else if ("RC2".equals(algorithm)) {
            cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, key, new RC2ParameterSpec(Integer.parseInt(keySize)));
        } else if ("RC5".equals(algorithm)) {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        }
        fis = new FileInputStream(fileSrc);
        fos = new FileOutputStream(fileDes);

        byte[] container = new byte[1024];

        int byteRead, i = 0;
        while ((byteRead = fis.read(container)) != -1) {
            byte[] output = cipher.update(container, 0, byteRead);
            if (output != null) {
                fos.write(output);
            }
            this.updateProgress((double) i * 1024, fileSrc.length());
            updatePercent(i);
            i++;
        }

        fos.write(cipher.doFinal());
        fos.flush();
        fis.close();
        fos.close();
    }

    private void updatePercent(int i) {
        this.updateMessage(((int) ((i * 1024) / fileSrc.length()) * 100) + "%");
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
