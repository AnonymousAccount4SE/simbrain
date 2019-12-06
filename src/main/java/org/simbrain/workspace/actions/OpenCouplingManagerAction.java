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

import org.simbrain.util.ResourceManager;
import org.simbrain.util.genericframe.GenericJInternalFrame;
import org.simbrain.workspace.gui.SimbrainDesktop;
import org.simbrain.workspace.gui.couplingmanager.DesktopCouplingManager;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;

/**
 * Open data world in current workspace.
 */
public final class OpenCouplingManagerAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * Reference to Simbrain desktop.
     */
    private SimbrainDesktop desktop;

    /**
     * Create an open data world with the specified workspace.
     *
     * @param desktop
     */
    public OpenCouplingManagerAction(final SimbrainDesktop desktop) {
        super("Open Coupling Manager...");
        this.desktop = desktop;
        putValue(SMALL_ICON, ResourceManager.getImageIcon("menu_icons/Coupling.png"));
        putValue(SHORT_DESCRIPTION, "Open coupling manager");
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        GenericJInternalFrame frame = new GenericJInternalFrame();
        desktop.addInternalFrame(frame);
        if (!DesktopCouplingManager.isVisible) {
            DesktopCouplingManager cm = new DesktopCouplingManager(desktop, frame);
            frame.setTitle("Coupling Manager");
            frame.setContentPane(cm);
            // frame.setSize(850, 420);
            frame.setResizable(true);
            frame.setClosable(true);
            frame.setMaximizable(true);
            frame.setIconifiable(true);
            frame.setVisible(true);
            frame.pack();
            frame.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    DesktopCouplingManager.isVisible = false;
                }
            });
        }
    }
}