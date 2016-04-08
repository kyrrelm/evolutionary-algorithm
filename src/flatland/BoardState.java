package flatland;

import java.util.Random;

/**
 * Created by Kyrre on 08.04.2016.
 */
public class BoardState {

    Cell[][] board;
    int moleX;
    int moleY;

    public BoardState(int moleX, int moleY, float foodRate, float poisonRate) {
        this.board = new Cell[10][10];
        this.moleX = moleX;
        this.moleY = moleY;
        board[moleY][moleX] = new Cell(Cell.Type.MOLE_UP);
        initBoard(foodRate, poisonRate);
    }

    private void initBoard(float foodRate, float poisonRate) {
        Random r = new Random();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                board[y][x] = r.nextFloat() < foodRate && !(moleX == x && moleY == y) ? new Cell(Cell.Type.FOOD) : new Cell(Cell.Type.BLANK);
            }
        }
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (r.nextFloat() < poisonRate && board[y][x].getType() == Cell.Type.BLANK)
                    board[y][x] = new Cell(Cell.Type.BLANK);
            }
        }
    }

}
