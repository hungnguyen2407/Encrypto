package encrypto;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Controller {

    public void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    public void about() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("About");
        dialog.setHeaderText("Encrypto v1.2");
        dialog.setContentText("Copyright © 2018 Nguyen Hoang Hung.");
        dialog.showAndWait();
    }
}
