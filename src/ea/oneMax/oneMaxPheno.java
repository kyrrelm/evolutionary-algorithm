package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.BitSet;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class OneMaxPheno extends Phenotype {

    BitSet penome;

    public OneMaxPheno(GenoType genoType) {
        super(genoType, genoType.length);
    }

    @Override
    protected int fitness() {
        return penome.cardinality();
    }

    @Override
    protected void develop(GenoType genoType) {
        penome = (BitSet) genoType.getGenome().clone();
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return new OneMaxPheno(genoType.crossover(partner.genoType));
    }
}
