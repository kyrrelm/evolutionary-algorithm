package flatland;

import java.util.Random;

/**
 * Created by Kyrre on 08.04.2016.
 */
public class BoardState {

    public final int BOARD_SIZE = 10;

    private RawCell[][] board;
    private int moleX;
    private int moleY;
    private Cell.Type currentDir;

    public BoardState(int moleX, int moleY, float foodRate, float poisonRate) {
        this.board = new RawCell[BOARD_SIZE][BOARD_SIZE];
        this.moleX = moleX;
        this.moleY = moleY;
        currentDir = Cell.Type.MOLE_UP;
        board[moleY][moleX] = new RawCell(currentDir);
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

    public Cell.Type move(Cell.Type direction){
        currentDir = direction;
        Cell.Type value = board[moleY][moleX].getType();
        board[moleY][moleX].setType(Cell.Type.BLANK);
        switch (direction){
            case MOLE_RIGHT: {
                moleX = moleX+1%BOARD_SIZE;
                break;
            }
            case MOLE_DOWN:{
                moleY = moleY+1%BOARD_SIZE;
                break;
            }
            case MOLE_LEFT:{
                if (moleX>0){
                    --moleX;
                }else {
                    moleX = BOARD_SIZE;
                }
                break;
            }
            case MOLE_UP:{
                if (moleY>0){
                    --moleY;
                }else {
                    moleY = BOARD_SIZE;
                }
                break;
            }
        }
        board[moleY][moleX].setType(direction);
        return value;
    }

    public Cell.Type getType(int x, int y) {
        return board[y][x].getType();
    }

    public BoardState deepCopy(){
        RawCell[][] copy = new RawCell[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                copy[y][x] = new RawCell(board[y][x].getType());
            }
        }
        return new BoardState(moleX, moleY, currentDir, copy);
    }

    private BoardState(int moleX, int moleY, Cell.Type currentDir, RawCell[][] copy) {
        this.board = new RawCell[BOARD_SIZE][BOARD_SIZE];
        this.moleX = moleX;
        this.moleY = moleY;
        this.currentDir = currentDir;
    }


    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
}
