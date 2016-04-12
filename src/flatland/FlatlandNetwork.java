package flatland;

import ann.Network;
import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class FlatlandNetwork extends Phenotype{

    private Network network;
    private Agent agent;
    private final int TIME_STEPS = 60;

    protected FlatlandNetwork(GenoType genoType, int fitnessGoal, Agent agent) {
        super(genoType, fitnessGoal);
        this.agent = agent;
        network = null;
    }

    @Override
    protected int fitness() {
        agent.reset();
        for (int i = 0; i < TIME_STEPS; i++) {
            //agent.
        }
        return 0;
    }

    @Override
    protected void develop(GenoType genoType) {
        float[] weights = new float[54];
        Random r = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = r.nextFloat();
        }
        network = new Network(6,3,weights,6);
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
