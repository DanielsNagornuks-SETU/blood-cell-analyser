package daniels_nagornuks;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetailsWindowController {

    @FXML
    private Label detailsLabel;

    public void setDetails(int numBloodCellClusters, int numRedBloodCells, int numWhiteBloodCells) {
        String details = "Number of blood cell clusters: " + numBloodCellClusters
                + "\nNumber of red blood cells: " + numRedBloodCells
                + "\nNumber of white blood cells: " + numWhiteBloodCells
                + "\nTotal number of blood cells: " + (numRedBloodCells + numWhiteBloodCells);
        detailsLabel.setText(details);
    }

}
