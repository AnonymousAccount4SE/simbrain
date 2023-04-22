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

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * <b>SimbrainJTable</b> is a version of a JXTable (itself an improved JTable
 * from SwingLabs) which provides GUI access to a SimbrainDataTable. Provides
 * various features that are useful in simbrain, e.g. ability to set row and
 * column headings.
 * <p>
 * The first column of all tables is a special header column that displays row
 * number or other information. Calls to the underlying SimbrainDataTable model
 * thus must either be offset by 1 as necessary, or can call special methods
 * that refer to "logical" row / column indices that refer to location in the
 * data itself (i.e. column 0 is the first column of the data, not the special
 * row-header column).
 *
 * @author jyoshimi
 * @see org.simbrain.util.table.SimbrainDataTable
 */
@Deprecated
public class SimbrainJTable extends JXTable {

    /**
     * The data to be displayed in the jtable.
     */
    private SimbrainDataTable<?> data;

    /**
     * Row headings. Only used if set, otherwise default row headings (1...n)
     * used.
     */
    private List<String> rowHeadings;

    /**
     * Point selected.
     */
    private Point selectedPoint;

    /**
     * Grid Color.
     */
    private Color gridColor = Color.LIGHT_GRAY;

    /**
     * Whether to display the default popup menu.
     */
    private boolean displayPopUpMenu = true;

    /**
     * Whether the table's data has changed or not, since the last save. Used
     * externally.
     */
    private boolean hasChangedSinceLastSave = false;

    /**
     * Flags for popup menus.
     */
    private boolean showInsertRowPopupMenu = true;
    private boolean showInsertColumnPopupMenu = true;
    private boolean showDeleteRowPopupMenu = true;
    private boolean showDeleteColumnPopupMenu = true;
    private boolean showEditInPopupMenu = true;
    private boolean showRandomizeInPopupMenu = true;
    private boolean showNormalizeInPopupMenu = true;
    private boolean showFillInPopupMenu = true;
    private boolean showCSVInPopupMenu = true;

    /**
     * Creates a new simbrain gui jtable.
     *
     * @param dataModel the initial data
     * @return the jtable the constructed table
     */
    public static SimbrainJTable createTable(SimbrainDataTable dataModel) {
        SimbrainJTable table = new SimbrainJTable(dataModel);
        table.initJTable();
        table.setHasChangedSinceLastSave(false);
        return table;
    }

    /**
     * Construct the jtable.
     *
     * @param dataModel the data
     */
    protected SimbrainJTable(SimbrainDataTable dataModel) {
        data = dataModel;
        this.setModel(data);
    }

