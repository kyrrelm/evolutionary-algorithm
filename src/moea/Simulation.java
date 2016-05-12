package moea;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.util.*;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Simulation {

    private  int iterations;
    private int TOUR_SIZE = 48;
    private int K = 50;
    private float crossoverRate;
    private float mutationRate;

    private float e = 0.3f;
    private List<Tour> childPopulation;
    private List<Tour> adultPopulation;
    private int populationSize;

    public Simulation(int iterations, int populationSize, float crossoverRate, float mutationRate) {
        this.iterations = iterations;
        this.childPopulation = new ArrayList<>();
        this.adultPopulation = new ArrayList<>();
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    public void run(){
        init();
        mainLoop();
    }

    private void mainLoop() {
        int count = 0;
        while (true) {
            adultPopulation.forEach(Tour::reset);
            develop();
            adultPopulation.addAll(childPopulation);
            childPopulation.clear();
            fastNonDominatedSort();
//            System.out.println("Best individual1: "+ adultPopulation.get(0));
//            System.out.println("Best individual2: "+ adultPopulation.get(1));
//            System.out.println("Best individual3: "+ adultPopulation.get(2));
//            System.out.println("Best individual4: "+ adultPopulation.get(3));
//            System.out.println("Best individual5: "+ adultPopulation.get(4));
//            System.out.println("Best individual6: "+ adultPopulation.get(5));
//            System.out.println("Best individual7: "+ adultPopulation.get(6));
//            System.out.println("----------------------------------------------------------------------------------------------");
            adultPopulation.subList(populationSize, adultPopulation.size()).clear();
            if (count > iterations)
                break;
            tournamentSelection();
            count++;
        }
        System.out.println("------------------------------- DONE -------------------------------");
        ArrayList<Tour> paretoFront = new ArrayList<>();
        for (Tour t:adultPopulation) {
            if (t.getRank() == 1){
                paretoFront.add(t);
            }
        }
        paretoFront.sort((o1, o2) -> o2.getTotalCost()-o1.getTotalCost());
        System.out.println("First:"+ paretoFront.get(0));
        System.out.println("Last:"+ paretoFront.get(paretoFront.size()-1));
    }

    private void tournamentSelection() {
        for (int i = 0; i < populationSize /2; i++) {
            Tour firstPick = pickChampion();
            Tour secondPick = null;
            boolean duplicate = true;
            while (duplicate){
                secondPick = pickChampion();
                duplicate = secondPick == firstPick;
            }
            mate(firstPick, secondPick);
    }
    }

    private Tour pickChampion() {
        HashSet<Tour> fighters = new HashSet<>();
        Random rnd = new Random();
        while (fighters.size()<K){
            int r = rnd.nextInt(adultPopulation.size());
            fighters.add(adultPopulation.get(r));
        }
        Tour[] fightArray = fighters.toArray(new Tour[fighters.size()]);
        Arrays.sort(fightArray);
        if (1f-e>rnd.nextFloat()){
            return fightArray[0];
        }
        return fightArray[rnd.nextInt(fightArray.length-1)+1];
    }

    private void mate(Tour firstPick, Tour secondPick) {
        childPopulation.addAll(firstPick.mate(secondPick));
    }

    private void fastNonDominatedSort() {
        for (int i = 0; i < adultPopulation.size(); i++) {
            for (int j = i+1; j < adultPopulation.size(); j++) {
                Tour outer = adultPopulation.get(i);
                Tour inner = adultPopulation.get(j);
                if (outer.isDominating(inner)){
                    outer.getDominatingSet().add(inner);
                    inner.incrementDominationCount();
                } else if (inner.isDominating(outer)){
                    inner.getDominatingSet().add(outer);
                    outer.incrementDominationCount();
                }
            }
        }
        ArrayList<Tour> currentFront = new ArrayList<>();
        for (Tour tour: adultPopulation) {
            if (tour.getDominationCount() == 0) {
                currentFront.add(tour);
            }
        }
        System.out.println("Size of Pareto Front: "+currentFront.size());
        currentFront.sort((o1, o2) -> o2.getTotalCost()-o1.getTotalCost());
        System.out.println("First:"+ currentFront.get(0));
        System.out.println("Last:"+ currentFront.get(currentFront.size()-1));
        System.out.println("----------------------------------------------------------------");
        int rankCount = 1;
        ArrayList<Tour> nextFront = new ArrayList<>();
        while (!currentFront.isEmpty()){
            double maxCost = -1;
            double minCost = Integer.MAX_VALUE;

            double maxDist = -1;
            double minDist = Integer.MAX_VALUE;

            for (Tour tour: currentFront) {
                tour.setRank(rankCount);
                maxCost = Math.max(maxCost,tour.getTotalCost());
                minCost = Math.min(minCost, tour.getTotalCost());
                maxDist = Math.max(maxDist, tour.getTotalDist());
                minDist = Math.min(minDist, tour.getTotalDist());

                for (Tour dominated: tour.getDominatingSet()){
                    if (dominated.decrementDominationCount() == 0){
                        nextFront.add(dominated);
                    }
                }
            }
            currentFront.sort((o1, o2) -> o2.getTotalCost()-o1.getTotalCost());
            currentFront.get(0).setCrowdDistance(Integer.MAX_VALUE);
            currentFront.get(currentFront.size()-1).setCrowdDistance(Integer.MAX_VALUE);
            for (int i = 1; i < currentFront.size()-1; i++) {
                double sum = Math.abs((double)currentFront.get(i+1).getTotalCost() - (double)currentFront.get(i-1).getTotalCost())/(maxCost-minCost);
                sum += Math.abs((double)currentFront.get(i+1).getTotalDist() - (double)currentFront.get(i-1).getTotalDist())/(maxDist-minDist);
                currentFront.get(i).setCrowdDistance(sum);
            }

            currentFront = nextFront;
            nextFront = new ArrayList<>();
            rankCount++;
        }
        Collections.sort(adultPopulation);
    }

    private void develop() {
        childPopulation.forEach(Tour::develop);
    }

    private void init() {
        for (int i = 0; i < populationSize; i++) {
            adultPopulation.add(new Tour(new TspGenom(TOUR_SIZE, crossoverRate, mutationRate)).develop());
        }
    }

     protected LineChart<Number,Number> createChart(boolean onlyPareto){
         List<Tour> population;
         if (onlyPareto){
             population = new ArrayList<>();
             for (Tour t:adultPopulation) {
                 if (t.getRank() == 1){
                     population.add(t);
                 }
             }
         }else {
             population = adultPopulation;
         }
         int maxDist = -1;
         int minDist = Integer.MAX_VALUE;
         int maxCost = -1;
         int minCost = Integer.MAX_VALUE;
         for(Tour t: population){
             if (t.getTotalDist()<minDist)
                 minDist = t.getTotalDist();
             if (t.getTotalDist()>maxDist)
                 maxDist = t.getTotalDist();

             if (t.getTotalCost()<minCost)
                 minCost = t.getTotalCost();
             if (t.getTotalCost()>maxCost)
                 maxCost = t.getTotalCost();
         }
         final NumberAxis xAxis = new NumberAxis(minDist-1000,maxDist+1000,1000);
         final NumberAxis yAxis = new NumberAxis(minCost-10, maxCost+10, 10);
         xAxis.setLabel("Distance");
         yAxis.setLabel("Cost");
         //creating the chart
         final LineChart<Number, Number> lineChart =
                 new LineChart<Number, Number>(xAxis, yAxis);
         lineChart.setTitle("");
         ArrayList< XYChart.Series> fronts = new ArrayList<>();
         int rank = 1;
         XYChart.Series front = new XYChart.Series();
         Collections.sort(population);
         fronts.add(front);
         for (Tour tour : population) {
             //System.out.println(tour);
             if (tour.getRank() == rank){
                 front.getData().add(new XYChart.Data(tour.getTotalDist(), tour.getTotalCost()));
             }else {
                 front = new XYChart.Series();
                 fronts.add(front);
                 front.getData().add(new XYChart.Data(tour.getTotalDist(), tour.getTotalCost()));
                 rank++;
             }
         }
         for (XYChart.Series f: fronts){
             lineChart.getData().add(f);
         }
         //lineChart.getData().addAll((XYChart.Series<Number, Number>[]) fronts.toArray());

         return lineChart;
    }
}
