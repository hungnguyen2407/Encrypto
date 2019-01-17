package encrypto;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Date;

public class Controller {

    public Tab tabSymmetric;

    public void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    public Controller() {

    }

    public void about() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-information.png").toString()));
        dialog.setTitle("About");
        dialog.setHeaderText("Encrypto v" + Main.APP_VERSION);
        dialog.setContentText("Copyright © " + (new Date().getYear() + 1900) + " Nguyen Hoang Hung.");
        dialog.showAndWait();
    }
}
