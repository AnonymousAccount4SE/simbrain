/*
 * Part of Simbrain--a java-based neural network kit Copyright (C) 2005,2007 The
 * Authors. See http://www.simbrain.net/credits This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 59 Temple Place
 * - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.simbrain.network.gui.dialogs.layout;

import org.simbrain.network.layouts.LineLayout;
import org.simbrain.network.layouts.LineLayout.LineOrientation;

import javax.swing.*;

/**
 * <b>LayoutPanel</b> allows the user to define the layout of a network.
 */
public class LineLayoutPanel extends AbstractLayoutPanel {

    /**
     * Spacing field.
     */
    private JTextField tfSpacing = new JTextField();

    /**
     * Layout style selected.
     */
    private JComboBox<LineOrientation> cbLayouts = new JComboBox<LineOrientation>(new LineOrientation[]{LineOrientation.HORIZONTAL, LineOrientation.VERTICAL});

    /**
     * Default constructor.
     *
     * @param layout
     */
    public LineLayoutPanel(LineLayout layout) {
        this.layout = layout;
        fillFieldValues();
        this.addItem("Layout Style", cbLayouts);
        this.addItem("Spacing:", tfSpacing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commitChanges() {
        ((LineLayout) layout).setOrientation((LineOrientation) cbLayouts.getSelectedItem());
        ((LineLayout) layout).setSpacing(Double.parseDouble(tfSpacing.getText()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillFieldValues() {
        cbLayouts.setSelectedItem(((LineLayout) layout).getOrientation());
        tfSpacing.setText(Double.toString(((LineLayout) layout).getSpacing()));
    }

}
