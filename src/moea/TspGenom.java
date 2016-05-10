package moea;

import java.util.Random;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class TspGenom {

    private final float crossoverRate;
    private final float mutationRate;
    private int[] order;
    Random rnd;

    public TspGenom(int numberOfCities, float crossoverRate, float mutationRate) {
        this.crossoverRate = crossoverRate;
        this.order = new int[numberOfCities];
        this.mutationRate = mutationRate;
        for (int i = 0; i < numberOfCities; i++) {
            order[i] = i;
        }
        Random rnd = new Random();
        for (int i = order.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = order[index];
            order[index] = order[i];
            order[i] = a;
        }
        this.rnd = new Random();
    }

    private TspGenom(int[] copy, float crossoverRate, float mutationRate) {
        this.order = copy;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.rnd = new Random();
    }

    public int getCity(int index){
        return order[index];
    }

    public int getLength() {
        return order.length;
    }

    public TspGenom crossover(TspGenom mate) {
        if (rnd.nextFloat()<crossoverRate){

            int[] inv = invert(order);
            int[] mateInv = invert(mate.order);

            int crossoverPoint = rnd.nextInt(inv.length-2)+1;
            for (int i = crossoverPoint; i < mateInv.length; i++) {
                int tmp = inv[i];
                inv[i] = mateInv[i];
                mateInv[i] = tmp;
            }
            int[] perm = unInvert(inv);
            int[] matePerm = unInvert(mateInv);
            System.out.println();
        }


        //mutate(copy);
        return null;//return new TspGenom(copy, crossoverRate, mutationRate);
    }

    private int[] invert(int[] perm){
        int[] inv = new int[perm.length];
        for (int i = 0; i < perm.length; i++) {
            int m = 0;
            while (perm[m] != i){
                if (perm[m] > i){
                    inv[i]++;
                }
                m++;
            }
        }
        return inv;
    }

    private int[] unInvert(int[] inv){
        int[] perm = new int[inv.length];
        int[] pos = new int[inv.length];
        for (int i = inv.length-1; i >= 0; i--) {
            for (int m = i+1; m < inv.length; m++) {
                if (pos[m] >= inv[i]){
                    pos[m]++;
                }
                pos[i] = inv[i];
            }
        }
        for (int i = 0; i < perm.length; i++) {
            perm[pos[i]] = i;
        }
        return perm;
    }

    private void mutate(int[] copy) {
        for (int i = 0; i < copy.length; i++) {
            if (mutationRate>rnd.nextFloat()){
                int tmp = copy[i];
                int randPos = rnd.nextInt(copy.length);
                copy[i] = copy[randPos];
                copy[randPos] = tmp;
            }
        }
    }
}
