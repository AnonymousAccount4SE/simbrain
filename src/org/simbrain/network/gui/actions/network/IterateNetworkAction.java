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
package org.simbrain.network.gui.actions.network;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.resource.ResourceManager;

/**
 * Iterate network action.
 */
public final class IterateNetworkAction extends AbstractAction {
    Logger LOGGER = Logger.getLogger(IterateNetworkAction.class);

    /** Network panel. */
    private final NetworkPanel networkPanel;

    /**
     * Create a new iterate network action with the specified network panel.
     *
     * @param networkPanel network panel, must not be null
     */
    public IterateNetworkAction(final NetworkPanel networkPanel) {
        super();

        if (networkPanel == null) {
            throw new IllegalArgumentException("networkPanel must not be null");
        }

        this.networkPanel = networkPanel;
        putValue(SMALL_ICON, ResourceManager.getImageIcon("Step.png"));
        putValue(SHORT_DESCRIPTION,
                "Step network update algorithm (\"spacebar\")");

        networkPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(' '), this);
        networkPanel.getActionMap().put(this, this);
    }

    /** @see AbstractAction 
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        LOGGER.debug("actionPerformed called");
        networkPanel.getNetwork().setOneOffRun(true);
        networkPanel.getNetwork().update();
        networkPanel.getNetwork().setOneOffRun(false);
    }
}