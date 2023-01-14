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
package org.simbrain.network.gui;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import javax.swing.*;
import java.awt.geom.Point2D;

/**
 * Context menu event handler.
 */
final class ContextMenuEventHandler extends PBasicInputEventHandler {

    /**
     * Network Panel.
     */
    private final NetworkPanel networkPanel;

    /**
     * Construct with a network panel.
     *
     * @param networkPanel reference to panel
     */
    public ContextMenuEventHandler(NetworkPanel networkPanel) {
        this.networkPanel = networkPanel;
    }

    /**
     * Show the context menu.
     *
     * @param event event
     */
    private void showContextMenu(final PInputEvent event) {

        event.setHandled(true); // seems to confuse zoom event handler??
        JPopupMenu contextMenu =  NetworkPanelMenusKt.creatContextMenu(networkPanel);
        Point2D canvasPosition = event.getCanvasPosition();
        contextMenu.show(networkPanel, (int) canvasPosition.getX(), (int) canvasPosition.getY());
        networkPanel.getCanvas().getCamera().localToView(canvasPosition);
        // Set this position so that new objects are added here
        networkPanel.getNetwork().getPlacementManager().setLastClickedLocation(canvasPosition);
    }

    @Override
    public void mousePressed(final PInputEvent event) {
        if (event.isPopupTrigger()) {
            NetworkPanelMenusKt.creatContextMenu(networkPanel);
            showContextMenu(event);
        }
    }

    @Override
    public void mouseReleased(final PInputEvent event) {
        if (event.isPopupTrigger()) {
            NetworkPanelMenusKt.creatContextMenu(networkPanel);
            showContextMenu(event);
        }
    }
}