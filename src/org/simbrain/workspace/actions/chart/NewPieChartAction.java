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
package org.simbrain.workspace.actions.chart;

import org.simbrain.plot.piechart.PieChartComponent;
import org.simbrain.resource.ResourceManager;
import org.simbrain.workspace.Workspace;
import org.simbrain.workspace.actions.WorkspaceAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Add pie chart component to workspace.
 */
public final class NewPieChartAction extends WorkspaceAction {

    /**
     * Create a new pie chart component.
     *
     * @param workspace workspace, must not be null
     */
    public NewPieChartAction(final Workspace workspace) {
        super("Pie Chart", workspace);
        putValue(SMALL_ICON, ResourceManager.getImageIcon("PieChart.png"));
        putValue(SHORT_DESCRIPTION, "New Pie Chart");
    }

    /**
     * @param event
     * @see AbstractAction
     */
    public void actionPerformed(final ActionEvent event) {
        PieChartComponent plot = new PieChartComponent("");
        workspace.addWorkspaceComponent(plot);
    }
}