package flatland;/**
 * Created by Kyrre on 06.04.2016.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Flatland extends Application {

    Player player = Player.O_PLAYER;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button[] cells = new Button[1000];
        Image imgBlank = new Image("flatland/img/blank.png", 50, 50, false, false);
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

        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TicTacToe By Legato");
        primaryStage.getIcons().add(new Image("flatland/img/icons.png", 50, 50, false, false));
        primaryStage.show();
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
