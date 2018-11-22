package encrypto.symmetric;

import javafx.concurrent.Task;
import org.bouncycastle.crypto.engines.RC564Engine;
import org.bouncycastle.crypto.params.RC5Parameters;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricEncryptionTask extends Task<File> {

    private String algorithm, mode, keySize, padding;
    private File fileSrc, fileDes, keyDes;

    private byte[] iv = new byte[8];
    private IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    SymmetricEncryptionTask(String algorithm, String mode, String keySize, String padding, File fileSrc, File fileDes, File keyDes) {
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
        FileInputStream fis = new FileInputStream(fileSrc);
        FileOutputStream fos;

        if ("RC5".equals(algorithm)) {
            RC564Engine rc564Engine = new RC564Engine();
            rc564Engine.init(true, new RC5Parameters(new byte[Integer.parseInt(keySize)], 20));

            //Save key file
            fos = new FileOutputStream(keyDes);
            fos.write(new byte[Integer.parseInt(keySize)]);
            fos.flush();
            fos.close();
            fos = new FileOutputStream(fileDes);
            int byteRead, i = 0;
            byte[] container = new byte[Integer.parseInt(keySize)];
            while ((byteRead = fis.read(container)) != -1) {
                byte[] output = new byte[Integer.parseInt(keySize)];
                rc564Engine.processBlock(container, 0, output, 0);
                fos.write(output,0, byteRead);
                this.updateProgress((double) i * Integer.parseInt(keySize), fileSrc.length());
                i++;
            }
            fos.flush();
            fis.close();
            fos.close();
            return;
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(Integer.parseInt(keySize));
        key = keyGenerator.generateKey();

        //Save key file
        fos = new FileOutputStream(keyDes);
        fos.write(key.getEncoded());
        fos.flush();
        fos.close();

        Cipher cipher;
        if ("RC4".equals(algorithm)) {
            RC4 rc4 = new RC4(key.getEncoded());
            fis = new FileInputStream(fileSrc);
            fos = new FileOutputStream(fileDes);

            byte[] container = new byte[1024];

            int byteRead, i = 0;
            while (fis.read(container) != -1) {
                byte[] output = rc4.encrypt(container);
                if (output != null) {
                    fos.write(output);
                }
                this.updateProgress((double) i * 1024, fileSrc.length());
                i++;
            }
            fos.flush();
            fis.close();
            fos.close();
            return;
        }
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
        } else {
            cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        }

        fos = new FileOutputStream(fileDes);

        byte[] container = new byte[1024];

        int byteRead, i = 0;
        while ((byteRead = fis.read(container)) != -1) {
            byte[] output = cipher.update(container, 0, byteRead);
            if (output != null) {
                fos.write(output);
            }
            this.updateProgress((double) i * 1024, fileSrc.length());
            i++;
        }

        fos.write(cipher.doFinal());
        fos.flush();
        fis.close();
        fos.close();
    }
}
