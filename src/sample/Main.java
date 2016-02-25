package sample;

import ea.core.Phenotype;
import ea.core.Simulator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Scanner;

public class Main extends Application {

    @Override public void start(Stage stage) {
        Platform.setImplicitExit(false);
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Stock Monitoring, 2010");

        XYChart.Series bestFitness = new XYChart.Series();
        bestFitness.setName("Best fitness");
        int generations = 0;
        for(Double p: Simulator.generationalBest){
            bestFitness.getData().add(new XYChart.Data(generations++, p));
        }
        XYChart.Series avgFitness = new XYChart.Series();
        avgFitness.setName("Avg fitness");
        generations = 0;
        for(Double p: Simulator.avgFitnessList){
            avgFitness.getData().add(new XYChart.Data(generations++, p));
        }
        XYChart.Series standardDev = new XYChart.Series();
        standardDev.setName("Standard deviation");
        generations = 0;
        for(Double p: Simulator.standardDeviationList){
            standardDev.getData().add(new XYChart.Data(generations++, p));
        }
        XYChart.Series globalBest = new XYChart.Series();
        globalBest.setName("Global best");
        generations = 0;
        for(Double p: Simulator.globalBest){
            globalBest.getData().add(new XYChart.Data(generations++, p));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(globalBest,bestFitness, avgFitness, standardDev);

        stage.setScene(scene);
        stage.show();

        Platform.runLater(new Runnable() {
            @Override public void run() {
                System.out.println("scan scanner");
                Scanner sc = new Scanner(System.in);
                sc.next();
                System.out.println("past");
                Simulator.init();
                Simulator.run();
            }
        });


    }


    public static void main(String[] args) {
        Simulator.init();
        Simulator.run();
        launch(args);
    }
}
