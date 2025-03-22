package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class WelcomeScreenController {

    private final FileChooser fileChooser = new FileChooser();
    private File imageFile;

    @FXML
    private Label welcomeLabel;

    private MainViewController mainViewController;
    private Scene mainViewScene;

    public MainViewController getMainViewController() {
        return mainViewController;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public Scene getMainViewScene() {
        return mainViewScene;
    }

    public void setMainViewScene(Scene mainViewScene) {
        this.mainViewScene = mainViewScene;
    }

    @FXML
    private void initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
    }

    @FXML
    private void exitApplication() {
        System.exit(0);
    }

    @FXML
    private void loadImage() {
        imageFile = fileChooser.showOpenDialog(welcomeLabel.getScene().getWindow());
        if (imageFile != null) {
            try {
                switchScene();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    private void switchScene() throws IOException {
        mainViewController.setImageFile(imageFile);
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setScene(mainViewScene);
        mainViewController.adjustImage();
    }

}
