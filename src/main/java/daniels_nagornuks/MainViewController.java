package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainViewController {

    @FXML
    private ImageView imageView;

    @FXML
    private CheckBox originalImageCheckBox;

    @FXML
    private Pane pane;

    private final FileChooser fileChooser = new FileChooser();
    private File imageFile;

    private WritableImage processedImage;
    private WritableImage processedOriginalImage;
    private Image image;

    private PixelReader pixelReader;
    private PixelWriter processedImagePixelWriter;
    private PixelWriter processedOriginalImagePixelWriter;

    private int imageWidth;
    private int imageHeight;

    private final Color redBloodCellColor = new Color(0.75, 0.3, 0.6, 1);
    private final Color whiteBloodCellColor = new Color(0.45, 0.2, 0.65, 1);
    private final Color backgroundCellColor = new Color(0.95, 0.7, 0.7, 1);

    private HashMap<Integer, BloodCellCluster> bloodCellClusters;
    private PixelArray pixelArray;

    private final int bloodCellPixelSize = 500;

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
        imageFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (imageFile != null) {
            makeAdjustments();
        }
    }

    @FXML
    private void makeAdjustments() {
        Stage adjustmentStage = new Stage();
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ImagePreview.fxml"));
        Scene imagePreviewScene;
        try {
            imagePreviewScene = new Scene(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adjustmentStage.setScene(imagePreviewScene);
        adjustmentStage.setTitle("Image Adjustments Preview");
        ImagePreviewController imagePreviewController = fxmlloader.getController();
        imagePreviewController.setImage(imageFile);
        adjustmentStage.setResizable(false);
        adjustmentStage.initModality(Modality.APPLICATION_MODAL);
        imagePreviewController.setMainViewController(this);
        imagePreviewController.setMainViewStage(adjustmentStage);
        adjustmentStage.show();
    }

    public void setAdjustedImage(Image adjustedImage) {
        image = adjustedImage;
        setImage();
    }

    public double[] scaleToFixedHeight(double originalWidth, double originalHeight) {
        double newWidth = 800;
        double newHeight = originalHeight / originalWidth * newWidth;
        return new double[]{newWidth, newHeight};
    }

    private void setImage() {
        imageWidth = (int) image.getWidth();
        imageHeight = (int) image.getHeight();
        pixelReader = image.getPixelReader();
        processedImage = new WritableImage(imageWidth, imageHeight);
        processedOriginalImage = new WritableImage(imageWidth, imageHeight);
        processedImagePixelWriter = processedImage.getPixelWriter();
        processedOriginalImagePixelWriter = processedOriginalImage.getPixelWriter();
        pane.getChildren().clear();
        pane.getChildren().add(imageView);
        double[] dimensions = scaleToFixedHeight(imageWidth, imageHeight);
        imageView.setFitHeight(dimensions[1]);
        imageView.setFitWidth(dimensions[0]);
        pane.setPrefSize(dimensions[0], dimensions[1]);
        drawImages();
        displayImage();
        detectCells();
        defineCells();
        drawRectangles();
    }

    private void drawImages() {
        drawOriginalImage();
        drawModifiedImage();
    }

    @FXML
    private void displayImage() {
        if (originalImageCheckBox.isSelected()) {
            imageView.setImage(processedOriginalImage);
        } else {
            imageView.setImage(processedImage);
        }
    }

    private void detectCells() {
        int pixelIdBelow;
        int pixelIdToRight;
        int pixelId;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                pixelId = getPixelByCoordinates(x, y);
                if (!pixelArray.isWhite(pixelId)) {
                    pixelIdBelow = y < imageHeight - 1 ? getPixelByCoordinates(x, y + 1) : -1;
                    pixelIdToRight = x < imageWidth - 1 ? getPixelByCoordinates(x + 1, y) : -1;
                    pixelArray.processPixel(pixelId, pixelIdBelow, pixelIdToRight);
                    if (pixelArray.getSize(pixelId) > bloodCellPixelSize) {
                        String type = pixelArray.isRed(pixelId) ? "Red" : "White";
                        bloodCellClusters.put(pixelArray.find(pixelId), new BloodCellCluster(type));
                    }
                }
            }
        }
    }

    private void defineCells() {
        int pixelId;
        BloodCellCluster bloodCellCluster;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                pixelId = getPixelByCoordinates(x, y);
                bloodCellCluster = bloodCellClusters.get(pixelArray.find(pixelId));
                if (bloodCellCluster != null) {
                    updateBloodCellCluster(pixelId);
                }
            }
        }
    }

    private void updateBloodCellCluster(int pixelId) {
        BloodCellCluster bloodCellCluster = bloodCellClusters.get(pixelArray.find(pixelId));
        int[] pixelCoordinates = getCoordinatesByPixel(pixelId);
        if (bloodCellCluster.getStartPosY() == -1) {
            bloodCellCluster.setStartPosY(pixelCoordinates[1]);
            bloodCellCluster.setStartPosX(pixelCoordinates[0]);
            bloodCellCluster.setEndPosX(pixelCoordinates[0]);
            bloodCellCluster.setEndPosY(pixelCoordinates[1]);
            return;
        }

        if (pixelCoordinates[0] < bloodCellCluster.getStartPosX()) {
            bloodCellCluster.setStartPosX(pixelCoordinates[0]);
        }
        if (pixelCoordinates[0] > bloodCellCluster.getEndPosX()) {
            bloodCellCluster.setEndPosX(pixelCoordinates[0]);
        }
        if (pixelCoordinates[1] > bloodCellCluster.getEndPosY()) {
            bloodCellCluster.setEndPosY(pixelCoordinates[1]);
        }
    }

    private int getPixelByCoordinates(int x, int y) {
        return y * imageWidth + x;
    }

    private int[] getCoordinatesByPixel(int pixel) {
        return new int[] {pixel % imageWidth, pixel / imageWidth};
    }

    private void drawModifiedImage() {
        pixelArray = new PixelArray(imageWidth * imageHeight);
        bloodCellClusters = new HashMap<>();
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (isRedBloodCell(x, y)) {
                    processedImagePixelWriter.setColor(x, y, Color.RED);
                    pixelArray.setRed(getPixelByCoordinates(x, y));
                } else if (isWhiteBloodCell(x, y)) {
                    processedImagePixelWriter.setColor(x, y, Color.PURPLE);
                    pixelArray.setPurple(getPixelByCoordinates(x, y));
                } else {
                    processedImagePixelWriter.setColor(x, y, Color.WHITE);
                }
            }
        }
    }

    private void drawOriginalImage() {
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color color = pixelReader.getColor(x, y);
                processedOriginalImagePixelWriter.setColor(x, y, color);
            }
        }
    }

    private void drawRectangles() {
        for (BloodCellCluster bloodCellCluster : bloodCellClusters.values()) {
            double scaleX = imageView.getFitWidth() / imageView.getImage().getWidth();
            double scaleY = imageView.getFitHeight() / imageView.getImage().getHeight();
            Rectangle rectangle = new Rectangle(bloodCellCluster.getStartPosX() * scaleX, bloodCellCluster.getStartPosY() * scaleY, bloodCellCluster.getWidth() * scaleX, bloodCellCluster.getHeight() * scaleY);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(bloodCellCluster.getType().equals("Red") ? Color.GREEN : Color.RED);
            rectangle.setStrokeWidth(2);
            pane.getChildren().add(rectangle);
        }
    }

    private boolean isRedBloodCell(int x, int y) {
        return colorDistance(pixelReader.getColor(x, y), redBloodCellColor) < colorDistance(pixelReader.getColor(x, y), whiteBloodCellColor) &&
                colorDistance(pixelReader.getColor(x, y), redBloodCellColor) < colorDistance(pixelReader.getColor(x, y), backgroundCellColor);
    }

    private boolean isWhiteBloodCell(int x, int y) {
        return colorDistance(pixelReader.getColor(x, y), whiteBloodCellColor) < colorDistance(pixelReader.getColor(x, y), redBloodCellColor) &&
                colorDistance(pixelReader.getColor(x, y), whiteBloodCellColor) < colorDistance(pixelReader.getColor(x, y), backgroundCellColor);
    }

    private static double colorDistance(Color color1, Color color2) {
        double redDistance = color1.getRed() - color2.getRed();
        double greenDistance = color1.getGreen() - color2.getGreen();
        double blueDistance = color1.getBlue() - color2.getBlue();
        return Math.sqrt(redDistance * redDistance + greenDistance * greenDistance + blueDistance * blueDistance);
    }

    /*
    TODO: Add quartile ranges to detect:
            more than 1 cell in a cluster
            detect and ignore noise from tri-color conversion
    TODO: Add numbering and tooltips to rectangles
    TODO: Modify rectangles to highlight cell clusters with blue which have more than 1 cell
    TODO: Add option for user to specify custom min/max sizes for cells
    TODO: Add option to clear image
    TODO: Add info section to view stats (total number of cells, red blood cells, clusters, etc.)
     */

}
