package ann;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Network {

    private ArrayList<Neuron> inputNodes;
    private ArrayList<ArrayList<Neuron>> hiddenLayers;
    private ArrayList<Neuron> outputNodes;

    public Network(int numberOfInputNodes, int numberOfOutputNodes, float[] weights, int... hiddenNodesInLayer) {
        this.inputNodes = new ArrayList<>();
        this.hiddenLayers = new ArrayList<>();
        this.outputNodes = new ArrayList<>();
        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes.add(new Neuron(0.5f));
        }
        int weightIndex = 0;
        ArrayList<Neuron> previousLayer = inputNodes;
        for (int hiddenNodes: hiddenNodesInLayer) {
            ArrayList<Neuron> layer = new ArrayList<>();
            for (int i = 0; i < hiddenNodes; i++) {
                Neuron n = new Neuron(0.5f);
                for (Neuron parent : previousLayer) {
                    n.connect(new Connection(parent, weights[weightIndex++]));
                }
                layer.add(n);
            }
            hiddenLayers.add(layer);
            previousLayer = layer;
        }
        for (int i = 0; i <numberOfOutputNodes; i++) {
            Neuron outputNode = new Neuron(0.5f);
            for (Neuron parent:previousLayer) {
                outputNode.connect(new Connection(parent,weights[weightIndex++]));
            }
            outputNodes.add(outputNode);
        }
    }

    public float[] run(float... inputs){
        for (int i = 0; i <inputs.length; i++) {
            inputNodes.get(i).setValue(inputs[i]);
        }
        float[] result = new float[outputNodes.size()];
        for (int i = 0; i < result.length; i++) {
            outputNodes.get(i).activate(Neuron.Function.STEP);
            result[i] = outputNodes.get(i).getValue();
        }
        return result;
    }

    public static void main(String[] args) {
        float[] weights = new float[18];
        Random r = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = r.nextFloat();
        }
        Network net = new Network(2,2,weights,2,3);
        float[] result = net.run(1f,0.5f);
        System.out.print("Result: ");
        for (float f:result) {
            System.out.print(f+", ");
        }
    }
}
