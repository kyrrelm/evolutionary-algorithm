package ann;

import java.util.ArrayList;

/**
 * Created by Kyrre on 05.04.2016.
 */
public class Neuron {
    private ArrayList<Connection> inputs;
    private float value;
    private float threshold;
    private boolean fired;

    public Neuron (float threshold) {
        this.threshold = threshold;
        fired = false;
        inputs = new ArrayList();
        this.value = 0.0f;
    }

    public void connect (Connection ... cs) {
        for (Connection c : cs) inputs.add(c);
    }
    public void connect (ArrayList<Connection> connections) {
        inputs.addAll(connections);
    }

    public void setValue(float newWeight) {
        value = newWeight;
    }

    public float getValue() {
        return value;
    }

    public float activate(Function f) {
        if (inputs.size() > 0) {
            value = 0.0f;
            for (Connection connection : inputs) {
                Neuron n = connection.n;
                if (!n.hasFired()){
                    n.activate(f);
                }
                value += n.getValue()* connection.getWeight();
            }
        }
        value = activationFunction(f, value);
        fired = true;
        return value;
    }

    private float activationFunction(Function f, float value){
        switch (f){
            case STEP:{
                return stepFunction(value);
            }
            case SIGMOID:{
                return sigmoid(value);
            }
        }
        return -1;
    }

    private float sigmoid(float value){
        return (float) (1 / (1 + Math.exp(-value)));
    }

    private float stepFunction(float value){
        return value > threshold ? 1 : 0;
    }

    public boolean hasFired() {
        return fired;
    }

    public static void main (String [] args) {
        //XOR test
        Neuron xor = new Neuron(1.5f);
        Neuron left = new Neuron(-1.5f);
        Neuron right = new Neuron(0.5f);
        xor.connect(new Connection(left, 1.0f), new Connection(right, 1.0f));


        Neuron inputLeft = new Neuron(0.0f);
        Neuron inputRight = new Neuron(0.0f);
        left.connect(new Connection(inputLeft, -1.0f), new Connection(inputRight, -1.0f));
        right.connect(new Connection(inputLeft, 1.0f), new Connection(inputRight, 1.0f));

        inputLeft.setValue(1f);
        inputRight.setValue(1f);
        xor.activate(Function.STEP);

        System.out.println("Result: " + xor.getValue());



    }

    public enum Function{
        STEP,
        HYPERBOLIC,
        SIGMOID;
    }
}
