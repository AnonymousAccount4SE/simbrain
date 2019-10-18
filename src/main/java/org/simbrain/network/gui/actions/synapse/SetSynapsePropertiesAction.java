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
package org.simbrain.network.gui.actions.synapse;

import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.NetworkSelectionEvent;
import org.simbrain.network.gui.NetworkSelectionListener;
import org.simbrain.network.gui.nodes.SynapseNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Set synapse properties.
 */
public final class SetSynapsePropertiesAction extends AbstractAction {

    /**
     * Network panel.
     */
    private final NetworkPanel networkPanel;

    /**
     * Create a new set synapse properties action with the specified network
     * panel.
     *
     * @param networkPanel networkPanel, must not be null
     */
    public SetSynapsePropertiesAction(final NetworkPanel networkPanel) {

        super("Synapse Properties...");

        if (networkPanel == null) {
            throw new IllegalArgumentException("networkPanel must not be null");
        }
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        networkPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('e'), this);
        putValue(SHORT_DESCRIPTION, "Set the properties of selected synapses");

        this.networkPanel = networkPanel;
        updateAction();

        // add a selection listener to update state based on selection
        networkPanel.addSelectionListener(new NetworkSelectionListener() {

            /** @see NetworkSelectionListener */
            public void selectionChanged(final NetworkSelectionEvent event) {
                updateAction();
            }
        });
    }

    /**
     * Set action text based on number of selected neurons.
     */
    private void updateAction() {
        int numSynapses = networkPanel.getSelectedNodes(SynapseNode.class).size();

        if (numSynapses > 0) {
            String text = new String(("Edit " + numSynapses + ((numSynapses > 1) ? " Selected Synapses" : " Selected Synapse")));
            putValue(NAME, text);
            setEnabled(true);
        } else {
            putValue(NAME, "Edit Selected Synapse(s)");
            setEnabled(false);
        }
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {

        networkPanel.showSelectedSynapseProperties();

    }
}