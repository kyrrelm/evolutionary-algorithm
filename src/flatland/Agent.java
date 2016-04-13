package flatland;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Agent {

    private final boolean staticRun;
    private final int numberOfRuns;
    private BoardState prevState;
    private BoardState currentState;
    private final ArrayList<BoardState> initStates;
    private boolean recordRun;
    private ArrayList<BoardState> history;


    public Agent(boolean staticRun, boolean fiveRuns, boolean recordRun) {
        this.staticRun = staticRun;
        this.initStates = new ArrayList<>();
        this.numberOfRuns = (fiveRuns?5:1);
        for (int i = 0; i <numberOfRuns; i++) {
            initStates.add(new BoardState(4, 4, 0.33f, 0.33f));
        }
        this.currentState = null;
        this.recordRun = recordRun;
        this.history = new ArrayList<>();
        this.prevState = null;
    }

    public Cell.Type act(BoardState.Direction direction){
        Cell.Type type = currentState.move(direction);
        if (recordRun){
            prevState = currentState.deepCopy();
            history.add(prevState);

        }
        return type;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public ArrayList<BoardState> getHistory() {
        return history;
    }

    public void reset(int index) {
        currentState = initStates.get(index).deepCopy();
        history.clear();
        history.add(currentState.deepCopy());
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
