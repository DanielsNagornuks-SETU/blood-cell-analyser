package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class MainViewController {

    @FXML
    private ImageView imageView;

    @FXML
    private CheckBox originalImageCheckBox;

    private final FileChooser fileChooser = new FileChooser();
    private File imageFile;

    private WritableImage writableImage;
    private Image image;

    private PixelReader pixelReader;
    private PixelWriter pixelWriter;

    private int imageWidth;
    private int imageHeight;

    private final Color redBloodCellColor = new Color(0.75, 0.3, 0.6, 1);
    private final Color whiteBloodCellColor = new Color(0.45, 0.2, 0.65, 1);
    private final Color backgroundCellColor = new Color(0.95, 0.7, 0.7, 1);

    @FXML
    private void initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
    }

    @FXML
    private void loadImage() {
        imageFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (imageFile != null) {
            setImage();
        }
    }

    @FXML
    private void exitApplication() {
        System.exit(0);
    }

    @FXML
    private void makeAdjustments() {

    }

    @FXML
    private void drawImage() {
        if (originalImageCheckBox.isSelected()) {
            drawOriginalImage();
        } else {
            drawModifiedImage();
        }
    }

    private void setImage() {
        image = new Image(imageFile.toURI().toString());
        imageWidth = (int) image.getWidth();
        imageHeight = (int) image.getHeight();
        pixelReader = image.getPixelReader();
        writableImage = new WritableImage(imageWidth, imageHeight);
        pixelWriter = writableImage.getPixelWriter();
        imageView.setImage(writableImage);
        drawImage();
    }

    private void drawModifiedImage() {
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                if (isRed(x, y)) {
                    pixelWriter.setColor(x, y, Color.RED);
                } else if (isWhite(x, y)) {
                    pixelWriter.setColor(x, y, Color.PURPLE);
                } else {
                    pixelWriter.setColor(x, y, Color.WHITE);
                }
            }
        }
    }

    private void drawOriginalImage() {
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }
    }

    private boolean isRed(int x, int y) {
        return colorDistance(pixelReader.getColor(x, y), redBloodCellColor) < colorDistance(pixelReader.getColor(x, y), whiteBloodCellColor) &&
                colorDistance(pixelReader.getColor(x, y), redBloodCellColor) < colorDistance(pixelReader.getColor(x, y), backgroundCellColor);
    }

    private boolean isWhite(int x, int y) {
        return colorDistance(pixelReader.getColor(x, y), whiteBloodCellColor) < colorDistance(pixelReader.getColor(x, y), redBloodCellColor) &&
                colorDistance(pixelReader.getColor(x, y), whiteBloodCellColor) < colorDistance(pixelReader.getColor(x, y), backgroundCellColor);
    }

    private static double colorDistance(Color color1, Color color2) {
        double redDistance = color1.getRed() - color2.getRed();
        double greenDistance = color1.getGreen() - color2.getGreen();
        double blueDistance = color1.getBlue() - color2.getBlue();
        return Math.sqrt(redDistance * redDistance + greenDistance * greenDistance + blueDistance * blueDistance);
    }



}
