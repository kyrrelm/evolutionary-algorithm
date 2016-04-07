package flatland;/**
 * Created by Kyrre on 06.04.2016.
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Flatland extends Application {

    Player player = Player.O_PLAYER;
    Button[] cells;
    GridPane outerGrid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Image imgBlank = new Image("flatland/img/blank.png", 50, 50, false, false);
        cells = new Button[100];
        outerGrid = new GridPane();
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
        Slider slider = new Slider(-100, 100, 0);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setMaxWidth(400);
        slider.setBlockIncrement(100);
        //setHgrow(slider, Priority.ALWAYS);

        outerGrid.add(board,0,0);
        outerGrid.add(slider, 0, 1);
        //outerGrid.add();

        Scene scene = new Scene(outerGrid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TicTacToe By Legato");
        primaryStage.getIcons().add(new Image("flatland/img/icons.png", 50, 50, false, false));
        primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> setButton((int) (Math.random()*100))));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

//        int i = 0;
//        while (true)
//        {
//            final int finalI = i++;
//            Platform.runLater ( () -> label.setText ("" + finalI));
//            Thread.sleep (1000);
//        }
    }

    private void setButton(int index){
        System.out.println("setButton");
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
