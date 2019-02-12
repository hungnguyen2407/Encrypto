package encrypto.digitalsignature;

import encrypto.ui.ErrorDialog;
import encrypto.ui.InformationDialog;
import encrypto.ui.WarningDialog;
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
    private ProgressIndicator progressIndicator;
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
            if (src != null) {
                if (!src.exists()) {
                    new WarningDialog("Warning!", "Access dined. Please check permission of the file.");
                } else {
                    tfSrc.setText(src.getAbsolutePath());
                    sign = new File(src.getAbsolutePath() + ".sign");
                    tfSign.setText(sign.getAbsolutePath());
                }
            }
        });

        btnSign.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sign file (*.sign)", "*.sign"));
            sign = fileChooser.showOpenDialog(null);
            if (sign != null) {
                if (!sign.exists()) {
                    new WarningDialog("Warning!", "Access dined. Please check permission of sign file.");
                } else {
                    tfSign.setText(sign.getAbsolutePath());
                }
            }
        });

        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public key (*.pubkey)", "*.pubkey"));
            key = fileChooser.showOpenDialog(null);
            if (key != null) {
                if (!key.exists()) {
                    new WarningDialog("Warning!", "Access dined. Please check permission of public key file.");
                } else {
                    tfKey.setText(key.getAbsolutePath());
                }
            }
        });
        btnStart.setOnAction(event -> {
            if (comboBoxAlgorithm.getSelectionModel().getSelectedItem() == null) {
                new WarningDialog("Warning!", "Please choose a type algorithm.");
            } else if (key == null) {
                new WarningDialog("Warning!", "Please choose a public key.");
            } else if (src == null) {
                new WarningDialog("Warning!", "Please choose a source file to sign.");
            } else if (sign == null) {
                new WarningDialog("Warning!", "Please choose a sign file to verify.");
            } else {
                paneDI.setDisable(true);
                rBtnSign.setDisable(true);
                rBtnVerify.setDisable(true);
                btnStart.setVisible(false);
                progressIndicator.progressProperty().unbind();
                progressIndicator.setVisible(true);
                VerifyTask verifyTask = new VerifyTask(src, key, sign, comboBoxAlgorithm.getSelectionModel().getSelectedItem());
                progressIndicator.progressProperty().bind(verifyTask.progressProperty());
                verifyTask.setOnSucceeded(event1 -> {
                    paneDI.setDisable(false);
                    rBtnSign.setDisable(false);
                    rBtnVerify.setDisable(false);
                    btnStop.setVisible(false);
                    btnStop.setDisable(true);
                    btnStart.setVisible(true);
                    progressIndicator.setVisible(false);
                    if (verifyTask.getValue()) {
                        new InformationDialog("Success!", "Verify success. Your file is the original.");
                    } else {
                        new ErrorDialog("Failed!", "Verify failed. The file or the sign file had been modified.");
                    }
                });

                verifyTask.setOnFailed(event1 -> {
                    paneDI.setDisable(false);
                    rBtnSign.setDisable(false);
                    rBtnVerify.setDisable(false);
                    btnStop.setVisible(false);
                    btnStop.setDisable(true);
                    btnStart.setVisible(true);
                    progressIndicator.setVisible(false);
                    new ErrorDialog("Error!", verifyTask.getException().getMessage());
                });

                btnStop.setVisible(true);
                btnStop.setDisable(false);
                btnStop.setOnAction(event1 -> {
                    verifyTask.cancel();
                    paneDI.setDisable(false);
                    rBtnSign.setDisable(false);
                    rBtnVerify.setDisable(false);
                    btnStop.setVisible(false);
                    btnStop.setDisable(true);
                    btnStart.setVisible(true);
                    progressIndicator.setVisible(false);
                });

                new Thread(verifyTask).start();
            }
        });
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
            if (src != null) {
                if (!src.exists()) {
                    new WarningDialog("Warning!", "Access dined. Please check permission of the file.");
                } else {
                    tfSrc.setText(src.getAbsolutePath());
                    sign = new File(src.getAbsolutePath() + ".sign");
                    tfSign.setText(sign.getAbsolutePath());
                }
            }
        });

        btnSign.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sign file (*.sign)", "*.sign"));
            sign = fileChooser.showSaveDialog(null);
            if (sign != null) {
                if (!sign.getName().endsWith(".sign")) {
                    sign.renameTo(new File(sign.getName() + ".sign"));
                }
                tfSign.setText(sign.getAbsolutePath());
            }
        });

        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private key (*.prikey)", "*.prikey"));
            key = fileChooser.showOpenDialog(null);
            if (key != null) {
                if (!key.exists()) {
                    new WarningDialog("Warning!", "Access dined. Please check permission of private key file.");
                } else {
                    tfKey.setText(key.getAbsolutePath());
                }
            }
        });

        btnStart.setOnAction(event -> {
            if (comboBoxAlgorithm.getSelectionModel().getSelectedItem() == null) {
                new WarningDialog("Warning!", "Please choose a type algorithm.");
            } else if (key == null) {
                new WarningDialog("Warning!", "Please choose a public key.");
            } else if (src == null) {
                new WarningDialog("Warning!", "Please choose a source file to sign.");
            } else if (sign == null) {
                new WarningDialog("Warning!", "Please choose a sign file to verify.");
            } else {
                paneDI.setDisable(true);
                rBtnSign.setDisable(true);
                rBtnVerify.setDisable(true);
                btnStart.setVisible(false);
                progressIndicator.progressProperty().unbind();
                progressIndicator.setVisible(true);
                SignTask signTask = new SignTask(src, key, sign, comboBoxAlgorithm.getSelectionModel().getSelectedItem());
                progressIndicator.progressProperty().bind(signTask.progressProperty());
                signTask.setOnSucceeded(event1 -> {
                    paneDI.setDisable(false);
                    rBtnSign.setDisable(false);
                    rBtnVerify.setDisable(false);
                    btnStop.setVisible(false);
                    btnStop.setDisable(true);
                    btnStart.setVisible(true);
                    progressIndicator.setVisible(false);
                    new InformationDialog("Success!", "Sign file success.");
                });

                signTask.setOnFailed(event1 -> {
                    paneDI.setDisable(false);
                    rBtnSign.setDisable(false);
                    rBtnVerify.setDisable(false);
                    btnStop.setVisible(false);
                    btnStop.setDisable(true);
                    btnStart.setVisible(true);
                    progressIndicator.setVisible(false);
                    new ErrorDialog("Error!", signTask.getException().getMessage());
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
                    progressIndicator.setVisible(false);
                });

                new Thread(signTask).start();
            }
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
