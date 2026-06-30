package be.mnt.template.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class TemplateStage {

    private final Stage primaryStage;
    private final URL fxmlTemplate = TemplateStage.class.getResource ("/fxml/template.fxml");

    public TemplateStage(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() throws IOException {
        final Parent root = FXMLLoader.load(fxmlTemplate);
        final Scene scene = new Scene(root);

        primaryStage.setTitle("Template");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void close() {
        primaryStage.close();
    }
}
