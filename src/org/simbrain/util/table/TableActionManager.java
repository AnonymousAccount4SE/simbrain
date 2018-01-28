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

import org.simbrain.resource.ResourceManager;
import org.simbrain.util.SFileChooser;
import org.simbrain.util.StandardDialog;
import org.simbrain.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains actions for use in SimbrainJTables.
 *
 * @author jyoshimi
 */
public class TableActionManager {

    /**
     * Default directory where tables are stored.
     */
    private static String CSV_DIRECTORY = "." + System.getProperty("file.separator") + "simulations" + System.getProperty("file.separator") + "tables";

    /**
     * Action for opening from comma separate value file.
     *
     * @param table              table to load data in to
     * @param allowRowChanges    whether to allow number of rows to change
     * @param allowColumnChanges whether to allow number of columns to change
     * @return the action
     */
    public static Action getOpenCSVAction(final NumericTable table, final boolean allowRowChanges, final boolean allowColumnChanges) {
        return new AbstractAction() {

            // Initialize
            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Open.png"));
                putValue(NAME, "Import (.csv)");
                putValue(SHORT_DESCRIPTION, "Import table from .csv");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                SFileChooser chooser = new SFileChooser(CSV_DIRECTORY, "comma-separated-values (csv)", "csv");
                File theFile = chooser.showOpenDialog();
                if (theFile != null) {
                    try {
                        table.readData(theFile, allowRowChanges, allowColumnChanges);
                    } catch (TableDataException e) {
                        JOptionPane.showOptionDialog(null, e.getMessage(), "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                    }
                }
            }

        };
    }

    /**
     * Action for opening text table from comma separated value file.
     *
     * @param table              table to load data in to
     * @param allowRowChanges    whether to allow number of rows to change
     * @param allowColumnChanges whether to allow number of columns to change
     * @return the action
     */
    public static Action getOpenCSVAction(final TextTable table, final boolean allowRowChanges, final boolean allowColumnChanges) {
        return new AbstractAction() {

            // Initialize
            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Open.png"));
                putValue(NAME, "Import (.csv)");
                putValue(SHORT_DESCRIPTION, "Import table from .csv");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                SFileChooser chooser = new SFileChooser(CSV_DIRECTORY, "comma-separated-values (csv)", "csv");
                File theFile = chooser.showOpenDialog();
                if (theFile != null) {
                    try {
                        table.readData(theFile, allowRowChanges, allowColumnChanges);
                    } catch (TableDataException e) {
                        JOptionPane.showOptionDialog(null, e.getMessage(), "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                    }
                }
            }

        };
    }

    /**
     * Action for saving to comma separated value file.
     *
     * @param table table to load data in to
     * @return the action
     */
    public static Action getSaveCSVAction(final SimbrainDataTable table) {
        return new AbstractAction() {

            // Initialize
            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Save.png"));
                putValue(NAME, "Export (.csv)");
                putValue(SHORT_DESCRIPTION, "Save table as .csv");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                SFileChooser chooser = new SFileChooser(CSV_DIRECTORY, "comma-separated-values (csv)", "csv");
                File theFile = chooser.showSaveDialog();
                if (theFile != null) {
                    Utils.writeMatrix(table.asStringArray(), theFile);
                }
            }

        };
    }

    /**
     * Action for randomizing selected parts of a table.
     *
     * @param table table to randomize
     * @return the action
     */
    public static Action getRandomizeAction(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Rand.png"));
                putValue(NAME, "Randomize");
                putValue(SHORT_DESCRIPTION, "Randomize");
                KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
                putValue(ACCELERATOR_KEY, keyStroke);
            }

            /**
             * {@ineritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                table.randomize();
            }

        };
    }

    /**
     * Action for normalizing selected parts of a table.
     *
     * @param table table to normalize
     * @return the action
     */
    public static Action getNormalizeAction(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // putValue(SMALL_ICON, ResourceManager.getImageIcon(""));
                putValue(NAME, "Normalize Column(s)");
                putValue(SHORT_DESCRIPTION, "Normalize Selected Columns");
                KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
                putValue(ACCELERATOR_KEY, keyStroke);
            }

            /**
             * {@ineritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                table.normalize();
            }

        };
    }

    /**
     * Action for setting table bounds.
     *
     * @param table table to adjust bounds on
     * @return the action
     */
    public static Action getSetTableBoundsAction(final NumericTable table) {
        return new AbstractAction() {

            // Initialize
            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Prefs.gif"));
                putValue(NAME, "Randomization bounds");
                putValue(SHORT_DESCRIPTION, "Set randomization bounds");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                StandardDialog dialog = new StandardDialog();
                JPanel pane = new JPanel();
                JTextField lower = new JTextField();
                JTextField upper = new JTextField();
                lower.setText(Integer.toString(table.getLowerBound()));
                lower.setColumns(3);
                upper.setText(Integer.toString(table.getUpperBound()));
                upper.setColumns(3);
                pane.add(new JLabel("Lower Bound"));
                pane.add(lower);
                pane.add(new JLabel("Upper Bound"));
                pane.add(upper);

                dialog.setContentPane(pane);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                if (!dialog.hasUserCancelled()) {
                    table.setLowerBound(Integer.parseInt(lower.getText()));
                    table.setUpperBound(Integer.parseInt(upper.getText()));
                }
            }

        };
    }

