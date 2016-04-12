package ann;

import java.util.ArrayList;

/**
 * Created by Kyrre on 11.04.2016.
 */
public class Network {

    private ArrayList<Neuron> inputNodes;
    private ArrayList<ArrayList<Neuron>> hiddenLayers;
    private ArrayList<Neuron> outputNodes;

    public Network(int numberOfInputNodes, int numberOfOutputNodes, int... hiddenNodesInLayer) {
        this.inputNodes = new ArrayList<>();
        this.hiddenLayers = new ArrayList<>();
        this.outputNodes = new ArrayList<>();
        for (int i = 0; i < numberOfInputNodes; i++) {
            inputNodes.add(new Neuron(0.5f));
        }
        ArrayList<Neuron> previousLayer = inputNodes;
        for (int hiddenNodes: hiddenNodesInLayer) {
            ArrayList<Neuron> layer = new ArrayList<>();
            for (int i = 0; i < hiddenNodes; i++) {
                Neuron n = new Neuron(0.5f);
                //n.connect(previousLayer);
                layer.add(n);
            }
            hiddenLayers.add(layer);
            previousLayer = layer;
        }
        for (int i = 0; i <numberOfOutputNodes; i++) {
            Neuron outputNode = new Neuron(0.5f);
            //outputNode.connect(previousLayer);
            outputNodes.add(outputNode);
        }
    }

    public int[] run(float... inputs){
        for (int i = 0; i <inputs.length; i++) {
            inputNodes.get(i).setValue(inputs[i]);
        }
        int[] result = new int[outputNodes.size()];
        for (int i = 0; i < result.length; i++) {
            //outputNodes.get(i).activate();
            result[i] = outputNodes.get(i).hasFired() ? 1 : 0;
        }
        return result;
    }

    public static void main(String[] args) {
        Network net = new Network(2,2,2,3);

    }
}
