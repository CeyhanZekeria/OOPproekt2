package bg.autosalon;

import bg.autosalon.utils.SceneLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneLoader.setStage(primaryStage);
        SceneLoader.openScene("register.fxml");
    }


    public static void main(String[] args) {
        launch();
    }
}