    /**
     * Action for setting table structure.
     *
     * @param table table to change structure of
     * @return the action
     */
    public static Action getChangeTableStructureAction(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // TODO: Throw exception if jtable.getData() is not mutable
                // putValue(SMALL_ICON,
                // ResourceManager.getImageIcon("Prefs.gif"));
                putValue(NAME, "Reset table");
                putValue(SHORT_DESCRIPTION, "Set number of rows and columns (cells are zeroed out)");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                StandardDialog dialog = new StandardDialog();
                JPanel pane = new JPanel();
                JTextField rows = new JTextField();
                JTextField columns = new JTextField();
                rows.setText(Integer.toString(table.getRowCount()));
                rows.setColumns(3);
                columns.setText(Integer.toString(table.getColumnCount()));
                columns.setColumns(3);
                pane.add(new JLabel("Rows"));
                pane.add(rows);
                pane.add(new JLabel("Columns"));
                pane.add(columns);

                dialog.setContentPane(pane);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                if (!dialog.hasUserCancelled()) {
                    ((MutableTable) table.getData()).reset(Integer.parseInt(rows.getText()), Integer.parseInt(columns.getText()));
                }
            }

        };
    }

    /**
     * Action for changing the number of rows and columns in the table.
     *
     * @param table table to change structure of
     * @return the action
     */
    public static Action changeRowsColumns(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // putValue(SMALL_ICON,
                // ResourceManager.getImageIcon("Prefs.gif"));
                putValue(NAME, "Set rows / columns");
                putValue(SHORT_DESCRIPTION, "Set number of rows and columns (cells are zeroed out)");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                StandardDialog dialog = new StandardDialog();
                JPanel pane = new JPanel();
                JTextField rows = new JTextField();
                JTextField columns = new JTextField();
                rows.setText(Integer.toString(table.getData().getRowCount()));
                rows.setColumns(3);
                columns.setText(Integer.toString(table.getData().getLogicalColumnCount()));
                columns.setColumns(3);
                pane.add(new JLabel("Rows"));
                pane.add(rows);
                pane.add(new JLabel("Columns"));
                pane.add(columns);

                dialog.setContentPane(pane);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                if (!dialog.hasUserCancelled()) {
                    ((MutableTable) table.getData()).modifyRowsColumns(Integer.parseInt(rows.getText()), Integer.parseInt(columns.getText()), 0);
                }
            }

        };
    }

    /**
     * Action for inserting a row in to a jtable.
     *
     * @param table jtable to insert row into.
     * @return the action
     */
    public static Action getInsertRowAction(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // TODO: Throw exception if jtable.getData() is not mutable
                putValue(SMALL_ICON, ResourceManager.getImageIcon("AddTableRow.png"));
                putValue(NAME, "Insert row");
                putValue(SHORT_DESCRIPTION, "Insert row (above)");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                if (table.getSelectedRow() != -1) {
                    if (table.getData() instanceof MutableTable) {
                        ((MutableTable<?>) table.getData()).insertRow(table.getSelectedRow());
                    }
                }
            }

        };
    }

    /**
     * Action for inserting a column in to a jtable. Assumes the table is
     * mutable.
     *
     * @param jtable table to insert column into
     * @return the action
     */
    public static Action getInsertColumnAction(final SimbrainJTable jtable) {
        return new AbstractAction() {

            // Initialize
            {
                // TODO: Throw exception if jtable.getData() is not mutable
                putValue(SMALL_ICON, ResourceManager.getImageIcon("AddTableColumn.png"));
                putValue(NAME, "Insert column");
                putValue(SHORT_DESCRIPTION, "Insert column (to right)");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                if (jtable.getSelectedColumn() != -1) {
                    ((MutableTable) jtable.getData()).insertColumn(jtable.getSelectedColumn());
                }
            }

        };
    }

    /**
     * Action for deleting a row from to a jtable.
     *
     * @param jtable table to delete a row from
     * @return the action
     */
    public static Action getDeleteRowAction(final SimbrainJTable jtable) {
        return new AbstractAction() {

            // Initialize
            {
                // TODO: Throw exception if jtable.getData() is not mutable
                putValue(SMALL_ICON, ResourceManager.getImageIcon("DeleteRowTable.png"));
                putValue(NAME, "Delete row");
                putValue(SHORT_DESCRIPTION, "Delete row");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                // TODO: Also allow multiple column selection using this method?
                List<Integer> selection = new ArrayList<Integer>(0);
                for (int i = 0; i < jtable.getSelectedRows().length; i++) {
                    selection.add(jtable.getSelectedRows()[i]);
                }
                Collections.sort(selection, Collections.reverseOrder());
                if (selection.size() > 0) {
                    for (Integer i : selection) {
                        ((MutableTable) jtable.getData()).removeRow(i);
                    }
                }
                // Rule for selecting row after deleting a row. Needs work.
                // Should work well when button is repeatedly pressed
                if (selection.size() > 0) {
                    int newSelection = selection.get(selection.size() - 1) - 1;
                    if (newSelection >= 0) {
                        jtable.setRowSelectionInterval(newSelection, newSelection);
                    }
                }
            }
        };
    }

    /**
     * Action for deleting a column from a jtable.
     *
     * @param jtable table to delete column from
     * @return the action
     */
    public static Action getDeleteColumnAction(final SimbrainJTable jtable) {
        return new AbstractAction() {

            // Initialize
            {
                // TODO: Throw exception if jtable.getData() is not mutable
                putValue(SMALL_ICON, ResourceManager.getImageIcon("DeleteColumnTable.png"));
                putValue(NAME, "Delete column");
                putValue(SHORT_DESCRIPTION, "Delete column");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                if (jtable.getSelectedColumn() != -1) {
                    ((MutableTable) jtable.getData()).removeColumn(jtable.getSelectedColumn() - 1);
                }
            }

        };
    }

    /**
     * Action for adding rows to a table.
     *
     * @param table table to add rows to
     * @return the action
     */
    public static Action getAddRowsAction(final MutableTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // putValue(SMALL_ICON,
                // ResourceManager.getImageIcon("Eraser.png"));
                putValue(NAME, "Add rows");
                putValue(SHORT_DESCRIPTION, "Add rows");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                String numRows = JOptionPane.showInputDialog(null, "Number of rows to add:", "5");
                table.addRows(Integer.parseInt(numRows));
            }

        };
    }

    /**
     * Action for adding columns to a jtable.
     *
     * @param table table to insert column into
     * @return the action
     */
    public static Action getAddColumnsAction(final MutableTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // putValue(SMALL_ICON,
                // ResourceManager.getImageIcon("Eraser.png"));
                putValue(NAME, "Add columns");
                putValue(SHORT_DESCRIPTION, "Add columns");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                String numCols = JOptionPane.showInputDialog(null, "Number of columns to add:", "5");
                table.addColumns(Integer.parseInt(numCols));
            }

        };
    }

    /**
     * Action for zeroing out cells of a table.
     *
     * @param table table to zero out
     * @return the action
     */
    public static Action getZeroFillAction(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // putValue(SMALL_ICON,
                // ResourceManager.getImageIcon("Eraser.png"));
                putValue(NAME, "Zero fill cells");
                putValue(SHORT_DESCRIPTION, "Zero fill selected cells");
                KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
                putValue(ACCELERATOR_KEY, keyStroke);
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                table.fill(new Double(0));
            }

        };
    }

    /**
     * Action for filling a table with specific values.
     *
     * @param table table to fill
     * @return the action
     */
    public static Action getFillAction(final SimbrainJTable table) {
        return new AbstractAction() {

            // Initialize
            {
                // putValue(SMALL_ICON,
                // ResourceManager.getImageIcon("Eraser.png"));
                putValue(NAME, "Fill table cells...");
                putValue(SHORT_DESCRIPTION, "Fill table selected cells with specified value");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                String val = JOptionPane.showInputDialog(null, "Value:", "0");
                table.fill(Double.parseDouble(val));
            }

        };
    }

    /**
     * Action for shuffling the rows of a table.
     *
     * @param table table whose rows should be shuffled
     * @return the action
     */
    public static Action getShuffleAction(final SimbrainDataTable<?> table) {
        return new AbstractAction() {

            // Initialize
            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Shuffle.png"));
                putValue(NAME, "Shuffle rows");
                putValue(SHORT_DESCRIPTION, "Randomize the positions of the rows");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                table.shuffle();
            }

        };
    }

}
