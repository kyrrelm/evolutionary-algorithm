package flatland;

/**
 * Created by Kyrre on 08.04.2016.
 */
public class RawCell {

    Cell.Type type;

    public RawCell(Cell.Type type) {
        this.type = type;
    }

    public Cell.Type getType() {
        return type;
    }

    public void setType(Cell.Type type) {
        this.type = type;
    }
}
