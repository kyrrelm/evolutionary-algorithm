package ea.core;

import ea.oneMax.oneMaxPheno;

import java.util.*;

import static ea.core.Simulator.AdultSelection.*;
import static ea.core.Simulator.ParentSelection.*;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class Simulator {

    private static List<Individual> childPopulation;
    private static List<Individual> adultPopulation;

    public static int populationSize = 100;
    public static int productionSize = populationSize;
    //0.2 Best for OneMax: 0.8f pop:300
    public static float crossoverRate = 0.7f;
    //0.01 Best for OneMax: 0.001f
    public static float perComponentMutationRate = 0.001f;
    public static float elitism = 0.1f;
    public static AdultSelection adultSelection = OVER_PRODUCTION;
    public static ParentSelection parentSelection = RANK;

    public static double RANK_MAX = 1.5;
    public static double RANK_MIN = 2-RANK_MAX;

   enum  AdultSelection {
       FULL_GENERATIONAL_REPLACEMENT,
       OVER_PRODUCTION,
       GENERATIONAL_MIXING,
       OVER_PRODUCED_GENERATIONAL_MIXING;
    }

    enum ParentSelection {
        FITNESS_PROPORTIONAL,
        SIGMA,
        RANK;

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
        //Sigma------------------------
        double averageFitness = -1;
        double standardDeviation = -1;
        if (parentSelection == SIGMA){
            averageFitness = (double) totalFitness/adultPopulation.size();
            double standardSum = 0;
            for (Individual i: adultPopulation) {
                standardSum+=Math.pow((i.getFitness()-averageFitness),2);
            }
            standardDeviation = Math.sqrt(standardSum/adultPopulation.size());
        }


        //Rank---------------------
        ArrayList<RankWrapper> rankList = new ArrayList<>();
        double rankTotal = 0;
        if (parentSelection == RANK){
            Collections.sort(adultPopulation);
            double rank = adultPopulation.size();
            for (Individual i: adultPopulation) {
                double expVal = RANK_MIN+((RANK_MAX-RANK_MIN)*((rank-1d)/((double) adultPopulation.size()-1d)));
                rankTotal += expVal;
                rankList.add(new RankWrapper(expVal, i));
                rank--;
            }
        }


        for (int i = 0; i < productionSize /2; i++) {
            Individual firstPick = spinWheel(totalFitness, averageFitness, standardDeviation, rankList, rankTotal);
            Individual secondPick = null;
            boolean duplicate = true;
            while (duplicate){
                secondPick = spinWheel(totalFitness, averageFitness, standardDeviation, rankList, rankTotal);
                duplicate = secondPick == firstPick;
            }
            mate(firstPick, secondPick);
        }


    }

    private static Individual spinWheel(int totalFitness, double averageFitness, double standardDeviation, ArrayList<RankWrapper> rankList, double rankTotal) {
        switch (parentSelection){
            case FITNESS_PROPORTIONAL: return fitnessProportionate(totalFitness);
            case SIGMA: return sigma(averageFitness,standardDeviation);
            case RANK: return rank(rankList, rankTotal);
        }
        return null;
    }

    private static Individual sigma(double averageFitness, double standardDeviation){
        double partialSum = 0;
        double rand = new Random().nextDouble()*adultPopulation.size();
        for (Individual i: adultPopulation) {
            partialSum += 1+((i.getFitness()-averageFitness)/(2*standardDeviation));
            if (partialSum > rand){
                return i;
            }
        }
        return null;
    }

    private static Individual fitnessProportionate(int totalFitness){
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

    private static Individual rank(ArrayList<RankWrapper> rankList, double rankTotal){
        double partialSum = 0;
        double rand = new Random().nextDouble()*rankTotal;
        for (RankWrapper r: rankList) {
            partialSum += r.expVal;
            if (partialSum > rand){
                return r.individual;
            }
        }
        return null;
    }

    private static class RankWrapper{
        private final double expVal;
        private final Individual individual;

        RankWrapper(double expVal, Individual individual){
           this.expVal = expVal;
            this.individual = individual;
        }
    }


    private static void mate(Individual firstPick, Individual secondPick) {
        childPopulation.add(new Individual(firstPick.genotype.crossover(secondPick.genotype)));
        if (childPopulation.size()< productionSize){
            childPopulation.add(new Individual(secondPick.genotype.crossover(firstPick.genotype)));

        }
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

