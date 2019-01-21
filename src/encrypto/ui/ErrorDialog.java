package encrypto.ui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ErrorDialog extends Alert {

    public ErrorDialog(String title, String headerText) {
        super(AlertType.ERROR);
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-error.png").toString()));
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.show();
    }
}
