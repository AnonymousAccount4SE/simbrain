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
package org.simbrain.network.neurons;

import org.simbrain.network.interfaces.Neuron;
import org.simbrain.network.interfaces.SpikingNeuron;
import org.simbrain.network.util.RandomSource;


/**
 * <b>IntegrateAndFireNeuron</b>.
 */
public class IntegrateAndFireNeuron extends SpikingNeuron {

    /** Resistance. */
    private double resistance = 1;

    /** Time constant. */
    private double timeConstant = 1;

    /** Threshold. */
    private double threshold = .8;

    /** Reset potential. */
    private double resetPotential = .1;

    /** Resting potential. */
    private double restingPotential = .5;

    /** Noise dialog. */
    private RandomSource noiseGenerator = new RandomSource();

    /** Add noise to neuron. */
    private boolean addNoise = false;

    /** Clipping. */
    private boolean clipping = false;

    /**
     * Default constructor needed for external calls which create neurons then
     * set their parameters.
     */
    public IntegrateAndFireNeuron() {
    }

    /**
     * This constructor is used when creating a neuron of one type from another
     * neuron of another type Only values common to different types of neuron
     * are copied.
     *
     * @param n Neuron to be made type integrate and fire
     */
    public IntegrateAndFireNeuron(final Neuron n) {
        super(n);
    }

    /**
     * @return duplicate IntegrateAndFireNeuron (used, e.g., in copy/paste).
     */
    public IntegrateAndFireNeuron duplicate() {
        IntegrateAndFireNeuron ifn = new IntegrateAndFireNeuron();
        ifn = (IntegrateAndFireNeuron) super.duplicate(ifn);
        ifn.setRestingPotential(getRestingPotential());
        ifn.setResetPotential(getResetPotential());
        ifn.setThreshold(getThreshold());
        ifn.setTimeConstant(getTimeConstant());
        ifn.setResistance(getResistance());
        ifn.setClipping(getClipping());
        ifn.setAddNoise(getAddNoise());
        ifn.noiseGenerator = noiseGenerator.duplicate(noiseGenerator);

        return ifn;
    }

    /**
     * Update neuron.
     */
    public void update() {
        double inputs = getWeightedInputs();

        if (addNoise) {
            inputs += noiseGenerator.getRandom();
        }

        double val = getActivation()
                     + (this.getParentNetwork().getRootNetwork().getTimeStep() / timeConstant * (restingPotential - getActivation()
                     + (resistance * inputs)));

        if (val > threshold) {
            setHasSpiked(true);
            val = resetPotential;
        } else {
            setHasSpiked(false);
        }

        if (clipping) {
            val = clip(val);
        }

        setBuffer(val);
    }

    /**
     * @return Returns the lowerValue.
     */
    public double getRestingPotential() {
        return restingPotential;
    }

    /**
     * @param restingPotential The restingPotential to set.
     */
    public void setRestingPotential(final double restingPotential) {
        this.restingPotential = restingPotential;
    }

    /**
     * @return Returns the upperValue.
     */
    public double getResistance() {
        return resistance;
    }

    /**
     * @param resistance The resistance to set.
     */
    public void setResistance(final double resistance) {
        this.resistance = resistance;
    }

    /**
     * @return Name of neuron type.
     */
    public static String getName() {
        return "Integrate and fire";
    }

    /**
     * @return Returns the lowerValue.
     */
    public boolean getAddNoise() {
        return addNoise;
    }

    /**
     * @param addNoise The addNoise to set.
     */
    public void setAddNoise(final boolean addNoise) {
        this.addNoise = addNoise;
    }

    /**
     * @param noise The noise to set.
     */
    public void setAddNoise(final RandomSource noise) {
        this.noiseGenerator = noise;
    }

    /**
     * @return Returns the clipping.
     */
    public boolean getClipping() {
        return clipping;
    }

    /**
     * @param clipping The clipping to set.
     */
    public void setClipping(final boolean clipping) {
        this.clipping = clipping;
    }

    /**
     * @return Returns the noiseGenerator.
     */
    public RandomSource getNoiseGenerator() {
        return noiseGenerator;
    }

    /**
     * @param noiseGenerator The noiseGenerator to set.
     */
    public void setNoiseGenerator(final RandomSource noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    /**
     * @return Returns the resetPotential.
     */
    public double getResetPotential() {
        return resetPotential;
    }

    /**
     * @param resetPotential The resetPotential to set.
     */
    public void setResetPotential(final double resetPotential) {
        this.resetPotential = resetPotential;
    }

    /**
     * @return Returns the threshold.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * @param threshold The threshold to set.
     */
    public void setThreshold(final double threshold) {
        this.threshold = threshold;
    }

    /**
     * @return Returns the timeConstant.
     */
    public double getTimeConstant() {
        return timeConstant;
    }

    /**
     * @param timeConstant The timeConstant to set.
     */
    public void setTimeConstant(final double timeConstant) {
        this.timeConstant = timeConstant;
    }
}
