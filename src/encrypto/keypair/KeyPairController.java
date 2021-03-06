package encrypto.keypair;

import encrypto.ui.InformationDialog;
import encrypto.ui.WarningDialog;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
                new WarningDialog("Missing algorithm!", "Please choose an algorithm.");
            } else if (tfPubKey.getText() == null | "".equals(tfPubKey.getText())) {
                new WarningDialog("Missing public key!", "Please choose a location for save the public key.");
            } else if (tfPriKey.getText() == null | "".equals(tfPriKey.getText())) {
                new WarningDialog("Missing private key!", "Please choose a location for save the private key.");
            } else {
                KeyPairTask keyPairTask = new KeyPairTask(comboBoxKeyPairAlgorithm.getSelectionModel().getSelectedItem(), publicKey, privateKey);
                keyPairTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event1 -> {
                    new InformationDialog("Done!", "Create key pair success.");
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
