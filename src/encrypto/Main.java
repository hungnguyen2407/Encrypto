package encrypto;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.stage.Stage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main extends Application {

    static final String APP_VERSION = "1.4.8";

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
        primaryStage.getIcons().add(new javafx.scene.image.Image(Main.class.getResourceAsStream("icon.png")));

        //set icon for macOS
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method getApplication = util.getMethod("getApplication");
            Object application = getApplication.invoke(util);
            Class params[] = new Class[1];
            params[0] = java.awt.Image.class;
            Method setDockIconImage = util.getMethod("setDockIconImage", params);
            setDockIconImage.invoke(application, SwingFXUtils.fromFXImage(new javafx.scene.image.Image(Main.class.getResourceAsStream("icon.png")), null));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //create scene
        Scene scene = new Scene(root);

        //set scene for stage
        primaryStage.setScene(scene);

        //not allow resize window
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
