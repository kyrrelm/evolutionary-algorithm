package ea.core;

import ea.oneMax.LolzPrefix;
import ea.oneMax.OneMaxPheno;
import ea.oneMax.SurprisingSequence;

import java.util.*;

import static ea.core.Simulator.AdultSelection.*;
import static ea.core.Simulator.ParentSelection.*;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class Simulator {

    private static List<Phenotype> childPopulation;
    private static List<Phenotype> adultPopulation;


    public static List<Double> generationalBest;
    public static List<Double> globalBest;
    public static List<Double> avgFitnessList;
    public static List<Double> standardDeviationList;

    public static Phenotype bestPhenotype;
    public static double averageFitness = -1;
    public static double standardDeviation = -1;
    public static int totalFitness = -1;
    public static int productionSize;
    public static int iterations = 0;

    public static int populationSize = 1000;
    public static int loopLimit = 100;

    //0.2 Best for OneMax: 0.8f pop:300
    public static float crossoverRate = 0.7f;
    //Best for OneMax: 0.001f
    //Best for LOLZ:0.01f
    public static float perComponentMutationRate = 0.01f;
    //Best for OneMax: 0.1f
    //Best for LOLZ:0.2f
    public static float elitism = 0.1f;
    public static AdultSelection adultSelection = OVER_PRODUCED_GENERATIONAL_MIXING;
    public static ParentSelection parentSelection = RANK;

    //RANK
    public static double RANK_MAX = 1.5;
    public static double RANK_MIN = 2-RANK_MAX;

    //TOURNAMENT
    public static int K = 5;
    public static float e = 0.3f;

    enum  AdultSelection {
       FULL_GENERATIONAL_REPLACEMENT,
       OVER_PRODUCTION,
       GENERATIONAL_MIXING,
       OVER_PRODUCED_GENERATIONAL_MIXING;
    }

    enum ParentSelection {
        FITNESS_PROPORTIONAL,
        SIGMA,
        RANK,
        TOURNAMENT;

    }


    public static void init(){
        bestPhenotype = null;
        generationalBest = new ArrayList<>();
        avgFitnessList = new ArrayList<>();
        standardDeviationList = new ArrayList<>();
        globalBest = new ArrayList<>();
        productionSize = populationSize;
        if (adultSelection == OVER_PRODUCTION || adultSelection == FULL_GENERATIONAL_REPLACEMENT){
            productionSize = populationSize*2;
        }
        childPopulation = new ArrayList<>();
        for (int i = 0; i < productionSize; i++) {
            childPopulation.add(new OneMaxPheno(new GenoType(40)));
            //childPopulation.add(new LolzPrefix(new GenoType(40), 21));
            //childPopulation.add(new SurprisingSequence(26,10,true));
        }
    }

    /**
     *
     * @return true if one of the developed child's meets fitness goal.
     */
    private static boolean develop() {
        boolean goalReached = false;
        Phenotype genBest = null;
        for (Phenotype i: childPopulation) {
            int score = i.mature();
            if (genBest == null || score>genBest.getFitness()){
                genBest = i;
            }
            if (bestPhenotype == null || score>bestPhenotype.getFitness()){
                bestPhenotype = i;
                System.out.println("Best Phenotype{ fitness: "+bestPhenotype.getFitness()+" phenome: "+bestPhenotype.getPhenome()+" }");
            }

            if (i.isFit()){
                goalReached = true;
            }
        }
        generationalBest.add((double) genBest.getFitness());
        if (bestPhenotype != null){
            globalBest.add((double) bestPhenotype.getFitness());
        }
        return goalReached;
    }

    private static void mate(Phenotype firstPick, Phenotype secondPick) {
        childPopulation.add(firstPick.mate(secondPick));
        if (childPopulation.size()< productionSize){
            childPopulation.add(secondPick.mate(firstPick));

        }
    }

    public static void run(){
        System.out.println("Running up to: "+loopLimit+" iterations");
        while (iterations<loopLimit){
            if (develop()){
                break;
            }
            adultSelection();
            parentSelection();
            if (iterations%1000 == 0){
                System.out.println("Iteration: "+iterations);
            }
            iterations++;
        }
        System.out.println("Done after: "+iterations+" iterations, make charts and shit");

    }

    private static void parentSelection() {
        totalFitness = 0;
        for (Phenotype i: adultPopulation) {
            totalFitness += i.getFitness();
        }
        //Sigma------------------------
        averageFitness = (double) totalFitness/adultPopulation.size();
        avgFitnessList.add(averageFitness);
        double standardSum = 0;
        for (Phenotype i: adultPopulation) {
            standardSum+=Math.pow((i.getFitness()-averageFitness),2);
        }
        standardDeviation = Math.sqrt(standardSum/adultPopulation.size());
        standardDeviationList.add(standardDeviation);
        if (parentSelection == TOURNAMENT){
            tournamentSelection();
        }else {
            globalSelection();
        }
    }

    private static void tournamentSelection() {
        for (int i = 0; i < productionSize /2; i++) {
            Phenotype firstPick = pickChampion();
            Phenotype secondPick = null;
            boolean duplicate = true;
            while (duplicate){
                secondPick = pickChampion();
                duplicate = secondPick == firstPick;
            }
            mate(firstPick, secondPick);
        }
    }

    private static Phenotype pickChampion(){
        HashSet<Phenotype> fighters = new HashSet<>();
        Random rnd = new Random();
        while (fighters.size()<K){
            int r = rnd.nextInt(adultPopulation.size());
            fighters.add(adultPopulation.get(r));
        }
        Phenotype[] fightArray = fighters.toArray(new Phenotype[fighters.size()]);
        Arrays.sort(fightArray);
        if (1f-e>rnd.nextFloat()){
            return fightArray[0];
        }
        return fightArray[rnd.nextInt(fightArray.length-1)+1];
    }

    private static void globalSelection() {
        //Rank---------------------
        ArrayList<RankWrapper> rankList = new ArrayList<>();
        double rankTotal = 0;
        if (parentSelection == RANK){
            Collections.sort(adultPopulation);
            double rank = adultPopulation.size();
            for (Phenotype i: adultPopulation) {
                double expVal = RANK_MIN+((RANK_MAX-RANK_MIN)*((rank-1d)/((double) adultPopulation.size()-1d)));
                rankTotal += expVal;
                rankList.add(new RankWrapper(expVal, i));
                rank--;
            }
        }


        for (int i = 0; i < productionSize /2; i++) {
            Phenotype firstPick = rouletteWheel(totalFitness, averageFitness, standardDeviation, rankList, rankTotal);
            Phenotype secondPick = null;
            boolean duplicate = true;
            while (duplicate){
                secondPick = rouletteWheel(totalFitness, averageFitness, standardDeviation, rankList, rankTotal);
                duplicate = secondPick == firstPick;
            }
            mate(firstPick, secondPick);
        }


    }

    private static Phenotype rouletteWheel(int totalFitness, double averageFitness, double standardDeviation, ArrayList<RankWrapper> rankList, double rankTotal) {
        switch (parentSelection){
            case FITNESS_PROPORTIONAL: return fitnessProportionate(totalFitness);
            case SIGMA: return sigma(averageFitness,standardDeviation);
            case RANK: return rank(rankList, rankTotal);
        }
        return null;
    }

    private static Phenotype sigma(double averageFitness, double standardDeviation){
        double partialSum = 0;
        double rand = new Random().nextDouble()*adultPopulation.size();
        for (Phenotype i: adultPopulation) {
            partialSum += 1+((i.getFitness()-averageFitness)/(2*standardDeviation));
            if (partialSum > rand || standardDeviation == 0){
                return i;
            }
        }
        return null;
    }

    private static Phenotype fitnessProportionate(int totalFitness){
        int partialSum = 0;
        int rand = new Random().nextInt(totalFitness);
        for (Phenotype i: adultPopulation) {
            partialSum += i.getFitness();
            if (partialSum > rand){
                return i;
            }
        }
        return null;
    }
    private static Phenotype rank(ArrayList<RankWrapper> rankList, double rankTotal){
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
        private final Phenotype individual;


        RankWrapper(double expVal, Phenotype individual){
           this.expVal = expVal;
            this.individual = individual;
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
}

