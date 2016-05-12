package moea;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kyrre on 12.05.2016.
 */
public class FXWrapper extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Simulation sim = new Simulation(3000, 500, 0.8f, 0.01f);
        sim.run();
        Stage graphStage = new Stage();
        graphStage.setScene(new Scene(sim.createChart(false),800,800));
        graphStage.show();
        Stage graphStage2 = new Stage();
        graphStage2.setScene(new Scene(sim.createChart(true),800,800));
        graphStage2.show();
    }

    public static void main(String[] args) {
        try {
            Cities.populateFromFile("files/Cost.xlsx","files/Distance.xlsx");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        launch(args);
    }
}
