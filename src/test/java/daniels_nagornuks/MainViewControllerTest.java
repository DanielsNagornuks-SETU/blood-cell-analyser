package daniels_nagornuks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainViewControllerTest {

    private MainViewController mainViewController;

    /* Image pixels (see .ods file in resources) */
    private int[] redPixels = {11, 12, 13, 21, 22, 23, 24, 31, 32, 33, 34, 43, 44,
            37, 38, 46, 47, 48, 56, 57,
            62, 63, 71, 72, 73, 74};

    @BeforeEach
    public void setUp() {
        mainViewController = new MainViewController();
        mainViewController.MIN_CELL_CLUSTER_PIXEL_SIZE = 1;
        mainViewController.imageWidth = 10;
        mainViewController.imageHeight = 10;
        mainViewController.pixelArray = new PixelArray(mainViewController.imageWidth * mainViewController.imageHeight);
        mainViewController.bloodCellClusters = new LinkedHashMap<>();
        for (int pixel : redPixels) {
            mainViewController.pixelArray.setRed(pixel);
        }
    }

    @AfterEach
    public void tearDown() {
        mainViewController = null;
    }

    @Test
    public void numCellsTest() {
        mainViewController.detectCells();
        assertEquals(3, mainViewController.bloodCellClusters.size());
    }

    @Test
    public void bloodCellClustersDimensionsTest() {
        mainViewController.detectCells();
        mainViewController.defineCells();
        /* Expected cells and their dimensions */
        BloodCellCluster bcl1 = new BloodCellCluster("Red", 0);
        bcl1.setStartPosX(1);
        bcl1.setStartPosY(1);
        bcl1.setEndPosX(4);
        bcl1.setEndPosY(4);
        BloodCellCluster bcl2 = new BloodCellCluster("Red", 1);
        bcl2.setStartPosX(6);
        bcl2.setStartPosY(3);
        bcl2.setEndPosX(8);
        bcl2.setEndPosY(5);
        BloodCellCluster bcl3 = new BloodCellCluster("Red", 2);
        bcl3.setStartPosX(1);
        bcl3.setStartPosY(6);
        bcl3.setEndPosX(4);
        bcl3.setEndPosY(7);
        /* Getting roots to use as keys to access the clusters in the hashmap */
        ArrayList<Integer> roots = getCellRoots(mainViewController.pixelArray);
        assertTrue(identicalDimensions(bcl1, mainViewController.bloodCellClusters.get(roots.get(0))));
        assertTrue(identicalDimensions(bcl2, mainViewController.bloodCellClusters.get(roots.get(1))));
        assertTrue(identicalDimensions(bcl3, mainViewController.bloodCellClusters.get(roots.get(2))));
    }

    private boolean identicalDimensions(BloodCellCluster cluster1, BloodCellCluster cluster2) {
        return cluster1.getStartPosX() == cluster2.getStartPosX() &&
                cluster1.getStartPosY() == cluster2.getStartPosY() &&
                cluster1.getHeight() == cluster2.getHeight() &&
                cluster1.getWidth() == cluster2.getWidth();
    }

    private ArrayList<Integer> getCellRoots(PixelArray pixelArray) {
        ArrayList<Integer> roots = new ArrayList<>();
        for (int pixel = 0; pixel < mainViewController.imageHeight * mainViewController.imageWidth; pixel++) {
            if (pixel != pixelArray.find(pixel) && !roots.contains(pixelArray.find(pixel))) {
                roots.add(pixelArray.find(pixel));
            }
        }
        return roots;
    }

}
