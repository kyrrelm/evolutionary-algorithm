package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.BitSet;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class oneMaxPheno extends Phenotype {

    BitSet penome;

    public oneMaxPheno(GenoType genoType) {
        super(genoType, genoType.getGenome().length());
    }

    @Override
    protected int fitness() {
        return penome.cardinality();
    }

    @Override
    public void develop(GenoType genoType) {
        penome = (BitSet) genoType.getGenome().clone();
    }
}
