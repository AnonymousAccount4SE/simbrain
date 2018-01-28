/*
 * Part of Simbrain--a java-based neural network kit Copyright (C) 2005,2007 The
 * Authors. See http://www.simbrain.net/credits This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 59 Temple Place
 * - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.simbrain.network.gui.dialogs.connect.connector_panels;

import org.simbrain.network.connections.ConnectNeurons;
import org.simbrain.network.connections.Radial;
import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.Synapse;
import org.simbrain.network.gui.dialogs.connect.AbstractConnectionPanel;

import javax.swing.*;
import java.util.List;

/**
 * <b>SparsePanel</b> creates a dialog for setting preferences of Sparse neuron
 * connections.
 * <p>
 * TODO: Currently not implemented pending refactor of Radial.java
 */
@SuppressWarnings("serial")
public class RadialPanel extends AbstractConnectionPanel {

    /**
     * Excitatory Probability.
     */
    private JTextField tfExciteProbability = new JTextField();

    /**
     * Excitatory Radius.
     */
    private JTextField tfExciteRadius = new JTextField();

    /**
     * Inhibitory Probability.
     */
    private JTextField tfInhibitProbability = new JTextField();

    /**
     * Inhibitory Radius.
     */
    private JTextField tfInhibitRadius = new JTextField();

    /**
     * Allow self connections check box.
     */
    private JCheckBox allowSelfConnect = new JCheckBox();

    /**
     * Set the inhibitory synapse type.
     */
    private JButton setInhibitorySynapseType = new JButton();

    /**
     * Set the excitatory synapse type.
     */
    private JButton setExcitatorySynapseType = new JButton();

    /**
     * This method is the default constructor.
     *
     * @param connection type
     */
    public RadialPanel(final Radial connection) {
    }

    /**
     * {@inheritDoc}
     */
    public boolean commitChanges() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void fillFieldValues() {
    }

    @Override
    public ConnectNeurons getConnection() {
        return null;
    }

    @Override
    public List<Synapse> applyConnection(List<Neuron> source, List<Neuron> target) {
        return null;
    }

}
