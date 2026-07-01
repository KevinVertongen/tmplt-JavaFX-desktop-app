package be.mnt.template.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
public final class TemplateStage {
    private final FXMLLoader fxmlLoader;
    private final URL fxmlTemplate = TemplateStage.class.getResource("/fxml/template.fxml");

    private Stage templateStage;

    public TemplateStage(final FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public void show(final Stage stage) throws IOException {
        fxmlLoader.setLocation(fxmlTemplate);
        final Parent root = fxmlLoader.load();

        this.templateStage = Objects.requireNonNull(stage);
        templateStage.setScene(new Scene(root));
        templateStage.setTitle("Template");
        templateStage.show();
    }

    public void close() {
        templateStage.close();
    }
}
