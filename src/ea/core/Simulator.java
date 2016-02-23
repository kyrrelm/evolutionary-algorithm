package ea.core;

import ea.oneMax.oneMaxPheno;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class Simulator {

    private static ArrayList<Individual> childPopulation;
    private static ArrayList<Individual> adultPopulation;
    public static final int POPULATION_SIZE = 10;
    public static final float CROSSOVER_RATE = 0.5f;
    public static final float PER_COMPONENT_MUTATION_RATE = 0.01f;


    public static void init(){
        childPopulation = new ArrayList<Individual>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            childPopulation.add(new Individual(new GenoType(20)));
        }
    }

    public static void run(){
        develop();
        adultSelection();
        parentSelection();
    }

    private static void parentSelection() {
        rouletteWheel();
    }

    private static void rouletteWheel() {
        int totalFitness = 0;
        for (Individual i: adultPopulation) {
            totalFitness += i.getFitness();
        }

        for (int i = 0; i < POPULATION_SIZE/2; i++) {
            Individual firstPick = spinWheel(totalFitness);
            Individual secondPick = null;
            boolean duplicate = true;
            while (duplicate){
                secondPick = spinWheel(totalFitness);
                duplicate = secondPick == firstPick;
            }
            mate(firstPick, secondPick);
        }


    }

    private static void mate(Individual firstPick, Individual secondPick) {
        childPopulation.add(new Individual(firstPick.genotype.crossover(secondPick.genotype)));
    }

    private static Individual spinWheel(int totalFitness){
        int partialSum = 0;
        int rand = new Random().nextInt(totalFitness);
        for (Individual i: adultPopulation) {
            partialSum += i.getFitness();
            if (partialSum > rand){
                return i;
            }
        }
        return null;
    }

    private static void adultSelection() {
        //Full generational replacement
        adultPopulation = childPopulation;

        childPopulation = new ArrayList<>();
    }

    private static void develop() {
        for (Individual i: childPopulation) {
            i.develop(new oneMaxPheno(i.genotype));
        }
    }
}
