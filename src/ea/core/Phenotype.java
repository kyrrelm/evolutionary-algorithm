package ea.core;

/**
 * Created by Kyrre on 22/2/2016.
 */
public abstract class Phenotype implements Comparable<Phenotype> {

    private int fitness = -1;
    final int fitnessGoal;
    public final GenoType genoType;

    protected Phenotype(GenoType genoType, int fitnessGoal) {
        this.genoType = genoType;
        this.fitnessGoal = fitnessGoal;
    }
    protected abstract int fitness();
    protected abstract void develop(GenoType genoType);

    public int getFitness() {
        return fitness;
    }

    public void mature(){
        develop(genoType);
        fitness = fitness();
    }

    /**
     * Must be called after develop
     * @return
     */
    public boolean isFit(){
        return fitness >= fitnessGoal;
    }

    @Override
    public int compareTo(Phenotype that) {
        return that.getFitness()-this.getFitness();
    }
}
