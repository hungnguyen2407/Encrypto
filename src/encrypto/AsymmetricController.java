package encrypto;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class AsymmetricController {

    public ToggleGroup cryptography;
    @FXML
    private AnchorPane paneKeyPair, paneCrypto;
    @FXML
    private JFXTextField tfSrc, tfDes, tfKey, tfPubKey, tfPriKey;
    @FXML
    private JFXButton btnSrc, btnDes, btnKey, btnStart, btnStop, btnPubKey, btnPriKey, btnCreate;
    @FXML
    private JFXRadioButton rBtnEn, rBtnDe, rBtnKeyPair;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding, comboBoxKeyPairAlgorithm;
    @FXML
    private JFXSlider sliderKeySize;

    private File fileInput, fileOutput, key;

    public AsymmetricController() {
    }

    @FXML
    public void initialize() {
        /*
         * ASYMMETRIC PANEL
         * This code below is handle for asymmetric cryptography features and create key pair
         */

        rBtnKeyPair.setOnAction(event -> {
            paneCrypto.setVisible(false);
            paneCrypto.setDisable(true);
            paneKeyPair.setVisible(true);
            paneKeyPair.setDisable(false);
        });

        rBtnEn.setOnAction(event -> {
            paneCrypto.setVisible(true);
            paneCrypto.setDisable(false);
            paneKeyPair.setVisible(false);
            paneKeyPair.setDisable(true);
        });

        rBtnDe.setOnAction(event -> {
            paneCrypto.setVisible(true);
            paneCrypto.setDisable(false);
            paneKeyPair.setVisible(false);
            paneKeyPair.setDisable(true);
        });

        comboBoxKeyPairAlgorithm.getItems().addAll("RSA");
        btnPubKey.setOnAction(event -> {

        });
    }


}
