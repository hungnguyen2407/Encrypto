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
    private ComboBox<String> comboBoxAlgorithm;
    @FXML
    private Label lblKey;
    private File key, sign, src;

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
        btnSrc.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            src = fileChooser.showOpenDialog(null);
            if (src == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a source file to sign.");
                alert.showAndWait();
            } else if (!src.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Access dined. Please check permission of the file.");
                alert.showAndWait();
            } else {
                tfSrc.setText(src.getAbsolutePath());
            }
        });

        btnSign.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sign file (*.sign)", "*.sign"));
            sign = fileChooser.showOpenDialog(null);
            if (sign == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a sign file to verify.");
                alert.showAndWait();
            } else if (!sign.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Access dined. Please check permission of sign file.");
                alert.showAndWait();
            } else {
                tfSrc.setText(sign.getAbsolutePath());
            }
        });

        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public key (*.pubkey)", "*.pubkey"));
            key = fileChooser.showOpenDialog(null);
            if (key == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a public key.");
                alert.showAndWait();
            } else if (!key.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Access dined. Please check permission of public key file.");
                alert.showAndWait();
            } else {
                tfKey.setText(key.getAbsolutePath());
            }
        });

        //TODO btnStart
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
        btnSrc.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            src = fileChooser.showOpenDialog(null);
            if (src == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a source file to sign.");
                alert.showAndWait();
            } else if (!src.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Access dined. Please check permission of the file.");
                alert.showAndWait();
            } else {
                tfSrc.setText(src.getAbsolutePath());
            }
        });

        btnSign.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sign file (*.sign)", "*.sign"));
            sign = fileChooser.showSaveDialog(null);
            if (!sign.getName().endsWith(".sign")) {
                sign.renameTo(new File(sign.getName() + ".sign"));
            }
            tfSign.setText(sign.getAbsolutePath());
        });

        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private key (*.prikey)", "*.prikey"));
            key = fileChooser.showOpenDialog(null);
            if (key == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a private key.");
                alert.showAndWait();
            } else if (!key.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Access dined. Please check permission of private key file.");
                alert.showAndWait();
            } else {
                tfKey.setText(key.getAbsolutePath());
            }
        });

        btnStart.setOnAction(event -> {
            paneDI.setDisable(true);
            rBtnSign.setDisable(true);
            rBtnVerify.setDisable(true);
            btnStart.setVisible(false);
            SignTask signTask = new SignTask(src, key, sign, comboBoxAlgorithm.getSelectionModel().getSelectedItem());
            signTask.setOnSucceeded(event1 -> {
                paneDI.setDisable(false);
                rBtnSign.setDisable(false);
                rBtnVerify.setDisable(false);
                btnStop.setVisible(false);
                btnStop.setDisable(true);
                btnStart.setVisible(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText(null);
                alert.setContentText("Sign file success.");
                alert.showAndWait();
            });

            signTask.setOnFailed(event1 -> {
                paneDI.setDisable(false);
                rBtnSign.setDisable(false);
                rBtnVerify.setDisable(false);
                btnStop.setVisible(false);
                btnStop.setDisable(true);
                btnStart.setVisible(true);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed!");
                alert.setHeaderText(null);
                alert.setContentText("Sign file failed.");
                alert.showAndWait();
            });

            btnStop.setVisible(true);
            btnStop.setDisable(false);
            btnStop.setOnAction(event1 -> {
                signTask.cancel();
                paneDI.setDisable(false);
                rBtnSign.setDisable(false);
                rBtnVerify.setDisable(false);
                btnStop.setVisible(false);
                btnStop.setDisable(true);
                btnStart.setVisible(true);
            });

            new Thread(signTask).start();
        });
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
