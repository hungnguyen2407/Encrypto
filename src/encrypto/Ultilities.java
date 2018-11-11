package encrypto;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class Ultilities {

    public static void showExceptionHandler(Task task) {
        if (task.getException() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error had occurred.");
            alert.showAndWait();
        }
    }
}
