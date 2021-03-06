package encrypto.ui;

import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.shape.SVGPath;

public class PasteButton {

    public static void factory(Button pasteBtn, TextInputControl textInputControl) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M128 184c0-30.879 25.122-56 56-56h136V56c0-13.255-10.745-24-24-24h-80.61C204.306 12.89 183.637 0 160 0s-44.306 12.89-55.39 32H24C10.745 32 0 42.745 0 56v336c0 13.255 10.745 24 24 24h104V184zm32-144c13.255 0 24 10.745 24 24s-10.745 24-24 24-24-10.745-24-24 10.745-24 24-24zm184 248h104v200c0 13.255-10.745 24-24 24H184c-13.255 0-24-10.745-24-24V184c0-13.255 10.745-24 24-24h136v104c0 13.2 10.8 24 24 24zm104-38.059V256h-96v-96h6.059a24 24 0 0 1 16.97 7.029l65.941 65.941a24.002 24.002 0 0 1 7.03 16.971z");
        Bounds bounds = svgPath.getBoundsInParent();
        double scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);
        pasteBtn.setGraphic(svgPath);
        pasteBtn.setMaxSize(30, 30);
        pasteBtn.setMinSize(30, 30);
        pasteBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        pasteBtn.setOnAction(event -> {
            textInputControl.setText(Clipboard.getSystemClipboard().getString());
        });
    }
}
