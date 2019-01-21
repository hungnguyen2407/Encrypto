package encrypto.asymmetric;

import encrypto.ui.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This controller is handle for asymmetric tab
 */
public class AsymmetricController {

    public ToggleGroup cryptography;
    @FXML
    private AnchorPane paneCrypto;
    @FXML
    private TextField tfKey;
    @FXML
    private Button btnStart, btnStop, btnKey, btnPaste;
    @FXML
    private RadioButton rBtnEn, rBtnDe;
    @FXML
    private ComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding;
    @FXML
    private TextArea taInput;
    @FXML
    private Label lblKey, lblInput;
    private File publicKey, privateKey;

    /**
     * Constructor
     */
    public AsymmetricController() {
    }

    /**
     * Initialize for the asymmetric
     */
    @FXML
    public void initialize() {
        /*
         * ASYMMETRIC PANEL
         * This code below is handle for asymmetric cryptography features and create key pair
         */

        radioBtnCryptoHandler(rBtnEn);

        radioBtnCryptoHandler(rBtnDe);

        paneCryptoHandler();

        btnStartHandler();
    }

    /**
     * Handler for pane crypto
     */
    private void paneCryptoHandler() {
        comboBoxAlgorithmHandler();
        btnPasteHandler();
    }

    /**
     * Handler for algorithm combobox
     */
    private void comboBoxAlgorithmHandler() {
        comboBoxAlgorithm.getItems().addAll("RSA");
        comboBoxAlgorithm.setOnAction(event -> {
            comboBoxModeHandler();
            comboBoxPaddingHandler();
        });
    }

    /**
     * Handler for mode combobox
     */
    private void comboBoxModeHandler() {
        comboBoxMode.setDisable(false);
        switch (comboBoxAlgorithm.getSelectionModel().getSelectedItem()) {
            case "RSA":
                comboBoxMode.getItems().clear();
                comboBoxMode.getItems().addAll("ECB");
                comboBoxMode.setValue("ECB");
                break;
        }
    }

    /**
     * Handler for padding combobox
     */
    private void comboBoxPaddingHandler() {
        comboBoxPadding.setDisable(false);
        switch (comboBoxAlgorithm.getSelectionModel().getSelectedItem()) {
            case "RSA":
                comboBoxPadding.getItems().clear();
                comboBoxPadding.getItems().addAll("PKCS1Padding", "OAEPWithSHA-1AndMGF1Padding", "OAEPWithSHA-256AndMGF1Padding");
                comboBoxPadding.setValue("PKCS1Padding");
                break;
        }
    }

    /**
     * Handler for decryption radio button
     */
    private void cryptoRadioBtnDeHandler() {
        privateKey = null;
        lblKeyDeHandler();
        tfKeyDeHandler();
        btnKeyDeHandler();
        lblInputDeHandler();
        taInputHandler();
    }

    /**
     * Handler for encryption radio button
     */
    private void cryptoRadioBtnEnHandler() {
        publicKey = null;
        lblKeyEnHandler();
        tfKeyEnHandler();
        btnKeyEnHandler();
        lblInputEnHandler();
        taInputHandler();
    }

    private void taInputHandler() {
        taInput.clear();
        taInput.setWrapText(true);
    }

