package ea.core;

import ann.Network;
import ann.Neuron;
import ea.oneMax.LolzPrefix;
import ea.oneMax.OneMaxPheno;
import ea.oneMax.SurprisingSequence;
import flatland.Agent;
import flatland.FlatlandNetwork;

import java.util.*;

import static ea.core.Simulator.AdultSelection.*;
import static ea.core.Simulator.ParentSelection.*;
import static ea.core.Simulator.Problem.*;

/**
 * Created by Kyrre on 22/2/2016.
 */
public class Simulator {

    private static List<Phenotype> childPopulation;
    private static List<Phenotype> adultPopulation;


    public static List<Double> generationalBestFitness;
    public static List<Double> globalBest;
    public static List<Double> avgFitnessList;
    public static List<Double> standardDeviationList;

    public static Phenotype lastGenerationalBest;
    public static Phenotype bestPhenotype;
    public static double bestScore = 0;
    public static double averageFitness = -1;
    public static double standardDeviation = -1;
    public static int totalFitness = -1;
    public static int productionSize;

    public static int iterations = 0;
    public static int populationSize = 500;

    public static int loopLimit = 50;
    //0.2 Best for OneMax: 0.8f pop:300
    public static float crossoverRate = 0.8f;
    //Best for OneMax: 0.001f
    //Best for LOLZ:0.01f
    public static float perComponentMutationRate = 0.001f;
    //Best for OneMax: 0.1f
    //Best for LOLZ:0.2f
    public static float elitism = 0.02f;
    public static AdultSelection adultSelection = FULL_GENERATIONAL_REPLACEMENT;
    public static ParentSelection parentSelection = SIGMA;

    public static Problem problem = SURPRISE;
    public static int oneMaxSize = 40;
    public static int lolzSize = 40;
    public static int lolzZ = 21;
    public static int surpriseLength = 600;
    public static int surpriseS = 20;

    public static boolean global = false;

    //RANK
    public static double RANK_MAX = 1.5;
    public static double RANK_MIN = 2-RANK_MAX;

    //TOURNAMENT
    public static int K = 5;
    public static float e = 0.3f;

    public enum  AdultSelection {
       FULL_GENERATIONAL_REPLACEMENT,
       OVER_PRODUCTION,
       GENERATIONAL_MIXING,
       OVER_PRODUCED_GENERATIONAL_MIXING;
    }
    public enum ParentSelection {
        FITNESS_PROPORTIONAL,
        SIGMA,
        RANK,
        TOURNAMENT;
    }
    public enum Problem {
        ONE_MAX,
        LOLZ,
        SURPRISE,
        FLATLAND;

    }
    static Agent agent;
    static Network network;
    public static boolean staticRun = true;
    public static boolean fiveRuns = false;
    public static void init(){
        agent = new Agent(staticRun, fiveRuns, false);
        network = new Network(6,3,0.5f,6);
        bestPhenotype = null;
        generationalBestFitness = new ArrayList<>();
        avgFitnessList = new ArrayList<>();
        standardDeviationList = new ArrayList<>();
        globalBest = new ArrayList<>();
        lastGenerationalBest = null;
        averageFitness = -1;
        standardDeviation = -1;
        totalFitness = -1;
        iterations = 0;
        productionSize = populationSize;
        if (adultSelection == OVER_PRODUCTION){
            productionSize = populationSize*2;
        }
        childPopulation = new ArrayList<>();
        for (int i = 0; i < productionSize; i++) {
            switch (problem){
                case ONE_MAX:{
                    childPopulation.add(new OneMaxPheno(new GenoType(oneMaxSize)));
                    break;
                }
                case LOLZ:{
                    childPopulation.add(new LolzPrefix(new GenoType(lolzSize), lolzZ));
                    break;
                }
                case SURPRISE:{
                    childPopulation.add(new SurprisingSequence(surpriseLength, surpriseS, global));
                    break;
                }
                case FLATLAND:{
                    childPopulation.add(new FlatlandNetwork(new GenoType(810), 1000, agent, network, Neuron.Function.HYPERBOLIC));
                    break;
                }
            }
        }
    }
    public static void init(Problem p){
        problem = p;
        init();
    }

    public static void run(){
        System.out.println("Running up to: "+loopLimit+" iterations");
        while (iterations<loopLimit){
            if (develop()){
                break;
            }
            adultSelection();
            parentSelection();
            agent.changeBoards();
            System.out.println("Iteration: "+iterations);
            iterations++;
        }
        System.out.println("Done after: "+iterations+" iterations.");
        System.out.println("Best fitness: "+bestPhenotype.getFitness()+" phenotype: "+bestPhenotype.getPhenome());
        System.out.println("Best fitness: "+bestPhenotype.getFitness()+" phenotype: "+bestPhenotype);

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
                System.out.println("Best Tour{ fitness: "+bestPhenotype.getFitness()+" phenome: "+bestPhenotype.getPhenome()+" }");
            }

            if (i.isFit()){
                goalReached = true;
            }
        }
        if (adultPopulation != null){
            Collections.sort(adultPopulation);
            List<Phenotype> bestParent = adultPopulation.subList(0, (int) (adultPopulation.size()*elitism));
            for (Phenotype p: bestParent) {
                int score = p.mature();
                if (genBest == null || score>genBest.getFitness()){
                    genBest = p;
                }
            }
            int size = childPopulation.size();
            childPopulation.addAll(bestParent);
            Collections.sort(childPopulation);
            childPopulation = childPopulation.subList(0, size);
        }
        lastGenerationalBest = genBest;
        if (genBest.getFitness()>bestScore){
            bestScore = genBest.getFitness();
        }
        generationalBestFitness.add((double) genBest.getFitness());
        if (bestPhenotype != null){
            globalBest.add(bestScore);
        }
        return goalReached;
    }

    private static void mate(Phenotype firstPick, Phenotype secondPick) {
        childPopulation.add(firstPick.mate(secondPick));
        if (childPopulation.size()< productionSize){
            childPopulation.add(secondPick.mate(firstPick));

        }
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
        try {
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
        }catch (Exception e){
            e.printStackTrace();
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

    //TODO: FIX bug with break
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

