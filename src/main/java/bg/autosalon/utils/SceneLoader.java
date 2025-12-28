package bg.autosalon.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SceneLoader {

    private static Stage primaryStage;

    private SceneLoader() {}

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }


    public static void openScene(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneLoader.class.getResource("/bg/autosalon/views/" + fxmlName)
            );
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Object openSceneWithController(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneLoader.class.getResource("/bg/autosalon/views/" + fxmlName)
            );
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
