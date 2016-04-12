package flatland;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Agent {

    private BoardState prevState;
    private BoardState currentState;
    private final BoardState initState;
    private boolean recordRun;
    private ArrayList<BoardState> history;


    public Agent(BoardState state, boolean recordRun) {
        this.initState = state.deepCopy();
        this.currentState = state.deepCopy();
        this.recordRun = recordRun;
        this.history = new ArrayList<>();
        this.prevState = null;
        if (recordRun){
            this.prevState = currentState.deepCopy();
            history.add(prevState);
        }
    }

    public Cell.Type act(BoardState.Direction direction){
        Cell.Type type = currentState.move(direction);
        if (recordRun){
            prevState = currentState.deepCopy();
            history.add(prevState);

        }
        return type;
    }


    public void actRand(){
        Random r = new Random();
        switch (r.nextInt(3)){
            case 0:{
                currentState.move(BoardState.Direction.LEFT);
                break;
            }
            case 1:{
                currentState.move(BoardState.Direction.RIGHT);
                break;
            }
            case 2:{
                currentState.move(BoardState.Direction.STRAGHT);
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

    public void reset() {
        currentState = initState.deepCopy();
    }

    public Cell.Type[] sense() {
        return currentState.sense();
    }

    public boolean isRecordingRun() {
        return recordRun;
    }

    public void setRecordRun(boolean recordRun) {
        this.recordRun = recordRun;
    }
}
