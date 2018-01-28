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
package org.simbrain.network.gui.dialogs.network;

import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.util.StandardDialog;

import javax.swing.*;

/**
 * <b>FeedForwardCreationDialog</b> is a dialog box for creating a generic
 * layered network.
 */
public class FeedForwardCreationDialog extends StandardDialog {

    /**
     * Network panel.
     */
    protected NetworkPanel networkPanel;

    /**
     * Main network creation panel.
     */
    protected LayeredNetworkCreationPanel networkCreationPanel;

    /**
     * This method is the default constructor.
     *
     * @param np Network panel
     */
    public FeedForwardCreationDialog(final NetworkPanel np) {
        networkPanel = np;
        init();
    }

    /**
     * This method initializes the components on the panel.
     */
    private void init() {
        setTitle("New Feed-forward Network");
        networkCreationPanel = new LayeredNetworkCreationPanel(3, this);
        setContentPane(networkCreationPanel);
    }

    /**
     * Called when dialog closes.
     */
    protected void closeDialogOk() {
        try {
            networkCreationPanel.commit(networkPanel, "FeedForward");
            super.closeDialogOk();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Inappropriate Field Values (Numbers only in all all field)", "Error", JOptionPane.ERROR_MESSAGE);
            nfe.printStackTrace();
        }
    }
}
