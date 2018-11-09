package encrypto;

import com.jfoenix.controls.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
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
    private JFXTextField tfPubKey, tfPriKey;
    @FXML
    private JFXButton btnStart, btnStop, btnPubKey, btnPriKey, btnCreate;
    @FXML
    private JFXRadioButton rBtnEn, rBtnDe, rBtnKeyPair;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding, comboBoxKeyPairAlgorithm;
    @FXML
    private JFXSlider sliderKeySize;
    @FXML
    private JFXTextArea taInput;
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

    }

    /**
     * Handler for decryption radio button
     */
    private void cryptoRadioBtnDeHandler() {
        //TODO
    }

    /**
     * Handler for encryption radio button
     */
    private void cryptoRadioBtnEnHandler() {
        //TODO
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
        else
            btnCrypto.setOnAction(event -> {
                paneCrypto.setVisible(true);
                paneCrypto.setDisable(false);
                paneKeyPair.setVisible(false);
                paneKeyPair.setDisable(true);
            });
    }


}
