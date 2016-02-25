package ea.core;

/**
 * Created by Kyrre on 22/2/2016.
 */
public abstract class Phenotype implements Comparable<Phenotype> {

    private int fitness = -1;
    public final int fitnessGoal;
    public final GenoType genoType;

    protected Phenotype(GenoType genoType, int fitnessGoal) {
        this.genoType = genoType;
        this.fitnessGoal = fitnessGoal;
    }
    protected abstract int fitness();
    protected abstract void develop(GenoType genoType);
    protected abstract Object getPhenome();

    public abstract Phenotype mate(Phenotype partner);

    public int getFitness() {
        return fitness;
    }

    public int mature(){
        develop(genoType);
        fitness = fitness();
        return fitness;
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
