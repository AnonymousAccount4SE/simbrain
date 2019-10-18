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
package org.simbrain.network.gui.actions;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.layouts.Layout;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Apply specified layout to selected neurons.
 */
public final class ApplyLayoutAction extends ConditionallyEnabledAction {

    /**
     * The connection to apply.
     */
    private Layout layout;

    /**
     * Construct the action.
     *
     * @param networkPanel networkPanel, must not be null
     * @param layout       the layout to apply
     * @param name         the name of this action
     */
    public ApplyLayoutAction(final NetworkPanel networkPanel, Layout layout, String name) {
        super(networkPanel, name, EnablingCondition.NEURONS);
        this.layout = layout;
        putValue(SHORT_DESCRIPTION, "Apply the " + name + " layout to selected neurons");
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        layout.layoutNeurons(networkPanel.getSelectedModels(Neuron.class));
        networkPanel.repaint();
    }
}