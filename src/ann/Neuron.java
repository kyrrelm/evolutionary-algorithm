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
        switch (f){
            case STEP:{
                return stepFunction();
            }
        }
        return -1;
    }

    private float stepFunction(){
        if (inputs.size() > 0) {
            value = 0.0f;
            for (Connection connection : inputs) {
                Neuron n = connection.n;
                if (!n.hasFired()){
                    n.stepFunction();
                }
                value += n.getValue()* connection.getWeight();
            }
        }
        fired = value > threshold;
        if (fired){
            value = 1;
        }else {
            value = 0;
        }
        return value;
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
        inputRight.setValue(0f);
        xor.activate(Function.STEP);

        System.out.println("Result: " + xor.hasFired());

    }

    public enum Function{
        STEP,
        HYPERBOLIC,
        SIGMOIDS;
    }
}
