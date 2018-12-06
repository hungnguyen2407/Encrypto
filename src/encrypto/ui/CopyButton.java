package encrypto.ui;

import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class CopyButton extends Button {
    private TextInputControl inputControl;
    private Labeled labeled;

    public CopyButton(TextInputControl inputControl, Labeled labeled) {
        this.inputControl = inputControl;
        this.labeled = labeled;
        init();
    }

    private void init() {
        this.setText("Copy");
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M320 448v40c0 13.255-10.745 24-24 24H24c-13.255 0-24-10.745-24-24V120c0-13.255 10.745-24 24-24h72v296c0 30.879 25.121 56 56 56h168zm0-344V0H152c-13.255 0-24 10.745-24 24v368c0 13.255 10.745 24 24 24h272c13.255 0 24-10.745 24-24V128H344c-13.2 0-24-10.8-24-24zm120.971-31.029L375.029 7.029A24 24 0 0 0 358.059 0H352v96h96v-6.059a24 24 0 0 0-7.029-16.97z");
        Bounds bounds = svgPath.getBoundsInParent();
        double scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);
        this.setGraphic(svgPath);
        this.setMaxSize(30, 30);
        this.setMinSize(30, 30);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setOnAction(event2 -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(this.inputControl.getText());
            if (clipboard.setContent(content)) {
                labeled.setTextFill(Color.GREEN);
                labeled.setText("  Copied!");
            } else {
                labeled.setTextFill(Color.RED);
                labeled.setText("  Failed!");
            }
        });
    }

}
