package encrypto.digitalsignature;

import javafx.concurrent.Task;

import java.io.File;

public class SignTask extends Task<File> {

    private File src, key, des;

    @Override
    protected File call() throws Exception {
        sign();

        writeFile();
        return null;
    }

    private void writeFile() {

    }

    private byte[] sign(){

        return null;
    }
}
