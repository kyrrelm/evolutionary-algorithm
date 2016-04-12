package ann;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Connection {

    public final Neuron n;
    private float weight;

    public Connection(Neuron n, float weight) {
        this.n = n;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
