package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CellSizesController {

    @FXML
    private ChoiceBox<String> minBloodCellSizeChoiceBox, maxBloodCellSizeChoiceBox;

    @FXML
    private TextField minBloodCellSizeField, maxBloodCellSizeField;

    private Stage mainViewStage;

    private MainViewController mainViewController;

    private boolean isAutoSetMinBloodCellSize;

    private boolean isAutoSetMaxBloodCellSize;

    public void setup() {
        minBloodCellSizeChoiceBox.getItems().addAll("Automatic", "Custom");
        maxBloodCellSizeChoiceBox.getItems().addAll("Automatic", "Custom");
        minBloodCellSizeChoiceBox.getSelectionModel().select(isAutoSetMinBloodCellSize ? "Automatic" : "Custom");
        maxBloodCellSizeChoiceBox.getSelectionModel().select(isAutoSetMaxBloodCellSize ? "Automatic" : "Custom");
        minBloodCellSizeField.setVisible(!isAutoSetMinBloodCellSize);
        maxBloodCellSizeField.setVisible(!isAutoSetMaxBloodCellSize);
        minBloodCellSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isAutoSetMinBloodCellSize = newValue.equals("Automatic");
            minBloodCellSizeField.setVisible(!isAutoSetMinBloodCellSize);
        });
        maxBloodCellSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isAutoSetMaxBloodCellSize = newValue.equals("Automatic");
            maxBloodCellSizeField.setVisible(!isAutoSetMaxBloodCellSize);
        });
        minBloodCellSizeField.setText(Integer.toString(mainViewController.getMinBloodCellSize()));
        maxBloodCellSizeField.setText(Integer.toString(mainViewController.getMaxBloodCellSize()));
    }

    @FXML
    private void confirm() {
        mainViewController.setAutoSetMinBloodCellSize(isAutoSetMinBloodCellSize);
        mainViewController.setAutoSetMaxBloodCellSize(isAutoSetMaxBloodCellSize);
        if (!isAutoSetMinBloodCellSize)
            mainViewController.setMinBloodCellSize(Integer.parseInt(minBloodCellSizeField.getText()));
        if (!isAutoSetMaxBloodCellSize)
            mainViewController.setMaxBloodCellSize(Integer.parseInt(maxBloodCellSizeField.getText()));
        mainViewController.applyCellSizesAndDisplayInfographics();
        mainViewStage.close();
    }

    public void setAutoSetMinBloodCellSize(boolean autoSetMinBloodCellSize) {
        isAutoSetMinBloodCellSize = autoSetMinBloodCellSize;
    }

    public void setAutoSetMaxBloodCellSize(boolean autoSetMaxBloodCellSize) {
        isAutoSetMaxBloodCellSize = autoSetMaxBloodCellSize;
    }

    public void setMainViewStage(Stage mainViewStage) {
        this.mainViewStage = mainViewStage;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
