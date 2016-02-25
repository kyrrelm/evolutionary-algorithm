package ea.oneMax;

import ea.core.GenoType;
import ea.core.Phenotype;

import java.util.HashSet;

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
        //TODO: remove this
        develop(genoType);
        fitness();
    }

    @Override
    protected int fitness() {
        HashSet<Sequence> sequences = new HashSet<>();
        for (int i = 0; i < phenome.length; i++) {
            int a = phenome[i];
            for (int j = i+1; j < phenome.length; j++) {
                int b = phenome[j];
            }
        }
        return 0;
    }

    private class Sequence{
        public final int a;
        public final int b;
        public final int distance;
        public Sequence(int a, int b, int distance) {
            this.a = a;
            this.b = b;
            this.distance = distance;
        }

        @Override
        public boolean equals(Object obj) {
            Sequence other = (Sequence) obj;
            if (a == other.a && b == other.b && distance == other.distance){
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return a+(b+1)*10000+(distance+1)*100000000;
        }
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
            phenome[(i/numberOfBite)-1] = number%S;
            bitsUsed = 0;
            number = 0;
        }
    }

    @Override
    public Phenotype mate(Phenotype partner) {
        return null;
    }
}
