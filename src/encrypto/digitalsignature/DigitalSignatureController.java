package encrypto.digitalsignature;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * This controller is handle for digital signature tab
 */
public class DigitalSignatureController {

    public ToggleGroup digitalMode;
    @FXML
    private AnchorPane paneDI;
    @FXML
    private TextField tfKey, tfSrc, tfSign;
    @FXML
    private Button btnStart, btnStop, btnKey, btnSrc, btnSign;
    @FXML
    private RadioButton rBtnSign, rBtnVerify;
    @FXML
    private ComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding;
    @FXML
    private Label lblKey, lblSrc, lblSign;
    private File publicKey, privateKey, key, sign, src;

    /**
     * Constructor
     */
    public DigitalSignatureController() {
    }

    @FXML
    public void initialize() {

        radioBtnDIHandler(rBtnSign);

        radioBtnDIHandler(rBtnVerify);

        paneDIHandler();
    }

    /**
     * Handler for radio button digital signature mode
     *
     * @param rBtn : radio button
     */
    private void radioBtnDIHandler(RadioButton rBtn) {
        if (rBtn.equals(rBtnSign)) {
            rBtnSign.setOnAction(event -> {
                paneDI.setVisible(true);
                paneDI.setDisable(false);
                rBtnSignHandler();
            });
        } else if (rBtn.equals(rBtnVerify)) {
            rBtnVerify.setOnAction(event -> {
                paneDI.setVisible(true);
                paneDI.setDisable(false);
                rBtnVerifyHandler();
            });
        }
    }

    /**
     * Handler for verify radio button
     */
    private void rBtnVerifyHandler() {
        tfSrc.clear();
        tfKey.clear();
        tfSign.clear();
        key = null;
        lblKey.setText("Public key");
    }

    /**
     * Handler for sign radio button
     */
    private void rBtnSignHandler() {
        tfSrc.clear();
        tfKey.clear();
        tfSign.clear();
        key = null;
        lblKey.setText("Private key");
    }

    /**
     * Handler for pane digital signature
     */
    private void paneDIHandler() {
        comboBoxAlgorithmHandler();
    }

    /**
     * Handler for algorithm combo box
     */
    private void comboBoxAlgorithmHandler() {
        comboBoxAlgorithm.getItems().addAll("SHA1withDSA", "SHA1withRSA", "SHA256withRSA");
    }


}
