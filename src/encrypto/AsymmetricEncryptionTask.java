package encrypto;

import javafx.concurrent.Task;

import java.io.File;

public class AsymmetricEncryptionTask  extends Task<File> {

    private String algorithm, mode, keySize, padding;
    private File fileSrc, fileDes;

    @Override
    protected File call() {
        encryptHandler();
        return fileDes;
    }

    private void encryptHandler() {

    }
}
