package gui;

import ea.core.Simulator;
import ea.core.Simulator.ParentSelection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Scanner;

import static ea.core.Simulator.AdultSelection.*;

public class Main extends Application {

    @Override public void start(Stage stage) {
        Platform.setImplicitExit(false);
        stage.setTitle("");
        runEa(stage);
    }

    protected void runEa(Stage stage){
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                simulatorRun();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            stage.setScene(new Scene(createChart(),800,300));
            stage.show();
            runEa(stage);
        });
        new Thread(task).start();
    }

    protected LineChart<Number,Number> createChart(){
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generations");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("");

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

        lineChart.getData().addAll(globalBest,bestFitness, avgFitness, standardDev);

        return lineChart;
    }

    protected void simulatorRun(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Run default values: (Y/N)");
        if (!sc.next().toLowerCase().equals("y")){
            System.out.println("Number of iterations default("+Simulator.loopLimit+"): ");
            Simulator.loopLimit = sc.nextInt();
            System.out.println("Population size default("+Simulator.populationSize+"): ");
            Simulator.populationSize = sc.nextInt();
            System.out.println("Crossover rate default("+Simulator.crossoverRate+"): ");
            Simulator.crossoverRate = sc.nextFloat();
            System.out.println("PerComponentMutationRate default("+Simulator.perComponentMutationRate+"): ");
            Simulator.perComponentMutationRate = sc.nextFloat();
        }
        System.out.println("adultSelection default("+Simulator.adultSelection+"):");
        System.out.println("1) FULL_GENERATIONAL_REPLACEMENT");
        System.out.println("2) OVER_PRODUCTION");
        System.out.println("3) GENERATIONAL_MIXING");
        System.out.println("4) OVER_PRODUCED_GENERATIONAL_MIXING");
        switch (sc.nextInt()){
            case 1: {
                Simulator.adultSelection = FULL_GENERATIONAL_REPLACEMENT;
                break;
            }
            case 2: {
                Simulator.adultSelection = OVER_PRODUCTION;
                break;
            }
            case 3: {
                Simulator.adultSelection = GENERATIONAL_MIXING;
                System.out.println("Elitism default("+Simulator.elitism+"): ");
                Simulator.elitism = sc.nextFloat();
                break;
            }
            case 4: {
                Simulator.adultSelection = OVER_PRODUCED_GENERATIONAL_MIXING;
                System.out.println("Elitism default("+Simulator.elitism+"): ");
                Simulator.elitism = sc.nextFloat();
                break;
            }
        }
        System.out.println("parentSelection default("+Simulator.parentSelection+"):");
        System.out.println("1) FITNESS_PROPORTIONAL");
        System.out.println("2) SIGMA");
        System.out.println("3) RANK");
        System.out.println("4) TOURNAMENT");
        switch (sc.nextInt()){
            case 1: {
                Simulator.parentSelection = ParentSelection.FITNESS_PROPORTIONAL;
                break;
            }
            case 2: {
                Simulator.parentSelection = ParentSelection.SIGMA;
                break;
            }
            case 3: {
                Simulator.parentSelection = ParentSelection.RANK;
                System.out.println("RANK_MAX default("+Simulator.RANK_MAX+"): ");
                Simulator.RANK_MAX = sc.nextDouble();
                Simulator.RANK_MIN=2-Simulator.RANK_MAX;
                break;
            }
            case 4: {
                Simulator.parentSelection = ParentSelection.TOURNAMENT;
                System.out.println("Size of K default("+Simulator.K+"): ");
                Simulator.K = sc.nextInt();
                System.out.println("Size of e default("+Simulator.e+"): ");
                Simulator.e = sc.nextFloat();
                break;
            }
        }
        System.out.println("Problem default("+Simulator.parentSelection+"):");
        System.out.println("1) OneMax");
        System.out.println("2) LolzPrefix");
        System.out.println("3) Surprising Sequence");
        switch (sc.nextInt()) {
            case 1: {
                Simulator.problem = Simulator.Problem.ONE_MAX;
                System.out.println("OneMax size default(" + Simulator.oneMaxSize + "): ");
                Simulator.oneMaxSize = sc.nextInt();
                break;
            }
            case 2: {
                Simulator.problem = Simulator.Problem.LOLZ;
                System.out.println("Lolz size default(" + Simulator.lolzSize + "): ");
                Simulator.lolzSize = sc.nextInt();
                System.out.println("Lolz z default(" + Simulator.lolzZ + "): ");
                Simulator.lolzZ = sc.nextInt();
                break;
            }
            case 3: {
                Simulator.problem = Simulator.Problem.SURPRISE;
                System.out.println("Surprise S default(" + Simulator.surpriseS + "): ");
                Simulator.surpriseS = sc.nextInt();
                System.out.println("Surprise length default(" + Simulator.surpriseLength + "): ");
                Simulator.surpriseLength = sc.nextInt();
                System.out.println("Global default(" + Simulator.global + ") 1 = true: ");
                Simulator.global = (sc.nextInt() == 1);
                break;
            }
        }
        Simulator.init();
        Simulator.run();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
