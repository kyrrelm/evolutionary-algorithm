package flatland;/**
 * Created by Kyrre on 06.04.2016.
 */

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class Flatland extends Application {

    Player player = Player.O_PLAYER;
    Button[] cells;
    double playbackSpeed = 500;
    final double MAX_PLAYBACK_INTERVAL = 5000;
    final double MIN_PLAYBACK_INTERVAL = 100;
    long sinceLast = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Image imgBlank = new Image("flatland/img/blank.png", 50, 50, false, false);
        cells = new Button[100];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Button("", new ImageView(imgBlank));
            registerOnAction(cells[i]);
        }

        GridPane board = new GridPane();
        int num = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.add(cells[num++], i, j);
            }
        }
        Label labelSpeedSlider = new Label("Animation speed:");
        Slider speedSlider = new Slider(100, MAX_PLAYBACK_INTERVAL, playbackSpeed);
        speedSlider.setOrientation(Orientation.HORIZONTAL);
        speedSlider.setMaxWidth(300);
        speedSlider.setPrefWidth(300);
        speedSlider.setBlockIncrement(100);
        speedSlider.setValue(playbackSpeed);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(5);
        hbox.getChildren().addAll(labelSpeedSlider,speedSlider);
        board.add(hbox,0,10,10,1);



        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TicTacToe By Legato");
        primaryStage.getIcons().add(new Image("flatland/img/icons.png", 50, 50, false, false));
        primaryStage.show();

        Timeline timer = new Timeline();
        timer.setCycleCount(Timeline.INDEFINITE);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - sinceLast > playbackSpeed*1000000){
                    sinceLast = now;
                    System.out.println("happend");
                }
            }
        }.start();

        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(1000),
                //ae -> setButton((int) (Math.random()*100)));
                ae -> System.out.println("hur dur"));
        timer.stop();
        timer.getKeyFrames().setAll(
                keyFrame
        );
        timer.play();

        speedSlider.valueProperty().addListener(cl -> {
            playbackSpeed = speedSlider.getValue();
            System.out.println("speed slider: "+playbackSpeed);
            //resetTimer(timer, playbackSpeed);
        });

        //resetTimer(timer, playbackSpeed);




//        Timeline timeline = new Timeline(new KeyFrame(
//                Duration.millis(playbackSpeed),
//                ae -> setButton((int) (Math.random()*100))));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();


//        int i = 0;
//        while (true)
//        {
//            final int finalI = i++;
//            Platform.runLater ( () -> label.setText ("" + finalI));
//            Thread.sleep (1000);
//        }
    }

    private void resetTimer(Timeline timer, final double timerInterval) {
//        timer = new Timeline(new KeyFrame(
//        Duration.millis(playbackSpeed),
//        ae -> setButton((int) (Math.random()*100))));
//        timer.setCycleCount(Animation.INDEFINITE);
//        timer.play();
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(timerInterval),
                ae -> setButton((int) (Math.random()*100)));
                //ae -> System.out.println("hur dur"));
        timer.stop();
        timer.getKeyFrames().setAll(
                keyFrame
        );
        timer.play();
    }

    private void setButton(int index){
        //System.out.println("setButton");
        cells[index].setGraphic(new ImageView(retrieveMarker()));
    }

    private Image retrieveMarker() {
        player = player == Player.X_PLAYER ? Player.O_PLAYER : Player.X_PLAYER;
        return player.marker();
    }

    private void registerOnAction(Button button) {
        button.setOnAction(e ->
                button.setGraphic(new ImageView(retrieveMarker()))
        );
    }

    public enum Player {
        X_PLAYER(new ImageView(new Image("flatland/img/x.png", 50, 50, false, false))),
        O_PLAYER(new ImageView(new Image("flatland/img/o.png", 50, 50, false, false)));

        private final ImageView view;

        Player(ImageView view) {
            this.view = view;
        }

        public Image marker() {
            return view.getImage();
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(2).toLowerCase();
        }
    }

}
