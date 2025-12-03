package bg.autosalon.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    // Зареждане на цяла сцена
    public static void openScene(String fxmlName) {
        try {
            String fullPath = "/bg/autosalon/views/" + fxmlName;
            Parent root = FXMLLoader.load(SceneLoader.class.getResource(fullPath));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Parent loadNode(String fxmlName) {
        try {
            String fullPath = "/bg/autosalon/views/" + fxmlName;
            return FXMLLoader.load(SceneLoader.class.getResource(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
