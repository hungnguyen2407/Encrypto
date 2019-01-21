package encrypto.messagedigest;

import encrypto.ui.CopyButton;
import encrypto.ui.ErrorDialog;
import encrypto.ui.InformationDialog;
import encrypto.ui.PasteButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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
        PasteButton.factory(btnPaste, taInput);
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
                    Alert alert = new InformationDialog("Success!", "Hash the file success");
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
                });
                messageDigestCreateDigestTask.setOnFailed(event1 -> {
                    resetView();
                    new ErrorDialog("Error!", messageDigestCreateDigestTask.getException().getMessage());
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
                        new InformationDialog("Success!", "Hash matched.");
                    } else {
                        new ErrorDialog("Failed!", "Hash not matched.");
                    }
                });

                messageDigestVerifyDigestTask.setOnFailed(event1 -> {
                    resetView();
                    new ErrorDialog("Error!", messageDigestVerifyDigestTask.getException().getMessage());
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
