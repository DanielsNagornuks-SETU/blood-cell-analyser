package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class ImagePreviewController {

    @FXML
    private Slider saturationSlider;

    @FXML
    private Slider hueSlider;

    @FXML
    private Slider brightnessSlider;

    @FXML
    private ImageView previewImageView;

    private final ColorAdjust colorAdjust = new ColorAdjust();

    private MainViewController mainViewController;
    private Stage mainViewStage;

    @FXML
    public void initialize() {
        colorAdjust.brightnessProperty().bind(brightnessSlider.valueProperty());
        colorAdjust.hueProperty().bind(hueSlider.valueProperty());
        colorAdjust.saturationProperty().bind(saturationSlider.valueProperty());
        previewImageView.setEffect(colorAdjust);
    }

    @FXML
    private void confirm() {
        mainViewController.setAdjustedImage(previewImageView.snapshot(null, null));
        mainViewStage.close();
    }

    public void setImage(File imageFile) {
        previewImageView.setImage(new Image(imageFile.toURI().toString()));
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setMainViewStage(Stage mainViewStage) {
        this.mainViewStage = mainViewStage;
    }

}
