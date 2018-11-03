package encrypto;

import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private File fileInput, fileOutput, key;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        //Load fxml file
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        //set title
        primaryStage.setTitle("Encrypto");

        //set icon for Windows
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));

        //set icon for macOS
//        com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon(Main.class.getResource("icon.png")).getImage());

        //create scene
        Scene scene = new Scene(root);

        //set scene for stage
        primaryStage.setScene(scene);

        //not allow resize window
        primaryStage.setResizable(false);

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
        //TextField file input
        final TextField smTfFI = (TextField) primaryStage.getScene().lookup("#smTfFI");

        //TextField file output
        final TextField smTfFO = (TextField) primaryStage.getScene().lookup("#smTfFO");

        //TextField key output
        final TextField smTfK = (TextField) primaryStage.getScene().lookup("#smTfK");


        //Combo box mode
        final ComboBox<String> smCbbMode = (ComboBox<String>) primaryStage.getScene().lookup("#smCbbMode");

        //Combo box key size
        final ComboBox<String> smCbbKeySize = (ComboBox<String>) primaryStage.getScene().lookup("#smCbbKeySize");

        //Combo box padding
        final ComboBox<String> smCbbPadding = (ComboBox<String>) primaryStage.getScene().lookup("#smCbbPadding");

        //Combo box algorithms
        final ComboBox<String> smCbbAlgorithms = (ComboBox<String>) primaryStage.getScene().lookup("#smCbbAlgorithms");
        smCbbAlgorithms.getItems().addAll(
                "AES", "DES", "DESede", "Blowfish", "RC2", "RC5"
        );

        //Handler for choose algorithm, mode, key size, padding
        smCbbAlgorithms.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != smCbbAlgorithms.getSelectionModel().getSelectedItem())
                    switch (smCbbAlgorithms.getSelectionModel().getSelectedItem()) {
                        case "AES":
                            smCbbMode.getItems().clear();
                            smCbbKeySize.getItems().clear();
                            smCbbPadding.getItems().clear();
                            smCbbMode.getItems().addAll("CBC", "ECB", "GCM");
                            smCbbKeySize.getItems().addAll("128", "192", "256");
                            smCbbKeySize.setValue("128");
                            break;
                        case "DES":
                            smCbbMode.getItems().clear();
                            smCbbKeySize.getItems().clear();
                            smCbbPadding.getItems().clear();
                            smCbbMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                            smCbbKeySize.getItems().addAll("56");
                            smCbbKeySize.setValue("56");
                            break;
                        case "DESede":
                            smCbbMode.getItems().clear();
                            smCbbKeySize.getItems().clear();
                            smCbbPadding.getItems().clear();
                            smCbbMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                            smCbbKeySize.getItems().addAll("112", "168");
                            smCbbKeySize.setValue("112");
                            break;
                        case "RC2":
                            smCbbMode.getItems().clear();
                            smCbbKeySize.getItems().clear();
                            smCbbPadding.getItems().clear();
                            smCbbMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                            for (int i = 40; i < 1025; i++) {
                                smCbbKeySize.getItems().add(i + "");
                            }
                            smCbbKeySize.setValue("40");
                            break;
                        case "RC5":
                            smCbbMode.getItems().clear();
                            smCbbKeySize.getItems().clear();
                            smCbbPadding.getItems().clear();
                            smCbbMode.getItems().addAll("CBC", "ECB", "PCBC", "CFB", "OFB", "CTR");
                            for (int i = 40; i < 1025; i++) {
                                smCbbKeySize.getItems().add(i + "");
                            }
                            smCbbKeySize.setValue("40");
                            break;
                    }
            }
        });

        smCbbMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != smCbbMode.getSelectionModel().getSelectedItem())
                    switch (smCbbMode.getSelectionModel().getSelectedItem()) {
                        case "ECB":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                            smCbbPadding.setValue("PKCS5Padding");
                            break;
                        case "CBC":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                            smCbbPadding.setValue("PKCS5Padding");
                            break;
                        case "PCBC":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                            smCbbPadding.setValue("PKCS5Padding");
                            break;
                        case "CFB":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                            smCbbPadding.setValue("PKCS5Padding");
                            break;
                        case "OFB":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                            smCbbPadding.setValue("PKCS5Padding");
                            break;
                        case "CTR":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding", "PKCS5Padding");
                            smCbbPadding.setValue("PKCS5Padding");
                            break;
                        case "GCM":
                            smCbbPadding.getItems().clear();
                            smCbbPadding.getItems().addAll("NoPadding");
                            smCbbPadding.setValue("NoPadding");
                            break;
                    }
            }
        });
        //Btn file input handler
        final Button smBtnFI = (Button) primaryStage.getScene().lookup("#smBtnFI");
        openFileDialogHandler(primaryStage, smTfFI, smBtnFI);

        //Btn file output handler
        final Button smBtnFO = (Button) primaryStage.getScene().lookup("#smBtnFO");
        smBtnFO.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save encrypted file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
                fileOutput = fileChooser.showSaveDialog(primaryStage);
                if (fileOutput != null) {
                    if (!fileOutput.getName().contains(".")) {
                        fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
                    }
                    smTfFO.setText(fileOutput.getAbsolutePath());
                }
            }
        });

        //Btn key output handler
        final Button smBtnK = (Button) primaryStage.getScene().lookup("#smBtnK");
        saveDialogKeyFileExHandler(primaryStage, smTfK, smBtnK);

        //Radio key
        ToggleGroup smCrOrUseKeyGr = new ToggleGroup();
        final RadioButton smRdbtnCrK = (RadioButton) primaryStage.getScene().lookup("#smRdbtnCrK");
        smRdbtnCrK.setToggleGroup(smCrOrUseKeyGr);
        smRdbtnCrK.setSelected(true);
        smRdbtnCrK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                smTfK.clear();
                key = null;
                saveDialogKeyFileExHandler(primaryStage, smTfK, smBtnK);
            }
        });
        final RadioButton smRdbtnUseK = (RadioButton) primaryStage.getScene().lookup("#smRdbtnUseK");
        smRdbtnUseK.setToggleGroup(smCrOrUseKeyGr);
        smRdbtnUseK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                smTfK.clear();
                key = null;
                smBtnK.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save file");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key file (*.key)", "*.key"));
                        key = fileChooser.showOpenDialog(primaryStage);
                        if (key != null) {
                            if (!key.getName().contains(".")) {
                                key = new File(key.getAbsolutePath() + ".key");
                            }
                            smTfK.setText(key.getAbsolutePath());
                        }
                    }
                });
            }
        });

        //Handler radio button encryption and decryption
        ToggleGroup smEnOrDeGr = new ToggleGroup();
        final RadioButton smRdbtnEn = (RadioButton) primaryStage.getScene().lookup("#smRdbtnEn");
        smRdbtnEn.setToggleGroup(smEnOrDeGr);
        smRdbtnEn.setSelected(true);
        smRdbtnEn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                smTfFI.clear();
                smTfFO.clear();
                smTfK.clear();
                openFileDialogHandler(primaryStage, smTfFI, smBtnFI);

                smBtnFO.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save file");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
                        fileOutput = fileChooser.showSaveDialog(primaryStage);
                        if (fileOutput != null) {
                            if (!fileOutput.getName().contains(".")) {
                                fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
                            }
                            smTfFO.setText(fileOutput.getAbsolutePath());
                        }
                    }
                });
                smRdbtnCrK.setDisable(false);
                smRdbtnCrK.setSelected(true);
                saveDialogKeyFileExHandler(primaryStage, smTfK, smBtnK);
            }
        });

        final RadioButton smRdbtnDe = (RadioButton) primaryStage.getScene().lookup("#smRdbtnDe");
        smRdbtnDe.setToggleGroup(smEnOrDeGr);
        smRdbtnDe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                smTfFI.clear();
                smTfFO.clear();
                smTfK.clear();
                smBtnFI.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Choose file");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
                        fileInput = fileChooser.showOpenDialog(primaryStage);
                        if (fileInput != null)
                            smTfFI.setText(fileInput.getAbsolutePath());
                    }
                });

                smBtnFO.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save file");
                        fileOutput = fileChooser.showSaveDialog(primaryStage);
                        if (fileOutput != null) {
                            if (!fileOutput.getName().contains(".")) {
                                fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
                            }
                            smTfFO.setText(fileOutput.getAbsolutePath());
                        }
                    }
                });

                smRdbtnCrK.setDisable(true);
                smRdbtnUseK.setSelected(true);
                smBtnK.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Choose file");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key file (*.key)", "*.key"));
                        key = fileChooser.showOpenDialog(primaryStage);
                        if (key != null) {
                            if (!key.getName().contains(".")) {
                                key = new File(key.getAbsolutePath() + ".key");
                            }
                            smTfK.setText(key.getAbsolutePath());
                        }
                    }
                });
            }
        });

        final ProgressIndicator smProi = (ProgressIndicator) primaryStage.getScene().lookup("#smProi");
        final Button smBtnSt = (Button) primaryStage.getScene().lookup("#smBtnSt");
        smBtnSt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!fileInput.exists()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("File encrypt is missing");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
                    alert.setHeaderText(null);
                    alert.setContentText("Please choose a file for encrypt.");
                    alert.showAndWait();
                } else if (fileOutput == null | "".equals(fileOutput)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Save location is missing");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
                    alert.setHeaderText(null);
                    alert.setContentText("Please choose a location to save the encrypted file.");
                    alert.showAndWait();
                } else if (key == null | "".equals(key)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Key is missing");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
                    alert.setHeaderText(null);
                    alert.setContentText("Please choose a key");
                    alert.showAndWait();
                } else if (smRdbtnEn.isSelected()) {
                    smProi.setProgress(0);
                    smProi.setVisible(true);
                    smBtnSt.setDisable(true);
                    smRdbtnEn.setDisable(true);
                    smRdbtnDe.setDisable(true);
                    smTfFI.setDisable(true);
                    smTfFO.setDisable(true);
                    smTfK.setDisable(true);
                    smBtnFI.setDisable(true);
                    smBtnFO.setDisable(true);
                    smBtnK.setDisable(true);
                    smCbbAlgorithms.setDisable(true);
                    smCbbKeySize.setDisable(true);
                    smCbbMode.setDisable(true);
                    smCbbPadding.setDisable(true);
                    smBtnSt.setText("Encrypting...");
                    smProi.progressProperty().unbind();
                    final SymmetricEncryptionTask symmetricEncryptionTask = new SymmetricEncryptionTask(smCbbAlgorithms.getSelectionModel().getSelectedItem(), smCbbMode.getSelectionModel().getSelectedItem(), smCbbKeySize.getSelectionModel().getSelectedItem(), smCbbPadding.getSelectionModel().getSelectedItem(), fileInput, fileOutput, key);
                    smProi.progressProperty().bind(symmetricEncryptionTask.progressProperty());
                    symmetricEncryptionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            smProi.progressProperty().unbind();
                            smProi.setVisible(false);
                            smBtnSt.setDisable(false);
                            smBtnSt.setText("Start");
                            smRdbtnEn.setDisable(false);
                            smRdbtnDe.setDisable(false);
                            smTfFI.setDisable(false);
                            smTfFO.setDisable(false);
                            smTfK.setDisable(false);
                            smBtnFI.setDisable(false);
                            smBtnFO.setDisable(false);
                            smBtnK.setDisable(false);
                            smCbbAlgorithms.setDisable(false);
                            smCbbKeySize.setDisable(false);
                            smCbbMode.setDisable(false);
                            smCbbPadding.setDisable(false);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Done!");
                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
                            alert.setHeaderText(null);
                            alert.setContentText("Your file had been encrypted.");
                            alert.showAndWait();
                        }
                    });
                    new Thread(symmetricEncryptionTask).start();

                } else if (smRdbtnDe.isSelected()) {
                    smProi.setProgress(0);
                    smProi.setVisible(true);
                    smBtnSt.setDisable(true);
                    smRdbtnEn.setDisable(true);
                    smRdbtnDe.setDisable(true);
                    smTfFI.setDisable(true);
                    smTfFO.setDisable(true);
                    smTfK.setDisable(true);
                    smBtnFI.setDisable(true);
                    smBtnFO.setDisable(true);
                    smBtnK.setDisable(true);
                    smCbbAlgorithms.setDisable(true);
                    smCbbKeySize.setDisable(true);
                    smCbbMode.setDisable(true);
                    smCbbPadding.setDisable(true);
                    smBtnSt.setText("Decrypting...");
                    smProi.progressProperty().unbind();
                    SymmetricDecryptionTask symmetricDecryptionTask = new SymmetricDecryptionTask(smCbbAlgorithms.getSelectionModel().getSelectedItem(), smCbbMode.getSelectionModel().getSelectedItem(), smCbbKeySize.getSelectionModel().getSelectedItem(), smCbbPadding.getSelectionModel().getSelectedItem(), fileInput, fileOutput, key);
                    smProi.progressProperty().bind(symmetricDecryptionTask.progressProperty());
                    symmetricDecryptionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            smProi.progressProperty().unbind();
                            smProi.setVisible(false);
                            smBtnSt.setDisable(false);
                            smBtnSt.setText("Start");
                            smRdbtnEn.setDisable(false);
                            smRdbtnDe.setDisable(false);
                            smTfFI.setDisable(false);
                            smTfFO.setDisable(false);
                            smTfK.setDisable(false);
                            smBtnFI.setDisable(false);
                            smBtnFO.setDisable(false);
                            smBtnK.setDisable(false);
                            smCbbAlgorithms.setDisable(false);
                            smCbbKeySize.setDisable(false);
                            smCbbMode.setDisable(false);
                            smCbbPadding.setDisable(false);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Done!");
                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
                            alert.setHeaderText(null);
                            alert.setContentText("Your file had been decrypted.");
                            alert.showAndWait();
                        }
                    });
                    new Thread(symmetricDecryptionTask).start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Unknown error");
                    alert.showAndWait();
                }
            }
        });


        /*
        ASYMMETRIC PANEL
         */
