/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.network.neuron_update_rules;

import org.simbrain.network.core.Layer;
import org.simbrain.network.core.Network.TimeType;
import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.updaterules.interfaces.ClippedUpdateRule;
import org.simbrain.network.updaterules.interfaces.NoisyUpdateRule;
import org.simbrain.network.util.BiasedMatrixData;
import org.simbrain.network.util.BiasedScalarData;
import org.simbrain.util.UserParameter;
import org.simbrain.util.stats.ProbabilityDistribution;
import org.simbrain.util.stats.distributions.UniformRealDistribution;

/**
 * <b>DecayNeuron</b> implements various forms of standard decay.
 */
public class DecayRule extends NeuronUpdateRule<BiasedScalarData, BiasedMatrixData> implements ClippedUpdateRule, NoisyUpdateRule {

    /**
     * The Default upper bound.
     */
    private static final double DEFAULT_CEILING = 1.0;

    /**
     * The Default lower bound.
     */
    private static final double DEFAULT_FLOOR = -1.0;

    /**
     * Update type.
     */
    public enum UpdateType {Relative, Absolute};

    @UserParameter(
            label = "Update Type",
            description = "Relative (percentage decay of current activation) vs. absolute (fixed decay amount)",
            order = 1)
    private UpdateType updateType = UpdateType.Relative;

    /**
     * Decay amount.
     */
    @UserParameter(
            label = "Decay amount",
            description = "The amount by which the activation is changed each iteration if absolute decay is chosen.",
            increment = .1, order = 3)
    private double decayAmount = .1;

    /**
     * Decay fraction.
     */
    @UserParameter(
            label = "Decay fraction",
            description = "The proportion of the distance between the current value and the base-line value, "
                    + "by which the activation is changed each iteration if relative decay is chosen.",
            increment = .1, order = 4)
    private double decayFraction = .1;

    /**
     * Base line.
     */
    @UserParameter(
            label = "Base Line",
            description = "An option to add noise.",
            increment = .1, order = 2)
    private double baseLine = 0;

    /**
     * Clipping.
     */
    private boolean clipping = true;

    /**
     * Noise generator.
     */
    private ProbabilityDistribution noiseGenerator = new UniformRealDistribution();

    /**
     * Add noise to the neuron.
     */
    private boolean addNoise = false;

    /**
     * The upper bound of the activity if clipping is used.
     */
    private double ceiling = DEFAULT_CEILING;

    /**
     * The lower bound of the activity if clipping is used.
     */
    private double floor = DEFAULT_FLOOR;

    @Override
    public void apply(Layer array, BiasedMatrixData data) {
        for (int i = 0; i < array.getOutputs().nrow() ; i++) {
            array.getOutputs().set(i, 0, decayRule(
                    array.getInputs().get(i, 0),
                    array.getOutputs().get(i, 0),
                    data.getBiases().get(i, 0)
            ));
        }
        clip(array.getOutputs());
    }

    @Override
    public void apply(Neuron neuron, BiasedScalarData data) {
        neuron.setActivation(decayRule(neuron.getInput(),
                neuron.getActivation(), data.getBias()));
        neuron.clip();
    }

    public double decayRule(double in, double activation, double bias) {
        double val = in  + activation + bias;
        double decayVal = 0;
        if (updateType == UpdateType.Relative) {
            decayVal = decayFraction * Math.abs(val - baseLine);
        } else  {
            decayVal = decayAmount;
        }
        if (val < baseLine) {
            val += decayVal;
            if (val > baseLine) {
                val = baseLine;
            }
        } else if (val > baseLine) {
            val -= decayVal;
            if (val < baseLine) {
                val = baseLine;
            }
        }
        if (addNoise) {
            val += noiseGenerator.sampleDouble();
        }
        return val;
    }

    @Override
    public BiasedMatrixData createMatrixData(int size) {
        return new BiasedMatrixData(size);
    }

    @Override
    public BiasedScalarData createScalarData() {
        return new BiasedScalarData();
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    @Override
    public TimeType getTimeType() {
        return TimeType.DISCRETE;
    }

    @Override
    public DecayRule deepCopy() {
        DecayRule dn = new DecayRule();
        dn.setUpdateType(getUpdateType());
        dn.setDecayAmount(getDecayAmount());
        dn.setDecayFraction(getDecayFraction());
        dn.setClipped(isClipped());
        dn.setUpperBound(getUpperBound());
        dn.setLowerBound(getLowerBound());
        dn.setAddNoise(getAddNoise());
        dn.noiseGenerator = noiseGenerator.deepCopy();
        return dn;
    }

    public double getDecayAmount() {
        return decayAmount;
    }

    public void setDecayAmount(final double decayAmount) {
        this.decayAmount = decayAmount;
    }

    public double getDecayFraction() {
        return decayFraction;
    }

    public void setDecayFraction(final double decayFraction) {
        this.decayFraction = decayFraction;
    }

    @Override
    public boolean getAddNoise() {
        return addNoise;
    }

    @Override
    public void setAddNoise(final boolean addNoise) {
        this.addNoise = addNoise;
    }

    @Override
    public ProbabilityDistribution getNoiseGenerator() {
        return noiseGenerator;
    }

    @Override
    public void setNoiseGenerator(final ProbabilityDistribution noise) {
        this.noiseGenerator = noise;
    }

    public double getBaseLine() {
        return baseLine;
    }

    public void setBaseLine(final double baseLine) {
        this.baseLine = baseLine;
    }

    @Override
    public String getName() {
        return "Decay";
    }

    @Override
    public double getUpperBound() {
        return ceiling;
    }

    @Override
    public double getLowerBound() {
        return floor;
    }

    @Override
    public void setUpperBound(double ceiling) {
        this.ceiling = ceiling;
    }

    @Override
    public void setLowerBound(double floor) {
        this.floor = floor;
    }

    @Override
    public boolean isClipped() {
        return clipping;
    }

    @Override
    public void setClipped(boolean clipping) {
        this.clipping = clipping;
    }

}
