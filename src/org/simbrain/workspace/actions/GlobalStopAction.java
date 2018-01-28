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
package org.simbrain.workspace.actions;

import org.simbrain.resource.ResourceManager;
import org.simbrain.workspace.Workspace;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Run network action.
 */
public final class GlobalStopAction extends WorkspaceAction {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new run network action with the specified network panel.
     *
     * @param workspace network panel, must not be null
     */
    public GlobalStopAction(Workspace workspace) {
        super("Global stop", workspace);
        putValue(SMALL_ICON, ResourceManager.getImageIcon("Stop.png"));
        putValue(SHORT_DESCRIPTION, "Global iterate network update algorithm");
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        workspace.stop();
    }
}