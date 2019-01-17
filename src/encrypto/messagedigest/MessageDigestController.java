package encrypto.messagedigest;

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

public class MessageDigestController {

    public ToggleGroup rBtn;
    @FXML
    private ComboBox<String> comboBoxAlgorithm;

    @FXML
    private Button btnFile, btnStart, btnStop, btnPaste;

    @FXML
    private RadioButton rBtnHash, rBtnVerify;

    @FXML
    private TextArea taInput;

    @FXML
    private AnchorPane paneInput;

    @FXML
    private ProgressIndicator progressIndicator;

    private File file;

    public MessageDigestController() {
    }

    @FXML
    public void initialize() {
        rBtnHandler();
        btnPasteHandler();
        taInputHandler();
        comboBoxAlgorithmHandler();
        btnFileHandler();
        btnStartHandler();
    }

    private void taInputHandler() {
        taInput.setWrapText(true);
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

    private void rBtnHandler() {
        rBtnHashHandler();
        rBtnVerifyHandler();
    }

    private void rBtnVerifyHandler() {
        rBtnVerify.setOnAction(event -> {
            paneInput.setDisable(false);
            taInput.setVisible(true);
            taInput.setDisable(false);
            btnPaste.setVisible(true);
            btnPaste.setDisable(false);
        });
    }

    private void rBtnHashHandler() {
        rBtnHash.setOnAction(event -> {
            paneInput.setDisable(false);
            taInput.setVisible(false);
            taInput.setDisable(true);
            btnPaste.setVisible(false);
            btnPaste.setDisable(true);
        });
    }

    private void btnStartHandler() {
        btnStart.setOnAction(event -> {
            rBtnHash.setDisable(true);
            rBtnVerify.setDisable(true);
            paneInput.setDisable(true);
            btnStart.setVisible(false);
            btnStart.setDisable(true);
            btnStop.setVisible(true);
            btnStop.setDisable(false);
            progressIndicator.progressProperty().unbind();
            progressIndicator.setVisible(true);
            if (rBtnHash.isSelected()) {
                MessageDigestCreateDigestTask messageDigestCreateDigestTask = new MessageDigestCreateDigestTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), file);
                progressIndicator.progressProperty().bind(messageDigestCreateDigestTask.progressProperty());
                new Thread(messageDigestCreateDigestTask).start();
                messageDigestCreateDigestTask.setOnSucceeded(event1 -> {
                    resetView();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-information.png").toString()));
                    alert.setTitle("Success!");
                    alert.setHeaderText("Hash the file success.");
                    VBox dialogPaneContent = new VBox();
                    TextArea textArea = new TextArea(messageDigestCreateDigestTask.getValue());
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    Label lblCopy = new Label("");
                    Button btnCopy = new CopyButton(textArea, lblCopy);
                    FlowPane flowPane = new FlowPane();
                    flowPane.getChildren().addAll(btnCopy, lblCopy);
                    dialogPaneContent.getChildren().addAll(new Label("Hash:"), textArea, flowPane);
                    alert.getDialogPane().setContent(dialogPaneContent);
                    alert.showAndWait();
                });
                messageDigestCreateDigestTask.setOnFailed(event1 -> {
                    resetView();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-error.png").toString()));
                    alert.setTitle("Error!");
                    alert.setHeaderText(messageDigestCreateDigestTask.getException().getMessage());
                    alert.showAndWait();
                });
                btnStop.setOnAction(event1 -> {
                    messageDigestCreateDigestTask.cancel();
                    resetView();
                });
            } else {
                MessageDigestVerifyDigestTask messageDigestVerifyDigestTask = new MessageDigestVerifyDigestTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), file, taInput.getText());
                progressIndicator.progressProperty().bind(messageDigestVerifyDigestTask.progressProperty());
                new Thread(messageDigestVerifyDigestTask).start();
                messageDigestVerifyDigestTask.setOnSucceeded(event1 -> {
                    resetView();
                    if (messageDigestVerifyDigestTask.getValue()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-information.png").toString()));
                        alert.setTitle("Success!");
                        alert.setHeaderText("Verify success.");
                        alert.setContentText("Hash matched.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-error.png").toString()));
                        alert.setTitle("Failed!");
                        alert.setHeaderText("Verify failed.");
                        alert.setContentText("Hash not matched.");
                        alert.showAndWait();
                    }
                });

                messageDigestVerifyDigestTask.setOnFailed(event1 -> {
                    resetView();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-error.png").toString()));
                    alert.setTitle("Error!");
                    alert.setHeaderText(messageDigestVerifyDigestTask.getException().getMessage());
                    alert.showAndWait();
                });
                btnStop.setOnAction(event1 -> {
                    messageDigestVerifyDigestTask.cancel();
                    resetView();
                });
            }
        });
    }

    private void resetView() {
        rBtnHash.setDisable(false);
        rBtnVerify.setDisable(false);
        paneInput.setDisable(false);
        btnStart.setVisible(true);
        btnStart.setDisable(false);
        btnStop.setVisible(false);
        btnStop.setDisable(true);
        progressIndicator.setVisible(false);
        progressIndicator.progressProperty().unbind();
    }

    private void btnFileHandler() {
        btnFile.setOnAction(event -> {
            File tmp = new FileChooser().showOpenDialog(null);
            if (tmp != null) {
                file = tmp;
                btnFile.setText(tmp.getName());
            }
        });
    }

    private void comboBoxAlgorithmHandler() {
        comboBoxAlgorithm.getItems().addAll("MD2", "MD4", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-3", "SHA-384", "SHA-512");
    }
}
