package encrypto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
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
        if("Mac OS X".equals(System.getProperty("os.name")))
        com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon(Main.class.getResource("icon.png")).getImage());

        //create scene
        Scene scene = new Scene(root);

        //set scene for stage
        primaryStage.setScene(scene);

        //not allow resize window
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
