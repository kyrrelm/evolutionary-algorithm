package ea.core;

/**
 * Created by Kyrre on 22/2/2016.
 */
public abstract class Phenotype {

    private int fitness = -1;

    public Phenotype(GenoType genoType) {
        develop(genoType);
        fitness = fitness();
    }
    protected abstract int fitness();
    protected abstract void develop(GenoType genoType);

    public int getFitness() {
        return fitness;
    }
}
