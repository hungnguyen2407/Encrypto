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

    static final String APP_VERSION = "1.6";

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
        primaryStage.getIcons().add(new javafx.scene.image.Image(Main.class.getResourceAsStream("logo.png")));

        //set icon for macOS
        if ("Mac OS X".equals(System.getProperty("os.name"))) {
            try {
                Class macOSApplication = Class.forName("com.apple.eawt.Application");
                Method getApplication = Class.forName("com.apple.eawt.Application").getMethod("getApplication");
                Object application = getApplication.invoke(macOSApplication);
                Class params[] = new Class[1];
                params[0] = java.awt.Image.class;
                Method setDockIconImage = Class.forName("com.apple.eawt.Application").getMethod("setDockIconImage", params);
                setDockIconImage.invoke(application, SwingFXUtils.fromFXImage(new javafx.scene.image.Image(Main.class.getResourceAsStream("logo.png")), null));
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
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
