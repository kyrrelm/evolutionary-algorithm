package flatland;

import ann.Network;
import ann.Neuron;
import ea.core.GenoType;
import ea.core.Phenotype;
import sun.security.provider.SHA;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class FlatlandNetwork extends Phenotype{

    private final Neuron.Function function;
    private Network network;
    private Agent agent;
    private final int TIME_STEPS = 60;
    public FlatlandNetwork(GenoType genoType, int fitnessGoal, Agent agent, Neuron.Function function) {
        super(genoType, fitnessGoal);
        this.function = function;
        this.agent = agent;
        this.fitnessHax = 0;
        network = null;
    }
    private int fitnessHax;
    @Override
    protected int fitness() {
        runAgent(agent.isRecordingRun());
        return fitnessHax;
    }

    public ArrayList<BoardState> runAgent(boolean recordRun){
        fitnessHax = 0;
        int tmp = 0;
        agent.resetHistory();
        for (int y = 0; y < agent.getNumberOfRuns(); y++) {
            agent.reset(y);
            agent.setRecordRun(recordRun);
            for (int i = 0; i < TIME_STEPS; i++) {
                float[] input = generateInputs();
                float[] result = network.run(function, input);
                Cell.Type consumed = agent.act(calculateMove(result));
                if (consumed == Cell.Type.FOOD) tmp += 10;
                else if (consumed == Cell.Type.POISON) tmp -= 10;
                else if (consumed == Cell.Type.BLANK) tmp += 1;
            }
            if (tmp>fitnessHax){
                fitnessHax = tmp;
                tmp = 0;
            }
        }
//        fitnessHax = fitnessHax/agent.getNumberOfRuns();
        return agent.getHistory();
    }

    private BoardState.Direction calculateMove(float[] result) {
        BoardState.Direction dir = BoardState.Direction.STAY;
        float best = 0;
        if (result[0] > best){
            dir = BoardState.Direction.LEFT;
            best = result[0];
        }
        if (result[1] > best){
            dir = BoardState.Direction.STRAGHT;
            best = result[1];
        }
        if (result[2] > best){
            dir = BoardState.Direction.RIGHT;
            best = result[2];
        }
        return dir;
    }

    private float[] generateInputs(){
        Cell.Type[] sensors = agent.sense();
        float[] inputs = new float[6];
        for (int i = 0; i <inputs.length; i++) {
            inputs[i] = 0.0001f;
        }
        for (int j = 0; j < sensors.length; j++) {
            if (sensors[j] == Cell.Type.FOOD){
                inputs[j] = 1;
            }else if(sensors[j] == Cell.Type.POISON){
                inputs[j+3] = 1;
            }
        }
        return inputs;
    }

    @Override
    protected void develop(GenoType genoType) {

        float[] weights = new float[54];

        StringBuilder b = new StringBuilder();
        for (int i = 0, count = 0; i < genoType.length; i++) {
            b.append((genoType.getGenome().get(i)) ? 1 : 0);
            if (b.length() == 15){
                weights[count++] = (float) Integer.parseInt(b.toString(), 2)/Short.MAX_VALUE;
                b.setLength(0);
            }
        }
//        Random r = new Random();
//        for (int i = 0; i < weights.length; i++) {
//            weights[i] = r.nextInt(32);
//            weights[i] = r.nextFloat();
//        }
        network = new Network(6,3,0.5f,weights,6);
    }


    @Override
    protected Object getPhenome() {
        return network;
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return new FlatlandNetwork(genoType.crossover(partner.genoType), fitnessGoal, agent, function);
    }

    public static void main(String[] args) {
        float s = 10000;
        float f = s/Short.MAX_VALUE;
        System.out.println(f);
    }

}
