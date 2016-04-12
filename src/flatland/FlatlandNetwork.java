package flatland;

import ea.core.GenoType;
import ea.core.Phenotype;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class FlatlandNetwork extends Phenotype{


    protected FlatlandNetwork(GenoType genoType, int fitnessGoal) {
        super(genoType, fitnessGoal);
    }

    @Override
    protected int fitness() {
        return 0;
    }

    @Override
    protected void develop(GenoType genoType) {

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
