package flatland;

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
    private static final Image imgMoleLeft = new Image("flatland/img/mole_left.png", 50, 50, false, false);
    private static final Image imgMoleRight = new Image("flatland/img/mole_right.png", 50, 50, false, false);
    private static final Image imgMoleUp = new Image("flatland/img/mole_up.png", 50, 50, false, false);
    private static final Image imgMoleDown = new Image("flatland/img/mole_down.png", 50, 50, false, false);

    Type type;

    public Cell(Type type) {
        super();
        this.type = type;
        this.getChildren().add(new ImageView(type.img));
        this.setStyle("-fx-border-color: black;");
    }

    public void setType(Type type){
        this.type = type;
        this.getChildren().clear();
        this.getChildren().add(new ImageView(type.img));
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        BLANK(imgBlank),
        POISON(imgX),
        FOOD(imgO),
        MOLE_LEFT(imgMoleLeft),
        MOLE_RIGHT(imgMoleRight),
        MOLE_UP(imgMoleUp),
        MOLE_DOWN(imgMoleDown);
        public final Image img;
        Type(Image img) {
            this.img = img;
        }
    }
}
