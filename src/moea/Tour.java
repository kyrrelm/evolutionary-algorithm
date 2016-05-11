package moea;

import java.util.HashSet;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Tour implements Comparable<Tour> {

    private int rank;
    private int totalDistance;
    private int totalCost;
    private TspGenom tspGenom;
    private HashSet<Tour> dominatingSet;
    private int dominationCount;

    public Tour(TspGenom tspGenom) {
        this.totalDistance = 0;
        this.totalCost = 0;
        this.tspGenom = tspGenom;
        this.dominatingSet = new HashSet<>();
        this.dominationCount = 0;
        this.rank = Integer.MAX_VALUE;
    }

    public Tour develop(){
        for (int next = 1, current = 0; next < tspGenom.getLength(); next++, current++) {
            totalCost += Cities.coasts[tspGenom.getCity(current)][tspGenom.getCity(next)];
            totalDistance += Cities.distances[tspGenom.getCity(current)][tspGenom.getCity(next)];
        }
        totalCost += Cities.coasts[tspGenom.getCity(tspGenom.getLength()-1)][tspGenom.getCity(0)];
        totalDistance += Cities.distances[tspGenom.getCity(tspGenom.getLength()-1)][tspGenom.getCity(0)];
        return this;
    }

    public boolean isDominating(Tour other){
        if (this.totalCost > other.totalCost || this.totalDistance > other.totalDistance){
            return false;
        }
        if (this.totalCost < other.totalCost || this.totalDistance < other.totalDistance){
            return true;
        }
        return false;
    }

    public HashSet<Tour> getDominatingSet() {
        return dominatingSet;
    }

    public int incrementDominationCount() {
        return ++this.dominationCount;
    }

    public int getDominationCount() {
        return dominationCount;
    }

    public int decrementDominationCount() {
        return --dominationCount;
    }

    @Override
    public int compareTo(Tour o) {
        //return this.totalDistance -o.totalDistance;
        return this.rank - o.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Tour[] mate(Tour secondPick) {
        TspGenom[] childGenoms = tspGenom.crossover(secondPick.tspGenom);
        return new Tour[]{new Tour(childGenoms[0]), new Tour(childGenoms[1])};
    }

    @Override
    public String toString() {
        String value = "Distance: "+totalDistance+"  Cost: "+totalCost+"  Cities:";
        for (int i = 0; i < tspGenom.getLength(); i++) {
            value += " "+(tspGenom.getCity(i)+1)+",";
        }
        return value;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getTotalDist() {
        return totalDistance;
    }
}
