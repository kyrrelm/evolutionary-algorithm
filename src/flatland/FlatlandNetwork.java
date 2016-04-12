package flatland;

import ann.Network;
import ann.Neuron;
import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class FlatlandNetwork extends Phenotype{

    private Network network;
    private Agent agent;
    private final int TIME_STEPS = 10;

    protected FlatlandNetwork(GenoType genoType, int fitnessGoal, Agent agent) {
        super(genoType, fitnessGoal);
        this.agent = agent;
        network = null;
    }

    @Override
    protected int fitness() {
        agent.reset();
        for (int i = 0; i < TIME_STEPS; i++) {
            float[] input = generateInputs();
            float[] result = network.run(Neuron.Function.HYPERBOLIC, input);
            agent.act(move(result));
        }
        return 0;
    }

    private BoardState.Direction move(float[] result) {
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
        Random r = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = r.nextFloat();
        }
        network = new Network(6,3,0.5f,weights,6);
    }

    @Override
    protected Object getPhenome() {
        return null;
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return null;
    }

}
