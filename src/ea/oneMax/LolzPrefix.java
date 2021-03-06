package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.BitSet;

/**
 * Created by Kyrre on 24/2/2016.
 */
public class LolzPrefix extends Phenotype {

    int z;
    BitSet phenome = null;

    public LolzPrefix(GenoType genoType, int z) {
        super(genoType, genoType.length);
        this.z = z;
    }

    @Override
    protected int fitness() {
        int score = 1;
        int index = 1;
        if (phenome.get(0)){
            while (index < phenome.size() && phenome.get(index)){
                score++;
                index++;
            }
        }else {
            while (index < z && index < phenome.size() && !phenome.get(index)){
                score++;
                index++;
            }
        }
        return score;
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
        return new LolzPrefix(genoType.crossover(partner.genoType),z);
    }
}
