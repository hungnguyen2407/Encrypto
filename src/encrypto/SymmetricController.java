package encrypto;

import com.jfoenix.controls.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;

import java.io.File;

public class SymmetricController {

    public ToggleGroup cryptography;
    @FXML
    private JFXTextField tfSrc, tfDes, tfKey;
    @FXML
    private JFXButton btnSrc, btnDes, btnKey, btnStart, btnStop;
    @FXML
    private JFXRadioButton rBtnEn, rBtnDe;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXComboBox<String> comboBoxAlgorithm, comboBoxMode, comboBoxPadding;
    @FXML
    private JFXSlider sliderKeySize;

    private File fileInput, fileOutput, key;

    public SymmetricController() {
    }

    @FXML
    public void initialize() {
        /*
         * SYMMETRIC PANEL
         * This code below is handle for symmetric cryptography features
         * Cryptography supported list:
         * - AES
         * - DES
         * - DESede
         * - RC2
         * - RC4
         * - RC5
         */

        //Combo box algorithms
        comboBoxAlgorithm.getItems().addAll(
                "AES", "DES", "DESede", "Blowfish", "RC2", "RC4", "RC5"
        );

        //Handler for choose algorithm, mode, key size, padding
        comboBoxAlgorithm.setOnAction(event -> {
            if (null != comboBoxAlgorithm.getSelectionModel().getSelectedItem())
                switch (comboBoxAlgorithm.getSelectionModel().getSelectedItem()) {
                    case "AES":
                        comboBoxMode.setDisable(false);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxMode.getItems().addAll("CBC", "ECB", "GCM");
                        sliderKeySize.setMin(128);
                        sliderKeySize.setMax(256);
                        sliderKeySize.setBlockIncrement(64);
                        sliderKeySize.setMajorTickUnit(64);
                        sliderKeySize.setMinorTickCount(0);
                        sliderKeySize.setValue(128);
                        sliderKeySize.setSnapToTicks(true);
                        break;
                    case "DES":
                        comboBoxMode.setDisable(false);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                        sliderKeySize.setMin(56);
                        sliderKeySize.setMax(56);
                        sliderKeySize.setBlockIncrement(0);
                        sliderKeySize.setMajorTickUnit(1);
                        sliderKeySize.setMinorTickCount(0);
                        sliderKeySize.setValue(56);
                        sliderKeySize.setSnapToTicks(true);
                        break;
                    case "DESede":
                        comboBoxMode.setDisable(false);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                        sliderKeySize.setMin(112);
                        sliderKeySize.setMax(168);
                        sliderKeySize.setBlockIncrement(56);
                        sliderKeySize.setMajorTickUnit(56);
                        sliderKeySize.setMinorTickCount(0);
                        sliderKeySize.setValue(112);
                        sliderKeySize.setSnapToTicks(true);
                        break;
                    case "Blowfish":
                        comboBoxMode.setDisable(false);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                        sliderKeySize.setMin(0);
                        sliderKeySize.setMax(2040);
                        sliderKeySize.setBlockIncrement(1);
                        sliderKeySize.setMajorTickUnit(2046);
                        sliderKeySize.setMinorTickCount(0);
                        sliderKeySize.setValue(1024);
                        break;
                    case "RC2":
                        comboBoxMode.setDisable(false);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                        sliderKeySize.setMin(40);
                        sliderKeySize.setMax(1024);
                        sliderKeySize.setBlockIncrement(1);
                        sliderKeySize.setMajorTickUnit(1024);
                        sliderKeySize.setMinorTickCount(40);
                        sliderKeySize.setValue(512);
                        break;
                    case "RC4":
                        comboBoxMode.setDisable(true);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.setDisable(true);
                        comboBoxPadding.getItems().clear();
                        sliderKeySize.setMin(1);
                        sliderKeySize.setMax(256);
                        sliderKeySize.setBlockIncrement(1);
                        sliderKeySize.setMajorTickUnit(256);
                        sliderKeySize.setMinorTickCount(1);
                        sliderKeySize.setValue(128);
                        break;
                    case "RC5":
                        comboBoxMode.setDisable(true);
                        comboBoxMode.getItems().clear();
                        sliderKeySize.setDisable(false);
                        comboBoxPadding.setDisable(true);
                        comboBoxPadding.getItems().clear();
                        sliderKeySize.setMin(1);
                        sliderKeySize.setMax(255);
                        sliderKeySize.setBlockIncrement(1);
                        sliderKeySize.setMajorTickUnit(255);
                        sliderKeySize.setMinorTickCount(1);
                        sliderKeySize.setValue(128);
                        break;
                }
        });

        comboBoxMode.setOnAction(event -> {
            if (null != comboBoxMode.getSelectionModel().getSelectedItem())
                switch (comboBoxMode.getSelectionModel().getSelectedItem()) {
                    case "ECB":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                        comboBoxPadding.setValue("PKCS5Padding");
                        break;
                    case "CBC":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                        comboBoxPadding.setValue("PKCS5Padding");
                        break;
                    case "PCBC":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                        comboBoxPadding.setValue("PKCS5Padding");
                        break;
                    case "CFB":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                        comboBoxPadding.setValue("PKCS5Padding");
                        break;
                    case "OFB":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                        comboBoxPadding.setValue("PKCS5Padding");
                        break;
                    case "CTR":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                        comboBoxPadding.setValue("PKCS5Padding");
                        break;
                    case "GCM":
                        comboBoxPadding.setDisable(false);
                        comboBoxPadding.getItems().clear();
                        comboBoxPadding.getItems().addAll("NoPadding");
                        comboBoxPadding.setValue("NoPadding");
                        break;
                }
        });

        rBtnEn.setOnAction(event -> {
            btnStart.setDisable(false);
            tfSrc.setDisable(false);
            tfSrc.clear();
            tfDes.setDisable(false);
            tfDes.clear();
            tfKey.setDisable(false);
            tfKey.clear();
            btnSrc.setDisable(false);
            btnSrc.setOnAction(event1 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose file");
                fileInput = fileChooser.showOpenDialog(null);
                if (fileInput != null)
                    tfSrc.setText(fileInput.getAbsolutePath());
            });
            btnDes.setDisable(false);
            btnDes.setOnAction(event12 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save encrypted file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
                fileOutput = fileChooser.showSaveDialog(null);
                if (fileOutput != null) {
                    if (!fileOutput.getName().contains(".")) {
                        fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
                    }
                    tfDes.setText(fileOutput.getAbsolutePath());
                }
            });
            btnKey.setDisable(false);
            btnKey.setOnAction(event13 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key file (*.key)", "*.key"));
                key = fileChooser.showSaveDialog(null);
                if (key != null) {
                    if (!key.getName().contains(".")) {
                        key = new File(key.getAbsolutePath() + ".key");
                    }
                    tfKey.setText(key.getAbsolutePath());
                }
            });
        });

        rBtnDe.setOnAction(event -> {
            btnStart.setDisable(false);
            tfSrc.setDisable(false);
            tfSrc.clear();
            tfDes.setDisable(false);
            tfDes.clear();
            tfKey.setDisable(false);
            tfKey.clear();
            btnSrc.setDisable(false);
            btnSrc.setOnAction(event14 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose encrypted file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
                fileInput = fileChooser.showOpenDialog(null);
                if (fileInput != null)
                    tfSrc.setText(fileInput.getAbsolutePath());
            });
            btnDes.setDisable(false);
            btnDes.setOnAction(event15 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save file");
                fileOutput = fileChooser.showSaveDialog(null);
                if (fileOutput != null) {
                    if (!fileOutput.getName().contains(".")) {
                        fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
                    }
                    tfDes.setText(fileOutput.getAbsolutePath());
                }
            });
            btnKey.setDisable(false);
            btnKey.setOnAction(event16 -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key file (*.key)", "*.key"));
                key = fileChooser.showOpenDialog(null);
                if (key != null)
                    tfKey.setText(key.getAbsolutePath());
            });
        });


        btnStart.setOnAction(event -> {
            if (tfSrc.getText() == null | "".equals(tfSrc.getText()) || !fileInput.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("File is missing");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a file.");
                alert.showAndWait();
            } else if (tfDes.getText() == null | "".equals(tfDes.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Save file location is missing");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a location to save the file.");
                alert.showAndWait();
            } else if (tfKey.getText() == null | "".equals(tfKey.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Key location is missing");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a location");
                alert.showAndWait();
            } else if (rBtnEn.isSelected()) {
                makeViewWhenStart();
                final SymmetricEncryptionTask symmetricEncryptionTask = new SymmetricEncryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), String.valueOf((int) sliderKeySize.getValue()), comboBoxPadding.getSelectionModel().getSelectedItem(), fileInput, fileOutput, key);
                progressBar.progressProperty().bind(symmetricEncryptionTask.progressProperty());
                symmetricEncryptionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event17 -> {
                    resetViewWhenDone();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setHeaderText(null);
                    alert.setContentText("Your file had been encrypted.");
                    alert.showAndWait();
                });
                new Thread(symmetricEncryptionTask).start();
                btnStop.setOnAction(event1 -> {
                    resetViewWhenDone();
                    symmetricEncryptionTask.cancel();
                });
            } else if (rBtnDe.isSelected()) {
                makeViewWhenStart();
                final SymmetricDecryptionTask symmetricDecryptionTask = new SymmetricDecryptionTask(comboBoxAlgorithm.getSelectionModel().getSelectedItem(), comboBoxMode.getSelectionModel().getSelectedItem(), String.valueOf((int) sliderKeySize.getValue()), comboBoxPadding.getSelectionModel().getSelectedItem(), fileInput, fileOutput, key);
                progressBar.progressProperty().bind(symmetricDecryptionTask.progressProperty());
                symmetricDecryptionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event18 -> {
                    resetViewWhenDone();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setHeaderText(null);
                    alert.setContentText("Your file had been decrypted.");
                    alert.showAndWait();
                });
                Thread thread = new Thread(symmetricDecryptionTask);
                thread.start();
                btnStop.setOnAction(event1 -> {
                    resetViewWhenDone();
                    symmetricDecryptionTask.cancel();
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unknown error");
                alert.showAndWait();
            }
        });
    }

    private void makeViewWhenStart() {
        progressBar.setProgress(0);
        progressBar.setVisible(true);
        rBtnEn.setDisable(true);
        rBtnDe.setDisable(true);
        tfSrc.setDisable(true);
        tfDes.setDisable(true);
        tfKey.setDisable(true);
        btnSrc.setDisable(true);
        btnDes.setDisable(true);
        btnKey.setDisable(true);
        comboBoxAlgorithm.setDisable(true);
        sliderKeySize.setDisable(true);
        comboBoxMode.setDisable(true);
        comboBoxPadding.setDisable(true);
        btnStart.setVisible(false);
        btnStart.setDisable(true);
        btnStop.setVisible(true);
        btnStop.setDisable(false);
        progressBar.progressProperty().unbind();
    }

    private void resetViewWhenDone() {
        progressBar.progressProperty().unbind();
        progressBar.setVisible(false);
        btnStart.setDisable(false);
        btnStart.setVisible(true);
        btnStop.setDisable(true);
        btnStop.setVisible(false);
        rBtnEn.setDisable(false);
        rBtnDe.setDisable(false);
        tfSrc.setDisable(false);
        tfDes.setDisable(false);
        tfKey.setDisable(false);
        btnSrc.setDisable(false);
        btnDes.setDisable(false);
        btnKey.setDisable(false);
        comboBoxAlgorithm.setDisable(false);
        sliderKeySize.setDisable(false);
        comboBoxMode.setDisable(false);
        comboBoxPadding.setDisable(false);
        if (comboBoxAlgorithm.getSelectionModel().getSelectedItem().equals("RC4")) {
            comboBoxMode.setDisable(true);
            comboBoxPadding.setDisable(true);
        }
    }
}
