package flatland;/**
 * Created by Kyrre on 06.04.2016.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

public class Flatland extends Application {

    Player player = Player.O_PLAYER;
    Button[] cells;
    double playbackInterval = 3000;
    final double MAX_PLAYBACK_INTERVAL = 3000;
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
        primaryStage.setTitle("TicTacToe By Legato");
        primaryStage.getIcons().add(new Image("flatland/img/icons.png", 50, 50, false, false));
        primaryStage.show();

        //GUI updates
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - sinceLast > playbackInterval *1000000){
                    sinceLast = now;
                    setButton((int) (Math.random()*100));
                }
            }
        }.start();

        speedSlider.valueProperty().addListener(cl -> {
            playbackInterval = MAX_PLAYBACK_INTERVAL-speedSlider.getValue()+MIN_PLAYBACK_INTERVAL;
            System.out.println("speed slider: "+ playbackInterval);
            //resetTimer(timer, playbackInterval);
        });
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
