package moea;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    private double crowdDistance;

    public Tour(TspGenom tspGenom) {
        this.totalDistance = 0;
        this.totalCost = 0;
        this.tspGenom = tspGenom;
        this.dominatingSet = new HashSet<>();
        this.dominationCount = 0;
        this.rank = Integer.MAX_VALUE;
        this.crowdDistance = -1;
    }

    public Tour develop(){
        for (int next = 1, current = 0; next < tspGenom.getLength(); next++, current++) {
            totalCost += Cities.costs[tspGenom.getCity(current)][tspGenom.getCity(next)];
            totalDistance += Cities.distances[tspGenom.getCity(current)][tspGenom.getCity(next)];
        }
        totalCost += Cities.costs[tspGenom.getCity(tspGenom.getLength()-1)][tspGenom.getCity(0)];
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
        if (this.rank == o.rank){
            if (this.crowdDistance > o.crowdDistance)
                return -1;
            if (this.crowdDistance < o.crowdDistance)
                return 1;
        }
        return this.rank - o.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<Tour> mate(Tour secondPick) {
        ArrayList<TspGenom> childGenoms = tspGenom.crossover(secondPick.tspGenom);
        if (childGenoms == null){
            return new ArrayList<>();
        }
        ArrayList<Tour> value = new ArrayList<>();
        for (TspGenom genom: childGenoms) {
            value.add(new Tour(genom));
        }
        return value;
    }

    @Override
    public String toString() {
        String value = "Distance: "+totalDistance+"  Cost: "+totalCost+" Rank: "+rank+" CrowdDist: "+crowdDistance+"  Cities:";
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

    public void setCrowdDistance(double crowdDistance) {
        this.crowdDistance = crowdDistance;
    }

    public int getRank() {
        return rank;
    }

    public void reset() {
        this.dominatingSet.clear();
        this.dominationCount = 0;
        this.rank = Integer.MAX_VALUE;
        this.crowdDistance = -1;
    }
}
