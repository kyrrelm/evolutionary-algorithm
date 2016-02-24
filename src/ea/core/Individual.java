package ea.core;


/**
 * Created by Kyrre on 22/2/2016.
 */
public class Individual implements Comparable<Individual> {

    final GenoType genotype;
    Phenotype phenotype;

    public Individual(GenoType genotype) {
        this.genotype = genotype;
    }

    public void develop(Phenotype phenotype) {
        this.phenotype = phenotype;
    }

    public int getFitness() {
        return phenotype.getFitness();
    }

    /**
     * Must be called after develop
     * @return
     */
    public boolean isFit(){
        return phenotype.getFitness() >= phenotype.fitnessGoal;
    }

    @Override
    public int compareTo(Individual that) {
        return that.getFitness()-this.getFitness();
    }
}
