package moea;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Tour {

    private int totalDistance;
    private int totalCost;
    private TspGenom tspGenom;

    public void develop(){

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
