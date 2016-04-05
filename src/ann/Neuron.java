package ann;

import java.util.ArrayList;

/**
 * Created by Kyrre on 05.04.2016.
 */
public class Neuron {
    private ArrayList<Neuron> inputs;
    private float weight;
    private float threshold;
    private boolean fired;

    public Neuron (float threshold) {
        this.threshold = threshold;
        fired = false;
        inputs = new ArrayList();
    }

    public void connect (Neuron ... ns) {
        for (Neuron n : ns) inputs.add(n);
        //inputs.addAll(Arrays.asList(ns));
    }

    public void setWeight (float newWeight) {
        weight = newWeight;
    }

    public float getWeight () {
        return weight;
    }

    public float activate() {
        if (inputs.size() > 0) {
            float totalWeight = 0.0f;
            for (Neuron n : inputs) {
                if (!n.hasFired()){
                    n.activate();
                }
                totalWeight += (n.hasFired()) ? n.getWeight() : 0.0f;
            }
            fired = totalWeight > threshold;
            return totalWeight;
        }
        else if (weight != 0.0f) {
            fired = weight > threshold;
            return weight;
        }
        else {
            return 0.0f;
        }
    }

    public boolean hasFired() {
        return fired;
    }

    public static void main (String [] args) {
        //XOR test
        Neuron xor = new Neuron(0.5f);
        Neuron left = new Neuron(1.5f);
        Neuron right = new Neuron(0.5f);
        left.setWeight(-1.0f);
        right.setWeight(1.0f);
        xor.connect(left, right);

        Neuron inputLeft = new Neuron(.0f);
        inputLeft.setWeight(1f);
        Neuron inputRight = new Neuron(1.0f);
        inputRight.setWeight(1f);
        left.connect(inputLeft, inputRight);
        right.connect(inputLeft, inputRight);

        xor.activate();

        System.out.println("Result: " + xor.hasFired());

    }
}
