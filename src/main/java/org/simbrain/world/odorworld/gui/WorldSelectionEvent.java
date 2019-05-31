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
package org.simbrain.world.odorworld.gui;

import org.piccolo2d.PNode;
import org.simbrain.world.odorworld.OdorWorldPanel;

import java.util.Collections;
import java.util.EventObject;
import java.util.Set;

/**
 * An event object representing a change in network selection.
 */
public class WorldSelectionEvent extends EventObject {

    /**
     * Old selection.
     */
    private Set<PNode> oldSelection;

    /**
     * Selection.
     */
    private Set<PNode> selection;

    /**
     * Create a new network selection event with the specified source.
     *
     * @param source       source of the event
     * @param oldSelection old selection
     * @param selection    selection
     */
    public WorldSelectionEvent(final OdorWorldPanel source, final Set<PNode> oldSelection, final Set<PNode> selection) {
        super(source);
        this.oldSelection = Collections.unmodifiableSet(oldSelection);
        this.selection = Collections.unmodifiableSet(selection);
    }

    /**
     * Return the source of this event as a OdorWorldPanel.
     *
     * @return the source of this event as a OdorWorldPanel
     */
    public OdorWorldPanel getWorldPanel() {
        return (OdorWorldPanel) getSource();
    }

    /**
     * Return the old selection.
     *
     * @return the old selection
     */
    public Set<PNode> getOldSelection() {
        return oldSelection;
    }

    /**
     * Return the selection.
     *
     * @return the selection
     */
    public Set<PNode> getSelection() {
        return selection;
    }
}