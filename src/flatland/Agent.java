package flatland;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Agent {

    private BoardState prevState;
    private BoardState currentState;
    private boolean recordRun;
    private ArrayList<BoardState> history;


    public Agent(BoardState state, boolean recordRun) {
        this.currentState = state.deepCopy();
        this.recordRun = recordRun;
        this.history = new ArrayList<>();
        this.prevState = null;
        if (recordRun){
            this.prevState = currentState.deepCopy();
            history.add(prevState);
        }
    }

    public void act(){

    }

    public void actRand(){
        Random r = new Random();
        switch (r.nextInt(4)){
            case 0:{
                currentState.move(Cell.Type.MOLE_LEFT);
                break;
            }
            case 1:{
                currentState.move(Cell.Type.MOLE_RIGHT);
                break;
            }
            case 2:{
                currentState.move(Cell.Type.MOLE_UP);
                break;
            }
            case 3:{
                currentState.move(Cell.Type.MOLE_DOWN);
                break;
            }
        }
        if (recordRun){
            prevState = currentState.deepCopy();
            history.add(prevState);

        }
    }

    public ArrayList<BoardState> getHistory() {
        return history;
    }
}