    private void btnStartHandler() {
        btnStart.setOnAction(event -> {
            if (comboBoxAlgorithm.getSelectionModel().getSelectedItem() == null) {
                new WarningDialog("Missing algorithm!", "Please choose a type algorithm.");
            } else if (comboBoxMode.getSelectionModel().getSelectedItem() == null) {
                new WarningDialog("Missing mode!", "Please choose a mode.");
            } else if (comboBoxPadding.getSelectionModel().getSelectedItem() == null) {
                new WarningDialog("Missing padding!", "Please choose a mode's padding.");
            } else if (taInput.getText() == null | taInput.getText().trim().equals("")) {
                new WarningDialog("Missing information!", "Please fill all the required information.");
            } else if (rBtnEn.isSelected()) {
                if (publicKey == null) {
                    new WarningDialog("Missing public key!", "Please choose a public key.");
                } else {
                    disableCryptoPane();
                    final AsymmetricEncryptionTask asymmetricEncryptionTask = new AsymmetricEncryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), comboBoxPadding.getSelectionModel().getSelectedItem(), publicKey, taInput.getText().getBytes());

                    asymmetricEncryptionTask.setOnSucceeded(event1 -> {
                        Alert alert = new InformationDialog("Done!", "Your plain text had been encrypted.");
                        VBox dialogPaneContent = new VBox();
                        TextArea textArea = new TextArea(Base64.getEncoder().encodeToString(asymmetricEncryptionTask.getValue()));
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        Label lblCopy = new Label("");
                        Button btnCopy = new CopyButton(textArea, lblCopy);
                        FlowPane flowPane = new FlowPane();
                        flowPane.getChildren().addAll(btnCopy, lblCopy);
                        dialogPaneContent.getChildren().addAll(new Label("Cipher text:"), textArea, flowPane);
                        alert.getDialogPane().setContent(dialogPaneContent);
                        alert.setResizable(true);
                        alert.setHeight(300);
                        alert.setWidth(500);
                        resetCryptoPane();
                    });

                    asymmetricEncryptionTask.setOnFailed(event1 -> {
                        new ErrorDialog("Error!", asymmetricEncryptionTask.getException().getMessage());
                        resetCryptoPane();
                    });
                    asymmetricEncryptionTask.setOnCancelled(event1 -> resetCryptoPane());

                    new Thread(asymmetricEncryptionTask).start();

                    btnStop.setOnAction(event1 -> asymmetricEncryptionTask.cancel());
                }
            } else if (rBtnDe.isSelected()) {
                if (privateKey == null) {
                    new WarningDialog("Missing private key!", "Please choose a private key.");
                } else {
                    disableCryptoPane();
                    final AsymmetricDecryptionTask asymmetricDecryptionTask = new AsymmetricDecryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), comboBoxPadding.getSelectionModel().getSelectedItem(), privateKey, Base64.getDecoder().decode(taInput.getText()));
                    asymmetricDecryptionTask.setOnSucceeded(event1 -> {
                        Alert alert = new InformationDialog("Done!", "Your cipher text had been decrypted.");
                        VBox dialogPaneContent = new VBox();
                        TextArea textArea = new TextArea(new String(asymmetricDecryptionTask.getValue(), StandardCharsets.UTF_8));
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        dialogPaneContent.getChildren().addAll(new Label("Plain text:"), textArea);
                        alert.getDialogPane().setContent(dialogPaneContent);
                        alert.setResizable(true);
                        alert.setHeight(300);
                        alert.setWidth(500);
                        resetCryptoPane();
                    });

                    asymmetricDecryptionTask.setOnFailed(event1 -> {
                        new ErrorDialog("Error!", asymmetricDecryptionTask.getException().getMessage());
                        resetCryptoPane();
                    });

                    asymmetricDecryptionTask.setOnCancelled(event1 -> resetCryptoPane());

                    new Thread(asymmetricDecryptionTask).start();

                    btnStop.setOnAction(event1 -> asymmetricDecryptionTask.cancel());
                }
            }
        });
    }

    private void lblInputDeHandler() {
        lblInput.setText("Cipher text");
    }

    private void btnKeyDeHandler() {
        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private key (*.prikey)", "*.prikey"));
            privateKey = fileChooser.showOpenDialog(null);
            if (privateKey != null) {
                if (!privateKey.getName().endsWith(".prikey")) {
                    new WarningDialog("Wrong format!", "Please choose a file with private key extension (*.prikey).");
                } else {
                    tfKey.setText(privateKey.getAbsolutePath());
                }
            }
        });
    }

    private void btnKeyEnHandler() {
        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public key (*.pubkey)", "*.pubkey"));
            publicKey = fileChooser.showOpenDialog(null);
            if (publicKey != null) {
                if (!publicKey.getName().endsWith(".pubkey")) {
                    new WarningDialog("Wrong format!", "Please choose a file with public key extension (*.pubkey).");
                } else {
                    tfKey.setText(publicKey.getAbsolutePath());
                }
            }
        });
    }

    private void tfKeyDeHandler() {
        tfKey.clear();
    }

    private void lblKeyDeHandler() {
        lblKey.setText("Private key");
    }


    private void disableCryptoPane() {
        paneCrypto.setDisable(true);
    }

    private void resetCryptoPane() {
        paneCrypto.setDisable(false);
    }

    private void lblInputEnHandler() {
        lblInput.setText("Plain text");
    }

    private void tfKeyEnHandler() {
        tfKey.clear();
    }

    private void lblKeyEnHandler() {
        lblKey.setText("Public key");
    }

    private void btnPasteHandler() {
        PasteButton.factory(btnPaste, taInput);
    }

    /**
     * Handler for radio button crypto mode
     *
     * @param rBtn : radio button
     */
    private void radioBtnCryptoHandler(RadioButton rBtn) {
        if (rBtn.equals(rBtnEn)) {
            rBtnEn.setOnAction(event -> {
                paneCrypto.setVisible(true);
                paneCrypto.setDisable(false);
                cryptoRadioBtnEnHandler();
            });
        } else if (rBtn.equals(rBtnDe)) {
            rBtnDe.setOnAction(event -> {
                paneCrypto.setVisible(true);
                paneCrypto.setDisable(false);
                cryptoRadioBtnDeHandler();
            });
        }
    }
}