//        //TextField file input
//        final TextField asmTfFI = (TextField) primaryStage.getScene().lookup("#asmTfFI");
//
//        //TextField file output
//        final TextField asmTfFO = (TextField) primaryStage.getScene().lookup("#asmTfFO");
//
//        //TextField key output
//        final TextField asmTfK = (TextField) primaryStage.getScene().lookup("#asmTfK");
//
//
//        //Combo box mode
//        final ComboBox<String> asmCbbMode = (ComboBox<String>) primaryStage.getScene().lookup("#asmCbbMode");
//
//        //Combo box key size
//        final ComboBox<String> asmCbbKeySize = (ComboBox<String>) primaryStage.getScene().lookup("#asmCbbKeySize");
//
//        //Combo box padding
//        final ComboBox<String> asmCbbPadding = (ComboBox<String>) primaryStage.getScene().lookup("#asmCbbPadding");
//
//        //Combo box algorithms
//        final ComboBox<String> asmCbbAlgorithms = (ComboBox<String>) primaryStage.getScene().lookup("#asmCbbAlgorithms");
//        asmCbbAlgorithms.getItems().addAll(
//                "RSA"
//        );
//
//
//        //Handler for choose algorithm, mode, key size, padding
//        asmCbbAlgorithms.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                if (null != asmCbbAlgorithms.getSelectionModel().getSelectedItem())
//                    switch (asmCbbAlgorithms.getSelectionModel().getSelectedItem()) {
//                        case "RSA":
//                            asmCbbMode.getItems().clear();
//                            asmCbbKeySize.getItems().clear();
//                            asmCbbPadding.getItems().clear();
//                            asmCbbMode.getItems().addAll("ECB");
//                            asmCbbKeySize.getItems().addAll("1024", "2048");
//                            asmCbbKeySize.setValue("1024");
//                            break;
//                    }
//            }
//        });
//
//        asmCbbMode.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                if (null != asmCbbMode.getSelectionModel().getSelectedItem())
//                    switch (asmCbbMode.getSelectionModel().getSelectedItem()) {
//                        case "ECB":
//                            asmCbbPadding.getItems().clear();
//                            asmCbbPadding.getItems().addAll("PKCS1Padding", "OAEPWithSHA-1AndMGF1Padding", "OAEPWithSHA-256AndMGF1Padding");
//                            asmCbbPadding.setValue("PKCS1Padding");
//                            break;
//                    }
//            }
//        });
//        //Btn file input handler
//        final Button asmBtnFI = (Button) primaryStage.getScene().lookup("#asmBtnFI");
//        asmBtnFI.setText("Choose file");
//        asmBtnFI.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Choose file");
//                fileInput = fileChooser.showOpenDialog(primaryStage);
//                if (fileInput != null)
//                    asmTfFI.setText(fileInput.getAbsolutePath());
//            }
//        });
//
//        //Btn file output handler
//        final Button asmBtnFO = (Button) primaryStage.getScene().lookup("#asmBtnFO");
//        asmBtnFO.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Save encrypted file");
//                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
//                fileOutput = fileChooser.showSaveDialog(primaryStage);
//                if (fileOutput != null) {
//                    if (!fileOutput.getName().contains(".")) {
//                        fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
//                    }
//                    asmTfFO.setText(fileOutput.getAbsolutePath());
//                }
//            }
//        });
//
//        //Btn key output handler
//        final Button asmBtnK = (Button) primaryStage.getScene().lookup("#asmBtnK");
//        asmBtnK.setText("Save file");
//        saveDialogKeyFileExHandler(primaryStage, asmTfK, asmBtnK);
//
//        //Handler radio button encryption and decryption
//        ToggleGroup asmEnOrDeGr = new ToggleGroup();
//        final RadioButton asmRdbtnEn = (RadioButton) primaryStage.getScene().lookup("#asmRdbtnEn");
//        asmRdbtnEn.setToggleGroup(asmEnOrDeGr);
//        asmRdbtnEn.setSelected(true);
//        asmRdbtnEn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                asmTfFI.clear();
//                asmTfFO.clear();
//                asmTfK.clear();
//
//                asmBtnFI.setText("Choose file");
//                asmBtnFI.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setTitle("Choose file");
//                        fileInput = fileChooser.showOpenDialog(primaryStage);
//                        if (fileInput != null)
//                            asmTfFI.setText(fileInput.getAbsolutePath());
//                    }
//                });
//
//
//                asmBtnFO.setText("Save file");
//                asmBtnFO.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setTitle("Save file");
//                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
//                        fileOutput = fileChooser.showSaveDialog(primaryStage);
//                        if (fileOutput != null) {
//                            if (!fileOutput.getName().contains(".")) {
//                                fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
//                            }
//                            asmTfFO.setText(fileOutput.getAbsolutePath());
//                        }
//                    }
//                });
//
//
//                asmBtnK.setText("Save file");
//                saveDialogKeyFileExHandler(primaryStage, asmTfK, asmBtnK);
//            }
//        });
//
//        final RadioButton asmRdbtnDe = (RadioButton) primaryStage.getScene().lookup("#asmRdbtnDe");
//        asmRdbtnDe.setToggleGroup(asmEnOrDeGr);
//        asmRdbtnDe.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                asmTfFI.clear();
//                asmTfFO.clear();
//                asmTfK.clear();
//                asmBtnFI.setText("Choose file");
//                asmBtnFI.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setTitle("Choose file");
//                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Encrypted file (*.encrypt)", "*.encrypt"));
//                        fileInput = fileChooser.showOpenDialog(primaryStage);
//                        if (fileInput != null)
//                            asmTfFI.setText(fileInput.getAbsolutePath());
//                    }
//                });
//
//                asmBtnFO.setText("Save file");
//                asmBtnFO.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setTitle("Save file");
//                        fileOutput = fileChooser.showSaveDialog(primaryStage);
//                        if (fileOutput != null) {
//                            if (!fileOutput.getName().contains(".")) {
//                                fileOutput = new File(fileOutput.getAbsolutePath() + ".encrypt");
//                            }
//                            asmTfFO.setText(fileOutput.getAbsolutePath());
//                        }
//                    }
//                });
//
//                asmBtnK.setText("Choose file");
//                asmBtnK.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        FileChooser fileChooser = new FileChooser();
//                        fileChooser.setTitle("Choose file");
//                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key file (*.key)", "*.key"));
//                        key = fileChooser.showOpenDialog(primaryStage);
//                        if (key != null) {
//                            if (!key.getName().contains(".")) {
//                                key = new File(key.getAbsolutePath() + ".key");
//                            }
//                            asmTfK.setText(key.getAbsolutePath());
//                        }
//                    }
//                });
//            }
//        });
//
//        final ProgressBar asmProb = (ProgressBar) primaryStage.getScene().lookup("#asmProb");
//        final Button asmBtnSt = (Button) primaryStage.getScene().lookup("#asmBtnSt");
//        asmBtnSt.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                if (asmRdbtnEn.isSelected()) {
//                    asmProb.setVisible(true);
//                    asmBtnSt.setDisable(true);
//                    asmBtnSt.setText("Encrypting...");
//                    asmProb.progressProperty().unbind();
//                    final SymmetricEncryptionTask symmetricEncryptionTask = new SymmetricEncryptionTask(asmCbbAlgorithms.getSelectionModel().getSelectedItem(), asmCbbMode.getSelectionModel().getSelectedItem(), asmCbbKeySize.getSelectionModel().getSelectedItem(), asmCbbPadding.getSelectionModel().getSelectedItem(), fileInput, fileOutput, key);
//                    asmProb.progressProperty().bind(symmetricEncryptionTask.progressProperty());
//                    symmetricEncryptionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
//                        @Override
//                        public void handle(WorkerStateEvent event) {
//                            asmProb.progressProperty().unbind();
//                            asmProb.setVisible(false);
//                            asmBtnSt.setDisable(false);
//                            asmBtnSt.setText("Start");
//                        }
//                    });
//                    new Thread(symmetricEncryptionTask).start();
//
//                } else if (asmRdbtnDe.isSelected()) {
//                    asmProb.setVisible(true);
//                    asmBtnSt.setDisable(true);
//                    asmBtnSt.setText("Decrypting...");
//                    asmProb.progressProperty().unbind();
//                    SymmetricDecryptionTask symmetricDecryptionTask = new SymmetricDecryptionTask(asmCbbAlgorithms.getSelectionModel().getSelectedItem(), asmCbbMode.getSelectionModel().getSelectedItem(), asmCbbKeySize.getSelectionModel().getSelectedItem(), asmCbbPadding.getSelectionModel().getSelectedItem(), fileInput, fileOutput, key);
//                    asmProb.progressProperty().bind(symmetricDecryptionTask.progressProperty());
//                    symmetricDecryptionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
//                        @Override
//                        public void handle(WorkerStateEvent event) {
//                            asmProb.progressProperty().unbind();
//                            asmProb.setVisible(false);
//                            asmBtnSt.setDisable(false);
//                            asmBtnSt.setText("Start");
//                        }
//                    });
//                    new Thread(symmetricDecryptionTask).start();
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Warning");
//                    alert.setHeaderText(null);
//                    alert.setContentText("You need to choose encryption or decryption a file.");
//                    alert.showAndWait();
//                }
//            }
//        });


        primaryStage.show();
    }

    private void openFileDialogHandler(final Stage stage, final TextField tfFile, Button btnFile) {
        btnFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose file");
                fileInput = fileChooser.showOpenDialog(stage);
                if (fileInput != null)
                    tfFile.setText(fileInput.getAbsolutePath());
            }
        });
    }

    private void saveDialogKeyFileExHandler(final Stage stage, final TextField tfKey, Button btnKey) {
        btnKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key file (*.key)", "*.key"));
                key = fileChooser.showSaveDialog(stage);
                if (key != null) {
                    if (!key.getName().contains(".")) {
                        key = new File(key.getAbsolutePath() + ".key");
                    }
                    tfKey.setText(key.getAbsolutePath());
                }
            }
        });
    }
}
