package encrypto;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.security.InvalidKeyException;

public class Ultilities {

    public static void showExceptionHandler(Task task) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        if (task.getException() != null) {
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();
        } else {
            alert.setContentText("An unknown error had occurred.");
            alert.showAndWait();
        }


    }
}
