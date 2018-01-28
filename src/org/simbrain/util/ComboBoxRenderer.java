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
package org.simbrain.util;

import javax.swing.*;
import java.awt.*;

/**
 * <b>ComboBoxRenderer</b> formats and inserts images into combo boxes.
 */
public class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    /**
     * Combo box renderer.
     */
    public ComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    /**
     * Puts images next to images in a combo box.
     *
     * @param list         List
     * @param value        Objects to insert
     * @param index        Where to insert image and text
     * @param isSelected   Is cell selected
     * @param cellHasFocus Cell has focus
     * @return Returns the componet to be put into combo box
     */
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        ImageIcon icon = (ImageIcon) value;
        setText(icon.getDescription());
        setIcon(icon);

        return this;
    }
}
