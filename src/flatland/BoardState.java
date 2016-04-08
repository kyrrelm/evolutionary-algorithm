package flatland;

import java.util.Random;

/**
 * Created by Kyrre on 08.04.2016.
 */
public class BoardState {

    RawCell[][] board;
    int moleX;
    int moleY;

    public BoardState(int moleX, int moleY, float foodRate, float poisonRate) {
        this.board = new RawCell[10][10];
        this.moleX = moleX;
        this.moleY = moleY;
        board[moleY][moleX] = new RawCell(Cell.Type.MOLE_UP);
        initBoard(foodRate, poisonRate);
    }

    private void initBoard(float foodRate, float poisonRate) {
        Random r = new Random();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (moleX == x && moleY == y)continue;
                board[y][x] = r.nextFloat() < foodRate ? new RawCell(Cell.Type.FOOD) : new RawCell(Cell.Type.BLANK);
            }
        }
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (r.nextFloat() < poisonRate && board[y][x].getType() == Cell.Type.BLANK)
                    board[y][x] = new RawCell(Cell.Type.POISON);
            }
        }
    }

    public Cell.Type getType(int x, int y) {
        return board[y][x].getType();
    }
}
