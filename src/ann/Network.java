package ann;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Network {

    private final int[] hiddenNodesInLayer;
    private ArrayList<Neuron> inputNodes;
    private ArrayList<ArrayList<Neuron>> hiddenLayers;
    private ArrayList<Neuron> outputNodes;
    private ArrayList<Neuron> allNodes;

    public Network(int numberOfInputNodes, int numberOfOutputNodes, float standardThreshold, float[] weights, int... hiddenNodesInLayer) {
        this.inputNodes = new ArrayList<>();
        this.hiddenLayers = new ArrayList<>();
        this.outputNodes = new ArrayList<>();
        this.allNodes = new ArrayList<>();
        this.hiddenNodesInLayer = hiddenNodesInLayer;
        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes.add(new Neuron(standardThreshold));
        }
        allNodes.addAll(inputNodes);
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
            allNodes.addAll(layer);
            previousLayer = layer;
        }
        for (int i = 0; i <numberOfOutputNodes; i++) {
            Neuron outputNode = new Neuron(0.5f);
            for (Neuron parent:previousLayer) {
                outputNode.connect(new Connection(parent,weights[weightIndex++]));
            }
            outputNodes.add(outputNode);
        }
        allNodes.addAll(outputNodes);
    }

    public void setWeights(float[] weights){
        int weightIndex = 0;
        ArrayList<Neuron> previousLayer = inputNodes;
        for (ArrayList<Neuron> layer: hiddenLayers) {
            for (Neuron n: layer) {
                n.clearConnections();
                for (Neuron parent : previousLayer) {
                    n.connect(new Connection(parent, weights[weightIndex++]));
                }
            }
            previousLayer = layer;
        }
        for (Neuron outputNode: outputNodes) {
            outputNode.clearConnections();
            for (Neuron parent:previousLayer) {
                outputNode.connect(new Connection(parent,weights[weightIndex++]));
            }
        }

    }

    public float[] run(Neuron.Function function, float... inputs){
        for (int i = 0; i <inputs.length; i++) {
            inputNodes.get(i).setValue(inputs[i]);
        }
        float[] result = new float[outputNodes.size()];
        for (int i = 0; i < result.length; i++) {
            outputNodes.get(i).activate(function);
            result[i] = outputNodes.get(i).getValue();
        }
        reset();
        return result;
    }

    private void reset() {
        allNodes.forEach(Neuron::reset);
    }

    public static void main(String[] args) {
        float[] weights = new float[16];
        Random r = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = r.nextFloat();
        }
        Network net = new Network(2,2,0.5f,weights,2,3);
        float[] result = net.run(Neuron.Function.SIGMOID, 1f,0.5f);
        System.out.print("Result: ");
        for (float f:result) {
            System.out.print(f+", ");
        }
    }
}
