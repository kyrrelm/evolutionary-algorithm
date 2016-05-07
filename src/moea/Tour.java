package moea;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Tour {

    private int totalDistance;
    private int totalCost;
    private TspGenom tspGenom;

    public Tour(TspGenom tspGenom) {
        this.totalDistance = 0;
        this.totalCost = 0;
        this.tspGenom = tspGenom;
    }

    public void develop(){
        int current = tspGenom.getCity(0);
        for (int next = 1; next < tspGenom.getLength(); next++, current++) {
            totalCost += Cities.coasts[current][tspGenom.getCity(next)];
            totalDistance += Cities.distances[current][tspGenom.getCity(next)];
            current = next;
        }
        totalCost += Cities.coasts[current][tspGenom.getCity(0)];
        totalDistance += Cities.distances[current][tspGenom.getCity(0)];

    }

    public boolean isDomenating(Tour other){
        if (this.totalCost > other.totalCost || this.totalDistance > totalDistance){
            return false;
        }
        if (this.totalCost < other.totalCost || this.totalDistance < totalDistance){
            return true;
        }
        return false;
    }
}
