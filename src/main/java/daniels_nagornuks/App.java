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
        DisjointSets.resetPixelArray(100);
        System.out.println(DisjointSets.getSize(5));
        System.out.println(DisjointSets.getHeight(5));
        DisjointSets.quickUnion(4,5);
        System.out.println(DisjointSets.getSize(5));
        System.out.println(DisjointSets.getHeight(5));
        DisjointSets.quickUnion(5,6);
        System.out.println(DisjointSets.getSize(5));
        System.out.println(DisjointSets.getHeight(5));
        //launch();
    }

}