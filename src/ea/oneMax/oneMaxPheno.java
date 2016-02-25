package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.BitSet;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class OneMaxPheno extends Phenotype {

    BitSet phenome = null;

    public OneMaxPheno(GenoType genoType) {
        super(genoType, genoType.length);
    }

    @Override
    protected int fitness() {
        return phenome.cardinality();
    }

    @Override
    protected void develop(GenoType genoType) {
        phenome = (BitSet) genoType.getGenome().clone();
    }

    @Override
    protected Object getPhenome() {
        return phenome;
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return new OneMaxPheno(genoType.crossover(partner.genoType));
    }
}
