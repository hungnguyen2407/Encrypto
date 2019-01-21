package encrypto.ui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WarningDialog extends Alert {
    public WarningDialog(String title, String headerText) {
        super(AlertType.WARNING);
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png").toString()));
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.show();
    }

}
