/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2006 Jeff Yoshimi <www.jeffyoshimi.net>
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
package org.simbrain.world.odorworld.actions;

import org.simbrain.world.odorworld.entities.OdorWorldEntity;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action for adding tile sensors.
 */
public final class AddTileSensorsAction extends AbstractAction {

    /**
     * Entity to add tile sensors to.
     */
    private final OdorWorldEntity entity;

    /**
     * Construct the action.
     *
     * @param entity GUI component, must not be null.
     */
    public AddTileSensorsAction(OdorWorldEntity entity) {
        super("Add Tile Sensors");
        this.entity = entity;
        // putValue(SMALL_ICON, ResourceManager.getImageIcon("Prefs.png"));
        putValue(SHORT_DESCRIPTION, "Add tile sensors...");
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent event) {
        // TODO: Quick and dirty here. Make a proper dialog!
        String resultString = JOptionPane.showInputDialog(null, "Rows, Columns, Offset.", "10,10,0");
        if (resultString != null) {
            String[] parsedString = resultString.split(",");
            int rows = Integer.parseInt(parsedString[0]);
            int cols = Integer.parseInt(parsedString[1]);
            int offset = Integer.parseInt(parsedString[2]);
            entity.addTileSensors(rows, cols, offset);
        } else {
            return; // User pressed cancel
        }
    }

}
