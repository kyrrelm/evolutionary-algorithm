package moea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Simulation {

    private int TOUR_SIZE = 5;

    private List<Tour> childPopulation;
    private List<Tour> adultPopulation;
    private int populationSize;

    public Simulation(int populationSize) {
        this.childPopulation = new ArrayList<>();
        this.adultPopulation = new ArrayList<>();
        this.populationSize = populationSize;
    }

    public void run(){
        init();
        mainLoop();
    }

    private void mainLoop() {
        adultPopulation.addAll(childPopulation);
        fastNonDominatedSort();
    }

    private void fastNonDominatedSort() {
        for (int i = 0; i < adultPopulation.size(); i++) {
            for (int j = i+1; j < adultPopulation.size(); j++) {
                Tour outer = adultPopulation.get(i);
                Tour inner = adultPopulation.get(j);
                if (outer.isDomenating(inner)){
                    outer.getDominatingSet().add(inner);
                    inner.incrementDominationCount();
                } else if (inner.isDomenating(outer)){
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
        ArrayList<Tour> nextFront = new ArrayList<>();
        ArrayList<ArrayList<Tour>> fronts = new ArrayList<>();
        while (!currentFront.isEmpty()){
            for (Tour tour: currentFront) {
                for (Tour dominated: tour.getDominatingSet()){
                    if (dominated.decrementDominationCount() == 0){
                        nextFront.add(dominated);
                    }
                }
            }
            fronts.add(currentFront);
            currentFront = nextFront;
            nextFront = new ArrayList<>();
        }
        System.out.println();
    }

    private void init() {
        for (int i = 0; i < populationSize; i++) {
            adultPopulation.add(new Tour(new TspGenom(TOUR_SIZE)).develop());
        }
    }

    public static void main(String[] args) {
        new Simulation(20).run();
        new Tour(new TspGenom(5)).develop();
    }
}
