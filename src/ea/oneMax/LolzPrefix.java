package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

/**
 * Created by Kyrre on 24/2/2016.
 */
public class LolzPrefix extends Phenotype {
    protected LolzPrefix(GenoType genoType) {
        super(genoType, genoType.length);
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
        return new LolzPrefix(genoType.crossover(partner.genoType));
    }
}