    /**
     * Initialize the table.
     */
    protected void initJTable() {
        setColumnSelectionAllowed(true);
        setRolloverEnabled(true);
        setRowSelectionAllowed(true);
        setGridColor(gridColor);
        updateRowSelection();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    randomize();
                } else if (e.getKeyCode() == KeyEvent.VK_F) {
                    fill();
                } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                    fill(0);
                }
            }
        });

        // First column displays row numbers
        this.setDefaultRenderer(Double.class, new CustomCellRenderer());

        // Sorting is not helpful in datatable contexts (possibly add an option
        // to put it back in)
        this.setSortable(false);

        setFirstColumnWidth();

        // Disable specific popupmenus for text tables
        if (data instanceof TextTable) {
            showRandomizeInPopupMenu = false;
            showNormalizeInPopupMenu = false;
            showFillInPopupMenu = false;
            showCSVInPopupMenu = false;
        }

        // Header listener. Manage column selection.
        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown() || (e.getButton() == 1)) {
                    final int column = convertColumnIndexToModel(columnAtPoint(e.getPoint()));
                    if (e.isShiftDown()) {
                        for (int i = 1; i < getRowCount(); i++) {
                            changeSelection(i, column, true, true);
                        }
                    } // todo? implement: else if (e.isControlDown()) {}
                    else {
                        changeSelection(0, column, false, false);
                        for (int i = 1; i < getRowCount(); i++) {
                            changeSelection(i, column, true, true);
                        }
                    }
                }
            }
        });
        // Main mouse listener. Handle row selection and popup menu
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                selectedPoint = e.getPoint();

                if (data instanceof IterableRowsTable) {
                    ((IterableRowsTable) data).setCurrentRow(getSelectedRow());
                }
                if (e.isPopupTrigger() && displayPopUpMenu) {
                    JPopupMenu menu = buildPopupMenu();
                    menu.show(SimbrainJTable.this, (int) selectedPoint.getX(), (int) selectedPoint.getY());
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                final int row = rowAtPoint(e.getPoint());
                final int column = columnAtPoint(e.getPoint());
                if (e.isControlDown() || (e.getButton() == 1)) {
                    if (column == 0) {
                        for (int j = 1; j < getColumnCount(); j++) {
                            changeSelection(row, j, true, true);
                        }
                    }
                }
                if (e.isPopupTrigger() && displayPopUpMenu) {
                    JPopupMenu menu = buildPopupMenu();
                    menu.show(SimbrainJTable.this, (int) selectedPoint.getX(), (int) selectedPoint.getY());
                }
            }
        });

        data.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Upon opening a table an event is fired with first row = -1
                // The table data have not changed in this case, and so the
                // "hasChanged" flag should not be set to true.
                if (e.getFirstRow() >= 0) {
                    hasChangedSinceLastSave = true;
                }
            }
        });
        hasChangedSinceLastSave = false;

        // Force edits to take effect right away.
        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                CellEditor ce = SimbrainJTable.this.getCellEditor();
                if (ce != null) {
                    // Save the value being edited before focus was lost...
                    SimbrainJTable.this.getCellEditor().stopCellEditing();
                    // Fire an event so that listeners can commit the changes
                    // if need be
                    SimbrainJTable.this.firePropertyChange("hasChanged", hasChangedSinceLastSave, true);
                    hasChangedSinceLastSave = true;
                }
            }

        });
    }

    /**
     * Special width for first column.
     */
    public void setFirstColumnWidth() {
        // If no row headings set assume just integers
        if (rowHeadings == null) {
            // TODO: 20 gets overridden by something
            getColumnModel().getColumn(0).setPreferredWidth(20);
        }
    }

    /**
     * Disable popup menus that allow table structure to changed.
     */
    public void disableTableModificationMenus() {
        showInsertRowPopupMenu = false;
        showInsertColumnPopupMenu = false;
        showDeleteRowPopupMenu = false;
        showDeleteColumnPopupMenu = false;
        showEditInPopupMenu = false;
    }

    /**
     * Returns the currently selected column.
     *
     * @return the currently selected column
     */
    public int getSelectedColumn() {
        if (selectedPoint == null) {
            return this.getColumnCount() - 1;
        } else {
            return columnAtPoint(selectedPoint);
        }
    }

    /**
     * Returns the currently selected row.
     *
     * @return the currently selected row
     */
    public int getSelectedRow() {
        if (selectedPoint != null) {
            return rowAtPoint(selectedPoint);
        } else {
            return 0;
        }
    }

    /**
     * @return the data
     */
    public SimbrainDataTable<?> getData() {
        return data;
    }

    /**
     * Build the context menu for the table.
     *
     * @return The context menu.
     */
    protected JPopupMenu buildPopupMenu() {

        JPopupMenu ret = new JPopupMenu();
        if (getData() instanceof MutableTable) {

            if (showInsertRowPopupMenu) {
                ret.add(TableActionManager.getInsertRowAction(this));
            }
            if (showInsertColumnPopupMenu) {
                if (getSelectedColumn() >= 0) {
                    ret.add(TableActionManager.getInsertColumnAction(this));
                }
            }
            if (showDeleteRowPopupMenu) {
                ret.add(TableActionManager.getDeleteRowAction(this));
            }
            if (showDeleteColumnPopupMenu) {
                if (getSelectedColumn() != 0) {
                    ret.add(TableActionManager.getDeleteColumnAction(this));
                }
            }
        }
        if (showEditInPopupMenu) {
            JMenuItem editItem = getMenuEdit();
            if (editItem != null) {
                ret.add(editItem);
            }
        }
        if (showRandomizeInPopupMenu) {
            JMenuItem randomizeItem = getMenuRandomize();
            if (randomizeItem != null) {
                ret.add(randomizeItem);
            }
        }
        if (showNormalizeInPopupMenu) {
            JMenuItem normalizeItem = getMenuNormalize();
            if (normalizeItem != null) {
                ret.add(normalizeItem);
            }
        }
        if (showFillInPopupMenu) {
            JMenuItem fillItem = getMenuFill();
            if (fillItem != null) {
                ret.add(fillItem);
            }
        }
        return ret;
    }

    /**
     * Return a toolbar with buttons for opening from and saving to .csv files.
     *
     * @param allowRowChanges    whether to allow number of rows to change
     * @param allowColumnChanges whether to allow number of columns to change
     * @return the csv toolbar
     */
    public JToolBar getToolbarCSV(final boolean allowRowChanges, final boolean allowColumnChanges) {
        if (getData() instanceof NumericTable) {
            JToolBar toolbar = new JToolBar();
            toolbar.add(TableActionManager.getOpenCSVAction((NumericTable) getData(), allowRowChanges, allowColumnChanges));
            toolbar.add(TableActionManager.getSaveCSVAction((NumericTable) getData()));
            return toolbar;
        } else if (getData() instanceof TextTable) {
            JToolBar toolbar = new JToolBar();
            toolbar.add(TableActionManager.getOpenCSVAction((TextTable) getData(), allowRowChanges, allowColumnChanges));
            toolbar.add(TableActionManager.getSaveCSVAction((TextTable) getData()));
            return toolbar;
        }
        return null;
    }

    /**
     * Return a toolbar with buttons for editing the table cells.
     *
     * @return the edit toolbar
     */
    public JToolBar getToolbarEditTable() {
        if (getData() instanceof MutableTable) {
            JToolBar toolbar = new JToolBar();
            toolbar.add(TableActionManager.getInsertRowAction(this));
            if (getSelectedColumn() >= 0) {
                toolbar.add(TableActionManager.getInsertColumnAction(this));
            }
            toolbar.add(TableActionManager.getDeleteRowAction(this));
            if (getSelectedColumn() != 0) {
                toolbar.add(TableActionManager.getDeleteColumnAction(this));
            }
            return toolbar;
        }
        return null;
    }

    /**
     * Return a toolbar with buttons for editing the rows of a table.
     *
     * @return the edit toolbar
     */
    public JToolBar getToolbarEditRows() {
        if (getData() instanceof MutableTable) {
            JToolBar toolbar = new JToolBar();
            toolbar.add(TableActionManager.getInsertRowAction(this));
            toolbar.add(TableActionManager.getDeleteRowAction(this));
            return toolbar;
        }
        return null;
    }

    public JToolBar getToolbarRandomize() {
        if (getData() instanceof NumericTable) {
            JToolBar toolbar = new JToolBar();
            toolbar.add(TableActionManager.getRandomizeAction(this));
            toolbar.add(TableActionManager.getSetTableBoundsAction((NumericTable) getData()));
            return toolbar;
        }
        return null;
    }

    public JToolBar getToolbarNormalize() {
        if (getData() instanceof NumericTable) {
            JToolBar toolbar = new JToolBar();
            toolbar.add(TableActionManager.getNormalizeAction(this));
            return toolbar;
        }
        return null;
    }

    /**
     * Return a menu with items for opening from and saving to .csv files.
     *
     * @param allowRowChanges    whether to allow number of rows to change
     * @param allowColumnChanges whether to allow number of columns to change
     * @return the csv menu
     */
    public JMenu getMenuCSV(final boolean allowRowChanges, final boolean allowColumnChanges) {
        if (getData() instanceof NumericTable) {
            JMenu menu = new JMenu("Import / Export .csv");
            menu.add(new JMenuItem(TableActionManager.getOpenCSVAction((NumericTable) getData(), allowRowChanges, allowColumnChanges)));
            menu.add(new JMenuItem(TableActionManager.getSaveCSVAction((NumericTable) getData())));
            return menu;
        } else if (getData() instanceof TextTable) {
            JMenu menu = new JMenu("Import / Export .csv");
            menu.add(new JMenuItem(TableActionManager.getOpenCSVAction((TextTable) getData(), allowRowChanges, allowColumnChanges)));
            menu.add(new JMenuItem(TableActionManager.getSaveCSVAction((TextTable) getData())));
            return menu;
        }
        return null;
    }

    /**
     * Return a menu with items for randomizing table values.
     *
     * @return the randomize menu
     */
    public JMenu getMenuRandomize() {
        if (getData() instanceof NumericTable) {
            JMenu menu = new JMenu("Randomize");
            menu.add(TableActionManager.getRandomizeAction(this));
            menu.add(TableActionManager.getSetTableBoundsAction((NumericTable) getData()));
            return menu;
        }
        return null;
    }

    /**
     * Return a menu with items for normalizing the data in a table.
     *
     * @return the normalize menu
     */
    public JMenu getMenuNormalize() {
        if (getData() instanceof NumericTable) {
            JMenu menu = new JMenu("Normalize");
            menu.add(TableActionManager.getNormalizeAction(this));
            return menu;
        }
        return null;
    }

    /**
     * Return a menu with items for filling table values.
     *
     * @return the fill menu
     */
    public JMenu getMenuFill() {
        if (getData() instanceof NumericTable) {
            JMenu menu = new JMenu("Fill values");
            menu.add(new JMenuItem(TableActionManager.getFillAction(this)));
            menu.add(new JMenuItem(TableActionManager.getZeroFillAction(this)));
            return menu;
        }
        return null;
    }

    /**
     * Return a menu with items for changing the table structure.
     *
     * @return the edit menu
     */
    public JMenu getMenuEdit() {
        JMenu menu = new JMenu("Edit");
        if (getData() instanceof MutableTable) {
            menu.add(new JMenuItem(TableActionManager.changeRowsColumns(this)));
            return menu;
        }
        return null;
    }

    /**
     * Select current row.
     */
    public void updateRowSelection() {
        if (getData() instanceof IterableRowsTable) {
            // TODO: If I don't call this, the line below does not work. Not
            // sure why.
            selectAll();
            int currentRow = ((IterableRowsTable) data).getCurrentRow();
            setRowSelectionInterval(currentRow, currentRow);
        }
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(final Point selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    /**
     * Renderer for table. If custom row headings are used, treats first column
     * as a set of headings.
     */
    private class CustomCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {

            if (column == 0) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                if (column == 0) {
                    if (rowHeadings != null) {
                        label.setText(rowHeadings.get(row));
                    } else {
                        // First column displays the row number.
                        label.setText("" + (row + 1));
                    }
                }
                return label;
            } else {
                if (value == null) {
                    JLabel label = new JLabel();
                    label.setOpaque(true);
                    label.setBackground(Color.GRAY.brighter());
                    return label;
                }

                return super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            }
        }
    }

    public List<String> getRowHeadings() {
        return rowHeadings;
    }

    public void setRowHeadings(List<String> rowHeadings) {
        this.rowHeadings = rowHeadings;
    }

    public void setColumnHeadings(List<String> columnHeadings) {
        data.setColumnHeadings(columnHeadings);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        Object o = getValueAt(row, column);
        if (o == null) {
            return false;
        }
        return true;
    }

    /**
     * Forward setting to table model.
     *
     * @param displayColumnHeadings the displayColumnHeadings to set
     */
    public void setDisplayColumnHeadings(boolean displayColumnHeadings) {
        data.setDisplayColumnHeadings(displayColumnHeadings);
    }

    public boolean isDisplayPopUpMenu() {
        return displayPopUpMenu;
    }

    public void setDisplayPopUpMenu(boolean displayPopUpMenu) {
        this.displayPopUpMenu = displayPopUpMenu;
    }

    public boolean hasChanged() {
        return hasChangedSinceLastSave;
    }

    public void setHasChangedSinceLastSave(boolean hasChanged) {
        this.hasChangedSinceLastSave = hasChanged;
    }

    public void setShowInsertRowPopupMenu(boolean showInsertRowPopupMenu) {
        this.showInsertRowPopupMenu = showInsertRowPopupMenu;
    }

    public void setShowInsertColumnPopupMenu(boolean showInsertColumnPopupMenu) {
        this.showInsertColumnPopupMenu = showInsertColumnPopupMenu;
    }

    public void setShowDeleteRowPopupMenu(boolean showDeleteRowPopupMenu) {
        this.showDeleteRowPopupMenu = showDeleteRowPopupMenu;
    }

    public void setShowDeleteColumnPopupMenu(boolean showDeleteColumnPopupMenu) {
        this.showDeleteColumnPopupMenu = showDeleteColumnPopupMenu;
    }

    public void setShowEditInPopupMenu(boolean showEditInPopupMenu) {
        this.showEditInPopupMenu = showEditInPopupMenu;
    }

    public void setShowRandomizeInPopupMenu(boolean showRandomizeInPopupMenu) {
        this.showRandomizeInPopupMenu = showRandomizeInPopupMenu;
    }

    public void setShowNormalizeInPopupMenu(boolean showNormalizeInPopupMenu) {
        this.showNormalizeInPopupMenu = showNormalizeInPopupMenu;
    }

    public void setShowFillInPopupMenu(boolean showFillInPopupMenu) {
        this.showFillInPopupMenu = showFillInPopupMenu;
    }

    public void setShowCSVInPopupMenu(boolean showCSVInPopupMenu) {
        this.showCSVInPopupMenu = showCSVInPopupMenu;
    }

    /**
     * Normalize selected columns of selected cells.
     */
    public void normalize() {
        if (data instanceof NumericTable) {
            if (getSelectedColumns().length < 0) {
                return;
            }
            for (int i = 0; i < this.getSelectedColumns().length; i++) {
                ((NumericTable) data).normalizeColumn(getSelectedColumns()[i] - 1);
            }
        }
    }

    /**
     * Randomize selected cells.
     */
    public void randomize() {

        if (data instanceof NumericTable) {
            ((NumericTable) data).randomize(getSelectedLogicalCellIndices());
        }
    }

    private List<CellIndex> getAllIndices() {
        List<CellIndex> indices = new ArrayList<CellIndex>();
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 1; j < this.getColumnCount(); j++) {
                indices.add(new CellIndex(i, j - 1));
            }
        }
        return indices;
    }

    /**
     * Open a dialog and fill with the provided value.
     */
    public void fill() {
        String val = JOptionPane.showInputDialog(this, "Value:", "0");
        fill(Double.parseDouble(val));
    }

    /**
     * Fills the table with the given value.
     *
     * @param value value to fill the table with.
     */
    public void fill(final double value) {
        if (data instanceof NumericTable) {
            ((NumericTable) data).fill(getSelectedLogicalCellIndices(), value);
        }
    }

    /**
     * Holds reference to row / column index.
     */
    class CellIndex {

        /**
         * Row index.
         */
        public int row;

        /**
         * Column Index.
         */
        public int col;

        /**
         * Construct the cell index.
         *
         * @param row row index
         * @param col collumn index
         */
        public CellIndex(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Returns a list of selected cell-indices. These are "logical" indices of
     * the data, with all column indices reduced by 1 (since the first column of
     * a SimbrainDataTable is a header column).
     *
     * @return the list of cells
     */
    private List<CellIndex> getSelectedLogicalCellIndices() {
        List<CellIndex> selectedCellIndices = new ArrayList<CellIndex>();
        for (int i = 0; i < this.getSelectedRows().length; i++) {
            for (int j = 0; j < this.getSelectedColumns().length; j++) {
                int row = this.getSelectedRows()[i];
                int col = this.getSelectedColumns()[j];
                if (col > 0) {
                    selectedCellIndices.add(new CellIndex(row, col - 1));
                }
            }
        }
        return selectedCellIndices;
    }

    //
    // Improved cell editing courtesy of camick!
    //
    // http://www.camick.com/java/source/RXTable.java
    //
    private boolean isSelectAllForMouseEvent = true;
    private boolean isSelectAllForActionEvent = true;
    private boolean isSelectAllForKeyEvent = true;

    /*
     * Override to provide Select All editing functionality
     */
    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        boolean result = super.editCellAt(row, column, e);

        if (isSelectAllForMouseEvent || isSelectAllForActionEvent || isSelectAllForKeyEvent) {
            selectAll(e);
        }

        return result;
    }

    /*
     * Select the text when editing on a text related cell is started
     */
    private void selectAll(EventObject e) {
        final Component editor = getEditorComponent();

        if (editor == null || !(editor instanceof JTextComponent))
            return;

        if (e == null) {
            ((JTextComponent) editor).selectAll();
            return;
        }

        // Typing in the cell was used to activate the editor

        if (e instanceof KeyEvent && isSelectAllForKeyEvent) {
            ((JTextComponent) editor).selectAll();
            return;
        }

        // F2 was used to activate the editor

        if (e instanceof ActionEvent && isSelectAllForActionEvent) {
            ((JTextComponent) editor).selectAll();
            return;
        }

        // A mouse click was used to activate the editor.
        // Generally this is a double click and the second mouse click is
        // passed to the editor which would remove the text selection unless
        // we use the invokeLater()

        if (e instanceof MouseEvent && isSelectAllForMouseEvent) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ((JTextComponent) editor).selectAll();
                }
            });
        }
    }
    //// Camick additions end //////

}
