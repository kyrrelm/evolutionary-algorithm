package ea.core;

import ea.oneMax.oneMaxPheno;

import java.util.ArrayList;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class Simulator {

    private static ArrayList<Individual> population;

    public static void init(){
        population = new ArrayList<Individual>();
        for (int i = 0; i < 10; i++) {
            population.add(new Individual(new GenoType(20)));
        }
    }

    public static void run(){
        for (Individual i: population) {
            i.develop(new oneMaxPheno(i.genotype));
        }
    }
}
