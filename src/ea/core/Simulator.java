package ea.core;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import ea.oneMax.oneMaxPheno;

import java.util.*;

import static ea.core.Simulator.AdultSelection.*;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class Simulator {

    private static List<Individual> childPopulation;
    private static List<Individual> adultPopulation;

    public static int populationSize = 100;
    public static int productionSize = populationSize;
    public static float crossoverRate = 0.2f;
    public static float perComponentMutationRate = 0.01f;
    public static float elitism = 0.1f;
    public static AdultSelection adultSelection = OVER_PRODUCED_GENERATIONAL_MIXING;

   enum  AdultSelection {
       FULL_GENERATIONAL_REPLACEMENT,
       OVER_PRODUCTION,
       GENERATIONAL_MIXING,
       OVER_PRODUCED_GENERATIONAL_MIXING;
    }


    public static void init(){
        if (adultSelection == OVER_PRODUCTION || adultSelection == OVER_PRODUCED_GENERATIONAL_MIXING){
            productionSize = populationSize*2;
        }
        childPopulation = new ArrayList<>();
        for (int i = 0; i < productionSize; i++) {
            childPopulation.add(new Individual(new GenoType(40)));
        }
    }

    public static void run(){
        int iterations = 0;
        while (true){
            if (develop()){
                break;
            }
            adultSelection();
            parentSelection();
            iterations++;
        }
        System.out.println("Done after: "+iterations+" iterations, make charts and shit");

    }

    private static void parentSelection() {
        rouletteWheel();
    }

    private static void rouletteWheel() {
        int totalFitness = 0;
        for (Individual i: adultPopulation) {
            totalFitness += i.getFitness();
        }

        for (int i = 0; i < productionSize /2; i++) {
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
        if (childPopulation.size()< productionSize){
            childPopulation.add(new Individual(secondPick.genotype.crossover(firstPick.genotype)));

        }
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
        switch (adultSelection){
            case FULL_GENERATIONAL_REPLACEMENT:{
                adultPopulation = childPopulation;
                break;
            }
            case OVER_PRODUCED_GENERATIONAL_MIXING:
            case GENERATIONAL_MIXING:{
                if (adultPopulation != null){
                    Collections.sort(adultPopulation);
                    childPopulation.addAll(adultPopulation.subList(0, (int) (productionSize*elitism)));
                }
            }
            case OVER_PRODUCTION:{
                Collections.sort(childPopulation);
                adultPopulation = childPopulation.subList(0, populationSize);
                break;
            }
        }
        childPopulation = new ArrayList<>();
    }

    /**
     *
     * @return true if one of the developed child's meets fitness goal.
     */
    private static boolean develop() {
        boolean goalReached = false;
        for (Individual i: childPopulation) {
            i.develop(new oneMaxPheno(i.genotype));
            if (i.isFit()){
                goalReached = true;
            }
        }
        return goalReached;
    }
}
