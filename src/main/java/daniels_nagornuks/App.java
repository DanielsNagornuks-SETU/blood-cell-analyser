package daniels_nagornuks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderWelcomeScreen = new FXMLLoader(App.class.getResource("WelcomeScreen.fxml"));
        FXMLLoader fxmlLoaderMainView = new FXMLLoader(App.class.getResource("MainView.fxml"));
        Scene sceneWelcomeScreen = new Scene(fxmlLoaderWelcomeScreen.load());
        Scene sceneMainView = new Scene(fxmlLoaderMainView.load());
        WelcomeScreenController controllerWelcomeScreenController = fxmlLoaderWelcomeScreen.getController();
        MainViewController controllerMainViewController = fxmlLoaderMainView.getController();
        controllerWelcomeScreenController.setMainViewController(controllerMainViewController);
        controllerMainViewController.setWelcomeScreenController(controllerWelcomeScreenController);
        controllerWelcomeScreenController.setMainViewScene(sceneMainView);
        controllerMainViewController.setWelcomeScreenScene(sceneWelcomeScreen);
        stage.setScene(sceneWelcomeScreen);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("appIcon.png")));
        // <a href="https://www.flaticon.com/free-icons/red-blood-cells" title="red blood cells icons">Red blood cells icons created by dDara - Flaticon</a>
        stage.setTitle("Blood Cell Analyzer App");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}