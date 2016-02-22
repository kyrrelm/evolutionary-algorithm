package ea.core;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class GenoType {

    //TODO: not thread safe?
    private static Random rnd = new Random();

    private int length;
    BitSet genome;

    public GenoType(int length) {
        this.length = length;
        genome = new BitSet(length);
        for (int i = 0; i < length; i++){
            genome.set(i, rnd.nextBoolean());
        }
    }

    public BitSet getGenome() {
        return genome;
    }
}
