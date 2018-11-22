package encrypto.keypair;

import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;

public class KeyPairController {

    @FXML
    private AnchorPane paneKeyPair;
    @FXML
    private TextField tfPubKey, tfPriKey;
    @FXML
    private Button btnPubKey, btnPriKey, btnCreate;
    @FXML
    private Slider sliKeySize;
    @FXML
    private ComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding, comboBoxKeyPairAlgorithm;

    private File publicKey, privateKey;

    /**
     * Constructor
     */
    public KeyPairController(){}

    @FXML
    public void initialize(){
        paneKeyPairHandler();
    }

    /**
     * Handler for pane key pair
     */
    private void paneKeyPairHandler() {
        keyPairComboBoxAlgorithmHandler();

        keyPairKeySizeHandler();

        keyPairPubKeyHandler();

        keyPairPriKeyHandler();

        keyPairBtnCreateHandler();
    }

    private void keyPairKeySizeHandler() {
        sliKeySize.setMin(1024);
        sliKeySize.setMax(2048);
        sliKeySize.setBlockIncrement(1024);
        sliKeySize.setMajorTickUnit(1024);
        sliKeySize.setMinorTickCount(0);
        sliKeySize.setValue(1024);
        sliKeySize.setSnapToTicks(true);
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
}
