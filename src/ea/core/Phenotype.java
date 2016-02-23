package ea.core;

/**
 * Created by Kyrre on 22/2/2016.
 */
public abstract class Phenotype {

    private int fitness = -1;
    final int fitnessGoal;

    protected Phenotype(GenoType genoType, int fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
        develop(genoType);
        fitness = fitness();
    }
    protected abstract int fitness();
    protected abstract void develop(GenoType genoType);

    public int getFitness() {
        return fitness;
    }
}
