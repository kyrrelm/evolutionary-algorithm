package ea.core;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class GenoType {

    private Random rnd = new Random();

    private int length;
    private BitSet genome;

    public GenoType(int length) {
        this.length = length;
        rnd = new Random();
        genome = new BitSet(length);
        for (int i = 0; i < length; i++){
            genome.set(i, rnd.nextBoolean());
        }
    }
    private GenoType(BitSet genome){
        this.genome = genome;
        this.length = genome.length();
    }

    public BitSet getGenome() {
        return genome;
    }

    public GenoType crossover(GenoType mate){
        BitSet partOne = (BitSet) genome.clone();
        rnd.nextFloat();
        if (rnd.nextFloat()<Simulator.crossoverRate){
            int crossoverPoint = rnd.nextInt(length-1)+1;
            partOne.clear(0, crossoverPoint);
            BitSet partTwo = (BitSet) mate.genome.clone();
            partTwo.clear(crossoverPoint, length);
            partOne.or(partTwo);
        }
        mutate(partOne);
        return new GenoType(partOne);
    }

    //TODO: implement both types?
    private void mutate(BitSet partOne) {
        for (int i = 0; i < partOne.length(); i++) {
            if (rnd.nextFloat()<Simulator.perComponentMutationRate){
                partOne.flip(i);
            }
        }
    }
}
