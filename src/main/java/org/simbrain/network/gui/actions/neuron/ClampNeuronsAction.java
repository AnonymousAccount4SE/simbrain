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
package org.simbrain.network.gui.actions.neuron;

import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.util.ResourceManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Clamps neurons action. NOTE: No longer used as of 3.0. Leaving it here for
 * reference since the GUI worked well and could be used elsewhere.
 */
public final class ClampNeuronsAction extends AbstractAction {

    /**
     * Network panel.
     */
    private final NetworkPanel networkPanel;

    /**
     * Create a new clamp neurons action with the specified network panel.
     *
     * @param networkPanel networkPanel, must not be null
     */
    public ClampNeuronsAction(final NetworkPanel networkPanel) {

        super("Clamp Neurons");

        if (networkPanel == null) {
            throw new IllegalArgumentException("networkPanel must not be null");
        }

        this.networkPanel = networkPanel;

        putValue(SMALL_ICON, ResourceManager.getImageIcon("Clamp_N.png"));
        putValue(SHORT_DESCRIPTION, "Clamp Neurons");
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {

        // Perform action
        // JToggleButton cb = (JToggleButton) event.getSource();
        if (event.getSource() instanceof JToggleButton) {
            JToggleButton cb = (JToggleButton) event.getSource();
            // networkPanel.getNetwork().setClampNeurons(cb.isSelected());
        } else {
            JCheckBoxMenuItem cb = (JCheckBoxMenuItem) event.getSource();
            // networkPanel.getNetwork().setClampNeurons(cb.isSelected());
        }

        // Determine status
        // networkPanel.getNetwork().setClampNeurons(cb.isSelected());

    }
}