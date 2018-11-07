package encrypto;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

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
        dialog.setTitle("About");
        dialog.setHeaderText("Encrypto v1.4.1");
        dialog.setContentText("Copyright © 2018 Nguyen Hoang Hung.");
        dialog.showAndWait();
    }
}
