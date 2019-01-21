package encrypto.ui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class InformationDialog extends Alert {
    public InformationDialog(String title, String headerText) {
        super(AlertType.INFORMATION);
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/com/sun/javafx/scene/control/skin/modena/dialog-information.png").toString()));
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.show();
    }
}
