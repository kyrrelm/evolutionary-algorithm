package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

/**
 * Created by Kyrre on 24/2/2016.
 */
public class SurprisingSequence extends Phenotype{
    public final int S;
    public final boolean global;
    private final int numberOfBite;
    int[] phenome;

    public SurprisingSequence(int l, int S, boolean global) {
        super(new GenoType((Integer.SIZE-Integer.numberOfLeadingZeros(S-1))*l), l);
        this.S = S;
        this.numberOfBite = Integer.SIZE-Integer.numberOfLeadingZeros(S-1);
        this.global = global;
        phenome = new int[l];
        develop(genoType);
    }

    @Override
    protected int fitness() {
        return 0;
    }

    @Override
    protected void develop(GenoType genoType) {
        int bitsUsed = 0;
        int number = 0;
        int i = 0;
        while (i < genoType.length) {
            while (bitsUsed<numberOfBite){
                if (genoType.getGenome().get(i)){
                    number+= Math.pow(2,numberOfBite-1-bitsUsed);
                }
                bitsUsed++;
                i++;
            }
            phenome[(i/numberOfBite)-1] = number;
            bitsUsed = 0;
            number = 0;
        }
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return null;
    }
}
