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
package org.simbrain.network.core;

import org.simbrain.network.core.Network.TimeType;
import org.simbrain.util.Utils;

/**
 * A rule for updating a neuron.
 * 
 * @author jyoshimi
 */
public abstract class NeuronUpdateRule {

	/** The maximum number of digits to display in the tool tip. */
	private static final int MAX_DIGITS = 9;

	/** The default upper bound of a neuron using this rule. */
	public static final double DEFAULT_CEILING = 1.0;

	/** The default lower bound of a neuron using this rule. */
	public static final double DEFAULT_FLOOR = -1.0;

	/** The default increment of a neuron using this rule. */
	public static final double DEFAULT_INCREMENT = 0.1;

	/**
	 * Returns the type of time update (discrete or continuous) associated with
	 * this neuron.
	 * 
	 * @return the time type
	 */
	public abstract TimeType getTimeType();

	/**
	 * Apply the update rule.
	 * 
	 * @param neuron
	 *            parent neuron
	 */
	public abstract void update(Neuron neuron);

	/**
	 * Returns a deep copy of the update rule.
	 * 
	 * @return Duplicated update rule
	 */
	public abstract NeuronUpdateRule deepCopy();

	/**
	 * Returns a brief description of this update rule. Used in combo boxes in
	 * the GUI.
	 * 
	 * @return the description.
	 */
	public abstract String getDescription();

	/**
	 * Sets the default parameters of a neuron based on the default values of
	 * the implemented update rule.
	 * 
	 * @param n
	 *            the neuron whose bounds are being changed.
	 */
	public void setDefaultParameters(Neuron n) {
		n.setUpperBound(DEFAULT_CEILING);
		n.setLowerBound(DEFAULT_FLOOR);
		n.setIncrement(DEFAULT_INCREMENT);
	}

	/**
	 * Set activation to 0; override for other "clearing" behavior (e.g. setting
	 * other variables to 0. Called in Gui when "clear" button pressed.
	 * 
	 * @param neuron
	 *            reference to parent neuron
	 */
	public void clear(final Neuron neuron) {
		neuron.setActivation(0);
	}

	/**
	 * Returns string for tool tip or short description. Override to provide
	 * custom information.
	 * 
	 * @param neuron
	 *            reference to parent neuron
	 * @return tool tip text
	 */
	public String getToolTipText(final Neuron neuron) {
		return "(" + neuron.getId() + ") Activation: "
				+ Utils.round(neuron.getActivation(), MAX_DIGITS);
	}

	public double getDefaultCeiling() {
		return DEFAULT_CEILING;
	}

	public double getDefaultFloor() {
		return DEFAULT_FLOOR;
	}

	public double getDefaultIncrement() {
		return DEFAULT_INCREMENT;
	}

}
