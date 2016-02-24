package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

/**
 * Created by Kyrre on 24/2/2016.
 */
public class SurprisingSequence extends Phenotype{
    public final int S;
    public final boolean global;

    protected SurprisingSequence(GenoType genoType, int fitnessGoal, int S, boolean global) {
        super(genoType, fitnessGoal);
        this.S = S;
        this.global = global;
    }

    @Override
    protected int fitness() {
        return 0;
    }

    @Override
    protected void develop(GenoType genoType) {

    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return null;
    }
}
