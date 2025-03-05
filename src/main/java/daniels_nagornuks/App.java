package daniels_nagornuks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLOutput;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 900);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DisjointSets.resetElements(10);
        DisjointSets.smartUnion(2,1);
        DisjointSets.smartUnion(3,1);
        DisjointSets.smartUnion(4,3);
        DisjointSets.smartUnion(6,5);
        DisjointSets.smartUnion(0,7);
        DisjointSets.smartUnion(8,7);
        DisjointSets.smartUnion(9,7);
        DisjointSets.smartUnion(4,6);
        for (int i = 0; i < 10; i++) {
            System.out.println("Element: " + i + ", Root: " + DisjointSets.find(i) + ", Size: " + DisjointSets.getSize(i) + ", Height: " + DisjointSets.getHeight(i));
        }
        System.exit(0);
        //launch();
    }

}