package encrypto.asymmetric;

import encrypto.Ultilities;
import encrypto.ui.CopyButton;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
        lblKeyDeHandler();
        tfKeyDeHandler();
        btnKeyDeHandler();
        lblInputDeHandler();
        btnStartDeHandler();
        taInputHandler();
    }


    private void taInputHandler() {
        taInput.clear();
        taInput.setWrapText(true);
    }

    private void btnStartDeHandler() {
        btnStart.setOnAction(event -> {
            if (comboBoxAlgorithm.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a type algorithm.");
                alert.show();
            } else if (comboBoxMode.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a mode.");
                alert.show();
            } else if (comboBoxPadding.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a padding.");
                alert.show();
            } else if (privateKey == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a private key.");
                alert.show();
            } else if (taInput.getText() == null | taInput.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please fill content to decrypt.");
                alert.show();
            } else {
                disableCryptoPane();
                final AsymmetricDecryptionTask asymmetricDecryptionTask = new AsymmetricDecryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), comboBoxPadding.getSelectionModel().getSelectedItem(), privateKey, Base64.getDecoder().decode(taInput.getText()));
                asymmetricDecryptionTask.setOnSucceeded(event1 -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-information.png").toString()));
                    alert.setTitle("Done!");
                    alert.setHeaderText("Your cipher text had been decrypted. ");
                    VBox dialogPaneContent = new VBox();
                    TextArea textArea = new TextArea(new String(asymmetricDecryptionTask.getValue(), StandardCharsets.UTF_8));
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
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
            if (privateKey != null) {
                if (!privateKey.getName().endsWith(".prikey")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                    alert.setTitle("Wrong format");
                    alert.setHeaderText("Please choose a file with private key extension.");
                    alert.show();
                } else {
                    tfKey.setText(privateKey.getAbsolutePath());
                }
            }
        });
    }

    private void tfKeyDeHandler() {
        tfKey.clear();
        privateKey = null;
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
            if (comboBoxAlgorithm.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a type algorithm.");
                alert.show();
            } else if (comboBoxMode.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a mode.");
                alert.show();
            } else if (comboBoxPadding.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a padding.");
                alert.show();
            } else if (publicKey == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please choose a public key.");
                alert.show();
            } else if (taInput.getText() == null | taInput.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                alert.setTitle("Warning");
                alert.setHeaderText("Please fill content to encrypt.");
                alert.show();
            } else {
                disableCryptoPane();
                final AsymmetricEncryptionTask asymmetricEncryptionTask = new AsymmetricEncryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), comboBoxPadding.getSelectionModel().getSelectedItem(), publicKey, taInput.getText().getBytes());

                asymmetricEncryptionTask.setOnSucceeded(event1 -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-information.png").toString()));
                    alert.setTitle("Done!");
                    alert.setHeaderText("Your plain text had been encrypted. ");
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
    }

    private void resetCryptoPane() {
        paneCrypto.setDisable(false);
    }

    private void lblInputEnHandler() {
        lblInput.setText("Plain text");
    }

    private void btnKeyEnHandler() {
        btnKey.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Public key (*.pubkey)", "*.pubkey"));
            publicKey = fileChooser.showOpenDialog(null);
            if (publicKey != null) {
                if (!publicKey.getName().endsWith(".pubkey")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
                    alert.setTitle("Wrong format");
                    alert.setHeaderText("Please choose a file with public key extension.");
                    alert.show();
                } else {
                    tfKey.setText(publicKey.getAbsolutePath());
                }
            }
        });
    }

    private void tfKeyEnHandler() {
        tfKey.clear();
        publicKey = null;
    }

    private void lblKeyEnHandler() {
        lblKey.setText("Public key");
    }

    private void btnPasteHandler() {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M128 184c0-30.879 25.122-56 56-56h136V56c0-13.255-10.745-24-24-24h-80.61C204.306 12.89 183.637 0 160 0s-44.306 12.89-55.39 32H24C10.745 32 0 42.745 0 56v336c0 13.255 10.745 24 24 24h104V184zm32-144c13.255 0 24 10.745 24 24s-10.745 24-24 24-24-10.745-24-24 10.745-24 24-24zm184 248h104v200c0 13.255-10.745 24-24 24H184c-13.255 0-24-10.745-24-24V184c0-13.255 10.745-24 24-24h136v104c0 13.2 10.8 24 24 24zm104-38.059V256h-96v-96h6.059a24 24 0 0 1 16.97 7.029l65.941 65.941a24.002 24.002 0 0 1 7.03 16.971z");
        Bounds bounds = svgPath.getBoundsInParent();
        double scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);
        btnPaste.setGraphic(svgPath);
        btnPaste.setMaxSize(30, 30);
        btnPaste.setMinSize(30, 30);
        btnPaste.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnPaste.setOnAction(event -> {
            taInput.setText(Clipboard.getSystemClipboard().getString());
        });
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
