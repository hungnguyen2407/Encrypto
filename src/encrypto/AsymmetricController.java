package encrypto;

import com.jfoenix.controls.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * This controller is handle for asymmetric tab
 */
public class AsymmetricController {

    public ToggleGroup cryptography;
    @FXML
    private AnchorPane paneKeyPair, paneCrypto;
    @FXML
    private JFXTextField tfPubKey, tfPriKey, tfKey;
    @FXML
    private JFXButton btnStart, btnStop, btnPubKey, btnPriKey, btnCreate, btnKey;
    @FXML
    private JFXRadioButton rBtnEn, rBtnDe, rBtnKeyPair;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding, comboBoxKeyPairAlgorithm;
    @FXML
    private JFXTextArea taInput;
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
        radioBtnCryptoHandler(rBtnKeyPair);

        radioBtnCryptoHandler(rBtnEn);

        radioBtnCryptoHandler(rBtnDe);

        paneKeyPairHandler();

        paneCryptoHandler();
    }

    /**
     * Handler for pane crypto
     */
    private void paneCryptoHandler() {
        cryptoRadioBtnEnHandler();

        cryptoRadioBtnDeHandler();

        comboBoxAlgorithmHandler();

    }

    /**
     * Handler for algorithm combobox
     */
    private void comboBoxAlgorithmHandler() {
        comboBoxAlgorithm.getItems().addAll("RSA", "DSA");
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
            case "DSA":
                comboBoxMode.getItems().clear();
                comboBoxMode.getItems().addAll("");
                comboBoxMode.setValue("");
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
            case "DSA":
                comboBoxPadding.getItems().clear();
                comboBoxPadding.getItems().addAll("");
                comboBoxPadding.setValue("");
                break;
        }
    }

    /**
     * Handler for decryption radio button
     */
    private void cryptoRadioBtnDeHandler() {
        lblKeyDeHandler();
        tfKeyDeHandler();
        btnKeyDeHandler();
        lblInputDeHandler();
        btnStartDeHandler();
        taInputHandler();
    }

    private void taInputHandler() {
        taInput.clear();
    }

    private void btnStartDeHandler() {
        btnStart.setOnAction(event -> {
            if ("".equals(comboBoxAlgorithm.getSelectionModel().getSelectedItem()) | "".equals(comboBoxMode.getSelectionModel().getSelectedItem()) | "".equals(comboBoxPadding.getSelectionModel().getSelectedItem()) | "".equals(tfKey.getText()) | !privateKey.exists() | "".equals(taInput.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the required field.");
            } else {
                disableCryptoPane();
                final AsymmetricDecryptionTask asymmetricDecryptionTask = new AsymmetricDecryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), comboBoxPadding.getSelectionModel().getSelectedItem(), privateKey, taInput.getText().getBytes());

                asymmetricDecryptionTask.setOnSucceeded(event1 -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setHeaderText("Your cipher text had been decrypted. ");
                    VBox dialogPaneContent = new VBox();
                    TextArea textArea = new TextArea(asymmetricDecryptionTask.getValue());
                    textArea.setEditable(false);
                    textArea.setScrollLeft(200);
                    textArea.setPrefColumnCount(20);
                    textArea.setPrefRowCount(10);
                    dialogPaneContent.getChildren().addAll(new Label("Plain text:"), textArea);
                    alert.getDialogPane().setContent(dialogPaneContent);
                    alert.setResizable(true);
                    alert.showAndWait();
                    resetCryptoPane();
                });

                asymmetricDecryptionTask.setOnFailed(event1 -> {
                    Ultilities.showExceptionHandler(asymmetricDecryptionTask);
                    resetCryptoPane();
                });
                asymmetricDecryptionTask.setOnCancelled(event1 -> resetCryptoPane());

                new Thread(asymmetricDecryptionTask).start();

                btnStop.setOnAction(event1 -> asymmetricDecryptionTask.cancel());
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
            if (!privateKey.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Private key is not existed.");
            } else if (!privateKey.getName().endsWith(".prikey")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Private key is invalid.");
            } else {
                tfKey.setText(privateKey.getAbsolutePath());
            }
        });
    }

    private void tfKeyDeHandler() {
        tfKey.clear();
        tfKey.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(newValue);
            if (!file.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Private key is not existed.");
            } else if (!file.getName().endsWith(".prikey")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Private key is invalid.");
            } else {
                privateKey = file;
            }
        });
    }

    private void lblKeyDeHandler() {
        lblKey.setText("Private key");
    }

    /**
     * Handler for encryption radio button
     */
    private void cryptoRadioBtnEnHandler() {
        lblKeyEnHandler();
        tfKeyEnHandler();
        btnKeyEnHandler();
        lblInputEnHandler();
        btnStartEnHandler();
        taInputHandler();
    }

    private void btnStartEnHandler() {
        btnStart.setOnAction(event -> {
            if ("".equals(comboBoxAlgorithm.getSelectionModel().getSelectedItem()) | "".equals(comboBoxMode.getSelectionModel().getSelectedItem()) | "".equals(comboBoxPadding.getSelectionModel().getSelectedItem()) | "".equals(tfKey.getText()) | !publicKey.exists() | "".equals(taInput.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the required field.");
            } else {
                disableCryptoPane();
                final AsymmetricEncryptionTask asymmetricEncryptionTask = new AsymmetricEncryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), comboBoxPadding.getSelectionModel().getSelectedItem(), publicKey, taInput.getText().getBytes());

                asymmetricEncryptionTask.setOnSucceeded(event1 -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setHeaderText("Your plain text had been encrypted. ");
                    VBox dialogPaneContent = new VBox();
                    TextArea textArea = new TextArea(asymmetricEncryptionTask.getValue());
                    textArea.setEditable(false);
                    textArea.setScrollLeft(200);
                    textArea.setPrefColumnCount(20);
                    textArea.setPrefRowCount(10);
                    dialogPaneContent.getChildren().addAll(new Label("Cipher text:"), textArea);
                    alert.getDialogPane().setContent(dialogPaneContent);
                    alert.setResizable(true);
                    alert.showAndWait();
                    resetCryptoPane();
                });

                asymmetricEncryptionTask.setOnFailed(event1 -> {
                    Ultilities.showExceptionHandler(asymmetricEncryptionTask);
                    resetCryptoPane();
                });
                asymmetricEncryptionTask.setOnCancelled(event1 -> resetCryptoPane());

                new Thread(asymmetricEncryptionTask).start();

                btnStop.setOnAction(event1 -> asymmetricEncryptionTask.cancel());
            }
        });
    }

    private void disableCryptoPane() {
        paneCrypto.setDisable(true);
        progressBar.setVisible(true);
        progressBar.setDisable(false);
    }

    private void resetCryptoPane() {
        paneCrypto.setDisable(false);
        progressBar.setVisible(false);
        progressBar.setDisable(true);
    }

    private void lblInputEnHandler() {
        lblInput.setText("Plain text");
    }

    private void btnKeyEnHandler() {
        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public key (*.pubkey)", "*.pubkey"));
            publicKey = fileChooser.showOpenDialog(null);
            if (!publicKey.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Public key is not existed.");
            } else if (!publicKey.getName().endsWith(".pubkey")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Public key is invalid.");
            } else {

                tfKey.setText(publicKey.getAbsolutePath());
            }
        });
    }

    private void tfKeyEnHandler() {
        tfKey.clear();
        tfKey.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(newValue);
            if (!file.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Public key is not existed.");
            } else if (!file.getName().endsWith(".pubkey")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Public key is invalid.");
            } else {
                publicKey = file;
            }
        });
    }

    private void lblKeyEnHandler() {
        lblKey.setText("Public key");
    }

    /**
     * Handler for pane key pair
     */
    private void paneKeyPairHandler() {
        keyPairComboBoxAlgorithmHandler();

        keyPairPubKeyHandler();

        keyPairPriKeyHandler();

        keyPairBtnCreateHandler();
    }

    /**
     * Handler for create key pair button
     */
    private void keyPairBtnCreateHandler() {
        btnCreate.setOnAction(event -> {
            if (comboBoxKeyPairAlgorithm.getSelectionModel().getSelectedItem() == null | "".equals(comboBoxKeyPairAlgorithm.getSelectionModel().getSelectedItem())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Algorithm is missing.");
                alert.showAndWait();
            } else if (tfPubKey.getText() == null | "".equals(tfPubKey.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Public key location is missing");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a location for save the public key.");
                alert.showAndWait();
            } else if (tfPriKey.getText() == null | "".equals(tfPriKey.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Private key location is missing");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a location for save the private key.");
                alert.showAndWait();
            } else {
                KeyPairTask keyPairTask = new KeyPairTask(comboBoxKeyPairAlgorithm.getSelectionModel().getSelectedItem(), publicKey, privateKey);
                keyPairTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event1 -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setHeaderText(null);
                    alert.setContentText("Create key pair success.");
                    alert.showAndWait();
                });
                new Thread(keyPairTask).start();
            }
        });
    }

    /**
     * Handler for key pair combobox algorithm
     */
    private void keyPairComboBoxAlgorithmHandler() {
        comboBoxKeyPairAlgorithm.getItems().addAll("RSA", "DSA");
    }

    /**
     * Handler for choose location output public key
     */
    private void keyPairPubKeyHandler() {
        btnPubKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public key file (*.pubkey)", "*.pubkey"));
            publicKey = fileChooser.showSaveDialog(null);
            if (publicKey != null) {
                if (!publicKey.getName().endsWith(".pubkey"))
                    publicKey = new File(publicKey.getAbsolutePath() + ".pubkey");
                tfPubKey.setText(publicKey.getAbsolutePath());
            }
        });
        tfPubKey.setOnAction(event -> {
            publicKey = new File(tfPubKey.getText());
            if (!publicKey.getName().endsWith(".pubkey")) {
                publicKey = new File(publicKey.getAbsolutePath() + ".pubkey");
            }
        });
    }

    /**
     * Handler for choose location output private key
     */
    private void keyPairPriKeyHandler() {
        btnPriKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private key file (*.prikey)", "*.prikey"));
            privateKey = fileChooser.showSaveDialog(null);
            if (privateKey != null) {
                if (!privateKey.getName().endsWith(".prikey"))
                    privateKey = new File(privateKey.getName() + ".prikey");
                tfPriKey.setText(privateKey.getAbsolutePath());
            }
        });
        tfPriKey.setOnAction(event -> {
            privateKey = new File(tfPriKey.getText());
            if (!privateKey.getName().endsWith(".prikey")) {
                privateKey = new File(publicKey.getAbsolutePath() + ".prikey");
            }
        });
    }

    /**
     * Handler for radio button crypto mode
     *
     * @param btnCrypto : radio button
     */
    private void radioBtnCryptoHandler(JFXRadioButton btnCrypto) {
        if (btnCrypto.equals(rBtnKeyPair))
            rBtnKeyPair.setOnAction(event -> {
                paneCrypto.setVisible(false);
                paneCrypto.setDisable(true);
                paneKeyPair.setVisible(true);
                paneKeyPair.setDisable(false);
            });
        else if (btnCrypto.equals(rBtnEn)) {
            rBtnEn.setOnAction(event -> {
                paneCrypto.setVisible(true);
                paneCrypto.setDisable(false);
                paneKeyPair.setVisible(false);
                paneKeyPair.setDisable(true);
                cryptoRadioBtnEnHandler();
            });
        } else if (btnCrypto.equals(rBtnDe)) {
            rBtnDe.setOnAction(event -> {
                paneCrypto.setVisible(true);
                paneCrypto.setDisable(false);
                paneKeyPair.setVisible(false);
                paneKeyPair.setDisable(true);
                cryptoRadioBtnDeHandler();
            });
        }
    }
}
