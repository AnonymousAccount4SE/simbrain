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
package org.simbrain.network.gui.actions.edit;

import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.actions.ConditionallyEnabledAction;
import org.simbrain.network.gui.nodes.NeuronNode;
import org.simbrain.network.gui.nodes.SynapseNode;
import org.simbrain.resource.ResourceManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Randomize screen elements action.
 */
public final class RandomizeObjectsAction extends ConditionallyEnabledAction {

    /**
     * Create a new randomize screen elements action with the specified network
     * panel.
     *
     * @param networkPanel network panel, must not be null
     */
    public RandomizeObjectsAction(final NetworkPanel networkPanel) {
        super(networkPanel, "Randomize selection", EnablingCondition.ALLITEMS);

        putValue(SMALL_ICON, ResourceManager.getImageIcon("Rand.png"));
        putValue(SHORT_DESCRIPTION, "Randomize Selected Weights and Nodes (r)");

        networkPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('r'), this);
        networkPanel.getActionMap().put(this, this);
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        // TODO: Consider just updating the nodes while we have them
        //  (though this breaks model-view separation)
        for (NeuronNode node : networkPanel.getSelectedNeurons()) {
            node.getNeuron().randomize();
        }
        for (SynapseNode node : networkPanel.getSelectedSynapses()) {
            node.getSynapse().randomize();
        }
        networkPanel.getNetwork().fireNeuronsUpdated(networkPanel.getSelectedModelNeurons());
        networkPanel.getNetwork().fireSynapsesUpdated(networkPanel.getSelectedModelSynapses());
    }
}