package moea;

import java.util.List;

/**
 * Created by Kyrre on 07.05.2016.
 */
public class Simulation {

    private List<Tour> childPopulation;
    private List<Tour> adultPopulation;


    public void run(){

    }

    public static void main(String[] args) {
        new Simulation().run();
        new Tour(new TspGenom(5)).develop();
    }
}
