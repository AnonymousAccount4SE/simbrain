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

import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.nodes.ViewGroupNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Ungroup group.
 */
public final class UngroupAction extends AbstractAction {

    /**
     * Network panel.
     */
    private final NetworkPanel networkPanel;

    /**
     * Picked node.
     */
    private final ViewGroupNode node;

    /**
     * Ungroup selected objects.
     *
     * @param networkPanel network panel, must not be null.
     * @param node         the node being modify
     */
    public UngroupAction(final NetworkPanel networkPanel, final ViewGroupNode node) {
        super("Ungroup gui objects");
        this.node = node;
        if (networkPanel == null) {
            throw new IllegalArgumentException("networkPanel must not be null");
        }

        this.networkPanel = networkPanel;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_U, toolkit.getMenuShortcutKeyMask());

        putValue(ACCELERATOR_KEY, keyStroke);
        putValue(SHORT_DESCRIPTION, "Ungroup gui objects");

        // updateAction();
        // // add a selection listener to update state based on selection
        // networkPanel.addSelectionListener(new NetworkSelectionListener() {
        //
        // /** @see NetworkSelectionListener */
        // public void selectionChanged(final NetworkSelectionEvent event) {
        // updateAction();
        // }
        // });
    }

    /**
     * Set action text based on number of selected neurons.
     */
    private void updateAction() {
        int numSelected = networkPanel.getSelectedModels().size();
        if (numSelected > 0) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        // networkPanel.unGroup(node, true);
    }
}