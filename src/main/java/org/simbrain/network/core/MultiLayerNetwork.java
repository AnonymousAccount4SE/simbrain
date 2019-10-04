package org.simbrain.network.core;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.Layer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.simbrain.util.UserParameter;
import org.simbrain.util.propertyeditor.EditableObject;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultiLayerNetwork implements ArrayConnectable {

    private org.deeplearning4j.nn.multilayer.MultiLayerNetwork network;

    private Network parent;

    private INDArray input;

    private WeightMatrix incomingWeightMatrix;

    private WeightMatrix outgoingWeightMatrix;

    private long outputSize;

    private List<Integer> sizes;

    private Point2D location = new Point2D.Double();

    /**
     * Support for property change events.
     */
    private transient PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    // public MultiLayerNetwork(List<Layer> layers) {
    //     NeuralNetConfiguration.ListBuilder listBuilder = new NeuralNetConfiguration.Builder()
    //             .updater(new Sgd(0.1))
    //             .seed(1234)
    //             .biasInit(0) // init the bias with 0 - empirical value, too
    //             // from "http://deeplearning4j.org/architecture": The networks can
    //             // process the input more quickly and more accurately by ingesting
    //             // minibatches 5-10 elements at a time in parallel.
    //             // this example runs better without, because the dataset is smaller than
    //             // the mini batch size
    //             .miniBatch(false)
    //             .list();
    //
    //     for (Layer layer : layers) {
    //         listBuilder = listBuilder.layer(layer);
    //         if (layer instanceof OutputLayer) {
    //             outputSize = ((OutputLayer) layer).getNOut();
    //         }
    //     }
    //
    //     input = Nd4j.zeros(1, ((DenseLayer) layers.get(0)).getNIn());
    //
    //
    //
    //     network = new MultiLayerNetwork(listBuilder.build());
    //     network.init();
    // }

    public MultiLayerNetwork(Network parent, List<Integer> sizes) {

        if (sizes.size() < 2) {
            throw new IllegalArgumentException("Sizes must have least 2 elements");
        }

        this.parent = parent;

        this.sizes = sizes;

        input = Nd4j.zeros(1, sizes.get(0));

        outputSize = sizes.get(sizes.size() - 1);

        NeuralNetConfiguration.ListBuilder listBuilder = new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.1))
                .seed(1234)
                .biasInit(0) // init the bias with 0 - empirical value, too
                // from "http://deeplearning4j.org/architecture": The networks can
                // process the input more quickly and more accurately by ingesting
                // minibatches 5-10 elements at a time in parallel.
                // this example runs better without, because the dataset is smaller than
                // the mini batch size
                .miniBatch(false)
                .list();

        for (int i = 0; i < sizes.size() - 2; i++) {
            listBuilder = listBuilder.layer(
                    new DenseLayer.Builder().nIn(sizes.get(i)).nOut(sizes.get(i + 1))
                            .activation(Activation.SIGMOID)
                            // random initialize weights with values between 0 and 1
                            .weightInit(new UniformDistribution(0, 1))
                            .build()
            );
        }

        listBuilder
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nOut(sizes.get(sizes.size() - 1))
                .activation(Activation.SOFTMAX)
                .weightInit(new UniformDistribution(0, 1))
                .build());

        MultiLayerConfiguration conf = listBuilder.build();

        org.deeplearning4j.nn.multilayer.MultiLayerNetwork net = new org.deeplearning4j.nn.multilayer.MultiLayerNetwork(conf);
        net.init();

        network = net;
    }

    public static List<Layer> getLayerFromWeightMatrices(List<WeightMatrix> matrices) {
        List<Layer> ret = matrices.stream().map(WeightMatrix::asLayer).collect(Collectors.toList());
        ret.add(((NeuronArray) matrices.get(matrices.size() - 1).getTarget()).asLayer());
        return ret;
    }

    public org.deeplearning4j.nn.multilayer.MultiLayerNetwork getNetwork() {
        return network;
    }

    @Override
    public INDArray getActivationArray() {
        return network.output(input);
    }

    @Override
    public void setActivationArray(INDArray activations) {
        input = activations;
    }

    @Override
    public long arraySize() {
        return input.length();
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    @Override
    public long inputSize() {
        return input.length();
    }

    @Override
    public long outputSize() {
        return outputSize;
    }

    @Override
    public WeightMatrix getIncomingWeightMatrix() {
        return incomingWeightMatrix;
    }

    @Override
    public void setIncomingWeightMatrix(WeightMatrix incomingWeightMatrix) {
        this.incomingWeightMatrix = incomingWeightMatrix;
    }

    @Override
    public WeightMatrix getOutgoingWeightMatrix() {
        return outgoingWeightMatrix;
    }

    @Override
    public void setOutgoingWeightMatrix(WeightMatrix outgoingWeightMatrix) {
        this.outgoingWeightMatrix = outgoingWeightMatrix;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setLocation(Point2D location) {
        this.location = location;
        fireLocationChange();
    }

    @Override
    public Point2D getLocation() {
        return new Point2D.Double(location.getX() + 150 / 2.0, location.getY() + 50 / 2.0);
    }

    @Override
    public void onLocationChange(Runnable task) {
        addPropertyChangeListener(evt -> {
            if ("moved".equals(evt.getPropertyName())) {
                task.run();
            }
        });
    }

    public void fireLocationChange() {
        changeSupport.firePropertyChange("moved", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return Arrays.stream(network.getLayers()).map(l -> l.getClass().getSimpleName()).collect(Collectors.joining(", "));
    }

    public static class CreationTemplate implements EditableObject {

        @UserParameter(
                label = "Layer Size (Space Separated List)"
        )
        private String layerSize = "4 10 2";


        public MultiLayerNetwork create(Network parent) {

            List<Integer> sizes;

            try {
                sizes = Arrays.stream(layerSize.split(" "))
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
                sizes = Arrays.asList(4, 10, 2);
            }

            MultiLayerNetwork net =
                    new MultiLayerNetwork(parent, sizes);
            return net;
        }

        @Override
        public String getName() {
            return "Multi-Layer Network";
        }

    }
}
