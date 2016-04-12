package flatland;/**
 * Created by Kyrre on 06.04.2016.
 */

import ann.Neuron;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
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
        speedSlider.setMaxWidth(300);
        speedSlider.setPrefWidth(300);
        speedSlider.setBlockIncrement(1000);
        speedSlider.setValue(MAX_PLAYBACK_INTERVAL- playbackInterval +MIN_PLAYBACK_INTERVAL);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(5);
        hbox.getChildren().addAll(labelSpeedSlider,speedSlider);
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
                        System.out.println("weeeee");
                        //setButton((int) (Math.random()*100));
                        BoardState boardState = playback.poll();
                        updateBoard(boardState);
                        for (Cell.Type t: boardState.sense()){
                            System.out.print(t+", ");
                        }
                        System.out.println();
                    }
                }
            }
        }.start();

        Task task = new Task<ArrayList<BoardState>>() {
            @Override
            protected ArrayList<BoardState> call() throws Exception {
                try {
                    FlatlandNetwork fl = new FlatlandNetwork(null, 100, new Agent(new BoardState(4, 4, 0.33f, 0.33f), false), Neuron.Function.HYPERBOLIC);
                    fl.develop(null);
                    //fl.fitness();
                    playback.addAll(fl.runAgent(true));
                    System.out.println("here here");
                }catch (Exception e){
                    System.out.println("Error in work thread:");
                    e.printStackTrace();
                }

//                BoardState bs = new BoardState(2,1, 0.33f, 0.33f);
//                Agent a = new Agent(bs, true);
//                for (int i = 0; i < 20; i++) {
//                    a.actRand();
//                }
//                playback.addAll(a.getHistory());
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

    boolean turn = true;

    private void setButton(int index){
        Cell.Type type = turn ? Cell.Type.FOOD : Cell.Type.POISON;
        turn = !turn;
        cells[index].setType(type);
    }

}
