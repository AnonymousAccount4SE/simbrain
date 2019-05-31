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
package org.simbrain.workspace.component_actions;

import org.simbrain.util.ResourceManager;
import org.simbrain.workspace.gui.GuiComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Save as action.
 */
public final class SaveAsAction extends AbstractAction {

    /**
     * Network panel.
     */
    private final GuiComponent guiComponent;

    /**
     * Create a new save component action with the specified.
     *
     * @param guiComponent networkPanel, must not be null
     */
    public SaveAsAction(final GuiComponent guiComponent) {

        super("Save As...");

        if (guiComponent == null) {
            throw new IllegalArgumentException("component must not be null");
        }

        putValue(SMALL_ICON, ResourceManager.getImageIcon("SaveAs.png"));
        putValue(SHORT_DESCRIPTION, "Save this component with a new name");

        this.guiComponent = guiComponent;
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        guiComponent.showSaveFileDialog();
    }
}