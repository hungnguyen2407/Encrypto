package encrypto.messagedigest;

import javafx.concurrent.Task;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MessageDigestCreateDigestTask extends Task<String> {

    private String algorithm;
    private File file;

    MessageDigestCreateDigestTask(String algorithm, File file) {
        this.algorithm = algorithm;
        this.file = file;
    }

    @Override
    protected String call() throws Exception {
        byte[] md = createMessageDigest();
        StringBuilder sb = new StringBuilder(2 * md.length);
        for (byte b : md) {
            sb.append((String.format("%02X", b)).toLowerCase());
        }
        return sb.toString();
    }

    private byte[] createMessageDigest() throws Exception {
        FileInputStream fis = new FileInputStream(this.file);
        int byteRead, i = 1;
        byte[] bytes = new byte[1024];
        byte[] result;
        switch (algorithm) {
            case "MD2":
                MD2Digest md2Digest = new MD2Digest();
                while ((byteRead = fis.read(bytes)) != -1) {
                    md2Digest.update(bytes, 0, byteRead);
                    this.updateProgress(1024 * i, this.file.length());
                    i++;
                }
                result = new byte[md2Digest.getDigestSize()];
                md2Digest.doFinal(result, 0);
                return result;
            case "MD4":
                MD4Digest md4Digest = new MD4Digest();
                while ((byteRead = fis.read(bytes)) != -1) {
                    md4Digest.update(bytes, 0, byteRead);
                    this.updateProgress(1024 * i, this.file.length());
                    i++;
                }
                result = new byte[md4Digest.getDigestSize()];
                md4Digest.doFinal(result, 0);
                return result;
            case "SHA-3":
                SHA3Digest sha3Digest = new SHA3Digest();
                while ((byteRead = fis.read(bytes)) != -1) {
                    sha3Digest.update(bytes, 0, byteRead);
                    this.updateProgress(1024 * i, this.file.length());
                    i++;
                }
                result = new byte[sha3Digest.getDigestSize()];
                sha3Digest.doFinal(result, 0);
                return result;
            default:
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                while ((byteRead = fis.read(bytes)) != -1) {
                    messageDigest.update(bytes, 0, byteRead);
                    this.updateProgress(1024 * i, this.file.length());
                    i++;
                }
                return messageDigest.digest();
        }

    }
}
