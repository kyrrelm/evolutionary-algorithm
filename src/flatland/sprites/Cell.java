package flatland.sprites;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Created by Kyrre on 07.04.2016.
 */
public class Cell extends HBox {

    private static final Image imgBlank = new Image("flatland/img/blank.png", 50, 50, false, false);
    private static final Image imgX = new Image("flatland/img/x.png", 50, 50, false, false);
    private static final Image imgO = new Image("flatland/img/o.png", 50, 50, false, false);

    public Cell(Type type) {
        super();
        this.getChildren().add(new ImageView(type.img));
        this.setStyle("-fx-border-color: black;");
    }

    public void changeType(Type type){
        this.getChildren().clear();
        this.getChildren().add(new ImageView(type.img));
    }

    public enum Type {
        BLANK(imgBlank),
        POISON(imgX),
        FOOD(imgO);
        public final Image img;
        Type(Image img) {
            this.img = img;
        }
    }
}
