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
        Button[] cells = new Button[9];
        Image imgBlank = new Image("flatland/img/blank.png");
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Button("", new ImageView(imgBlank));
            registerOnAction(cells[i]);
        }

        GridPane board = new GridPane();
        for (int row = 1, col = 1, cell = 0; row <= 3; row++, col -=2) {
            board.add(cells[cell++], row, col++);
            board.add(cells[cell++], row, col++);
            board.add(cells[cell++], row, col);
        }

        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TicTacToe By Legato");
        primaryStage.getIcons().add(new Image("flatland/img/icons.png"));
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
        X_PLAYER(new ImageView(new Image("flatland/img/x.png"))),
        O_PLAYER(new ImageView(new Image("flatland/img/o.png")));

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
