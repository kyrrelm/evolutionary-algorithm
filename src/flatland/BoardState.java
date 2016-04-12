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
        board[moleX][moleY] = new RawCell(currentDir);
        initBoard(foodRate, poisonRate);
    }

    private void initBoard(float foodRate, float poisonRate) {
        Random r = new Random();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (moleX == x && moleY == y)continue;
                board[x][y] = r.nextFloat() < foodRate ? new RawCell(Cell.Type.FOOD) : new RawCell(Cell.Type.BLANK);
            }
        }
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (r.nextFloat() < poisonRate && board[x][y].getType() == Cell.Type.BLANK)
                    board[x][y] = new RawCell(Cell.Type.POISON);
            }
        }
    }

    public Cell.Type move(Cell.Type direction){
        currentDir = direction;
        board[moleX][moleY].setType(Cell.Type.BLANK);
        switch (direction){
            case MOLE_RIGHT: {
                moleX = (moleX+1)%BOARD_SIZE;
                break;
            }
            case MOLE_DOWN:{
                moleY = (moleY+1)%BOARD_SIZE;
                break;
            }
            case MOLE_LEFT:{
                if (moleX>0){
                    --moleX;
                }else {
                    moleX = BOARD_SIZE-1;
                }
                break;
            }
            case MOLE_UP:{
                if (moleY>0){
                    --moleY;
                }else {
                    moleY = BOARD_SIZE-1;
                }
                break;
            }
        }
        Cell.Type value = board[moleX][moleY].getType();
        board[moleX][moleY].setType(direction);
        return value;
    }

    public Cell.Type[] sense(){
        Cell.Type right = board[(moleX+1)%BOARD_SIZE][moleY].getType();
        Cell.Type left;
        if (moleX>0){
            left = board[moleX-1][moleY].getType();
        }else {
            left = board[moleX = BOARD_SIZE-1][moleY].getType();
        }
        Cell.Type down = board[moleX][(moleY+1)%BOARD_SIZE].getType();
        Cell.Type up;
        if (moleY>0){
            up = board[moleX][moleY-1].getType();
        }else {
            up = board[moleX][BOARD_SIZE-1].getType();
        }
        switch (currentDir){
            case MOLE_UP:{
                return new Cell.Type[]{left,up,right};
            }
            case MOLE_RIGHT:{
                return new Cell.Type[]{up,right,down};
            }
            case MOLE_DOWN:{
                return new Cell.Type[]{right,down,left};
            }
            case MOLE_LEFT:{
                return new Cell.Type[]{down,left,up};
            }
        }
        return null;
    }

    public Cell.Type getType(int x, int y) {
        return board[x][y].getType();
    }

    public BoardState deepCopy(){
        RawCell[][] copy = new RawCell[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                copy[x][y] = new RawCell(board[x][y].getType());
            }
        }
        return new BoardState(moleX, moleY, currentDir, copy);
    }

    private BoardState(int moleX, int moleY, Cell.Type currentDir, RawCell[][] copy) {
        this.moleX = moleX;
        this.moleY = moleY;
        this.currentDir = currentDir;
        this.board = copy;
    }


    public enum Direction {
        STRAGHT,
        LEFT,
        RIGHT;
    }
}
