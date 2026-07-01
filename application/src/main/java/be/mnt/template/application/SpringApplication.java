package be.mnt.template.application;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "be.mnt.template")
public class SpringApplication {

    static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }

}
