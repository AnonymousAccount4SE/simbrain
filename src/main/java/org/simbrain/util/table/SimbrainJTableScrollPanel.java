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
package org.simbrain.util.table;

import javax.swing.*;
import java.awt.*;

/**
 * A scroll panel for a jtable, which sizes itself to fit whatever data it
 * shows. The rule is: if there are fewer rows or columns than the user
 * specifies, "shrink to fit"; otherwise set the panel to a specified maximum
 * size. Maintain fixed size for column widths and row heights, except for the
 * first column.
 *
 * @author jyoshimi
 */
public class SimbrainJTableScrollPanel extends JScrollPane {

    /**
     * The underlying jtable .
     */
    private SimbrainJTable jtable;

    /**
     * Default column width.
     */
    private static final int DEFAULT_COLUMN_WIDTH = 100;

    /**
     * Default column width.
     */
    private static final int DEFAULT_MAX_COLS = 6;

    /**
     * Default column width.
     */
    private static final int DEFAULT_MAX_ROWS = 25;

    /**
     * How wide to make columns.
     */
    private int columnWidth = DEFAULT_COLUMN_WIDTH;

    /**
     * Maximum number of columns to display at one time in the scrollpane.
     */
    private int maxVisibleColumns = DEFAULT_MAX_COLS;

    /**
     * Maximum number of rows to display at one time in the scrollpane.
     */
    private int maxVisibleRows = DEFAULT_MAX_ROWS;

    /**
     * Default constructor.
     */
    public SimbrainJTableScrollPanel() {
        super();
    }

    /**
     * @param jtable
     */
    public SimbrainJTableScrollPanel(SimbrainJTable jtable) {
        super(jtable);
        this.jtable = jtable;
        resize();
    }

    /**
     * Sets the jtable.
     *
     * @param jtable the jtable.
     */
    public void setTable(SimbrainJTable jtable) {
        this.jtable = jtable;
        this.setViewportView(jtable);
        resize();
    }

    /**
     * Resize the panel based on the data. If below a set amount set size to
     * data, else set to fixed max size.
     */
    public void resize() {

        int rowHeight = jtable.getRowHeight();
        jtable.setFirstColumnWidth();
        int width, height;

        // Set width of scrollpane (and resize policy) based on number of
        // columns.
        int cols = jtable.getData().getColumnCount();
        if (cols < maxVisibleColumns) {
            // Table fits inside of visible space
            width = cols * columnWidth;
            jtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else if (cols <= maxVisibleColumns + 2) {
            width = maxVisibleColumns * columnWidth;
            jtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else {
            // Table extends beyond visible case
            width = maxVisibleColumns * columnWidth;
            jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }

        // Set height of scrollpane based on number of rows
        // Add 1 to numrows for header row
        int rows = jtable.getData().getRowCount();
        if (rows < maxVisibleRows) {
            height = (rows + 1) * rowHeight;
        } else {
            height = (maxVisibleRows + 1) * rowHeight;
        }

        setPreferredSize(new Dimension(width, height));
        revalidate();
    }

    /**
     * @return the maxVisibleColumns
     */
    public int getMaxVisibleColumns() {
        return maxVisibleColumns;
    }

    /**
     * @param maxVisibleColumns the maxVisibleColumns to set
     */
    public void setMaxVisibleColumns(int maxVisibleColumns) {
        this.maxVisibleColumns = maxVisibleColumns;
        resize();
    }

    /**
     * @return the maxVisibleRows
     */
    public int getMaxVisibleRows() {
        return maxVisibleRows;
    }

    /**
     * @param maxVisibleRows the maxVisibleRows to set
     */
    public void setMaxVisibleRows(int maxVisibleRows) {
        this.maxVisibleRows = maxVisibleRows;
        resize();
    }

}
