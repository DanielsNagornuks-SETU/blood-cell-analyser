package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MainViewController {

    @FXML
    private ImageView imageView;

    @FXML
    private CheckBox originalImageCheckBox, showClusterOutlinesCheckBox, showClustersNumberedCheckBox, showTooltipsCheckBox;

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

    /* Protected variables and methods to allow JUnit testing, otherwise they are to be private */

    protected int imageWidth;
    protected int imageHeight;

    private final Color redBloodCellColor = new Color(0.70, 0.44, 0.57, 1);
    private final Color whiteBloodCellColor = new Color(0.61, 0.45, 0.78, 1);
    private final Color backgroundCellColor = new Color(0.95, 0.8, 0.8, 1);

    protected LinkedHashMap<Integer, BloodCellCluster> bloodCellClusters;
    protected PixelArray pixelArray;

    protected int MIN_CELL_CLUSTER_PIXEL_SIZE = 25;

    private int minBloodCellSize;
    private int maxBloodCellSize;
    private int medianBloodCellSize = 0;

    private int calculatedMinBloodCellSize;
    private int calculatedMaxBloodCellSize;

    private boolean autoSetMinBloodCellSize = true;
    private boolean autoSetMaxBloodCellSize = true;

    private WelcomeScreenController welcomeScreenController;
    private Scene welcomeScreenScene;

    private int numRedBloodCells, numWhiteBloodCells, numBloodCellClusters;

    public WelcomeScreenController getWelcomeScreenController() {
        return welcomeScreenController;
    }

    public void setWelcomeScreenController(WelcomeScreenController welcomeScreenController) {
        this.welcomeScreenController = welcomeScreenController;
    }

    public Scene getWelcomeScreenScene() {
        return welcomeScreenScene;
    }

    public void setWelcomeScreenScene(Scene welcomeScreenScene) {
        this.welcomeScreenScene = welcomeScreenScene;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public int getMinBloodCellSize() {
        return minBloodCellSize;
    }

    public int getMaxBloodCellSize() {
        return maxBloodCellSize;
    }

    public void setMinBloodCellSize(int minBloodCellSize) {
        this.minBloodCellSize = minBloodCellSize;
    }

    public void setMaxBloodCellSize(int maxBloodCellSize) {
        this.maxBloodCellSize = maxBloodCellSize;
    }

    public boolean isAutoSetMaxBloodCellSize() {
        return autoSetMaxBloodCellSize;
    }

    public void setAutoSetMaxBloodCellSize(boolean autoSetMaxBloodCellSize) {
        this.autoSetMaxBloodCellSize = autoSetMaxBloodCellSize;
    }

    public boolean isAutoSetMinBloodCellSize() {
        return autoSetMinBloodCellSize;
    }

    public void setAutoSetMinBloodCellSize(boolean autoSetMinBloodCellSize) {
        this.autoSetMinBloodCellSize = autoSetMinBloodCellSize;
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
        imageFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (imageFile != null) {
            adjustImage();
        }
    }

    @FXML
    private void clearImage() {
        image = null;
        imageView.setImage(null);
        resetPane();
        try {
            switchScene();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private void switchScene() throws IOException {
        Stage stage = (Stage) imageView.getScene().getWindow();
        stage.setScene(welcomeScreenScene);
    }

    @FXML
    private void displayDetails() {
        Stage detailsStage = new Stage();
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("DetailsWindow.fxml"));
        Scene detailsScene;
        try {
            detailsScene = new Scene(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        detailsStage.setScene(detailsScene);
        detailsStage.setTitle("Image Details Window");
        DetailsWindowController detailsWindowController = fxmlloader.getController();
        detailsWindowController.setDetails(numBloodCellClusters, numRedBloodCells, numWhiteBloodCells);
        detailsStage.setResizable(false);
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.getIcons().add(new Image(App.class.getResourceAsStream("appIcon.png")));
        detailsStage.show();
    }

    @FXML
    public void adjustImage() {
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
        adjustmentStage.getIcons().add(new Image(App.class.getResourceAsStream("appIcon.png")));
        adjustmentStage.show();
    }

    @FXML
    private void setBloodCellSizes() {
        Stage bloodCellSizeStage = new Stage();
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("CellSizes.fxml"));
        Scene cellSizesScene;
        try {
            cellSizesScene = new Scene(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bloodCellSizeStage.setScene(cellSizesScene);
        bloodCellSizeStage.setTitle("Cell Sizes Settings");
        CellSizesController cellSizesController = fxmlloader.getController();
        cellSizesController.setAutoSetMinBloodCellSize(autoSetMinBloodCellSize);
        cellSizesController.setAutoSetMaxBloodCellSize(autoSetMaxBloodCellSize);
        cellSizesController.setMainViewStage(bloodCellSizeStage);
        cellSizesController.setMainViewController(this);
        cellSizesController.setup();
        bloodCellSizeStage.setResizable(false);
        bloodCellSizeStage.initModality(Modality.APPLICATION_MODAL);
        bloodCellSizeStage.getIcons().add(new Image(App.class.getResourceAsStream("appIcon.png")));
        bloodCellSizeStage.show();
    }

    public void setAdjustedImage(Image adjustedImage) {
        image = adjustedImage;
        setImage();
    }

    private void setImage() {
        imageWidth = (int) image.getWidth();
        imageHeight = (int) image.getHeight();
        pixelReader = image.getPixelReader();
        double[] dimensions = scaleToFixedHeight(imageWidth, imageHeight);
        imageView.setFitHeight(dimensions[1]);
        imageView.setFitWidth(dimensions[0]);
        pane.setPrefSize(dimensions[0], dimensions[1]);
        processedImage = new WritableImage(imageWidth, imageHeight);
        processedOriginalImage = new WritableImage(imageWidth, imageHeight);
        processedImagePixelWriter = processedImage.getPixelWriter();
        processedOriginalImagePixelWriter = processedOriginalImage.getPixelWriter();
        drawImages();
        displayImage();
        detectCells();
        defineCells();
        defineCalculatedCellSizes();
        applyCellSizesAndDisplayInfographics();
    }

    private void resetPane() {
        pane.getChildren().clear();
        pane.getChildren().add(imageView);
    }

    private void drawImages() {
        drawOriginalImage();
        drawModifiedImageAndSetupPixelArray();
    }

    @FXML
    private void displayImage() {
        if (originalImageCheckBox.isSelected()) {
            imageView.setImage(processedOriginalImage);
        } else {
            imageView.setImage(processedImage);
        }
    }

    @FXML
    private void updatePane() {
        drawInfographics(showTooltipsCheckBox.isSelected(), showClusterOutlinesCheckBox.isSelected(), showClustersNumberedCheckBox.isSelected());
    }

    protected void detectCells() {
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixelId = getPixelByCoordinates(x, y);
                if (!pixelArray.isWhite(pixelId)) {
                    int pixelIdBelow = y + 1 < imageHeight ? getPixelByCoordinates(x, y + 1) : -1;
                    int pixelIdToRight = x + 1 < imageWidth ? getPixelByCoordinates(x + 1, y) : -1;
                    pixelArray.processPixel(pixelId, pixelIdBelow, pixelIdToRight);
                }
            }
        }
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixelId = getPixelByCoordinates(x, y);
                if (!pixelArray.isWhite(pixelId) && pixelArray.getSize(pixelId) > MIN_CELL_CLUSTER_PIXEL_SIZE) {
                    String type = pixelArray.isRed(pixelId) ? "Red" : "White";
                    int rootPixel = pixelArray.find(pixelId);
                    bloodCellClusters.put(rootPixel, new BloodCellCluster(type, rootPixel));
                }
            }
        }
    }

    protected void defineCells() {
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixelId = getPixelByCoordinates(x, y);
                BloodCellCluster bloodCellCluster = bloodCellClusters.get(pixelArray.find(pixelId));
                if (bloodCellCluster != null) {
                    updateBloodCellClusterDimensions(pixelId);
                }
            }
        }
    }

    protected void updateBloodCellClusterDimensions(int pixelId) {
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

    protected int getPixelByCoordinates(int x, int y) {
        return y * imageWidth + x;
    }

    protected int[] getCoordinatesByPixel(int pixel) {
        return new int[] {pixel % imageWidth, pixel / imageWidth};
    }

    private void drawModifiedImageAndSetupPixelArray() {
        pixelArray = new PixelArray(imageWidth * imageHeight);
        bloodCellClusters = new LinkedHashMap<>();
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

    public void applyCellSizesAndDisplayInfographics() {
        minBloodCellSize = isAutoSetMinBloodCellSize() ? calculatedMinBloodCellSize : minBloodCellSize;
        maxBloodCellSize = isAutoSetMaxBloodCellSize() ? calculatedMaxBloodCellSize : maxBloodCellSize;
        medianBloodCellSize = (minBloodCellSize + maxBloodCellSize) / 2;
        detectCells();
        defineCells();
        defineCalculatedCellSizes();
        updateBloodCellClusters();
        drawInfographics(showTooltipsCheckBox.isSelected(), showClusterOutlinesCheckBox.isSelected(), showClustersNumberedCheckBox.isSelected());
    }

    public void updateBloodCellClusters() {
        for (BloodCellCluster bloodCellCluster : bloodCellClusters.values()) {
            int height = bloodCellCluster.getHeight();
            int width = bloodCellCluster.getWidth();
            if (bloodCellCluster.getType().equals("White")) {
                if (height < minBloodCellSize && width < minBloodCellSize)
                    bloodCellCluster.setNumCells(0);
                else
                    bloodCellCluster.setNumCells(1);
            } else {
                if (height < minBloodCellSize && width < minBloodCellSize) {
                    bloodCellCluster.setNumCells(0);
                } else if (height < maxBloodCellSize && width < maxBloodCellSize) {
                    bloodCellCluster.setNumCells(1);
                } else {
                    bloodCellCluster.setNumCells((height * width) / (medianBloodCellSize * medianBloodCellSize));
                }
            }
        }
    }

    private void drawInfographics(boolean tooltipsEnabled, boolean outlinesEnabled, boolean numberingEnabled) {
        resetPane();
        int seqNum = 1;
        numRedBloodCells = 0;
        numWhiteBloodCells = 0;
        numBloodCellClusters = 0;
        for (BloodCellCluster bloodCellCluster : bloodCellClusters.values()) {
            if (bloodCellCluster.getNumCells() > 0) {
                double scaleX = imageView.getFitWidth() / imageView.getImage().getWidth();
                double scaleY = imageView.getFitHeight() / imageView.getImage().getHeight();
                Rectangle rectangle = new Rectangle(bloodCellCluster.getStartPosX() * scaleX, bloodCellCluster.getStartPosY() * scaleY, bloodCellCluster.getWidth() * scaleX, bloodCellCluster.getHeight() * scaleY);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(!outlinesEnabled ? Color.TRANSPARENT : !bloodCellCluster.getType().equals("Red") ? Color.RED : bloodCellCluster.getNumCells() > 1 ? Color.BLUE : Color.GREEN);
                rectangle.setStrokeWidth(2);
                Text text = new Text((bloodCellCluster.getStartPosX() + 3) * scaleX, (bloodCellCluster.getStartPosY() + bloodCellCluster.getHeight() - 3) * scaleY, String.valueOf(seqNum));
                if (tooltipsEnabled) {
                    Tooltip tooltip = new Tooltip("Number of cells: " + bloodCellCluster.getNumCells());
                    tooltip.setShowDelay(Duration.millis(100));
                    tooltip.setFont(new Font(14));
                    Tooltip.install(rectangle, tooltip);
                    Tooltip.install(text, tooltip);
                }
                if (numberingEnabled) {
                    text.setFont(new Font(18));
                } else {
                    text.setFont(new Font(0));
                }
                pane.getChildren().addAll(rectangle, text);
                if (!bloodCellCluster.getType().equals("Red"))
                    numWhiteBloodCells ++;
                else
                    numRedBloodCells += bloodCellCluster.getNumCells();
                numBloodCellClusters++;
                seqNum++;
            }
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

    public double[] scaleToFixedHeight(double originalWidth, double originalHeight) {
        double newWidth = 1000;
        double newHeight = originalHeight / originalWidth * newWidth;
        return new double[]{newWidth, newHeight};
    }

    private void defineCalculatedCellSizes() {
        int[] cellSizes = getCalculatedBloodCellSizes();
        calculatedMinBloodCellSize = cellSizes[0];
        calculatedMaxBloodCellSize = cellSizes[1];
    }

    public int[] getCalculatedBloodCellSizes() {
        int sizesLength = bloodCellClusters.size();
        int[] sizes = new int[sizesLength];
        int index = 0;
        for (BloodCellCluster bloodCellCluster : bloodCellClusters.values()) {
            sizes[index++] = bloodCellCluster.getWidth();
        }
        sort(sizes);
        int d3 = sizes[sizesLength * 3 / 10];
        int d8 = sizes[sizesLength * 8 / 10];
        return new int[] {d3, d8};
    }

    private static void sort(int[] array) {
        final int arraySize = array.length;
        int gap = 1;
        while (gap < arraySize / 3) {
            gap = gap * 3 + 1;
        }
        while (gap > 0) {
            for (int index = gap; index < arraySize; index++) {
                int elem = array[index];
                int innerIndex = index;
                while (innerIndex >= gap && array[innerIndex - gap] > elem) {
                    array[innerIndex] = array[innerIndex - gap];
                    innerIndex -= gap;
                }
                array[innerIndex] = elem;
            }
            gap /= 3;
        }
    }

}
