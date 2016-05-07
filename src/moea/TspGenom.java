package moea;

import java.util.Random;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class TspGenom {

    int[] order;

    public TspGenom(int numberOfCities) {
        this.order = new int[numberOfCities];
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
    }

    public int getCity(int index){
        return order[index];
    }

    public int getLength() {
        return order.length;
    }
}
