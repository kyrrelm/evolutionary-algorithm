package flatland;/**
 * Created by Kyrre on 06.04.2016.
 */

import ea.core.Simulator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Flatland extends Application {

    private Cell[] cells;
    private double playbackInterval = 2000;
    final double MAX_PLAYBACK_INTERVAL = 3000;
    final double MIN_PLAYBACK_INTERVAL = 100;
    private long sinceLast = 0;

    public LinkedBlockingQueue<BoardState> playback;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        playback = new LinkedBlockingQueue<>();
        cells = new Cell[100];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell(Cell.Type.BLANK);
        }

        GridPane board = new GridPane();
        board.setPadding(new Insets(5, 5, 5, 5));

        for (int i = 0,num = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.add(cells[num++], j, i);
            }
        }
        //GUI setup
        Label labelSpeedSlider = new Label("Animation speed:");
        Slider speedSlider = new Slider(100, MAX_PLAYBACK_INTERVAL, playbackInterval);
        speedSlider.setOrientation(Orientation.HORIZONTAL);
        speedSlider.setMaxWidth(230);
        speedSlider.setPrefWidth(230);
        speedSlider.setBlockIncrement(1000);
        speedSlider.setValue(MAX_PLAYBACK_INTERVAL- playbackInterval +MIN_PLAYBACK_INTERVAL);

        Button newBoard = new Button("New Board(s)");
        newBoard.setOnAction(event ->{
            FlatlandNetwork best = (FlatlandNetwork) Simulator.bestPhenotype;
            best.freshBoards();
            playback.addAll(best.runAgent(true));
        });
        Button graph = new Button("Show graph");
        graph.setOnAction(event ->{
            Stage graphStage = new Stage();
            graphStage.setScene(new Scene(createChart(),800,300));
            graphStage.show();
        });

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(5);
        hbox.getChildren().addAll(labelSpeedSlider,speedSlider, newBoard, graph);
        board.add(hbox,0,10,10,1);

        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flatland");
        primaryStage.getIcons().add(new Image("flatland/img/icons.png", 50, 50, false, false));
        primaryStage.show();

        //GUI updates
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!playback.isEmpty()){
                    if (now - sinceLast > playbackInterval *1000000){
                        sinceLast = now;
                        updateBoard(playback.poll());
                    }
                }
            }
        }.start();

        Task task = new Task<ArrayList<BoardState>>() {
            @Override
            protected ArrayList<BoardState> call() throws Exception {
                try {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Static run: (Y/N)");
                    Simulator.staticRun = (sc.next().toLowerCase().equals("y"));
                    System.out.println("Five runs: (Y/N)");
                    Simulator.fiveRuns = (sc.next().toLowerCase().equals("y"));
                    Simulator.init(Simulator.Problem.FLATLAND);
                    Simulator.run();
                    FlatlandNetwork best = (FlatlandNetwork) Simulator.lastGenerationalBest;
                    playback.addAll(best.runAgent(true));
                }catch (Exception e){
                    System.out.println("Error in work thread:");
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        speedSlider.valueProperty().addListener(cl -> {
            playbackInterval = MAX_PLAYBACK_INTERVAL-speedSlider.getValue()+MIN_PLAYBACK_INTERVAL;
        });
    }

    private void updateBoard(BoardState boardState){
        for (int y = 0, index = 0; y < boardState.BOARD_SIZE; y++) {
            for (int x = 0; x < boardState.BOARD_SIZE; x++, index++) {
                cells[index].setType(boardState.getType(x,y));
            }
        }
    }
    @SuppressWarnings("Duplicates")
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
        for(Double p: Simulator.generationalBestFitness){
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
}
