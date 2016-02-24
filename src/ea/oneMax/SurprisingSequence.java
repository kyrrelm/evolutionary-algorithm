package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

/**
 * Created by Kyrre on 24/2/2016.
 */
public class SurprisingSequence extends Phenotype{
    public final int S;
    public final boolean global;
    private final int numberOfBite;
    int[] phenome;

    public SurprisingSequence(int l, int S, boolean global) {
        super(new GenoType((Integer.SIZE-Integer.numberOfLeadingZeros(S-1))*l), l);
        this.S = S;
        this.numberOfBite = Integer.SIZE-Integer.numberOfLeadingZeros(S-1);
        this.global = global;
        phenome = new int[l];
    }

    @Override
    protected int fitness() {
        return 0;
    }

    @Override
    protected void develop(GenoType genoType) {

        for (int i = 0; i < fitnessGoal; i++) {
            while (i<genoType.getGenome().size()){

            }
        }
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return null;
    }
}
